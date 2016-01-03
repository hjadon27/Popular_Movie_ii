package movies.mopular.bloom.mosi.popularmovieii.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by DELL on 01-01-2016.
 */
public class FavouriteMovieList extends Activity{

    static Context mContext;

    public static FavouriteMovieList favouriteMovies;

    public static Map<String, JSONObject> favouriteMoviesMap;
    public SharedPreferences mPrefs =  mContext.getSharedPreferences("favMovieList", Context.MODE_PRIVATE);
//    public static JSONObject favMovie;


    private FavouriteMovieList(){
        favouriteMoviesMap = new HashMap<>();

    }

    public Map<String, JSONObject> getFavMovieList(Context context){
        mContext = context;
        Gson gson = new Gson();
        String json = mPrefs.getString("favMovieList", "");
        favouriteMoviesMap = gson.fromJson(json, HashMap.class);
        return favouriteMoviesMap;
    }

    public static synchronized void create(Context context){
        mContext = context;
        if(favouriteMovies == null){
            favouriteMovies = new FavouriteMovieList();
        }
        favouriteMovies.getFavMovieList(mContext);
    }


    public static synchronized void addToFavList(JSONObject favMovie, Context context){
        mContext = context;
        if(favouriteMovies == null){
            favouriteMovies = new FavouriteMovieList();
        }
        String movieId = favMovie.optString("id").toString();
        movieId.replace("\"", "");
        favouriteMoviesMap.put(movieId,favMovie);
        Log.d("Fav List added :", String.valueOf(favouriteMoviesMap));
        favouriteMovies.saveFavMovieList();
    }

    public static synchronized void removeFromFavList(JSONObject favMovie, Context context){
        mContext = context;
        if(favouriteMovies != null){
            favouriteMoviesMap.remove(favMovie);
            favouriteMovies.saveFavMovieList();
        }
        Log.d("Fav List Removed:", String.valueOf(favouriteMoviesMap.size()));
    }

    void saveFavMovieList(){
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(favouriteMoviesMap); // myObject - instance of MyObjec
        Log.d("Before saving::: ", json);
        prefsEditor.putString("favMovieList", json);
        prefsEditor.commit();

        json = mPrefs.getString("favMovieList", "");
        favouriteMoviesMap = gson.fromJson(json, HashMap.class);
        Log.d("Fav List from Pref:", String.valueOf(favouriteMoviesMap));

    }
}
