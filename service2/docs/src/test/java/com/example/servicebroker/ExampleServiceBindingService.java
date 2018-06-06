package com.example.servicebroker;

import org.springframework.cloud.servicebroker.model.binding.CreateServiceInstanceAppBindingResponse;
import org.springframework.cloud.servicebroker.model.binding.CreateServiceInstanceBindingRequest;
import org.springframework.cloud.servicebroker.model.binding.CreateServiceInstanceBindingResponse;
import org.springframework.cloud.servicebroker.model.binding.DeleteServiceInstanceBindingRequest;
import org.springframework.cloud.servicebroker.model.binding.GetServiceInstanceAppBindingResponse;
import org.springframework.cloud.servicebroker.model.binding.GetServiceInstanceBindingRequest;
import org.springframework.cloud.servicebroker.model.binding.GetServiceInstanceBindingResponse;
import org.springframework.cloud.servicebroker.service.ServiceInstanceBindingService;
import org.springframework.stereotype.Service;

@Service
public class ExampleServiceBindingService implements ServiceInstanceBindingService {

	@Override
	public CreateServiceInstanceBindingResponse createServiceInstanceBinding(CreateServiceInstanceBindingRequest request) {
		String serviceInstanceId = request.getServiceInstanceId();
		String bindingId = request.getBindingId();

		//
		// create credentials and store for later retrieval
		//

		String url = new String(/* build a URL to access the service instance */);
		String bindingUsername = new String(/* create a user */);
		String bindingPassword = new String(/* create a password */);

		return CreateServiceInstanceAppBindingResponse.builder()
				.credentials("url", url)
				.credentials("username", bindingUsername)
				.credentials("password", bindingPassword)
				.bindingExisted(false)
				.build();
	}

	@Override
	public void deleteServiceInstanceBinding(DeleteServiceInstanceBindingRequest request) {
		String serviceInstanceId = request.getServiceInstanceId();
		String bindingId = request.getBindingId();

		//
		// delete any binding-specific credentials
		//
	}

	@Override
	public GetServiceInstanceBindingResponse getServiceInstanceBinding(GetServiceInstanceBindingRequest request) {
		String serviceInstanceId = request.getServiceInstanceId();
		String bindingId = request.getBindingId();

		//
		// retrieve the details of the specified service binding
		//

		String url = new String(/* retrieved URL */);
		String bindingUsername = new String(/* retrieved user */);
		String bindingPassword = new String(/* retrieved password */);

		return GetServiceInstanceAppBindingResponse.builder()
				.credentials("username", bindingUsername)
				.credentials("password", bindingPassword)
				.credentials("url", url)
				.build();
	}
}
