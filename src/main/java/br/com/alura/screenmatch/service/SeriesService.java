package br.com.alura.screenmatch.service;

import br.com.alura.screenmatch.dto.EpisodeDTO;
import br.com.alura.screenmatch.dto.SeriesDTO;
import br.com.alura.screenmatch.model.Series;
import br.com.alura.screenmatch.repository.SeriesRepository;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeriesService {
	
	@Autowired
	private SeriesRepository repository;
	
	public List<SeriesDTO> findAllSeries() {
		return convertToDTO(repository.findAll());
	}
	
	public List<SeriesDTO> findTop5Series() {
		return convertToDTO(repository.findTop5ByOrderByRatingDesc());
	}
	
	public List<SeriesDTO> findLatestSeries() {
		return convertToDTO(repository.findByOrderByEpisodesReleaseDateDesc());
	}
	
	public SeriesDTO findSeriesById(Long id) {
		var series = repository.findById(id);
		
		if(series.isPresent()){
			var s = series.get();
			
			return new SeriesDTO(s.getId(),
					s.getTitle(),
					s.getTotalSeasons(),
					s.getRating(),
					s.getType(),
					s.getGenre(),
					s.getActors(),
					s.getPoster(),
					s.getPlot());
		} else {
			return null;
		}
	}
	
	private List<SeriesDTO> convertToDTO(List<Series> series) {
		return series.stream()
				.map(s -> new SeriesDTO(s.getId(),
						s.getTitle(),
						s.getTotalSeasons(),
						s.getRating(),
						s.getType(),
						s.getGenre(),
						s.getActors(),
						s.getPoster(),
						s.getPlot()))
				.toList();
	}
	
	public List<EpisodeDTO> findAllSeasons(Long id) {
		var series = repository.findById(id);
		
		if(series.isPresent()){
			var s = series.get();
			
			return s.getEpisodes().stream()
					.map(e -> new EpisodeDTO(
							e.getSeason(),
							e.getTitle(),
							e.getEpisodeNumber()))
					.toList();
		} else {
			return Collections.emptyList();
		}
	}
}
