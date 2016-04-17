package io.pivotal.apac;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties("cf")
@Data
@Component
public class ClientProperties {

    private String host;
    private String username;
    private String password;
    private boolean skipSslValidation;
    private String organization;
    private String space;

}