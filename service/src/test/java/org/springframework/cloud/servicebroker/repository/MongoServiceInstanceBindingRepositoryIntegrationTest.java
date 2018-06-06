package org.springframework.cloud.servicebroker.repository;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.springframework.cloud.servicebroker.mongodb.IntegrationTestBase;
import org.springframework.cloud.servicebroker.mongodb.fixture.ServiceInstanceBindingFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;

import com.mongodb.MongoClient;

public class MongoServiceInstanceBindingRepositoryIntegrationTest extends IntegrationTestBase {

	private static final String COLLECTION = "serviceInstanceBinding";

	@Autowired
	private MongoClient client;

	@Autowired
	private MongoServiceInstanceBindingRepository repository;

	@Autowired
	private MongoOperations mongo;

	@Before
	public void setup() throws Exception {
		mongo.dropCollection(COLLECTION);
	}

	@After
	public void teardown() {
		mongo.dropCollection(COLLECTION);
		client.dropDatabase(DB_NAME);
	}

	@Test
	public void bindingInsertedSuccessfully() throws Exception {
		Assert.assertEquals(0, mongo.getCollection(COLLECTION).count());
		repository.save(ServiceInstanceBindingFixture.getServiceInstanceBinding());
		Assert.assertEquals(1, mongo.getCollection(COLLECTION).count());
	}

	@Test
	public void bindingDeletedSuccessfully() throws Exception {
		Assert.assertEquals(0, mongo.getCollection(COLLECTION).count());
		repository.save(ServiceInstanceBindingFixture.getServiceInstanceBinding());
		Assert.assertEquals(1, mongo.getCollection(COLLECTION).count());
		repository.delete(ServiceInstanceBindingFixture.getServiceInstanceBinding().getId());
		Assert.assertEquals(0, mongo.getCollection(COLLECTION).count());
	}
}