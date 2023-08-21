package aero.sitalab.idm.feed.models.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ConfigurationRequest extends BaseRequest {

	List<KeyValue> cfgs = new ArrayList<KeyValue>();

}
