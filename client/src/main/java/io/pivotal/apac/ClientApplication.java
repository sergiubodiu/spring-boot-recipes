package io.pivotal.apac;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class ClientApplication {

	final static String KEYSTORE_PASSWORD = "s3cr3t";
    String value = "https://server.local.micropcf.io/env";

	static
	{
		System.setProperty("javax.net.ssl.trustStore", ClientApplication.class.getClassLoader().getResource("client.jks").getFile());
		System.setProperty("javax.net.ssl.trustStorePassword", KEYSTORE_PASSWORD);
		System.setProperty("javax.net.ssl.keyStore", ClientApplication.class.getClassLoader().getResource("client.jks").getFile());
		System.setProperty("javax.net.ssl.keyStorePassword", KEYSTORE_PASSWORD);

		javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(
				new javax.net.ssl.HostnameVerifier() {

					public boolean verify(String hostname,
										  javax.net.ssl.SSLSession sslSession) {
						if (hostname.equals("localhost")) {
							return true;
						}
						return false;
					}
				});
	}

    @Bean
    CommandLineRunner runner() {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                ResponseEntity<String> response = new RestTemplate().getForEntity(value, String.class);
                System.out.println(response.getBody());
            }
        };
    }

	public static void main(String[] args) {
		SpringApplication.run(ClientApplication.class, args);
	}
}
