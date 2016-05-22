package io.pivotal.apac;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.*;
import java.security.cert.CertificateException;

@SpringBootApplication
public class ClientSimpleApplication {

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

    public HttpURLConnection getHttpsURLConnection() throws IOException {
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
        return (HttpsURLConnection) new URL(value).openConnection();
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
        ConfigurableApplicationContext context = SpringApplication.run(ClientSimpleApplication.class, args);

        ClientSimpleApplication app = context.getBean(ClientSimpleApplication.class);

        HttpURLConnection connection = app.getHttpsURLConnection();
        System.out.println(connection.toString());
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null)
            System.out.println(inputLine);
        in.close();

	}
}
