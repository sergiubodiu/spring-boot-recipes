# Generate server and client keystore
    
    keytool -genkeypair -keystore server.jks -storepass s3cr3t -alias server -keypass s3cr3t \
        -dname "CN=Client,OU=Spring team,O=Pivotal,L=Singapore,S=Singapore,C=SG"
        
    keytool -genkeypair -keystore client.jks -storepass s3cr3t -alias client -keypass s3cr3t \
        -dname "CN=Client,OU=Spring team,O=Pivotal,L=Singapore,S=Singapore,C=SG"

## List contents of a keystore

    $ keytool -list -keystore server.jks -storepass s3cr3t

    Keystore type: JKS
    Keystore provider: SUN
    
    Your keystore contains 1 entry
    
    server, May 24, 2016, PrivateKeyEntry,
    Certificate fingerprint (SHA1): 6E:A7:78:23:34:78:0F:36:C5:DC:BC:E3:7B:29:C5:44:C8:ED:64:B8
    
    $ keytool -list -keystore client.jks -storepass s3cr3t

    Keystore type: JKS
    Keystore provider: SUN
    
    Your keystore contains 1 entry
    
    server, May 24, 2016, PrivateKeyEntry,
    Certificate fingerprint (SHA1): 6E:A7:78:23:34:78:0F:36:C5:DC:BC:E3:7B:29:C5:44:C8:ED:64:B8


## Import Client PKCS12 trust store

    keytool -importkeystore -srckeystore client.jks -srcstoretype JKS -deststoretype PKCS12 -destkeypass s3cr3t -deststorepass s3cr3t -destkeystore client.p12

Move the server certificates
    
    mv server.jks src/main/resources/
    mv client.p12 src/main/resources/
    
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

## Import Server PKCS12 trust store
    
    keytool -importkeystore -srckeystore server.jks -srcstoretype JKS -deststoretype PKCS12 -destkeypass s3cr3t -deststorepass s3cr3t -destkeystore server.p12

 Move the client certificates
     
     mv client.jks ../client/src/main/resources/
     mv server.p12 ../client/src/main/resources/
     

## Export Server PEM    
    
    keytool -exportcert -rfc -keystore server.jks -storepass s3cr3t -alias server > server.pem
    
## If you need to delete a certificate

    keytool -delete -alias clientcert -keystore server.jks -storepass s3cr3t
