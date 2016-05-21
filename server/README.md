keytool -exportcert -rfc -keystore server.jks -storepass s3cr3t -alias server > server.pem
keytool -exportcert -rfc -keystore client.jks -storepass s3cr3t -alias client > client.pem

keytool -importkeystore -srckeystore client.jks -destkeystore client.pfx -deststoretype PKCS12 -srcalias client -deststorepass s3cr3t -destkeypass s3cr3t

openssl pkcs12 -in client.pfx -out client.p12