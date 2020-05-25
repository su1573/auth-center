package com.chinare;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import io.micrometer.core.instrument.MeterRegistry;
@EnableAutoConfiguration(exclude = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
})
@EnableTransactionManagement
@ServletComponentScan
@MapperScan("com.chinare.auth.**.*.dao")
@SpringBootApplication
@EnableCaching
@EnableFeignClients
@EnableScheduling
public class AuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
        System.out.println("ヾ(◍°∇°◍)ﾉﾞ    chinare auth server 启动成功 !！     ヾ(◍°∇°◍)ﾉﾞ\n" +
                "  ______  __    __   __  .__   __.       ___     .______      ________\n" + 
                " /      ||  |  |  | |  | |  \\ |  |     /   \\     |   _  \\    |  _____|\n" + 
                "|  ,----'|  |__|  | |  | |   \\|  |    /  ^  \\    |  |_)  |   |  |____ \n" + 
                "|  |     |   __   | |  | |  . `  |   /  /_\\  \\   |      /    |   ____|  \n" + 
                "|  `----.|  |  |  | |  | |  |\\   |  /  _____  \\  |  |\\  \\--| |  |_____ \n" + 
                " \\______||__|  |__| |__| |__| \\__| /__/     \\__\\ | _| `.___| |_______|");
    }
    /**
	 * @description Prometheus监控endpoint
	 * @param applicationName
	 * @return
	 */
	@Bean
	MeterRegistryCustomizer<MeterRegistry> configurer(@Value("${spring.application.name}") String applicationName){
		return registry -> registry.config().commonTags("application",applicationName);
	}
}
