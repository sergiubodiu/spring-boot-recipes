## Mutual SSL authentication

Content for application.yml will refer to to the keysotre and server certificate in classpath

    http:
      client:
        ssl:
          key-store: classpath:client.jks
          key-store-password: s3cr3t
          key-password: s3cr3t
          key-store-type: JKS
          trust-store: classpath:server.p12
          trust-store-type: PKCS12
          trust-store-password: s3cr3t
          
Update the IP address for test server

    hello:
      server: https://127.0.0.1:8080/
