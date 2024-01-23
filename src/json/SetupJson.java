package cs3500.pa04.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import cs3500.pa04.ShipType;
import java.util.Map;

/**
 * Represents the arguments from the server for the setup method
 */
public record SetupJson(
    @JsonProperty ("height") int height,
    @JsonProperty ("width") int width,

    @JsonProperty ("fleet-spec") Map<ShipType, Integer> specifications) {
}
