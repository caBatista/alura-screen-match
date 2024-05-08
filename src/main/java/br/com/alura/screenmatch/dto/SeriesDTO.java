package br.com.alura.screenmatch.dto;

import br.com.alura.screenmatch.model.Genre;

public record SeriesDTO(long id,
                        String title,
                        Integer totalSeasons,
                        Double rating,
                        String type,
                        Genre genre,
                        String actors,
                        String poster,
                        String plot) {}
