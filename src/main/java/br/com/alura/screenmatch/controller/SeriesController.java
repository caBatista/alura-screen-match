package br.com.alura.screenmatch.controller;

import br.com.alura.screenmatch.dto.SeriesDTO;
import br.com.alura.screenmatch.service.SeriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
	
	@GetMapping("/new-releases")
	public List<SeriesDTO> getNewReleases() {
		return service.findNewReleases();
	}
}
