package movies.mopular.bloom.mosi.popularmovieii;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import movies.mopular.bloom.mosi.popularmovieii.adapters.ImageAdapter;
import movies.mopular.bloom.mosi.popularmovieii.async.tasks.GetJSONListener;
import movies.mopular.bloom.mosi.popularmovieii.async.tasks.JSONClient;
import movies.mopular.bloom.mosi.popularmovieii.pojo.Movie;
import movies.mopular.bloom.mosi.popularmovieii.pojo.MovieMap;

public class MovieMainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    public static boolean isMovieUrl;
    public static boolean isFavourite;
    //will show movies poster
    @Bind(R.id.grid_view)GridView gridview;

    public static List<Movie> favMovieObjList;

    public static final String API_KEY = "Please add your own key!!!";
    public static String movieUrl =  "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=" + API_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_main);
        ButterKnife.bind(this);
        isMovieUrl = true;
        getSupportActionBar().setTitle("Popular Movies");

        executeWithNewJSONClient();
        gridview.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        Intent intent = new Intent(MovieMainActivity.this, MovieDetailActivity.class);
                        intent.putExtra("position", position);
                        startActivity(intent);
                    }
                }
        );
    }


    //set new movieUrl api call with correct sort by value
    void setUrl(String sortBy){
        movieUrl = "http://api.themoviedb.org/3/discover/movie?sort_by="+ sortBy +".desc&api_key=" + API_KEY;
    }

    void executeWithNewJSONClient(){
        JSONClient client = new JSONClient(this, jsonListener);
        client.execute(movieUrl);
    }

    GetJSONListener jsonListener = new GetJSONListener(){

        @Override
        public void onRemoteCallComplete(JSONObject jsonFromNet) {
            // add code to act on the JSON object that is returned
            gridview.setAdapter(new ImageAdapter(getBaseContext()));
        }

    };

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        setUrl(item.toLowerCase().replace(" ", ""));
        // Showing selected spinner item
        if(JSONClient.json_movie_list!=null && MovieMainActivity.isFavourite==false) {
            executeWithNewJSONClient();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    void sortBy(String sortBy){
        setUrl(sortBy);
        if(JSONClient.json_movie_list!=null) {
            JSONClient client = new JSONClient(this, jsonListener);
            client.execute(movieUrl);
        }
    }

    void showFav(){
        try {
            readFavMovieList();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        favMovieObjList = new ArrayList<>(MovieMap.favMovieMap.values());
        gridview.setAdapter(new ImageAdapter(getBaseContext()));
    }

    public void readFavMovieList() throws IOException, ClassNotFoundException {
        FileInputStream fis = null;
        try {
            fis = this.openFileInput("Moviemap.ser");
            ObjectInputStream is = new ObjectInputStream(fis);
            MovieMap.createMovie().favMovieMap = (Map<String, Movie>) is.readObject();
            Log.d("Fave List from File: ", String.valueOf(MovieMap.createMovie().favMovieMap));
            is.close();
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_sort_popularity) {
            isFavourite = false;
            sortBy("popularity");
            getSupportActionBar().setTitle("Popular Movies");
        }if (id == R.id.action_sort_ratings) {
            isFavourite = false;
            sortBy("highestrated");
            getSupportActionBar().setTitle("Movies By Rating");
        }if(id == R.id.action_show_favourite){
            isFavourite = true;
            showFav();
            getSupportActionBar().setTitle("My Favorite");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isMovieUrl = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isMovieUrl = true;
    }
}
