package org.springframework.cloud.servicebroker.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import java.util.Map;

/**
 * Details of a request to create a new service instance.
 * 
 * @author sgreenberg@pivotal.io
 * @author Scott Frederick
 */
@Getter
@ToString(callSuper = true, exclude = {"serviceDefinition"})
@EqualsAndHashCode(callSuper = true)
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE)
public class CreateServiceInstanceRequest extends AsyncParameterizedServiceInstanceRequest {

	/**
	 * The ID of the service to provision, from the broker catalog.
	 */
	@NotEmpty
	@JsonSerialize
	@JsonProperty("service_id")
	private final String serviceDefinitionId;

	/**
	 * The ID of the plan to provision within the service, from the broker catalog.
	 */
	@NotEmpty
	@JsonSerialize
	@JsonProperty("plan_id")
	private final String planId;

	/**
	 * The Cloud Controller GUID of the service instance to provision. This ID will be used for future
	 * requests for the same service instance (e.g. bind and deprovision), so the broker must use it to
	 * correlate any resource it creates.
	 */
	@JsonIgnore
	private transient String serviceInstanceId;

	/**
	 * The {@link ServiceDefinition} of the service to provision. This is resolved from the
	 * <code>serviceDefinitionId</code> as a convenience to the broker.
	 */
	@JsonIgnore
	private transient ServiceDefinition serviceDefinition;

	public CreateServiceInstanceRequest() {
		super(null, null);
		this.serviceDefinitionId = null;
		this.planId = null;
	}

	public CreateServiceInstanceRequest(String serviceDefinitionId, String planId,
										Context context,
										Map<String, Object> parameters) {
		super(parameters, context);
		this.serviceDefinitionId = serviceDefinitionId;
		this.planId = planId;
	}

	public CreateServiceInstanceRequest withServiceDefinition(ServiceDefinition serviceDefinition) {
		this.serviceDefinition = serviceDefinition;
		return this;
	}

	public CreateServiceInstanceRequest withCfInstanceId(String cfInstanceId) {
		this.cfInstanceId = cfInstanceId;
		return this;
	}

	public CreateServiceInstanceRequest withServiceInstanceId(final String serviceInstanceId) {
		this.serviceInstanceId = serviceInstanceId;
		return this;
	}

	public CreateServiceInstanceRequest withAsyncAccepted(boolean asyncAccepted) {
		this.asyncAccepted = asyncAccepted;
		return this;
	}

	public CreateServiceInstanceRequest withApiInfoLocation(String apiInfoLocation) {
		this.apiInfoLocation = apiInfoLocation;
		return this;
	}

	public CreateServiceInstanceRequest withOriginatingIdentity(Context originatingIdentity) {
		this.originatingIdentity = originatingIdentity;
		return this;
	}
}
