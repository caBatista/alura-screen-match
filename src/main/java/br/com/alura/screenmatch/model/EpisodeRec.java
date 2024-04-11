package br.com.alura.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record EpisodeRec(@JsonProperty("Title") String titulo,
                         @JsonProperty("Episode") Integer episode,
                         @JsonProperty("imdbRating") String rating,
                         @JsonProperty("Released") String releaseDate) {}
