package aero.sitalab.idm.feed.models.dto.smartpath.biometrictoken;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class DocumentData {

    @JsonProperty("documentNumber")
    public String documentNumber;

    @JsonProperty("expiryDate")
    public String expiryDate;

    @JsonProperty("issuingState")
    public String issuingState;

    @JsonProperty("dateOfBirth")
    public String dateOfBirth;

    @JsonProperty("givenName")
    public String givenName;

    @JsonProperty("surname")
    public String surname;

    @JsonProperty("nationality")
    public String nationality;

    @JsonProperty("sex")
    public String sex;

    @JsonProperty("personalNumber")
    public String personalNumber;

    @JsonProperty("mrz")
    public String mrz;

}
