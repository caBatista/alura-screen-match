package br.com.alura.screenmatch.main;

import br.com.alura.screenmatch.model.SeasonRec;
import br.com.alura.screenmatch.model.Series;
import br.com.alura.screenmatch.model.SeriesRec;
import br.com.alura.screenmatch.service.ConsumeAPI;
import br.com.alura.screenmatch.service.ConvertData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Main {
    
    private static final  String  PAGE_MESSAGE = "https://www.omdbapi.com/?t=";
    private static final  String API_MESSAGE = "&apikey=347e1780";
    
    private final ConsumeAPI consumeAPI = new ConsumeAPI();
    private final Scanner scanner = new Scanner(System.in);
    
    private final ConvertData converter = new ConvertData();
    
    private final List<Series> searchedSeries = new ArrayList<>();
    
    public void showMenu(){
        System.out.println("""
                Welcome to our TV series details application! \uD83D\uDCFA\s
                Get ready to dive into the captivating world of your favorite shows.\s
                Whether you're searching for plot summaries, cast information, or simply curious trivia, you're in the right place.\s
                Sit back, relax, and let's embark on an exciting journey through the realm of television together!""");

        var option = -1;
        
        while (option != 0) {
            System.out.println("""
                    
                    Please select one of the following options:
                    1 - Search for a TV series details
                    2 - Search for a TV series season details
                    3 - Print the searched TV series
                    0 - Exit""");

            while (!scanner.hasNextInt()) {
                System.out.println("Invalid option. Please try again.");
                scanner.next();
            }
            
            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1 -> searchSeries();
                case 2 -> searchSeason();
                case 3 -> printSearchedSeries();
                case 0 -> System.out.println("Goodbye! \uD83D\uDC4B");
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }
    
    private void searchSeries() {
        System.out.println("Enter the name of the TV series you want to search for:");
        var titleName = scanner.nextLine();

        var series = getSeries(titleName);
        System.out.println(series != null ? series : "Series not found.");
    }
    
    private void searchSeason() {
        System.out.println("Enter the name of the TV series you want to search for:");
        var titleName = scanner.nextLine();
        var series = getSeries(titleName);

        if (series == null) {
            System.out.println("Series not found.");
            return;
        }
        
        List<SeasonRec> seasons = new ArrayList<>();
        
        for(int i = 1; i <= series.getTotalSeasons(); i++){
            seasons.add(getSeason(titleName, i));
        }
        
        seasons.stream()
               .sorted(Comparator.comparing(SeasonRec::seasonNumber))
               .forEach(System.out::println);
    }
    
    private void printSearchedSeries() {
        searchedSeries.stream()
                        .sorted(Comparator.comparing(Series::getRating).reversed())
                        .forEach(System.out::println);
    }
    
    private Series getSeries(String titleName) {
        var uri = buildURI(titleName);
        var json = consumeAPI.getData(uri);
        
        if (!json.contains("Error")) {
            var seriesRec = converter.getData(json, SeriesRec.class);
            var series = new Series(seriesRec);
            
            var seriesExists = searchedSeries.stream()
                                             .anyMatch(s -> s.getTitle().equals(series.getTitle()));
            
            if(!seriesExists){
                searchedSeries.add(series);
            }
            
            return series;
        } else {
            return null;
        }
    }
    
    private SeasonRec getSeason(String titleName, int seasonNumber) {
        var uri = buildURI(titleName, seasonNumber);
        var json = consumeAPI.getData(uri);

        if (json != null) {
            return converter.getData(json, SeasonRec.class);
        } else {
            return null;
        }
    }
    
    private String buildURI(String titleName) {
        var titleMessage = titleName.toLowerCase().replace(' ', '-');

        return PAGE_MESSAGE +
               titleMessage +
               API_MESSAGE;
    }

    private String buildURI(String titleName, int seasonNumber) {
        var titleMessage = titleName.toLowerCase().replace(' ', '-');
        var seasonMessage = "&Season=" + seasonNumber;

        return PAGE_MESSAGE +
               titleMessage +
               seasonMessage +
               API_MESSAGE;
    }
}
