package aero.sitalab.idm.feed.models.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;

import aero.sitalab.idm.feed.utils.MiscUtil;

public class ApiLogRecord {

	@JsonProperty("rq-stamp")
	private LocalDateTime rqTimestamp;
	@JsonProperty("rs-stamp")
	private LocalDateTime rsTimestamp;
	private String path;
	private String query;
	private String method;
	private String user;
	@JsonRawValue
	private String request;
	@JsonRawValue
	private String response;
	private int responseCode;
	private long duration;

	public ApiLogRecord() {
	}

	public ApiLogRecord(ApiLogRecord apiLog) {
		this.duration = apiLog.getDuration();
		this.method = apiLog.getMethod();
		this.path = apiLog.getPath();
		this.request = apiLog.getRequest();
		this.query = apiLog.getQuery();
		this.response = apiLog.getResponse();
		this.responseCode = apiLog.getResponseCode();
		this.rqTimestamp = apiLog.getRqTimestamp();
		this.rsTimestamp = apiLog.getRsTimestamp();
		this.user = apiLog.getUser();
	}

	public LocalDateTime getRqTimestamp() {
		return rqTimestamp;
	}

	public void setRqTimestamp(LocalDateTime rqTimestamp) {
		this.rqTimestamp = rqTimestamp;
	}

	public LocalDateTime getRsTimestamp() {
		return rsTimestamp;
	}

	public void setRsTimestamp(LocalDateTime rsTimestamp) {
		this.rsTimestamp = rsTimestamp;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public String toJson() {
		return MiscUtil.toJson(this);
	}

	public String toJsonMinified() {
		return MiscUtil.toJson(this);
	}

	@Override
	public ApiLogRecord clone() {
		ApiLogRecord clone = new ApiLogRecord();
		clone.duration = this.getDuration();
		clone.method = this.getMethod();
		clone.path = this.getPath();
		clone.request = this.getRequest();
		clone.query = this.getQuery();
		clone.response = this.getResponse();
		clone.responseCode = this.getResponseCode();
		clone.rqTimestamp = this.getRqTimestamp();
		clone.rsTimestamp = this.getRsTimestamp();
		clone.user = this.getUser();
		return clone;
	}

}
