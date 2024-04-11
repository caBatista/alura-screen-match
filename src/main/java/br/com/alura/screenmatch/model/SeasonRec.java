package br.com.alura.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SeasonRec(@JsonProperty("Title") String title,
                        @JsonProperty("Season") String seasonNumber,
                        @JsonProperty("totalSeasons") Integer totalSeasons,
                        @JsonProperty("Episodes") EpisodeRec[] episodes) {
    @Override
    public String toString() {
        return "SeasonRec{" +
                "title='" + title + '\'' +
                ", seasonNumber='" + seasonNumber + '\'' +
                ", episodes=" + Arrays.toString(episodes) +
                '}';
    }
}
