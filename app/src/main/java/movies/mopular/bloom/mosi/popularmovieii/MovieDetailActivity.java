package movies.mopular.bloom.mosi.popularmovieii;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import movies.mopular.bloom.mosi.popularmovieii.adapters.MovieUtil;
import movies.mopular.bloom.mosi.popularmovieii.adapters.ReviewAdapter;
import movies.mopular.bloom.mosi.popularmovieii.adapters.TrailerAdapter;
import movies.mopular.bloom.mosi.popularmovieii.async.tasks.GetJSONListener;
import movies.mopular.bloom.mosi.popularmovieii.async.tasks.GetReviewJSONListener;
import movies.mopular.bloom.mosi.popularmovieii.async.tasks.JSONClient;
import movies.mopular.bloom.mosi.popularmovieii.async.tasks.ReviewJSONClient;
import movies.mopular.bloom.mosi.popularmovieii.pojo.Movie;
import movies.mopular.bloom.mosi.popularmovieii.pojo.MovieMap;

public class MovieDetailActivity extends AppCompatActivity {
    public static boolean isTraileUrl;

    Movie movieObject;
    public static String trailerUrl;
    public static String reviewUrl;

    @Bind(R.id.iv_movie_poster)ImageView ivPoster;
    @Bind(R.id.tv_movie_title)TextView tvTitle;
    @Bind(R.id.tv_release_date)TextView tvReleaseDate;
    @Bind(R.id.tv_vote)TextView tvVote;
    @Bind(R.id.tv_plot)TextView tvPlot;
    @Bind(R.id.tv_average)TextView tvAverage;
    @Bind(R.id.lv_trailer)ListView lvTrailers;
    @Bind(R.id.lv_review)ListView lvReview;
    @Bind(R.id.ll_trailers)LinearLayout vllTrailer;
    @Bind(R.id.ll_review)LinearLayout vllReview;
    @Bind(R.id.favoriteimageButton)ImageView favImageView;
    @Bind(R.id.scroll_movie_details)ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        //read data from favorite local file
        readFavMovieList();
        Log.d("Map on create :", String.valueOf(MovieMap.createMovie().favMovieMap));
        Intent intent = getIntent();
        int position = intent.getIntExtra("position", 0);
        isTraileUrl = true;
        JSONObject movieJsonObj = JSONClient.json_movie_list.get(position);

        if(MovieMainActivity.isFavourite) {
            movieObject = MovieMainActivity.favMovieObjList.get(position);
        }else{
            // converting json to movie pojo
            movieObject = MovieUtil.jsonObjToMovie(movieJsonObj);
        }
        getSupportActionBar().setTitle(movieObject.getTitle());
        getTrailerUrl(movieObject.getId());
        getReviewUrl(movieObject.getId());
        executeWithNewJSONClient();
        executeWithNewReviewJSONClient();
        ButterKnife.bind(this);
        if(MovieMap.favMovieMap.containsKey(movieObject.getId())){
            favImageView.setTag("fav");
            favImageView.setBackgroundResource(R.drawable.fav);
        }
        lvTrailers.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        playTrailer(JSONClient.json_trailer_list.get(position).optString("key").toString());
                    }
                }
        );
        String posterPath = "http://image.tmdb.org/t/p/w185/";
        Picasso.with(this).load(posterPath + movieObject.getPoster_path()).
                resize(260, 380).into(ivPoster);
        tvTitle.setText(movieObject.getTitle());
        tvReleaseDate.setText("Relased on : " + movieObject.getRelease_date());
        tvVote.setText("Vote : " + movieObject.getVote_count());
        tvAverage.setText("Average : " + movieObject.getVote_average());
        tvPlot.setText(movieObject.getOverview());

    }

    public String getTrailerUrl(String movieId) {
        trailerUrl = "http://api.themoviedb.org/3/movie/" + movieId + "/videos?api_key=" +
                MovieMainActivity.API_KEY;
        return trailerUrl;
    }

    public String getReviewUrl(String movieId) {
        reviewUrl = "http://api.themoviedb.org/3/movie/" + movieId + "/reviews?api_key=" +
                MovieMainActivity.API_KEY;
        return reviewUrl;
    }


    void executeWithNewJSONClient(){
        JSONClient client = new JSONClient(this, jsonListener);
        client.execute(trailerUrl);
    }

    void executeWithNewReviewJSONClient(){
        ReviewJSONClient client = new ReviewJSONClient(this, jsonReviewListener);
        client.execute(reviewUrl);
    }

    GetJSONListener jsonListener = new GetJSONListener(){

        @Override
        public void onRemoteCallComplete(JSONObject jsonFromNet) {
                lvTrailers.setAdapter(new TrailerAdapter(getBaseContext()));
//            }
        }
    };

    GetReviewJSONListener jsonReviewListener = new GetReviewJSONListener(){

        @Override
        public void onRemoteCallComplete(JSONObject jsonFromNet) {
            lvReview.setAdapter(new ReviewAdapter(getBaseContext()));
//            }
        }
    };

    public void playTrailer(String key){
//        String key = "OMOVFvcNfvE";
        String trailerUrl = "http://www.youtube.com/watch?v=" + key;
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(trailerUrl)));
    }

    public void showTrailers(View view){
        vllReview.setVisibility(View.GONE);
        scrollView.setVisibility(View.GONE);
        vllTrailer.setVisibility(View.VISIBLE);

    }
    public void showReviews(View view) {
        scrollView.setVisibility(View.GONE);
        vllTrailer.setVisibility(View.GONE);
        vllReview.setVisibility(View.VISIBLE);
//        isTraileUrl = false;
//        executeWithNewJSONClient(reviewUrl);

    }

    @Override
    public void onBackPressed() {
        if(vllTrailer.getVisibility()==View.INVISIBLE) {
            super.onBackPressed();
        }
        scrollView.setVisibility(View.VISIBLE);
        vllReview.setVisibility(View.INVISIBLE);
        vllTrailer.setVisibility(View.INVISIBLE);
    }

    public void setInvisible(View view){
        lvTrailers.setVisibility(View.INVISIBLE);
    }


    public void markFav(View view) {
        if(favImageView.getTag()!="fav") {
            favImageView.setTag("fav");
            favImageView.setBackgroundResource(R.drawable.fav);
            MovieMap.createMovie().favMovieMap.put(movieObject.getId(), movieObject);
        }else {
            favImageView.setTag("nofav");
            favImageView.setBackgroundResource(R.drawable.no_fav);
            MovieMap.createMovie().favMovieMap.remove(movieObject.getId());
        }
            saveFavMovieList();
    }

    void saveFavMovieList(){
        try {
            FileOutputStream fos = this.openFileOutput("Moviemap.ser", Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(MovieMap.favMovieMap);
            os.close();
            fos.close();
            Log.d("Fav List insert File:", String.valueOf(MovieMap.createMovie().favMovieMap));
            readFavMovieList();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch ( IOException e){
        e.printStackTrace();
    }
    }

    public void readFavMovieList(){
        FileInputStream fis = null;
        ObjectInputStream is = null;
        try {
            fis = this.openFileInput("Moviemap.ser");
        is = new ObjectInputStream(fis);
        MovieMap.createMovie().favMovieMap = (Map<String, Movie>) is.readObject();
            Log.d("Fave List from File: ", String.valueOf(MovieMap.createMovie().favMovieMap));
        is.close();
        fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (  ClassNotFoundException e){
            e.printStackTrace();
        }catch ( IOException e){
            e.printStackTrace();
        }finally {

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isTraileUrl = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isTraileUrl = true;
    }

}
