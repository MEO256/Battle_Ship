package cs3500.pa04.json;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the constructor for the server for the Ship class
 */
public record ShipJson(
    @JsonProperty ("coord") CoordJson coord,

    @JsonProperty ("length") int size,

    @JsonProperty ("direction") String direction) {
}
