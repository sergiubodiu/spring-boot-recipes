package io.pivotal.apac;

import org.cloudfoundry.client.CloudFoundryClient;
import org.cloudfoundry.spring.client.SpringCloudFoundryClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CfClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(CfClientApplication.class, args);
	}

	@Bean
    CloudFoundryClient cloudFoundryClient(@Value("${cf.host}") String host,
                                          @Value("${cf.username}") String username,
                                          @Value("${cf.password}") String password) {
		return SpringCloudFoundryClient.builder()
				.host(host)
				.username(username)
				.password(password)
				.build();
	}
}
