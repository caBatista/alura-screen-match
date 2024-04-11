package br.com.alura.screenmatch.main;

import br.com.alura.screenmatch.model.SeasonRec;
import br.com.alura.screenmatch.model.SeriesRec;
import br.com.alura.screenmatch.service.ConsumeAPI;
import br.com.alura.screenmatch.service.ConvertData;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private final String  PAGE_MESSAGE = "https://www.omdbapi.com/?t=";
    private final String API_MESSAGE = "&apikey=347e1780";

    private final ConsumeAPI consumeAPI = new ConsumeAPI();
    ConvertData converter = new ConvertData();

    public void showMenu(){
        Scanner scanner = new Scanner(System.in);

        System.out.println("""
                Welcome to our TV series details application! \uD83D\uDCFA\s
                Get ready to dive into the captivating world of your favorite shows.\s
                Whether you're searching for plot summaries, cast information, or simply curious trivia, you're in the right place.\s
                Sit back, relax, and let's embark on an exciting journey through the realm of television together!""");

        System.out.println("\nPlease specify the name of the TV series that you wanna know more about:");
        var titleName = scanner.nextLine();

        var uri = buildURI(titleName);

        var json = consumeAPI.getData(uri);

        SeriesRec seriesRec = converter.getData(json, SeriesRec.class);

        System.out.println(json);

        var titleType = seriesRec.type();

        System.out.println(titleType);

        if (!titleType.equals("series")) {
            System.out.println("The specified title is a " + titleType + ", not a series.\nFinishing application.");
            return;
        }

        System.out.println(seriesRec);

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

        seasons
//        seasons.forEach(s -> s.episodes().forEach(e -> System.out.println(e.title())));
    }

    public String buildURI(String titleName) {
        var titleMessage = titleName.toLowerCase().replace(' ', '-');

        return PAGE_MESSAGE +
               titleMessage +
               API_MESSAGE;
    }

    public String buildURI(String titleName, int seasonNumber) {
        var titleMessage = titleName.toLowerCase().replace(' ', '-');
        var seasonMessage = "&Season=" + seasonNumber;

        return PAGE_MESSAGE +
               titleMessage +
               seasonMessage +
               API_MESSAGE;
    }

    public String buildURI(String titleName, int seasonNumber, int episodeNumber) {

        var titleMessage = titleName.toLowerCase().replace(' ', '-');
        var seasonMessage = "&season=" + seasonNumber;
        var episodeMessage = "&episode=" + episodeNumber;

        return PAGE_MESSAGE +
               titleMessage +
               seasonMessage +
               episodeMessage +
               API_MESSAGE;
    }
}
