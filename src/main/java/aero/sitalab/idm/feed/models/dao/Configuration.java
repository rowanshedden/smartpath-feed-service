package aero.sitalab.idm.feed.models.dao;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import aero.sitalab.idm.feed.models.dto.KeyValue;

@Entity
@Table(name = "configuration")
public class Configuration {

	@Id
	@Column(name = "cfg_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
	private Long id;

	private String cfg;
	private String value;
	private LocalDateTime updated;

	public Configuration() {
	}

	public Configuration(KeyValue keyValue) {
		super();
		this.cfg = keyValue.getKey();
		this.value = keyValue.getValue();
		this.updated = LocalDateTime.now();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCfg() {
		return cfg;
	}

	public void setCfg(String cfg) {
		this.cfg = cfg;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public LocalDateTime getUpdated() {
		return updated;
	}

	public void setUpdated(LocalDateTime updated) {
		this.updated = updated;
	}

}
