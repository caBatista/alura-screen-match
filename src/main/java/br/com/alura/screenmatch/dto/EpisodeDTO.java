package br.com.alura.screenmatch.dto;

public record EpisodeDTO(Integer season,
						 String title,
						 Integer episodeNumber,
                         Double rating) {}
