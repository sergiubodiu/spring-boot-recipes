package io.pivotal.apac;

import org.cloudfoundry.logging.LogMessage;
import org.cloudfoundry.operations.CloudFoundryOperations;
import org.cloudfoundry.operations.CloudFoundryOperationsBuilder;
import org.cloudfoundry.operations.applications.*;
import org.cloudfoundry.operations.organizations.OrganizationSummary;
import org.cloudfoundry.spring.client.SpringCloudFoundryClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Timer;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.function.Predicate;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = CfClientApplication.class)
public class CfClientApplicationTests {

	@Autowired
	ClientProperties clientProperties;

//    @Bean
//    SpringUaaClient uaaClient(SpringCloudFoundryClient cloudFoundryClient) {
//        return SpringUaaClient.builder()
//                .cloudFoundryClient(cloudFoundryClient)
//                .build();
//    }
//    @Autowired
//    private UaaClient uaaClient;
//
//    @Test
//    public void getTokenKey() {
//        this.uaaClient.accessTokenAdministration()
//                .getTokenKey(GetTokenKeyRequest.builder()
//                        .build())
//                .subscribe();
//    }



    @Test
	public void contextLoads() throws InterruptedException {
		SpringCloudFoundryClient cloudFoundryClient = SpringCloudFoundryClient.builder()
				.host(clientProperties.getHost())
				.username(clientProperties.getUsername())
				.password(clientProperties.getPassword())
                .skipSslValidation(clientProperties.isSkipSslValidation())
				.build();

        CloudFoundryOperations operations = new CloudFoundryOperationsBuilder()
                .cloudFoundryClient(cloudFoundryClient)
                .target(clientProperties.getOrganization(), clientProperties.getSpace())
                .build();

        String name  = "hello";
        System.out.println(convertStreamToString(getApplicationBits()));


        Mono<Void> application = createApplication(operations, getApplicationBits(), name);

        Flux<LogMessage> logs = operations.applications()
                .logs(LogsRequest.builder()
                        .name(name)
                        .build())
                .doOnNext(x -> System.out.println(x.getMessage()));

        logs.map(LogMessage::getMessage)
                .useTimer(Timer.create())
                .buffer(Duration.ofSeconds(10))
                .consume( x -> System.out.println("Logs: " + x));


        operations.organizations()
                .list()
                .map(OrganizationSummary::getName)
                .consume(System.out::println);

        operations.applications()
				.list()
				.consume(System.out::println);

        Predicate<ApplicationSummary> predicate =
                summary -> summary.getName().equalsIgnoreCase(name);

        final Applications applications = operations.applications();

        applications.list()
                .filter(predicate)
                .consume(summary -> {
                    System.out.println(summary.getName());
                    applications.get(GetApplicationRequest.builder()
                            .name(summary.getName())
                            .build())
                            .consume(System.out::println);

                });

        application.get(Duration.ofSeconds(120));
	}

    private static Mono<Void> createApplication(CloudFoundryOperations cloudFoundryOperations,
                                                InputStream applicationBits, String name) {
        Mono<Void> app = cloudFoundryOperations.applications()
                .push(PushApplicationRequest.builder()
                        .application(applicationBits)
                        .healthCheckType(ApplicationHealthCheck.PORT)
                        .buildpack("java_buildpack")
                        .diskQuota(512)
                        .path("app.zip")
                        .memory(512)
                        .name(name)
                        .build())
                .doOnSuccess(s -> System.out.println("Pushing: " + name));

        return app;
    }

    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    private static InputStream getApplicationBits() {
        try {
            return new ClassPathResource("app.zip").getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
