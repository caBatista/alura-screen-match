package br.com.alura.screenmatch.model;

import java.time.LocalDate;

public class Episode {
    private Integer season;
    private String title;
    private Integer episodeNumber;
    private Double rating;
    private LocalDate releaseDate;

    public Episode(Integer seasonNumber, EpisodeRec episodeRec) {
        this.season = seasonNumber;
        this.title = episodeRec.title();
        this.episodeNumber = episodeRec.episode();
        try{
            this.rating = Double.valueOf(episodeRec.rating());
        } catch (NumberFormatException nfe){
            this.rating = 0.0;
        }
        this.releaseDate = LocalDate.parse(episodeRec.releaseDate());
    }

    public Integer getSeason() {
        return season;
    }

    public String getTitle() {
        return title;
    }

    public Integer getEpisodeNumber() {
        return episodeNumber;
    }

    public Double getRating() {
        return rating;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Episode{");
        sb.append("season=").append(season);
        sb.append(", title='").append(title).append('\'');
        sb.append(", episodeNumber=").append(episodeNumber);
        sb.append(", rating=").append(rating);
        sb.append(", releaseDate=").append(releaseDate);
        sb.append('}');
        return sb.toString();
    }
}
