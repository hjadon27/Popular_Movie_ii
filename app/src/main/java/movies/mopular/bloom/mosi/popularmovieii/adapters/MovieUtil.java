package movies.mopular.bloom.mosi.popularmovieii.adapters;

import org.json.JSONObject;

import movies.mopular.bloom.mosi.popularmovieii.pojo.Movie;

/**
 * Created by DELL on 03-01-2016.
 */
public class MovieUtil {

    public static Movie jsonObjToMovie(JSONObject jsonMovieObject){
        Movie movie = new Movie();
         movie.setId(jsonMovieObject.optString("id").toString());
         movie.setPoster_path(jsonMovieObject.optString("poster_path").toString());
         movie.setOverview(jsonMovieObject.optString("overview").toString());
         movie.setRelease_date(jsonMovieObject.optString("release_date").toString());
         movie.setGenre_ids(jsonMovieObject.optString("genre_ids").toString());
         movie.setOriginal_title(jsonMovieObject.optString("original_titl").toString());
         movie.setTitle(jsonMovieObject.optString("title").toString());
         movie.setBackdrop_path(jsonMovieObject.optString("backdrop_path").toString());
         movie.setPopularity(jsonMovieObject.optString("popularity").toString());
         movie.setVote_count(jsonMovieObject.optString("vote_count").toString());
         movie.setVideo(jsonMovieObject.optString("video").toString());
         movie.setVote_average(jsonMovieObject.optString("vote_average").toString());
        return movie;
    }
}
