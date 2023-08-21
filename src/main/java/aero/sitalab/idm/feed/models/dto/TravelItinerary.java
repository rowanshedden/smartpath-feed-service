package aero.sitalab.idm.feed.models.dto;

import javax.validation.Valid;

import org.springframework.lang.Nullable;

import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;

public class TravelItinerary {

	@Nullable
	@SerializedName("carrierType")
	@ApiModelProperty(example = "A", value = "Carrier type code ( A=Air G=General Aviation S=Sea L=Land)")
	private String carrierType;

	@Nullable
	@SerializedName("carrierCode")
	@ApiModelProperty(example = "DL", value = "Carrier Code of Airline, Shipping Company, Bus company etc (as defined for GG and APP)")
	private String carrierCode;

	@Nullable
	@SerializedName("serviceNumber")
	@ApiModelProperty(example = "DL0613", value = "Flight Number (Air), LLoyds number (Sea) or Route Number (Land)")
	private String serviceNumber;

	@Nullable
	@SerializedName("pnrNumber")
	@ApiModelProperty(example = "X36Q9C", value = "PNR Number of the traveller")
	private String pnrNumber;

	@Nullable
	@SerializedName("seatNumber")
	@ApiModelProperty(example = "18K", value = "Seat number of the traveller")
	private String seatNumber;

	@Valid
	@SerializedName("routeDetails")
	@Nullable
	@ApiModelProperty(value = "Route details")
	private List<RouteDetails> routeDetails = null;

	public TravelItinerary() {
		super();
	}

	public String getCarrierType() {
		return carrierType;
	}

	public void setCarrierType(String carrierType) {
		this.carrierType = carrierType;
	}

	public String getCarrierCode() {
		return carrierCode;
	}

	public void setCarrierCode(String carrierCode) {
		this.carrierCode = carrierCode;
	}

	public String getServiceNumber() {
		return serviceNumber;
	}

	public void setServiceNumber(String serviceNumber) {
		this.serviceNumber = serviceNumber;
	}

	public TravelItinerary pnrNumber(String pnrNumber) {
		this.pnrNumber = pnrNumber;
		return this;
	}

	public String getPnrNumber() {
		return pnrNumber;
	}

	public void setPnrNumber(String pnrNumber) {
		this.pnrNumber = pnrNumber;
	}

	public String getSeatNumber() {
		return seatNumber;
	}

	public void setSeatNumber(String seatNumber) {
		this.seatNumber = seatNumber;
	}

	public TravelItinerary addRouteDetailsItem(RouteDetails routeDetailsItem) {
		if (this.routeDetails == null) {
			this.routeDetails = new ArrayList<RouteDetails>();
		}
		this.routeDetails.add(routeDetailsItem);
		return this;
	}

	public List<RouteDetails> getRouteDetails() {
		if (this.routeDetails == null) {
			this.routeDetails = new ArrayList<RouteDetails>();
		}
		return routeDetails;
	}

	public void setRouteDetails(List<RouteDetails> routeDetails) {
		this.routeDetails = routeDetails;
	}

}
