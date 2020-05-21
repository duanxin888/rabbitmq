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
        jobName = "com.duanxin.esjob.test.TestJob",
        cron = "0/5 * * * * ?",
        description = "测试定时任务",
        shardingTotalCount = 5
)
@Component
public class TestJob implements SimpleJob {
    @Override
    public void execute(ShardingContext shardingContext) {
        System.out.println("执行testJob。。。。");
    }
}
