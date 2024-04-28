package br.com.alura.screenmatch.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name="episodes")
public class Episode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    private Series series;
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
    
    public Episode(){
    }
    
    public void setSeries(Series series) {
        this.series = series;
    }
    
    public Series getSeries() {
        return series;
    }
    
    @Override
    public String toString() {
        return "Episode{" +
                "id=" + id +
                ", season=" + season +
                ", title='" + title + '\'' +
                ", episodeNumber=" + episodeNumber +
                ", rating=" + rating +
                ", releaseDate=" + releaseDate +
                '}';
    }
}
