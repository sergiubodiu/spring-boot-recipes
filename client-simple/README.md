keytool -genkeypair -keystore client.jks -storepass s3cr3t -alias client -keypass s3cr3t -keyalg RSA -dname "CN=Client,OU=Spring team,O=Pivotal,L=Ave of Americas,S=NY,C=US"
  
keytool -exportcert -alias client -file client-public.cer -keystore client.jks -storepass s3cr3t

keytool -importcert -keystore server.jks -alias clientcert -file client-public.cer -storepass s3cr3t -noprompt

keytool -importkeystore -srckeystore server.jks -srcstoretype JKS -deststoretype PKCS12 -destkeypass s3cr3t -deststorepass s3cr3t -destkeystore server.p12
