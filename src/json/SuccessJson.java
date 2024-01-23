package cs3500.pa04.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Represents the arguments from the server for the successfulHits method
 */
public record SuccessJson(
    @JsonProperty ("coordinates") List<CoordJson> los
) {
}
