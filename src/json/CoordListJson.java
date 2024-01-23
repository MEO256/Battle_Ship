package cs3500.pa04.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;


/**
 * Represents the List of CoordJson for the server
 */
public record CoordListJson(
    @JsonProperty ("coordinates") List<CoordJson> los) {
}
