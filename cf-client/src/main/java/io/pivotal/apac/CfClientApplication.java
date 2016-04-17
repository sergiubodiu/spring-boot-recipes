package io.pivotal.apac;

import org.cloudfoundry.client.CloudFoundryClient;
import org.cloudfoundry.operations.CloudFoundryOperations;
import org.cloudfoundry.operations.CloudFoundryOperationsBuilder;
import org.cloudfoundry.operations.applications.ApplicationSummary;
import org.cloudfoundry.operations.applications.Applications;
import org.cloudfoundry.operations.applications.GetApplicationRequest;
import org.cloudfoundry.operations.applications.StopApplicationRequest;
import org.cloudfoundry.operations.organizations.OrganizationSummary;
import org.cloudfoundry.spring.client.SpringCloudFoundryClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.function.Predicate;

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
    CloudFoundryOperations cloudFoundryOperations(CloudFoundryClient cloudFoundryClient,
                                                  @Value("${cf.organization}") String organization,
                                                  @Value("${cf.space}") String space) {
        return new CloudFoundryOperationsBuilder()
                .cloudFoundryClient(cloudFoundryClient)
                .target(organization, space)
                .build();
    }

    @Bean
    CommandLineRunner runner(final CloudFoundryOperations cloudFoundryOperations) {
        return args -> {
            cloudFoundryOperations.organizations()
                    .list()
                    .map(OrganizationSummary::getName)
                    .consume(System.out::println);

            Predicate<ApplicationSummary> predicate =
                    summary -> !summary.getName().equalsIgnoreCase("spring-music");

            final Applications applications = cloudFoundryOperations.applications();

            applications.list()
                    .filter(predicate)
                    .consume(summary -> {
                        System.out.println(summary.getName());
                            applications.get(GetApplicationRequest.builder()
                                    .name(summary.getName())
                                    .build())
                                    .consume(System.out::println);

                            applications.stop(StopApplicationRequest.builder()
                                    .name(summary.getName())
                                    .build())
                                    .consume(System.out::println);

                    });
        };
    }

}
