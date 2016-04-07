package io.pivotal.apac;

import org.cloudfoundry.client.CloudFoundryClient;
import org.cloudfoundry.client.v2.organizations.ListOrganizationsRequest;
import org.cloudfoundry.operations.CloudFoundryOperations;
import org.cloudfoundry.operations.CloudFoundryOperationsBuilder;
import org.cloudfoundry.operations.organizations.OrganizationSummary;
import org.cloudfoundry.spring.client.SpringCloudFoundryClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Flux;

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
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {

                cloudFoundryOperations.organizations()
                        .list()
                        .map(OrganizationSummary::getName)
                        .consume(System.out::println);

                cloudFoundryOperations.organizations()
                        .list(ListOrganizationsRequest.builder()
                                .page(1)
                                .build())
                        .flatMap(response -> Flux.fromIterable(response.getResources))
                        .map(resource -> Organization.builder()
                                .id(resource.getMetadata().getId())
                                .name(resource.getEntity().getName())
                                .build());
            }
        };
    }
}
