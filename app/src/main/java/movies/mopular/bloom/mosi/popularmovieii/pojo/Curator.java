package movies.mopular.bloom.mosi.popularmovieii.pojo;

import java.util.List;

/**
 * Created by Harendra Kumar on 03-01-2016.
 */
public class Curator {
    public String page;
    public List<Dataset> results;

    public class Dataset{
        String id;
        String poster_path;
        String overview;
        String release_date;
        String genre_ids;
        String original_title;
        String title;
        String backdrop_path;
        String popularity;
        String vote_count;
        String video;
        String vote_average;
    }
}
