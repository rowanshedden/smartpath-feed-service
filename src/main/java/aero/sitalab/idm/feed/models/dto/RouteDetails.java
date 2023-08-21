package aero.sitalab.idm.feed.models.dto;

import org.springframework.lang.Nullable;

import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;

public class RouteDetails {

	@Nullable
	@SerializedName("departure")
	@ApiModelProperty(value = "")
	private PortDetails departure;

	@Nullable
	@SerializedName("arrival")
	@ApiModelProperty(value = "")
	private PortDetails arrival;

	public RouteDetails() {
		super();
	}

	public PortDetails getDeparture() {
		return departure;
	}

	public void setDeparture(PortDetails departure) {
		this.departure = departure;
	}

	public PortDetails getArrival() {
		return arrival;
	}

	public void setArrival(PortDetails arrival) {
		this.arrival = arrival;
	}

}
