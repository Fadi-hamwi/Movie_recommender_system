package edu.vandy.recommender.movies;

import edu.vandy.recommender.movies.model.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static edu.vandy.recommender.movies.Constants.EndPoint.*;

/**
 * The Spring controller for the {@link MoviesService}, whose endpoint
 * handler methods return a {@link List} of objects containing
 * information about movies.
 *
 * {@code @RestController} is a convenience annotation for creating
 * Restful controllers. It is a specialization of {@code @Component}
 * and is automatically detected through classpath scanning. It adds
 * the {@code @Controller} and {@code @ResponseBody} annotations. It
 * also converts responses to JSON or XML.
 */
@RestController
public class MoviesController {
    /**
     * A central interface that provides configuration for this
     * microservice and is read-only while the application is running,
     */
    ApplicationContext applicationContext;

    @Autowired
    private MoviesService service;

    /**
     * A request for testing Eureka connection.
     *
     * @return The application name.
     */
    @GetMapping({"/", "/actuator/info"})
    ResponseEntity<String> info() {
        // Indicate the request succeeded.  and return the application
        // name.
        return ResponseEntity
            .ok(applicationContext.getId() 
                + " is alive\n");
    }

    /**
     * @return A {@link List} of all movies
     */
    @GetMapping(GET_ALL_MOVIES)
    public List<Movie> getMovies() {
        return service.getMovies();
    }

    /**
     * Search for movie titles in the database containing the given
     * query {@link String}.
     *
     * @param query The search query
     * @return A {@link List} of movie titles containing the query
     *         represented as {@link Movie} objects
     */
    @GetMapping(GET_SEARCH + SEARCH_QUERY)
    public List<Movie> search(@PathVariable String query) {
        return service.search(query);
    }

    /**
     * Search for movie titles in the database containing the given
     * {@link String} queries
     *
     * @param queries The {@link List} of search queries
     * @return A {@link List} of movie titles containing the queries
     *         represented as {@link Movie} objects
     */
    @GetMapping(GET_SEARCHES)
    public List<Movie> search(@RequestParam(QUERY_PARAM) List<String> queries) {
        return service.search(queries);
    }
}
