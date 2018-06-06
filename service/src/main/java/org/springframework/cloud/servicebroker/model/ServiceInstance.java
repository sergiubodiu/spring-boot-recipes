package org.springframework.cloud.servicebroker.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * An instance of a ServiceDefinition.
 *
 * @author sgreenberg@pivotal.io
 */
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE)
public class ServiceInstance {

	@JsonSerialize
	@JsonProperty("service_instance_id")
	private String id;

	@JsonSerialize
	@JsonProperty("service_id")
	private String serviceDefinitionId;

	@JsonSerialize
	@JsonProperty("plan_id")
	private String planId;

	@JsonSerialize
	@JsonProperty("dashboard_url")
	private String dashboardUrl;

	@SuppressWarnings("unused")
	private ServiceInstance() {}

	public ServiceInstance(String serviceInstanceId, String serviceDefinitionId, String planId, String dashboardUrl) {
		this.id = serviceInstanceId;
		this.serviceDefinitionId = serviceDefinitionId;
		this.planId = planId;
		this.dashboardUrl = dashboardUrl;
	}

	/**
	 * Create a ServiceInstance from a create request. If fields
	 * are not present in the request they will remain null in the
	 * ServiceInstance.
	 * @param request containing details of ServiceInstance
	 */
	public ServiceInstance(CreateServiceInstanceRequest request) {
		this.serviceDefinitionId = request.getServiceDefinitionId();
		this.planId = request.getPlanId();
		this.id = request.getServiceInstanceId();
	}

	/**
	 * Create a ServiceInstance from a delete request. If fields
	 * are not present in the request they will remain null in the
	 * ServiceInstance.
	 * @param request containing details of ServiceInstance
	 */
	public ServiceInstance(DeleteServiceInstanceRequest request) {
		this.id = request.getServiceInstanceId();
		this.planId = request.getPlanId();
		this.serviceDefinitionId = request.getServiceDefinitionId();
	}

	/**
	 * Create a service instance from an update request. If fields
	 * are not present in the request they will remain null in the
	 * ServiceInstance.
	 * @param request containing details of ServiceInstance
	 */
	public ServiceInstance(UpdateServiceInstanceRequest request) {
		this.id = request.getServiceInstanceId();
		this.planId = request.getPlanId();
	}

	public String getServiceInstanceId() {
		return id;
	}

	public String getServiceDefinitionId() {
		return serviceDefinitionId;
	}

	public String getPlanId() {
		return planId;
	}

	public String getDashboardUrl() {
		return dashboardUrl;
	}

	public ServiceInstance and() {
		return this;
	}

	public ServiceInstance withDashboardUrl(String dashboardUrl) {
		this.dashboardUrl = dashboardUrl;
		return this;
	}
}
