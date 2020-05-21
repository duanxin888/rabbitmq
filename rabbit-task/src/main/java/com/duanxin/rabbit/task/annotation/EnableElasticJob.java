package com.duanxin.rabbit.task.annotation;
import	java.lang.reflect.Type;

import com.duanxin.rabbit.task.autoconfigure.JobParserAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 注解：声明自动装配job转化器
 * @author duanxin
 * @version 1.0
 * @className EnableElasticJob
 * @date 2020/05/09 20:17
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(JobParserAutoConfiguration.class)
public @interface EnableElasticJob {
}
