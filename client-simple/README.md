# Simple Java Client

    keytool -exportcert -alias server -file server-public.cer -keystore server.jks -storepass s3cr3t
    
    keytool -importcert -keystore client.jks -alias servercert -file server-public.cer -storepass s3cr3t -noprompt
    

## Test SSL connectivity
 
Download Unlimited JCE Policy

    $ cd usr/lib/jvm/java-7-openjdk-amd64/jre/lib/security$
    
    $ sudo mv ~/UnlimitedJCEPolicy/* .

Test

    java -Dhello.server=https://10.254.2.15:60002 -jar client-simple-0.0.1-SNAPSHOT.jar


Public certificate

    keytool -exportcert -alias client -file client-public.cer -keystore client.jks -storepass s3cr3t
    keytool -exportcert -alias server -file server-public.cer -keystore server.jks -storepass s3cr3t


    keytool -importcert -keystore server.jks -alias clientcert -file client-public.cer -storepass s3cr3t -noprompt
    keytool -importcert -keystore client.jks -alias servercert -file server-public.cer -storepass s3cr3t -noprompt

keytool -importkeystore -srckeystore client.jks -destkeystore client.pfx -deststoretype PKCS12 -srcalias client -deststorepass s3cr3t -destkeypass s3cr3t

openssl pkcs12 -in client.pfx -out client.p12

keytool -importkeystore -srckeystore server.jks -destkeystore server.pfx -deststoretype PKCS12 -srcalias serverkey -deststorepass s3cr3t -destkeypass s3cr3t

openssl pkcs12 -in server.pfx -out ca.pem -cacerts -nokeys  
openssl pkcs12 -in server.pfx -out client.pem -clcerts -nokeys  
openssl pkcs12 -in server.pfx -out key.pem -nocerts  

curl -k -v –key key.pem –cacert ca.pem –cert client.pem https://localhost:8080

curl -k -E ca.pem https://localhost:8080
    

CF_TRACE=true cf app server

openssl s_client -connect 10.0.2.15:60002

http://docs.run.pivotal.io/devguide/deploy-apps/environment-variable.html


sudo apt-get install openjdk-7-jre


You can add entry in ~/.ssh/config:

Host vagrant
    User vagrant
    HostName localhost
    Port 2222
    IdentityFile /home/user_name/.vagrant.d/insecure_private_key
and the simplescp file vagrant:/path/. You can find path to identity file using the vagrant ssh-config command. 

keytool -exportcert -alias spring-boot-ssl-sample -file client-public.cer -keystore sample.jks -storepass secret

 mvn clean package
 cf push server -p target/server-0.0.1-SNAPSHOT.jar -b java_buildpack
 
 scp target/client-simple-0.0.1-SNAPSHOT.jar vagrant:.
 
 java -Dhello.server=https://10.254.2.15:60002 -jar client-simple-0.0.1-SNAPSHOT.jar