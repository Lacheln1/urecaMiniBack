package com.blog.velog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:config/secu.properties")
@MapperScan("com.blog.velog.dao")

public class UrecaMiniApplication {

	public static void main(String[] args) {
		SpringApplication.run(UrecaMiniApplication.class, args);
	}

}
