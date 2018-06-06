/*
 * Copyright 2016-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package example.consul;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.consul.discovery.ConsulDiscoveryProperties;

/**
 * Sample Application using Spring Cloud Vault with Token authentication. Vault will
 * obtain a Consul ACL token.
 *
 * @author Mark Paluch
 */
@SpringBootApplication
@EnableDiscoveryClient
public class SpringBootVaultConsulApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootVaultConsulApplication.class, args);
	}

	@Autowired
	ConsulDiscoveryProperties properties;

	@PostConstruct
	private void postConstruct() throws Exception {

		System.out.println("##########################");
		System.out.println("Generated token: " + properties.getAclToken());
		System.out.println("##########################");
	}
}
