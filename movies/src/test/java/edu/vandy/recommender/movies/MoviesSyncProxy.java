package edu.vandy.recommender.movies;

import edu.vandy.recommender.movies.model.Movie;
import edu.vandy.recommender.movies.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

import static edu.vandy.recommender.movies.Constants.EndPoint.*;
import static edu.vandy.recommender.movies.Constants.MOVIES_BASE_URL;

/**
 * This class provides proxies to various endpoints in the 'movies'
 * microservice.
 */
@Component
public class MoviesSyncProxy {
    @Autowired
    private RestTemplate mMoviesRestTemplate;

    /**
     * @return A {@link List} of {@link Movie} objects
     */
    List<Movie> getMovies() {

        String url = UriComponentsBuilder.fromPath(GET_ALL_MOVIES)
                .build()
                .toUriString();

        List<Movie> movies = WebUtils.makeGetRequestList(mMoviesRestTemplate, url, Movie[].class);

        if (movies == null)
            throw new IllegalStateException
                ("Unable to retrieve movies from 'movies' microservice.");

        return movies;
    }

    /**
     * Search for movie titles in the database containing the given
     * query {@link String}.
     *
     * @param query The {@link String} to search for
     * @return A {@link List} of {@link Movie} objects that match the
     *         query
     */
    List<Movie> searchMovies(String query) {

        String url = UriComponentsBuilder.fromPath(GET_SEARCH + "/" + query)
                                .build()
                                .toUriString();

        List<Movie> matchingMovies = WebUtils.makeGetRequestList(mMoviesRestTemplate, url, Movie[].class);

        if (matchingMovies == null)
            throw new IllegalStateException
                    ("Unable to retrieve movies from 'movies' microservice.");
        return matchingMovies;
    }

    /**
     * Search for movie titles in the database containing the given
     * {@link List} of queries.
     *
     * @param queries The {@link List} queries to search for
     * @return A {@link List} of {@link Movie} objects that match the
     *         queries
     */
    List<Movie> searchMovies(List<String> queries) {

        String url = UriComponentsBuilder
                .fromPath(GET_SEARCHES)
                .queryParam(QUERY_PARAM, WebUtils.list2String(queries))
                .build()
                .toUriString();

        List<Movie> matchingMovies = WebUtils.makeGetRequestList(mMoviesRestTemplate, url, Movie[].class);

        if (matchingMovies == null)
            throw new IllegalStateException
                    ("Unable to retrieve movies from 'movies' microservice.");

        return matchingMovies;
    }
}

