package aero.sitalab.idm.feed.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import aero.sitalab.idm.feed.models.dto.Error;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import aero.sitalab.idm.feed.models.dto.BaseResponse;
import aero.sitalab.idm.feed.models.dto.KeyValue;
import aero.sitalab.idm.feed.models.dao.Configuration;
import aero.sitalab.idm.feed.models.dto.ConfigurationRequest;
import aero.sitalab.idm.feed.models.dto.ConfigurationResponse;
import aero.sitalab.idm.feed.repositories.ConfigurationRepository;
import aero.sitalab.idm.feed.utils.MiscUtil;

@Service
public class ConfigurationService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ConfigurationRepository configurationRepository;

	@Autowired
	private ConfigurableEnvironment env;

	/**
	 * Process the Configuration update request.
	 * 
	 * Update the Configuration table.
	 * 
	 * @param request
	 * @return
	 */
	public BaseResponse addConfiguration(ConfigurationRequest request) {
		BaseResponse response = new BaseResponse();
		List<Configuration> configurations = new ArrayList<Configuration>();
		try {
			List<KeyValue> keyValues = request.getCfgs();
			for (int k = 0; k < keyValues.size(); k++) {
				if (keyValues.get(k).getKey() == null || keyValues.get(k).getValue() == null) {
					throw new Exception("Element key or value is missing - check request");
				}
				configurations.add(new Configuration(keyValues.get(k)));
			}
			configurationRepository.saveAll(configurations);
			response.setSuccess(true);
			configurations.forEach(configuration -> {
				if (configuration.getCfg().equals("app.upk.attributes")) {
					MiscUtil.setUpkAttributes(configuration.getValue());
					logger.info("{} updated", configuration.getCfg());
				}
			});
		} catch (Exception e) {
			response.setError(
					new Error(System.currentTimeMillis(), 7000, "update configuration", e.getLocalizedMessage(), "exception"));
			logger.error(response.getError().getMessage());
		}
		return response;
	}

	/**
	 * Return the latest value of the configuration item if stored, otherwise
	 * retrieve and return the property value, if not found then return null
	 * 
	 * @param cfg
	 * @return
	 */
	public String getLastestConfigurationValue(String cfg) {
		ConfigurationResponse configurationResponse = this.getConfiguration(cfg);
		if (configurationResponse.isSuccess() && configurationResponse.getCfgs() != null && !configurationResponse.getCfgs().isEmpty()) {
			return configurationResponse.getCfgs().get(0).getValue();
		}
		Map<String, Object> properties = MiscUtil.getPropertiesStartingWith(env, "app");
		for (Map.Entry<String, Object> entry : properties.entrySet()) {
			if (entry.getKey().equals(cfg)) {
				KeyValue keyValue = new KeyValue(entry.getKey(), entry.getValue().toString());
				Configuration configuration = new Configuration(keyValue);
				update(configuration);
				return entry.getValue().toString();
			}
		}
		return null;
	}

	/**
	 * Process the Configuration retrieval request.
	 * 
	 * Get the latest configuration by key, or all, from the Configuration
	 * table.
	 * 
	 * @param cfg
	 * @return
	 */
	public ConfigurationResponse getConfiguration(String cfg) {
		ConfigurationResponse response = new ConfigurationResponse();
		try {
			if (cfg == null || cfg.equals("*") || cfg.toUpperCase().equals("ALL")) {
				response.getCfgs().addAll(findAllSortedByLatest());
			} else {
				List<Configuration> configurations = configurationRepository.findAllByCfg(cfg, Sort.by(Sort.Direction.DESC, "updated"));
				response.getCfgs().add(configurations.get(0));
			}
			response.setSuccess(true);
		} catch (Exception e) {
			response.setError(new Error(System.currentTimeMillis(), 7000, "get configuration", e.getLocalizedMessage(), "exception"));
			logger.error(response.getError().getMessage());
		}
		return response;
	}

	/**
	 * Return all the configurations, sorted by latest at top
	 * 
	 * @return
	 */
	public List<Configuration> findAllSortedByLatest() {
		return configurationRepository.findAll(Sort.by(Sort.Direction.DESC, "updated"));
	}

	/**
	 * Find and return a configuration item from a Configuration list
	 * 
	 * @param cfg
	 * @param configurations
	 * @return
	 */
	public ConfigurationResponse find(String cfg, List<Configuration> configurations) {
		ConfigurationResponse response = new ConfigurationResponse();
		try {
			for (Configuration configuration : configurations) {
				if (configuration.getCfg().equals(cfg)) {
					response.getCfgs().add(configuration);
					response.setSuccess(true);
				}
			}
		} catch (Exception e) {
			response.setError(
					new Error(System.currentTimeMillis(), 7000, "find configuration", e.getLocalizedMessage(), "exception"));
			logger.error(response.getError().getMessage());
		}
		return response;
	}

	/**
	 * Update the Configuration table
	 * 
	 * @param configuration
	 */
	public void update(Configuration configuration) {
		configurationRepository.save(configuration);
	}

}