package aero.sitalab.idm.feed.models.dto;

import java.util.ArrayList;
import java.util.List;

import aero.sitalab.idm.feed.models.dao.Configuration;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ConfigurationResponse extends BaseResponse {

	private List<Configuration> cfgs = new ArrayList<Configuration>();

}
