package aero.sitalab.idm.feed.models.dto;

import org.springframework.lang.Nullable;

import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;

public class PortDetails {

	@Nullable
	@SerializedName("portCode")
	@ApiModelProperty(example = "SYD", value = "IATA Port code (Air Travel) or UNLOC code (sea or land travel)")
	private String portCode;

	@Nullable
	@SerializedName("countryCode")
	@ApiModelProperty(example = "AUS", value = "ISO2 or ISO3 country code")
	private String countryCode;

	@Nullable
	@SerializedName("dateTime")
	@ApiModelProperty(example = "2021-06-29T20:00:00", value = "Either the departure or arrival datetime")
	private String dateTime;

	public PortDetails() {
		super();
	}

	public String getPortCode() {
		return portCode;
	}

	public void setPortCode(String portCode) {
		this.portCode = portCode;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

}
