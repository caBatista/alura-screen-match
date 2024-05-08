package br.com.alura.screenmatch.repository;

import br.com.alura.screenmatch.model.Series;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeriesRepository extends JpaRepository<Series, Long> {
	List<Series> findByTitleContainsIgnoreCase(String title);
	
	List<Series> findTop5ByOrderByRatingDesc();
}
