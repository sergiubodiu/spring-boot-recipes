If you do not have an oracle driver do steps 1 and 2 first.  Otherwise skip to step 3.

1) Download the jdbc driver from oracle.

2) Install the driver into your local maven .m2 repository. Example:

mvn install:install-file -DgroupId=com.oracle -DartifactId=ojdbc7 -Dversion=12.1.0.2 -Dpackaging=jar -Dfile=ojdbc7.jar -DgeneratePom=true

In the above example the ojdbc6.jar version 12.1.0.2 is used.  Replace the -DartifactId=ojdbc6 -Dversion=12.1.0.2 and -Dfile=ojdbc7.jar with the driver name/version you downloaded.

3) Add the dependency to your build.gradle file. Example:

    dependencies {
        ...
        runtime("com.oracle:ojdbc7:12.1.0.2")
        ...
    }

3) Add the dependency to your pom.xml file. Example:

	<dependencies>
        ...
		<dependency>
			<groupId>com.oracle</groupId>
			<artifactId>ojdbc7</artifactId>
			<version>12.1.0.2</version>
			<scope>runtime</scope>
		</dependency>
		...


replace driver name and version with the one you downloaded

4) In spring boot add/modify the application.properties file to use your db url, username and password e.g.:

    spring.datasource.driverClassName=oracle.jdbc.driver.OracleDriver
    spring.datasource.url=jdbc:oracle:thin:@127.0.0.1:1521:orcl

Credentials for Oracle Day Workstation

    spring.datasource.username=hr
    spring.datasource.password=orcl

Generate traffic script
   
    #!/bin/bash
    # generate traffic
    for i in `seq 1 100`; do
      echo "sequence $i"
      if test $RANDOM -gt 15000
      then
          x=$(($RANDOM/3600))
          echo $x
          for j in `seq 1 $x`; do
            echo $j
            curl {ChangeME.cfapps.io} &> /dev/null
          done
          sleep 1
      fi  
    done
    
