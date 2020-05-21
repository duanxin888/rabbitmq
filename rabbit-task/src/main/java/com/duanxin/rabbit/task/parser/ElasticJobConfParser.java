package com.duanxin.rabbit.task.parser;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.JobTypeConfiguration;
import com.dangdang.ddframe.job.config.dataflow.DataflowJobConfiguration;
import com.dangdang.ddframe.job.config.script.ScriptJobConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.event.rdb.JobEventRdbConfiguration;
import com.dangdang.ddframe.job.executor.handler.JobProperties;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import com.duanxin.rabbit.task.annotation.ElasticJobConfig;
import com.duanxin.rabbit.task.autoconfigure.JobZookeeperProperties;
import com.duanxin.rabbit.task.enums.ElasticJobTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.util.StringUtils;

/**
 * es-job配置解析类
 * 解析时机：在 spring bean 都装配好后进行解析
 * @author duanxin
 * @version 1.0
 * @className ElasticJobParser
 * @date 2020/05/09 20:46
 */
public class ElasticJobConfParser implements ApplicationListener<ApplicationReadyEvent> {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticJobConfParser.class);

    private JobZookeeperProperties jobZookeeperProperties;

    private ZookeeperRegistryCenter zookeeperRegistryCenter;

    public ElasticJobConfParser(JobZookeeperProperties jobZookeeperProperties,
                                ZookeeperRegistryCenter zookeeperRegistryCenter) {
        this.jobZookeeperProperties = jobZookeeperProperties;
        this.zookeeperRegistryCenter = zookeeperRegistryCenter;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        try {
            ApplicationContext applicationContext = event.getApplicationContext();

            Map<String, Object> beanMap = applicationContext.getBeansWithAnnotation(ElasticJobConfig.class);

            for (Iterator<?> it = beanMap.values().iterator(); it.hasNext(); ) {
                Object confBean = it.next();
                Class<?> clazz = confBean.getClass();
                if (clazz.getName().indexOf("$") > 0) {
                    String className = clazz.getName();
                    clazz = Class.forName(className.substring(0, className.indexOf("$")));
                }
                // 获取接口类型 用于判断是什么类型的任务
                String jobTypeName = clazz.getInterfaces()[0].getSimpleName();
                // 获取配置项 ElasticJobConfig
                ElasticJobConfig conf = clazz.getAnnotation(ElasticJobConfig.class);
                String jobClass = clazz.getName();
                String jobName = this.jobZookeeperProperties.getNamespace() + "." + conf.jobName();
                String cron = conf.cron();
                String shardingItemParameters = conf.shardingItemParameters();
                String description = conf.description();
                String jobParameter = conf.jobParameter();
                String jobExceptionHandler = conf.jobExceptionHandler();
                String executorServiceHandler = conf.executorServiceHandler();

                String jobShardingStrategyClass = conf.jobShardingStrategyClass();
                String eventTraceRdbDataSource = conf.eventTraceRdbDataSource();
                String scriptCommandLine = conf.scriptCommandLine();

                boolean failover = conf.failover();
                boolean misfire = conf.misfire();
                boolean overwrite = conf.overwrite();
                boolean disabled = conf.disabled();
                boolean monitorExecution = conf.monitorExecution();
                boolean streamingProcess = conf.streamingProcess();

                int shardingTotalCount = conf.shardingTotalCount();
                int monitorPort = conf.monitorPort();
                int maxTimeDiffSeconds = conf.maxTimeDiffSeconds();
                int reconcileIntervalMinutes = conf.reconcileIntervalMinutes();

                // 配置es-job的相关配置
                JobCoreConfiguration coreConfiguration = JobCoreConfiguration
                        .newBuilder(jobName, cron, shardingTotalCount)
                        .shardingItemParameters(shardingItemParameters)
                        .description(description)
                        .failover(failover)
                        .misfire(misfire)
                        .jobParameter(jobParameter)
                        .jobProperties(JobProperties.JobPropertiesEnum.JOB_EXCEPTION_HANDLER.getKey(), jobExceptionHandler)
                        .jobProperties(JobProperties.JobPropertiesEnum.EXECUTOR_SERVICE_HANDLER.getKey(), executorServiceHandler)
                        .build();

                // 创建那种类型的任务
                JobTypeConfiguration typeConfig = null;

                if (Objects.equals(ElasticJobTypeEnum.SIMPLE.getType(), jobTypeName)) { // 简单类型任务
                    typeConfig = new SimpleJobConfiguration(coreConfiguration, jobClass);
                }
                if (Objects.equals(ElasticJobTypeEnum.DATAFLOW.getType(), jobTypeName)) { // 流式类型任务
                    typeConfig = new DataflowJobConfiguration(coreConfiguration, jobClass, streamingProcess);
                }
                if (Objects.equals(ElasticJobTypeEnum.SCRIPT.getType(), jobTypeName)) { // 脚本类型任务
                    typeConfig = new ScriptJobConfiguration(coreConfiguration, scriptCommandLine);
                }

                // LiteJobConfiguration
                LiteJobConfiguration jobConfig = LiteJobConfiguration
                        .newBuilder(typeConfig)
                        .overwrite(overwrite)
                        .jobShardingStrategyClass(jobShardingStrategyClass)
                        .disabled(disabled)
                        .maxTimeDiffSeconds(maxTimeDiffSeconds)
                        .monitorExecution(monitorExecution)
                        .monitorPort(monitorPort)
                        .reconcileIntervalMinutes(reconcileIntervalMinutes)
                        .build();

                // 创建Spring的beanDefinition
                BeanDefinitionBuilder factory = BeanDefinitionBuilder.rootBeanDefinition(SpringJobScheduler.class);
                factory.setInitMethodName("init");
                factory.setScope("prototype");

                //	1.添加bean构造参数，相当于添加自己的真实的任务实现类
                if (!ElasticJobTypeEnum.SCRIPT.getType().equals(jobTypeName)) {
                    factory.addConstructorArgValue(confBean);
                }
                //	2.添加注册中心
                factory.addConstructorArgValue(this.zookeeperRegistryCenter);
                //	3.添加LiteJobConfiguration
                factory.addConstructorArgValue(jobConfig);

                //	4.如果有eventTraceRdbDataSource 则也进行添加
                if (StringUtils.hasText(eventTraceRdbDataSource)) {
                    BeanDefinitionBuilder rdbFactory = BeanDefinitionBuilder.rootBeanDefinition(JobEventRdbConfiguration.class);
                    rdbFactory.addConstructorArgReference(eventTraceRdbDataSource);
                    factory.addConstructorArgValue(rdbFactory.getBeanDefinition());
                }

                //  5.添加监听
                List<?> elasticJobListeners = getTargetElasticJobListeners(conf);
                factory.addConstructorArgValue(elasticJobListeners);

                // 	把SpringJobScheduler注入到Spring容器中
                DefaultListableBeanFactory defaultListableBeanFactory =
                        (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();

                String registerBeanName = conf.jobName() + "SpringJobScheduler";
                defaultListableBeanFactory.registerBeanDefinition(registerBeanName, factory.getBeanDefinition());
                SpringJobScheduler scheduler = (SpringJobScheduler)applicationContext.getBean(registerBeanName);
                scheduler.init();
                LOGGER.info("启动elastic-job作业: " + jobName);
            }
            LOGGER.info("共计Elastic-Job作业数启动 {} 个", beanMap.values().size());
        } catch (Exception e) {
            LOGGER.error("Elastic-Job启动失败，系统强制退出", e);
            System.exit(1);
        }
    }

    private List<BeanDefinition> getTargetElasticJobListeners(ElasticJobConfig conf) {
        List<BeanDefinition> result = new ManagedList<BeanDefinition>(2);
        String listeners = conf.listener();
        if (StringUtils.hasText(listeners)) {
            BeanDefinitionBuilder factory = BeanDefinitionBuilder.rootBeanDefinition(listeners);
            factory.setScope("prototype");
            result.add(factory.getBeanDefinition());
        }

        String distributedListeners = conf.distributedListener();
        long startedTimeoutMilliseconds = conf.startedTimeoutMilliseconds();
        long completedTimeoutMilliseconds = conf.completedTimeoutMilliseconds();

        if (StringUtils.hasText(distributedListeners)) {
            BeanDefinitionBuilder factory = BeanDefinitionBuilder.rootBeanDefinition(distributedListeners);
            factory.setScope("prototype");
            factory.addConstructorArgValue(Long.valueOf(startedTimeoutMilliseconds));
            factory.addConstructorArgValue(Long.valueOf(completedTimeoutMilliseconds));
            result.add(factory.getBeanDefinition());
        }
        return result;
    }
}
