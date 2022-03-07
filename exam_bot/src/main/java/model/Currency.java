package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Currency {
    @JsonProperty("Ccy")
    String ccy;
    @JsonProperty("CcyNm_UZ")
    String ccyNm_UZ;
    @JsonProperty("Nominal")
    String nominal;
    @JsonProperty("Rate")
    String rate;
    @JsonProperty("Date")
    String date;
    @JsonProperty("Diff")
    String diff;
}
