package io.markshen;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("io.markshen.dao")
public class MybatisPlusLearnApplication {
	public static void main(String[] args) {
		SpringApplication.run(MybatisPlusLearnApplication.class, args);
	}
}
