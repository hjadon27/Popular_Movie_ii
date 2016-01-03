package movies.mopular.bloom.mosi.popularmovieii.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import movies.mopular.bloom.mosi.popularmovieii.MovieMainActivity;
import movies.mopular.bloom.mosi.popularmovieii.async.tasks.JSONClient;

/**
 * Created by DELL on 29-12-2015.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;


    public ImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        if(MovieMainActivity.isFavourite){
            return MovieMainActivity.favMovieObjList.size();
        }else
        return JSONClient.json_movie_list.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);

            imageView.setPadding(1, 1, 1, 1);
        } else {
            imageView = (ImageView) convertView;
        }
        String posterPath = "http://image.tmdb.org/t/p/w185/";
        JSONObject movieJson = null;
        if(MovieMainActivity.isFavourite ){
//            Movie movie = MovieMainActivity.favMovieObjList.get(position).getPoster_path();
            posterPath = posterPath + MovieMainActivity.favMovieObjList.get(position).getPoster_path();

        }else {
            movieJson = JSONClient.json_movie_list.get(position);
            posterPath = posterPath+movieJson.optString("poster_path").toString();
          }
        Picasso.with(mContext).load(posterPath).resize(parent.getWidth() / 2 - 2, 0).into(imageView);
    return imageView;

    }





}
