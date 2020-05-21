package com.duanxin.rabbit.task.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * zk配置信息类
 * @author duanxin
 * @version 1.0
 * @className JobZookeeperProperties
 * @date 2020/05/09 19:53
 */
@ConfigurationProperties(prefix = "elastic.job.zk")
public class JobZookeeperProperties {

    /** zookeeper命名空间 */
    private String namespace;

    /** zookeeper服务器列表 */
    private String serverLists;

    /** 最大重试次数 */
    private int maxRetries = 3;

    /** 连接超时时间，单位：毫秒 */
    private int connectionTimeoutMilliseconds = 15000;

    /** 会话超时时间，单位：毫秒 */
    private int sessionTimeoutMilliseconds = 60000;

    /** 等待重试的间隔时间的初始值,单位：毫秒 */
    private int baseSleepTimeMilliseconds = 1000;

    /** 等待重试的间隔时间的最大值,单位：毫秒 */
    private int maxSleepTimeMilliseconds = 3000;

    /** 连接Zookeeper的权限令牌,缺省为不需要权限验证 */
    private String digest = "";

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getServerLists() {
        return serverLists;
    }

    public void setServerLists(String serverLists) {
        this.serverLists = serverLists;
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    public int getConnectionTimeoutMilliseconds() {
        return connectionTimeoutMilliseconds;
    }

    public void setConnectionTimeoutMilliseconds(int connectionTimeoutMilliseconds) {
        this.connectionTimeoutMilliseconds = connectionTimeoutMilliseconds;
    }

    public int getSessionTimeoutMilliseconds() {
        return sessionTimeoutMilliseconds;
    }

    public void setSessionTimeoutMilliseconds(int sessionTimeoutMilliseconds) {
        this.sessionTimeoutMilliseconds = sessionTimeoutMilliseconds;
    }

    public int getBaseSleepTimeMilliseconds() {
        return baseSleepTimeMilliseconds;
    }

    public void setBaseSleepTimeMilliseconds(int baseSleepTimeMilliseconds) {
        this.baseSleepTimeMilliseconds = baseSleepTimeMilliseconds;
    }

    public int getMaxSleepTimeMilliseconds() {
        return maxSleepTimeMilliseconds;
    }

    public void setMaxSleepTimeMilliseconds(int maxSleepTimeMilliseconds) {
        this.maxSleepTimeMilliseconds = maxSleepTimeMilliseconds;
    }
}
