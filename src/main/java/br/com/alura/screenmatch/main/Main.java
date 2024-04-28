package br.com.alura.screenmatch.main;

import br.com.alura.screenmatch.model.Episode;
import br.com.alura.screenmatch.model.SeasonRec;
import br.com.alura.screenmatch.model.Series;
import br.com.alura.screenmatch.model.SeriesRec;
import br.com.alura.screenmatch.repository.SeriesRepository;
import br.com.alura.screenmatch.service.ConsumeAPI;
import br.com.alura.screenmatch.service.ConvertData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    
    private static final  String  PAGE_MESSAGE = "https://www.omdbapi.com/?t=";
    private static final  String API_MESSAGE = "&apikey=347e1780";
    
    private final ConsumeAPI consumeAPI = new ConsumeAPI();
    private final Scanner scanner = new Scanner(System.in);
    
    private final ConvertData converter = new ConvertData();
    
    private final SeriesRepository seriesRepository;
    
    private List<Series> searchedSeries = new ArrayList<>();
    
    public Main(SeriesRepository seriesRepository) {
        this.seriesRepository = seriesRepository;
    }
    
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
        System.out.println("Enter the name of the TV series you're looking for, or enter '0' to return to the main menu:");
        var searchedTitle = scanner.nextLine();
        
        try {
            if (Integer.valueOf(searchedTitle) == 0) {
                return;
            }
        } catch (NumberFormatException e){}
        
        var series = getSeries(searchedTitle);
        System.out.println(series != null ? series : "Sorry, we couldn't find the series you're looking for. Please check the spelling and try again.");
    }
    
    private void searchSeason() {
        System.out.println("Enter the ID of the TV series you're looking for, or enter '0' to return to the main menu:");
        printSearchedSeries();
        var searchedId = scanner.nextLong();
        
        try {
            if (Long.valueOf(searchedId) == 0) {
                return;
            }
        } catch (NumberFormatException e){}
        
        var series = searchedSeries.stream()
                .filter(s -> s.getId() == searchedId)
                .findFirst();

        if (series.isEmpty()) {
            System.out.println("Not a valid ID.");
            return;
        }
        
        var findedSeries = series.get();
        
        List<SeasonRec> seasons = new ArrayList<>();
        
        for(int i = 1; i <= findedSeries.getTotalSeasons(); i++){
            seasons.add(getSeason(findedSeries.getTitle(), i));
        }
        
        seasons.stream()
               .sorted(Comparator.comparing(SeasonRec::seasonNumber))
               .forEach(System.out::println);
	    
    }
    
    private void printSearchedSeries() {
        searchedSeries = seriesRepository.findAll();
        searchedSeries.stream()
                .sorted(Comparator.comparing(Series :: getRating).reversed())
                .forEach(System.out::println);
    }
    
    private Series getSeries(String titleName) {
        try {
            var uri = buildURI(titleName);
            var json = consumeAPI.getData(uri);
            
            if (!json.contains("Error")) {
                var seriesRec = converter.getData(json, SeriesRec.class);
                var series = new Series(seriesRec);
                
                List<SeasonRec> seasons = new ArrayList<>();
                
                for(int i = 1; i <= series.getTotalSeasons(); i++){
                    seasons.add(getSeason(series.getTitle(), i));
                }
                
                List<Episode> episodes = seasons.stream()
                        .flatMap(s -> s.episodes().stream()
                                .map(e -> new Episode(Integer.valueOf(s.seasonNumber()), e)))
                        .collect(Collectors.toList());
                
                series.setEpisodes(episodes);
                seriesRepository.save(series);
                
                return series;
            } else {
                System.out.println("Error in API response: " + json);
                return null;
            }
        } catch (Exception e) {
            System.out.println("Error in getSeries: " + e.getMessage());
            e.printStackTrace();
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
