package com.duanxin.esjob.test;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.duanxin.rabbit.task.annotation.ElasticJobConfig;
import org.springframework.stereotype.Component;

/**
 * @author duanxin
 * @version 1.0
 * @className TestJob
 * @date 2020/05/20 09:40
 */
@ElasticJobConfig(
        jobName = "com.duanxin.esjob.test.DemoJob",
        cron = "0/10 * * * * ?",
        description = "样例定时任务",
        shardingTotalCount = 2
)
@Component
public class DemoJob implements SimpleJob {
    @Override
    public void execute(ShardingContext shardingContext) {
        System.out.println("执行demoJob。。。。");
    }
}
