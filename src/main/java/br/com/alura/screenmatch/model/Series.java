package br.com.alura.screenmatch.model;

import java.util.OptionalDouble;

public class Series {
	private final String title;
	private final Integer totalSeasons;
	private final Double rating;
	private final String type;
	private final Genre genre;
	private final String actors;
	private final String poster;
	private final String plot;
	
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
	
	public String getTitle() {
		return title;
	}
	
	public Double getRating() {
		return rating;
	}
	
	public Integer getTotalSeasons() {
		return totalSeasons;
	}
	
	@Override
	public String toString() {
		return "Series{" +
				"title='" + title + '\'' +
				", totalSeasons=" + totalSeasons +
				", rating=" + rating +
				", type='" + type + '\'' +
				", genre=" + genre +
				", actors='" + actors + '\'' +
				", poster='" + poster + '\'' +
				", plot='" + plot + '\'' +
				'}';
	}
}
