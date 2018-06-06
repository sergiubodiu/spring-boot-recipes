package org.springframework.cloud.servicebroker.service;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.servicebroker.exception.ServiceInstanceBindingDoesNotExistException;
import org.springframework.cloud.servicebroker.exception.ServiceInstanceBindingExistsException;
import org.springframework.cloud.servicebroker.model.CreateServiceInstanceAppBindingResponse;
import org.springframework.cloud.servicebroker.model.CreateServiceInstanceBindingRequest;
import org.springframework.cloud.servicebroker.model.CreateServiceInstanceBindingResponse;
import org.springframework.cloud.servicebroker.model.DeleteServiceInstanceBindingRequest;
import org.springframework.cloud.servicebroker.model.ServiceInstanceBinding;
import org.springframework.cloud.servicebroker.repository.MongoServiceInstanceBindingRepository;
import org.springframework.stereotype.Service;

/**
 * Mongo impl to bind services.  Binding a service does the following:
 * creates a new user in the database (currently uses a default pwd of "password"),
 * saves the ServiceInstanceBinding info to the Mongo repository.
 *  
 * @author sgreenberg@pivotal.io
 */
@Service
public class MongoServiceInstanceBindingService implements ServiceInstanceBindingService {

	private MongoAdminService mongo; 

	private MongoServiceInstanceBindingRepository bindingRepository;

	@Autowired
	public MongoServiceInstanceBindingService(MongoAdminService mongo,
											  MongoServiceInstanceBindingRepository bindingRepository) {
		this.mongo = mongo;
		this.bindingRepository = bindingRepository;
	}
	
	@Override
	public CreateServiceInstanceBindingResponse createServiceInstanceBinding(CreateServiceInstanceBindingRequest request) {

		String bindingId = request.getBindingId();
		String serviceInstanceId = request.getServiceInstanceId();

		Optional<ServiceInstanceBinding> binding = bindingRepository.findById(bindingId);
//		ServiceInstanceBinding binding
		if (binding.isPresent()) {

			String database = serviceInstanceId;
			String username = bindingId;
			String password = RandomStringUtils.randomAlphanumeric(25);

			// TODO check if user already exists in the DB
			mongo.createUser(database, username, password);

			Map<String, Object> credentials =
					Collections.singletonMap("uri", (Object) mongo.getConnectionString(database, username, password));

			binding = new ServiceInstanceBinding(bindingId, serviceInstanceId, credentials, null, request.getBoundAppGuid());
			bindingRepository.save(binding);

			return new CreateServiceInstanceAppBindingResponse().withCredentials(credentials);
		}
		throw new ServiceInstanceBindingExistsException(serviceInstanceId, bindingId);
	}

	@Override
	public void deleteServiceInstanceBinding(DeleteServiceInstanceBindingRequest request) {
		String bindingId = request.getBindingId();
		ServiceInstanceBinding binding = getServiceInstanceBinding(bindingId);

		if (binding == null) {
			throw new ServiceInstanceBindingDoesNotExistException(bindingId);
		}

		mongo.deleteUser(binding.getServiceInstanceId(), bindingId);
		bindingRepository.delete(bindingId);
	}

	protected ServiceInstanceBinding getServiceInstanceBinding(String id) {
		return bindingRepository.findOne(id);
	}

}
