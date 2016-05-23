package io.pivotal.apac;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class SSLServer {

	public static void main(String[] args) {
		SpringApplication.run(SSLServer.class, args);
	}

}

@RestController
class Controller {
	@RequestMapping("/")
	String hello() {return "hello";}
}
