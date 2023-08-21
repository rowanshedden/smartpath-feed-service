package aero.sitalab.idm.feed.models.dto.smartpath.enrolment;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class TravelDocumentInfo {

@JsonProperty("travelDocumentNumber")
public String travelDocumentNumber;

@JsonProperty("documentCode")
public String documentCode;

@JsonProperty("primaryName")
public String primaryName;

@JsonProperty("secondaryName")
public String secondaryName;

@JsonProperty("expiryDate")
public String expiryDate;

@JsonProperty("issuingState")
public String issuingState;

@JsonProperty("nationality")
public String nationality;

@JsonProperty("dateOfBirth")
public String dateOfBirth;

@JsonProperty("sex")
public String sex;

}