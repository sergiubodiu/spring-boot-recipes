package io.pivotal.apac;

import org.cloudfoundry.client.CloudFoundryClient;
import org.cloudfoundry.operations.CloudFoundryOperations;
import org.cloudfoundry.operations.CloudFoundryOperationsBuilder;
import org.cloudfoundry.spring.client.SpringCloudFoundryClient;
import org.cloudfoundry.spring.logging.SpringLoggingClient;
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
                                          @Value("${cf.password}") String password,
                                          @Value("${cf.skipSslValidation:false}") Boolean skipSslValidation) {
		return SpringCloudFoundryClient.builder()
				.host(host)
				.username(username)
				.password(password)
                .skipSslValidation(skipSslValidation)
				.build();
	}

    @Bean
    CloudFoundryOperations operations(CloudFoundryClient cloudFoundryClient,
                                                  @Value("${cf.organization}") String organization,
                                                  @Value("${cf.space}") String space) {
        return new CloudFoundryOperationsBuilder()
                .cloudFoundryClient(cloudFoundryClient)
                .target(organization, space)
                .build();
    }

    @Bean
    SpringLoggingClient loggingClient(SpringCloudFoundryClient cloudFoundryClient) {
        return SpringLoggingClient.builder()
                .cloudFoundryClient(cloudFoundryClient)
                .build();
    }

}
