package com.duanxin.esjob;

import com.duanxin.rabbit.task.annotation.EnableElasticJob;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


/**
 * @author duanxin
 * @version 1.0
 * @className Application
 * @date 2020/05/20 09:34
 */
@SpringBootApplication()
@EnableElasticJob
@ComponentScan(basePackages = {"com.duanxin.esjob.*"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
