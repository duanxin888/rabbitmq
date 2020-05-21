package com.duanxin.rabbit.task.autoconfigure;

import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import com.duanxin.rabbit.task.parser.ElasticJobConfParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * es-job自动转配
 * @author duanxin
 * @version 1.0
 * @className JobParserAutoConfiguration
 * @date 2020/05/09 19:36
 */
@Configuration
@ConditionalOnProperty(prefix = "elastic.job.zk", name = {"server-lists", "namespace"}, matchIfMissing = false)
@EnableConfigurationProperties(JobZookeeperProperties.class)
public class JobParserAutoConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobParserAutoConfiguration.class);

    @Bean(initMethod = "init")
    public ZookeeperRegistryCenter zookeeperRegistryCenter(JobZookeeperProperties jobZookeeperProperties) {
        ZookeeperConfiguration zkConfiguration =
                new ZookeeperConfiguration(jobZookeeperProperties.getServerLists(),
                        jobZookeeperProperties.getNamespace());
        zkConfiguration.setConnectionTimeoutMilliseconds(jobZookeeperProperties.getConnectionTimeoutMilliseconds());
        zkConfiguration.setBaseSleepTimeMilliseconds(jobZookeeperProperties.getBaseSleepTimeMilliseconds());
        zkConfiguration.setMaxSleepTimeMilliseconds(jobZookeeperProperties.getMaxSleepTimeMilliseconds());
        zkConfiguration.setSessionTimeoutMilliseconds(jobZookeeperProperties.getSessionTimeoutMilliseconds());
        zkConfiguration.setMaxRetries(jobZookeeperProperties.getMaxRetries());
        zkConfiguration.setBaseSleepTimeMilliseconds(jobZookeeperProperties.getBaseSleepTimeMilliseconds());
        zkConfiguration.setDigest(jobZookeeperProperties.getDigest());

        LOGGER.info("初始化job注册中心配置成功，zkAddress：{}, namespace: {}",
                jobZookeeperProperties.getServerLists(),
                jobZookeeperProperties.getNamespace());

        return new ZookeeperRegistryCenter(zkConfiguration);
    }

    @Bean
    public ElasticJobConfParser elasticJobParser(JobZookeeperProperties jobZookeeperProperties,
                                                 ZookeeperRegistryCenter zookeeperRegistryCenter) {
        return new ElasticJobConfParser(jobZookeeperProperties, zookeeperRegistryCenter);
    }
}
