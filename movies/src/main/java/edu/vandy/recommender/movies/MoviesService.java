package edu.vandy.recommender.movies;

import edu.vandy.recommender.movies.model.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This class defines implementation methods that are called by the
 * {@link MoviesController} to return a {@link List} of objects
 * containing information about movies.
 *
 * This class is annotated as a Spring {@code @Service}, which enables
 * the automatic detection and wiring of dependent implementation
 * classes via classpath scanning.  It also includes its name in the
 * {@code @Service} annotation below so that it can be identified as a
 * service.
 */
@Service
public class MoviesService {
    /**
     * This auto-wired field connects the {@link MoviesService} to the
     * {@link List} of {@link Movie} objects.
     */
    @Autowired
    List<Movie> mMovies;


    /**
     * @return A {@link List} of all the movies
     */
    public List<Movie> getMovies() {
        return mMovies;
    }

    /**
     * Search for movie titles containing the given query {@link
     * String} using the Java sequential streams framework.
     *
     * @param query The search query
     * @return A {@link List} of {@link Movie} objects containing the
     *         query
     */
    public List<Movie> search(String query) {
        return mMovies
                .stream()
                .filter(movie -> movie.id().toLowerCase().contains(query.toLowerCase()))
                .distinct()
                .toList();
    }

    /**
     * Search for movie titles in the database containing the given
     * {@link String} queries using the Java sequential streams
     * framework.
     *
     * @param queries The search queries
     * @return A {@link List} of {@link Movie} objects containing the
     *         queries
     */
    public List<Movie> search(List<String> queries) {

        return queries
                .stream()
                .flatMap(q -> search(q).stream())
                .distinct()
                .toList();
   }
}
