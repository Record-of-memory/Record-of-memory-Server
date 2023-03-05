package com.rom;

import com.rom.global.config.YamlPropertySourceFactory;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

import java.util.TimeZone;


@SpringBootApplication
@PropertySource(value = {"classpath:database/application-database.yml"}, factory = YamlPropertySourceFactory.class)
@PropertySource(value = {"classpath:oauth2/application-oauth2.yml"}, factory = YamlPropertySourceFactory.class)
@PropertySource(value = {"classpath:swagger/application-springdoc.yml" }, factory = YamlPropertySourceFactory.class)
public class ROMApplication {

	public static void main(String[] args) {
		SpringApplication.run(ROMApplication.class, args);
	}

	@PostConstruct
	public void started(){
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));

	}

}
