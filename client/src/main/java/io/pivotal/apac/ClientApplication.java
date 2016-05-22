package io.pivotal.apac;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

@SpringBootApplication
public class ClientApplication {

	static
	{
        System.setProperty("javax.net.debug","ssl,handshake,record");


		javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(
				new javax.net.ssl.HostnameVerifier() {

					public boolean verify(String hostname,
										  javax.net.ssl.SSLSession sslSession) {
						return true;
					}
				});
	}

    // e.g. Add http.client.ssl.trust-store=classpath:ssl/truststore.jks to application.properties
    @Value("${http.client.ssl.trust-store:classpath:client.jks}")
    private Resource trustStore;

    @Value("${http.client.ssl.trust-store-password:s3cr3t}")
    private char[] trustStorePassword;

    // e.g. Add http.client.ssl.trust-type=PKCS12 to application.properties
    @Value("${http.client.ssl.trust-store-type:JKS}")
    private String trustStoreType;

    // e.g. Add http.client.ssl.version=SSLv3 to application.properties
    @Value("${http.client.ssl.version:TLS}")
    private String sslVersion;

    @Value("${hello.server:https://localhost:8080/}")
    private String value;

    @Value("${http.client.maxPoolSize:10}")
    private Integer maxPoolSize;

    @Bean
    public HttpClient httpClient() {
        SSLConnectionSocketFactory socketFactory = null;
        try {
            socketFactory = new SSLConnectionSocketFactory(
                    new SSLContextBuilder()
                            .loadTrustMaterial(trustStore.getURL(), trustStorePassword, new TrustSelfSignedStrategy()).build());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        }

        return HttpClients.custom().setSSLSocketFactory(socketFactory).build();
    }

    @Bean
    public RestTemplate restTemplate(HttpClient httpClient) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));
        return restTemplate;
    }

    @Bean
    CommandLineRunner runner(final RestTemplate template) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                ResponseEntity<String> response = template.getForEntity(value, String.class);
                System.out.println(response.getBody());
            }
        };
    }

	public static void main(String[] args) {
		SpringApplication.run(ClientApplication.class, args);
	}
}
