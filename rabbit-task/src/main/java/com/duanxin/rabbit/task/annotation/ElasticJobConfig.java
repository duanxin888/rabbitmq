package com.duanxin.rabbit.task.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解：es-job配置信息
 * @author duanxin
 * @version 1.0
 * @className ElasticJobConfig
 * @date 2020/05/09 20:21
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ElasticJobConfig {

    /** 作业名称 */
    String jobName();

    /** cron表达式，用于控制作业触发时间 */
    String cron() default "";

    /** 作业分片总数 */
    int shardingTotalCount() default 1;

    /** 分片序列号和参数用等号分隔，多个键值对用逗号分隔
     * 分片序列号从0开始，不可大于或等于作业分片总数
     * */
    String shardingItemParameters() default "";

    /** 作业自定义参数，可通过传递该参数为作业调度的业务方法传参，用于实现带参数的作业 */
    String jobParameter() default "";

    /** 是否开启任务执行失效转移，开启表示如果作业在一次任务执行中途宕机，
     * 允许将该次未完成的任务在另一作业节点上补偿执行
     * 默认：不开启
     * */
    boolean failover() default false;

    /** 是否开启错过任务重新执行，默认开启 */
    boolean misfire() default true;

    /** 作业描述信息 */
    String description() default "";

    /** 本地配置是否可覆盖注册中心配置
     如果可覆盖，每次启动作业都以本地配置为准，默认不开启 */
    boolean overwrite() default false;

    /** 是否流式处理数据
     如果流式处理数据, 则fetchData不返回空结果将持续执行作业
     如果非流式处理数据, 则处理数据完成后作业结束, 默认不开启 */
    boolean streamingProcess() default false;

    /** 脚本型作业执行命令行 */
    String scriptCommandLine() default "";

    /** 监控作业运行时状态
     每次作业执行时间和间隔时间均非常短的情况，建议不监控作业运行时状态以提升效率。因为是瞬时状态，
     所以无必要监控。请用户自行增加数据堆积监控。并且不能保证数据重复选取，应在作业中实现幂等性。
     每次作业执行时间和间隔时间均较长的情况，建议监控作业运行时状态，可保证数据不会重复选取。
     默认不开启 */
    boolean monitorExecution() default false;

    /** must：作业监控端口
     建议配置作业监控端口, 方便开发者dump作业信息。
     使用方法: echo “dump” | nc 127.0.0.1 9888 */
    public int monitorPort() default -1;

    /** must：最大允许的本机与注册中心的时间误差秒数
     如果时间误差超过配置秒数则作业启动时将抛异常
     配置为-1表示不校验时间误差 */
    public int maxTimeDiffSeconds() default -1;

    /** must：作业分片策略实现类全路径，默认使用平均分配策略 */
    public String jobShardingStrategyClass() default "";

    /** must：修复作业服务器不一致状态服务调度间隔时间，配置为小于1的任意值表示不执行修复 */
    public int reconcileIntervalMinutes() default 10;

    /** must：作业事件追踪的数据源Bean引用 */
    public String eventTraceRdbDataSource() default "";

    /** must：监听器 */
    public String listener() default "";

    /** must： */
    public boolean disabled() default false;

    /** 分布式任务监听器 */
    public String distributedListener() default "";

    /** must：最后一个作业执行前的执行方法的超时时间 */
    public long startedTimeoutMilliseconds() default Long.MAX_VALUE;	//must

    /** must：最后一个作业执行后的执行方法的超时时间 */
    public long completedTimeoutMilliseconds() default Long.MAX_VALUE;		//must

    /** job异常处理类 */
    public String jobExceptionHandler()
            default "com.dangdang.ddframe.job.executor.handler.impl.DefaultJobExceptionHandler";

    /** 执行程序服务处理程序 */
    public String executorServiceHandler()
            default "com.dangdang.ddframe.job.executor.handler.impl.DefaultExecutorServiceHandler";
}
