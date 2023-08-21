package aero.sitalab.idm.feed.repositories;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import aero.sitalab.idm.feed.models.dao.Configuration;

@Repository("ConfigurationRepository")
public interface ConfigurationRepository extends JpaRepository<Configuration, Long> {
	
	List<Configuration>  findAllByCfg(String cfg, Sort sort);

}
