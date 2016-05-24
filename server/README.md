# Generate server and client keystore
    
    keytool -genkeypair -keystore server.jks -storepass s3cr3t -alias server -keypass s3cr3t \
        -dname "CN=*.local.pcfdev.io,OU=Spring team,O=Pivotal,L=Singapore,S=Singapore,C=SG" -ext SAN=dns:localhost
        
    keytool -genkeypair -keystore client.jks -storepass s3cr3t -alias client -keypass s3cr3t \
         -dname "CN=*.local.pcfdev.io,OU=Spring team,O=Pivotal,L=Singapore,S=Singapore,C=SG" -ext SAN=dns:localhost

## List contents of a keystore

    $ keytool -list -keystore server.jks -storepass s3cr3t

    Keystore type: JKS
    Keystore provider: SUN
    
    Your keystore contains 1 entry
    
    server, May 24, 2016, PrivateKeyEntry,
    Certificate fingerprint (SHA1): 21:DF:AF:DC:B0:65:79:06:22:76:56:73:FB:0B:9C:15:5E:50:79:61
    
    $ keytool -list -keystore client.jks -storepass s3cr3t

    Keystore type: JKS
    Keystore provider: SUN
    
    Your keystore contains 1 entry
    
    client, May 24, 2016, PrivateKeyEntry,
    Certificate fingerprint (SHA1): 10:26:C7:05:98:D9:80:E1:F9:3D:41:96:C9:E9:F2:0E:6C:5D:3C:A3


## Import Client PKCS12 trust store

    keytool -importkeystore -srckeystore client.jks -srcstoretype JKS -deststoretype PKCS12 -destkeypass s3cr3t -deststorepass s3cr3t -destkeystore client.p12

    keytool -importkeystore -srckeystore server.jks -srcstoretype JKS -deststoretype PKCS12 -destkeypass s3cr3t -deststorepass s3cr3t -destkeystore server.p12

Move the server certificates
    
    mv server.jks client.p12 src/main/resources/
    mv client.jks server.p12 ../client/src/main/resources/
    
Update the src/main/resources/application.yml

    server:
      ssl:
        key-store: 'classpath:server.jks'
        key-store-password: s3cr3t
        key-store-type: JKS
        key-password: s3cr3t
        trust-store: 'classpath:client.p12'
        trust-store-password: s3cr3t
        trust-store-type: PKCS12
        client-auth: "need"

## See client configuration [Client](../client/README.md) 

         
Export Server PEM (Optional)   
    
    keytool -exportcert -rfc -keystore server.jks -storepass s3cr3t -alias server > server.pem
    
If you need to delete a certificate

    keytool -delete -alias clientcert -keystore server.jks -storepass s3cr3t
