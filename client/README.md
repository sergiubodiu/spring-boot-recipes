keytool -genkeypair -keystore server.jks -storepass s3cr3t -alias server -keypass s3cr3t -keyalg RSA -dname "CN=Server,OU=Spring team,O=Pivotal,L=Ave of Americas,S=NY,C=US"
keytool -genkeypair -keystore client.jks -storepass s3cr3t -alias client -keypass s3cr3t -keyalg RSA -dname "CN=Client,OU=Spring team,O=Pivotal,L=Ave of Americas,S=NY,C=US"
  
keytool -exportcert -alias client -file client-public.cer -keystore client.jks -storepass s3cr3t
keytool -exportcert -alias server -file server-public.cer -keystore server.jks -storepass s3cr3t


keytool -importcert -keystore server.jks -alias clientcert -file client-public.cer -storepass s3cr3t -noprompt
keytool -importcert -keystore client.jks -alias servercert -file server-public.cer -storepass s3cr3t -noprompt

keytool -list -keystore server.jks -storepass s3cr3t
Enter keystore password:

Keystore type: JKS
Keystore provider: SUN

Your keystore contains 2 entries

serverkey, Sep 19, 2015, PrivateKeyEntry,
Certificate fingerprint (SHA1): 0B:33:77:2E:74:DB:55:F0:D9:89:B5:FD:C9:56:68:DB:E6:15:94:17
clientcert, Sep 20, 2015, trustedCertEntry,
Certificate fingerprint (SHA1): 4F:E9:D6:BD:83:7D:C5:31:C1:2D:6B:AC:5F:64:83:E0:36:57:D8:B9



keytool -list -keystore client.jks -storepass s3cr3t
Enter keystore password:

Keystore type: JKS
Keystore provider: SUN

Your keystore contains 2 entries

servercert, Sep 20, 2015, trustedCertEntry,
Certificate fingerprint (SHA1): 0B:33:77:2E:74:DB:55:F0:D9:89:B5:FD:C9:56:68:DB:E6:15:94:17
clientkey, Sep 19, 2015, PrivateKeyEntry,
Certificate fingerprint (SHA1): 4F:E9:D6:BD:83:7D:C5:31:C1:2D:6B:AC:5F:64:83:E0:36:57:D8:B9


