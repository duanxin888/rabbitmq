package com.duanxin.rabbit.test;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author duanxin
 * @version 1.0
 * @className MainConfig
 * @date 2020/05/20 15:51
 */
@Configuration
@ComponentScan(basePackages = {"com.duanxin.rabbit.*"})
public class MainConfig {
}
