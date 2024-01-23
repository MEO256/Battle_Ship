package cs3500.pa04.json;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Represents the coordinates for the server
 */
public record CoordJson(
    @JsonProperty("x") int col,

    @JsonProperty("y") int row
) {
}
