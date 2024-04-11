package br.com.alura.screenmatch;

import br.com.alura.screenmatch.model.EpisodeRec;
import br.com.alura.screenmatch.model.SeasonRec;
import br.com.alura.screenmatch.model.SerieRec;
import br.com.alura.screenmatch.service.ConsumeAPI;
import br.com.alura.screenmatch.service.ConvertData;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception{
		var consumeAPI = new ConsumeAPI();

		var titleName = "The Office";

		var uri = buildURI(titleName);

		var json = consumeAPI.getData(uri);

		ConvertData converter = new ConvertData();
		SerieRec serieRec = converter.getData(json, SerieRec.class);
		System.out.println(serieRec);

		var seasonNumber = 1;

		uri = buildURI(titleName, seasonNumber);

		json = consumeAPI.getData(uri);

		SeasonRec seasonRec = converter.getData(json, SeasonRec.class);

		var seasons = new ArrayList<SeasonRec>();
		seasons.add(seasonRec);

		var totalSeasons = seasonRec.totalSeasons();

		for(seasonNumber = 2; seasonNumber <= totalSeasons; seasonNumber++){
			uri = buildURI(titleName, seasonNumber);

			json = consumeAPI.getData(uri);

			seasonRec = converter.getData(json, SeasonRec.class);
			seasons.add(seasonRec);
		}

		seasons.forEach(System.out::println);

			seasonNumber = 1;
		var episodeNumber = 2;

		uri = buildURI(titleName, seasonNumber, episodeNumber);

		json = consumeAPI.getData(uri);

		EpisodeRec episodeRec = converter.getData(json, EpisodeRec.class);
		System.out.println(episodeRec);
	}

	public String buildURI(String titleName) {
		var pageMessage = "http://www.omdbapi.com/?t=";
		var apiMessage = "&apikey=347e1780";
		var titleMessage = titleName.toLowerCase().replace(' ', '-');

		return new StringBuilder()
				.append(pageMessage)
				.append(titleMessage)
				.append(apiMessage)
				.toString();
	}

	public String buildURI(String titleName, int seasonNumber) {
		var pageMessage = "http://www.omdbapi.com/?t=";
		var apiMessage = "&apikey=347e1780";
		var titleMessage = titleName.toLowerCase().replace(' ', '-');
		var seasonMessage = "&Season=" + seasonNumber;

		return new StringBuilder()
				.append(pageMessage)
				.append(titleMessage)
				.append(seasonMessage)
				.append(apiMessage)
				.toString();
	}

	public String buildURI(String titleName, int seasonNumber, int episodeNumber) {
		var pageMessage = "http://www.omdbapi.com/?t=";
		var apiMessage = "&apikey=347e1780";
		var titleMessage = titleName.toLowerCase().replace(' ', '-');
		var seasonMessage = "&Season=" + seasonNumber;
		var episodeMessage = "&Episode=" + episodeNumber;

		return new StringBuilder()
				.append(pageMessage)
				.append(titleMessage)
				.append(seasonMessage)
				.append(episodeMessage)
				.append(apiMessage)
				.toString();
	}
}
