# Simple Java Client

    keytool -genkeypair -keystore client.jks -storepass s3cr3t -alias client -keypass s3cr3t \
        -keyalg RSA -dname "CN=Client,OU=Spring team,O=Pivotal,L=Singapore,S=Singapore,C=SG"
      
    keytool -exportcert -alias client -file client-public.cer -keystore client.jks -storepass s3cr3t
    
    keytool -importcert -keystore server.jks -alias clientcert -file client-public.cer -storepass s3cr3t -noprompt
    
    keytool -importkeystore -srckeystore server.jks -srcstoretype JKS -deststoretype \ 
        PKCS12 -destkeypass s3cr3t -deststorepass s3cr3t -destkeystore server.p12


## Test SSL connectivity
 
Download Unlimited JCE Policy

    $ cd usr/lib/jvm/java-7-openjdk-amd64/jre/lib/security$
    
    $ sudo mv ~/UnlimitedJCEPolicy/* .

Test

    java -Dhello.server=https://10.254.2.15:60002 -jar client-simple-0.0.1-SNAPSHOT.jar
