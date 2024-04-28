package br.com.alura.screenmatch.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

@Entity
@Table(name = "series")
public class Series {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(unique = true)
	private String title;
	
	private Integer totalSeasons;
	
	private Double rating;
	private String type;
	
	@Enumerated(EnumType.STRING)
	private Genre genre;
	
	private String actors;
	private String poster;
	private String plot;
	
	@OneToMany(mappedBy = "series", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Episode> episodes = new ArrayList<>();
	
	public Series(SeriesRec seriesRec){
		this.title = seriesRec.title();
		this.totalSeasons = seriesRec.totalSeasons();
		this.rating = OptionalDouble.of(Double.parseDouble(seriesRec.rating())).orElse(0.0);
		this.type = seriesRec.type();
		this.genre = Genre.fromString(seriesRec.genre().split(",")[0].trim());
		this.actors = seriesRec.actors();
		this.poster = seriesRec.poster();
		this.plot = seriesRec.plot();
	}
	
	public Series() {
	}
	
	public String getTitle() {
		return title;
	}
	
	public Double getRating() {
		return rating;
	}
	
	public Integer getTotalSeasons() {
		return totalSeasons;
	}
	
	public long getId() {
		return id;
	}
	
	public void setEpisodes(List<Episode> episodes){
		episodes.forEach(e -> e.setSeries(this));
		this.episodes = episodes;
	}
	
	public List<Episode> getEpisodes() {
		return episodes;
	}
	
	@Override
	public String toString() {
		return "Series{" +
				"id=" + id +
				", title='" + title + '\'' +
				", totalSeasons=" + totalSeasons +
				", rating=" + rating +
				", type='" + type + '\'' +
				", genre=" + genre +
				", actors='" + actors + '\'' +
				", poster='" + poster + '\'' +
				", plot='" + plot + '\'' +
				", episodes=" + episodes +
				'}';
	}
}
