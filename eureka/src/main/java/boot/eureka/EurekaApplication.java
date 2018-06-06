package boot.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableAutoConfiguration
@EnableEurekaServer
public class EurekaApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(EurekaApplication.class, args);
	}

}
