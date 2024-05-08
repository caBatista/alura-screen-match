package br.com.alura.screenmatch.controller;

import br.com.alura.screenmatch.dto.EpisodeDTO;
import br.com.alura.screenmatch.dto.SeriesDTO;
import br.com.alura.screenmatch.service.SeriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/series")
public class SeriesController {
	
	@Autowired
	private SeriesService service;
	
	@GetMapping()
	public List<SeriesDTO> getSeries() {
		return service.findAllSeries();
	}
	
	@GetMapping("/top5")
	public List<SeriesDTO> getTop5Series() {
		return service.findTop5Series();
	}
	
	@GetMapping("/latest")
	public List<SeriesDTO> getLatestSeries() {
		return service.findLatestSeries();
	}
	
	@GetMapping("/{id}")
	public SeriesDTO getSeriesById(@PathVariable Long id) {
		return service.findSeriesById( id);
	}
	
	@GetMapping("{id}/seasons/all")
	public List<EpisodeDTO> getAllSeasons(@PathVariable Long id) {
		return service.findAllSeasons(id);
	}
}
