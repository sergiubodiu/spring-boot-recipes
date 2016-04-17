package io.pivotal.apac;

import org.cloudfoundry.operations.CloudFoundryOperationsBuilder;
import org.cloudfoundry.spring.client.SpringCloudFoundryClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = CfClientApplication.class)
public class CfClientApplicationTests {

	@Autowired
	ClientProperties clientProperties;

	@Test
	public void contextLoads() {
		SpringCloudFoundryClient cloudFoundryClient = SpringCloudFoundryClient.builder()
				.host(clientProperties.getHost())
				.username(clientProperties.getUsername())
				.password(clientProperties.getPassword())
                .skipSslValidation(clientProperties.isSkipSslValidation())
				.build();

		new CloudFoundryOperationsBuilder()
				.cloudFoundryClient(cloudFoundryClient)
				.target(clientProperties.getOrganization(), clientProperties.getSpace())
				.build()
				.applications()
				.list()
				.consume(System.out::println);


	}

}
