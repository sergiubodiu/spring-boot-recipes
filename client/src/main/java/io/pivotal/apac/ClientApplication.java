package io.pivotal.apac;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.*;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;

@SpringBootApplication
public class ClientApplication {

    @Value("${http.client.maxPoolSize:10}")
    private Integer maxPoolSize;

    static
    {
        System.setProperty("javax.net.debug","ssl,handshake,record");
    }

    // e.g. Add http.client.ssl.trust-store=classpath:ssl/truststore.jks to application.properties
    @Value("${http.client.ssl.key-store}")
    private Resource keyStore;

    @Value("${http.client.ssl.key-store-password}")
    private char[] keyStorePassword;

    // e.g. Add http.client.ssl.trust-type=PKCS12 to application.properties
    @Value("${http.client.ssl.key-store-type}")
    private String keyStoreType;

    // e.g. Add http.client.ssl.trust-store=classpath:ssl/truststore.jks to application.properties
    @Value("${http.client.ssl.trust-store}")
    private Resource trustStore;

    @Value("${http.client.ssl.trust-store-password}")
    private char[] trustStorePassword;

    // e.g. Add http.client.ssl.trust-type=PKCS12 to application.properties
    @Value("${http.client.ssl.trust-store-type}")
    private String trustStoreType;

    // e.g. Add http.client.ssl.version=SSLv3 to application.properties
    @Value("${http.client.ssl.version:TLS}")
    private String sslVersion;

    @Value("${hello.server}")
    private String value;

    public void getHttpsURLConnection() throws IOException {
        HttpsURLConnection.setDefaultHostnameVerifier(
                new HostnameVerifier() {
                    public boolean verify(String hostname, javax.net.ssl.SSLSession sslSession) {
                        return true;
                    }
                });
        try {
            HttpsURLConnection.setDefaultSSLSocketFactory(sslSocketFactory());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        ResponseEntity<String> response = new RestTemplate().getForEntity(value, String.class);
        System.out.println(response.getBody());
    }

    private SSLSocketFactory sslSocketFactory() throws NoSuchAlgorithmException, KeyStoreException, IOException, UnrecoverableKeyException, CertificateException, KeyManagementException {
        KeyStore ks = KeyStore.getInstance(keyStoreType);
        ks.load(keyStore.getInputStream(), keyStorePassword);

        String keyAlgorithm = KeyManagerFactory.getDefaultAlgorithm();
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(keyAlgorithm);
        kmf.init(ks, keyStorePassword);

        KeyStore ts = KeyStore.getInstance(trustStoreType);
        ts.load(trustStore.getInputStream(), trustStorePassword);

        String trustAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(trustAlgorithm);
        tmf.init(ts);

        SSLContext sslcontext = SSLContext.getInstance(sslVersion);
        sslcontext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
        return sslcontext.getSocketFactory();
    }


    public static void main(String[] args) throws IOException {
        ConfigurableApplicationContext context = SpringApplication.run(ClientApplication.class, args);

        ClientApplication app = context.getBean(ClientApplication.class);
        app.getHttpsURLConnection();
    }

}
