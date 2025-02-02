package edu.vandy.recommender.movies;

import edu.vandy.recommender.movies.model.Movie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This program tests the {@link MoviesSyncProxy} and its ability to
 * communicate with the {@link MoviesController} via Spring WebMVC
 * features.
 *
 * The {@code @SpringBootTest} annotation tells Spring to look for a
 * main configuration class (a {@code @SpringBootApplication}, i.e.,
 * {@link MoviesApplication}) and use that to start a Spring
 * application context to serve as the target of the tests.
 *
 * The {@code @SpringBootConfiguration} annotation indicates that a
 * class provides a Spring Boot application {@code @Configuration}.
 */
@SpringBootConfiguration
@SpringBootTest(classes = MoviesApplication.class,
                webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class MoviesTest {
    @Autowired
    private MoviesSyncProxy mMoviesSyncProxy;

    @Autowired
    Map<String, List<Double>> mMovies;

    @Test
    public void testGetMoviesSize() {
        var movies = mMoviesSyncProxy
            .getMovies();

        assertEquals(4801,
                     movies.size());
    }

    @Test
    public void testGetMoviesContents() {
        var movies = mMoviesSyncProxy
            .getMovies();

        int i = 0;
        for (var movie : mMovies.entrySet()) {
            assertEquals(movie.getKey(),
                         movies.get(i).id());
            assertIterableEquals(movie.getValue(),
                                 movies.get(i++).vector());
        }
    }

    @Test
    public void testSearchMoviesSize() {
        var searchWord = "Star";
        var matchingMovies = mMoviesSyncProxy
            .searchMovies(searchWord);

        assertEquals(33,
                     matchingMovies.size());
    }

    @Test
    public void testSearchMoviesContents() {
        var searchWord = "Star";
        var matchingMovies = mMoviesSyncProxy
            .searchMovies(searchWord);

        var iterator =
            mMovies.entrySet().iterator();
        for (Movie matchingMovie : matchingMovies) {
            String matchedMovie = "";

            while (iterator.hasNext()) {
                var nextMovie = iterator.next().getKey();
                if (nextMovie.toLowerCase()
                    .contains(searchWord.toLowerCase())) {
                    matchedMovie = nextMovie;
                    break;
                }
            }

            assertEquals(matchedMovie,
                         matchingMovie.id());
                         
        }
    }

    @Test
    public void testSearchMoviesManySize() {
        var watchedMovies = List
            .of("Trek", "War");

        var matchingMovies = mMoviesSyncProxy
            .searchMovies(watchedMovies);

        assertEquals(65,
                     matchingMovies.size());
    }

    @Test
    public void testSearchMoviesManyContents() {
        // Test data: List of search terms
        var watchedMovies = List.of("Trek", "War");

        // Call the method under test
        var matchingMovies = mMoviesSyncProxy.searchMovies(watchedMovies);

        // Collect all movies from mMovies that match any of the search terms
        var expectedMatchingMovies = new ArrayList<String>();
        for (var entry : mMovies.entrySet()) {
            String movieId = entry.getKey();
            for (var searchWord : watchedMovies) {
                if (movieId.toLowerCase().contains(searchWord.toLowerCase())) {
                    expectedMatchingMovies.add(movieId);
                    break; // No need to check other search words for this movie
                }
            }
        }

        // Verify that all expected movies are in the matchingMovies list
        for (var expectedMovieId : expectedMatchingMovies) {
            boolean found = false;
            for (var matchingMovie : matchingMovies) {
                if (matchingMovie.id().equals(expectedMovieId)) {
                    found = true;
                    break;
                }
            }
            assertTrue(found, "Movie with ID " + expectedMovieId + " should be included in matchingMovies");
        }
    }
}
    
