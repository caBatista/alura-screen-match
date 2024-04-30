package br.com.alura.screenmatch.main;

import br.com.alura.screenmatch.model.Episode;
import br.com.alura.screenmatch.model.SeasonRec;
import br.com.alura.screenmatch.model.Series;
import br.com.alura.screenmatch.model.SeriesRec;
import br.com.alura.screenmatch.repository.SeriesRepository;
import br.com.alura.screenmatch.service.ConsumeAPI;
import br.com.alura.screenmatch.service.ConvertData;

import java.io.IOException;
import java.util.*;

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
            System.out.println("\nPlease select one of the following options by entering the corresponding number:");
            System.out.println("1 - Search for a TV series details");
            System.out.println("2 - Search for a TV series season details");
            System.out.println("3 - Print the searched TV series");
            System.out.println("4 - Search for a new TV series");
            System.out.println("0 - Exit the application");
            System.out.println();

            while (!scanner.hasNextInt()) {
                System.out.println("Invalid option. Please try again.");
                scanner.next();
            }
            
            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1 -> searchExistingSeries();
                case 2 -> searchSeason();
                case 3 -> printSearchedSeries();
                case 4 -> searchNewSeries();
                case 0 -> System.out.println("Goodbye! \uD83D\uDC4B");
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }
    
    private void searchExistingSeries(){
        var series = searchSeries();
        
        if(series == null){
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
            return;
        }
        
        System.out.println(series);
        waitForEnter();
    }
    
    private Series searchSeries(){
        System.out.println("Enter the ID of the TV series you're looking for, or enter '0' to return to the main menu:");
        printSearchedSeries();
        
        if(!scanner.hasNextInt()){
            return null;
        }
        
        long searchedId = scanner.nextLong();
        
        if (searchedId == 0) {
            return null;
        }
        
        var series = searchedSeries.stream()
                .filter(s -> s.getId() == searchedId)
                .findFirst();
        
        if (series.isEmpty()) {
            System.out.println("Not a valid ID.");
            return null;
        }
        
        return series.get();
    }
    
    private void searchNewSeries() {
        System.out.println("Enter the name of the TV series you're looking for, or enter '0' to return to the main menu:");
        if(scanner.hasNextInt() && scanner.nextInt() == 0){
            return;
        }
        
        var titleToSearch = scanner.nextLine();
        
        var series = importSeries(titleToSearch);
        System.out.println(series != null ? series : "");
        waitForEnter();
    }
    
    private void searchSeason() {
        var findedSeries = searchSeries();
        
        if(findedSeries == null){
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
            return;
        }
        
        List<SeasonRec> seasons = new ArrayList<>();
        
        for(int i = 1; i <= findedSeries.getTotalSeasons(); i++){
            seasons.add(getSeason(findedSeries.getTitle(), i));
        }
        
        seasons.stream()
               .sorted(Comparator.comparing(SeasonRec::seasonNumber))
               .forEach(System.out::println);

        waitForEnter();
	    
    }
    
    private void printSearchedSeries() {
        searchedSeries = seriesRepository.findAll();
        
        if(searchedSeries.isEmpty()){
            System.out.println("No series created yet. Import a series to see it here");
            return;
        }
        
        searchedSeries.stream()
                .sorted(Comparator.comparing(Series :: getId))
                .forEach(e -> System.out.println(e.menuToString()));
        
    }
    
    private Series importSeries(String title) {
        try {
            var databaseSeries = seriesRepository.findByTitleContainsIgnoreCase(title);
            
            if(!databaseSeries.isEmpty()){
                System.out.println("That series already exists on the database:");
                databaseSeries.forEach(e -> System.out.print("\n" + e.menuToString()));
                return null;
            }
            
            var uri = buildURI(title);
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
                        .toList();
                
                series.setEpisodes(episodes);
                seriesRepository.save(series);
                
                return series;
            } else {
                System.out.println("Error in API response: " + json);
                return null;
            }
        } catch (Exception e) {
            System.out.println("Error in importSeries: " + e.getMessage());
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
        
    private void waitForEnter() {
        System.out.println("\nPress ENTER to return to the menu...");
        try {
            System.in.read();
        } catch (IOException e) {
            System.out.println("ERROR");
        }
        
        for (int i = 0; i < 50; i++) {
            System.out.println();
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
