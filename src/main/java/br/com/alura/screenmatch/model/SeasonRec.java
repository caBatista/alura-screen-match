package br.com.alura.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SeasonRec(@JsonProperty("Title") String title,
                        @JsonProperty("Season") String seasonNumber,
                        @JsonProperty("totalSeasons") Integer totalSeasons,
                        @JsonProperty("Episodes") ArrayList<EpisodeRec> episodes) {}
