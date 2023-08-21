package aero.sitalab.idm.feed.configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;

import aero.sitalab.idm.feed.models.dto.KeyValue;
import aero.sitalab.idm.feed.models.dto.ConfigurationResponse;
import aero.sitalab.idm.feed.services.ConfigurationService;
import aero.sitalab.idm.feed.utils.MiscUtil;

@Configuration
public class Config {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private ConfigurableEnvironment env;

	private List<aero.sitalab.idm.feed.models.dao.Configuration> savedConfigurations = null;

	@Bean("ConfigurationsHaveBeenLoaded")
	public void loadConfigurations() {

		savedConfigurations = configurationService.findAllSortedByLatest();

		/**
		 * add all the "app.*" properties to the configuration table
		 */
		Map<String, Object> properties = getProperties();
		for (Map.Entry<String, Object> entry : properties.entrySet()) {
			add(entry.getKey(), entry.getValue());
		}

		List<aero.sitalab.idm.feed.models.dao.Configuration> configurations = new ArrayList<aero.sitalab.idm.feed.models.dao.Configuration>();
		configurations.addAll(configurationService.findAllSortedByLatest());

		logger.info("Configurations: ");
		configurations.forEach(cfg -> {
			logger.info("name: {} [{}] updated: {}", cfg.getCfg(), cfg.getValue(), cfg.getUpdated());
		});
	}

	/**
	 * Retrieve latest from database
	 * 
	 * @param cfg
	 * @return
	 */
	public String retrieveLatest(String cfg) {
		if (!savedConfigurations.isEmpty()) {
			ConfigurationResponse configurationResponse = configurationService.find(cfg, savedConfigurations);
			if (configurationResponse != null && !configurationResponse.getCfgs().isEmpty())
				return configurationResponse.getCfgs().get(0).getValue();
		}
		return null;
	}

	/**
	 * Add the name and value to the configuration table
	 * 
	 * @param name
	 * @param value
	 */
	private void add(String name, Object value) {
		String stringValue = value.toString();
		if (retrieveLatest(name) == null) {
			configurationService.update(new aero.sitalab.idm.feed.models.dao.Configuration(new KeyValue(name, stringValue)));
			logger.info("Configuration added: {} [{}]", name, stringValue);
		}
	}

	/**
	 * Retrieve all the properties starting with app
	 * 
	 * @return
	 */
	private Map<String, Object> getProperties() {
		return MiscUtil.getPropertiesStartingWith(env, "app");
	}

}