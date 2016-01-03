package movies.mopular.bloom.mosi.popularmovieii.pojo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by DELL on 02-01-2016.
 */
public class MovieMap implements Serializable{
    public static Map<String, Movie> favMovieMap;

    private MovieMap(){
        favMovieMap = new HashMap<>();
    }
    private static MovieMap movieMap;

    public static MovieMap createMovie(){
        if(movieMap ==null){
            movieMap = new MovieMap();
        }
        return movieMap;
    }
}
