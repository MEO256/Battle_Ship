package cs3500.pa04.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import cs3500.pa04.GameResult;

/**
 * Takes an information if the user won or not
 */
public record WinJson(
    @JsonProperty ("result") GameResult win,
    @JsonProperty ("reason") String reason) {
}