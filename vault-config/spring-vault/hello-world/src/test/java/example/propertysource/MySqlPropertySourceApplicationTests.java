/*
 * Copyright 2017-2018 the original author or authors.
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
package example.propertysource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import example.helloworld.VaultTestConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.vault.annotation.VaultPropertySource;

import static org.assertj.core.api.Assertions.*;

/**
 * Integration test using {@link VaultPropertySource} to connect MySQL with generated
 * credentials.
 *
 * @author Mark Paluch
 */
@RunWith(SpringRunner.class)
@ContextConfiguration
@Slf4j
public class MySqlPropertySourceApplicationTests {

	@ComponentScan
	@VaultPropertySource(value = "mysql/creds/readonly", propertyNamePrefix = "database.")
	static class Config extends VaultTestConfiguration {
	}

	@Autowired
	DatabaseConfiguration databaseConfiguration;

	@Test
	public void shouldProvideConfigurationThroughBean() throws SQLException {

		Connection connection = DriverManager.getConnection(
				"jdbc:mysql://localhost?useSSL=false",
				databaseConfiguration.getUsername(), databaseConfiguration.getPassword());
		Statement statement = connection.createStatement();

		ResultSet resultSet = statement.executeQuery("SELECT CURRENT_USER();");

		assertThat(resultSet.next()).isTrue();
		log.info("Database user: Config({}), Reported by MySQL({})",
				databaseConfiguration.getUsername(), resultSet.getString(1));

		resultSet.close();
		statement.close();
		connection.close();
	}

	@Component
	static class DatabaseConfiguration {

		@Value("${database.username}")
		String username;

		@Value("${database.password}")
		String password;

		public String getUsername() {
			return username;
		}

		public String getPassword() {
			return password;
		}
	}
}
