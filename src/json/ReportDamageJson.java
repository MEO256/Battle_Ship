package cs3500.pa04.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Represents the arguments from the server for the reportDamage method
 */
public record ReportDamageJson(
    @JsonProperty("coordinates") List<CoordJson> coords
) {
}
