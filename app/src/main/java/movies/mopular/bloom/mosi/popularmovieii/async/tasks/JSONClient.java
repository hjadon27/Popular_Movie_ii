package movies.mopular.bloom.mosi.popularmovieii.async.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import movies.mopular.bloom.mosi.popularmovieii.MovieDetailActivity;
import movies.mopular.bloom.mosi.popularmovieii.MovieMainActivity;

/**
 * Created by DELL on 29-12-2015.
 */
public class JSONClient extends AsyncTask<String, Void, JSONObject> {

    ProgressDialog progressDialog ;
    GetJSONListener getJSONListener;
    Context curContext;
    HttpURLConnection urlConnection;
    BufferedReader reader = null;
    String jasonString;
    public static List<JSONObject> json_movie_list;
    public static List<JSONObject> json_trailer_list;

    public JSONClient(Context context, GetJSONListener listener){
        this.getJSONListener = listener;
        curContext = context;
    }

    @Override
    public void onPreExecute() {
        progressDialog = new ProgressDialog(curContext);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        makeJason(getJasonString());
        return null;
    }

    public String getJasonString(){

        try {
            URL url = null;
            if(MovieMainActivity.isMovieUrl) {//movie
                url = new URL(MovieMainActivity.movieUrl);
            }else if(MovieDetailActivity.isTraileUrl) {
                url = new URL(MovieDetailActivity.trailerUrl);
            }
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }
            jasonString = buffer.toString();
        } catch (IOException e) {
            Log.e("PlaceholderFragment", "Error ", e);
            return null;
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("PlaceholderFragment", "Error closing stream", e);
                }
            }
        }
        return jasonString;
    }

    public void makeJason(String jasonString){
        try {
            if(MovieMainActivity.isMovieUrl) {
                json_movie_list = new ArrayList<>();
            }else if(MovieDetailActivity.isTraileUrl) {json_trailer_list = new ArrayList<>();}
            JSONObject jsonRootObject = new JSONObject(jasonString);
            //Get the instance of JSONArray that contains JSONObjects
            JSONArray jsonArray = jsonRootObject.optJSONArray("results");

            //Iterate the jsonArray and print the info of JSONObjects
            for(int i=0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if(MovieMainActivity.isMovieUrl) {
                    json_movie_list.add(jsonObject);
                }else if(MovieDetailActivity.isTraileUrl) {
                    json_trailer_list.add(jsonObject);
                }
            }
        } catch (JSONException e) {e.printStackTrace();}
    }

    @Override
    protected void onPostExecute(JSONObject json ) {
        getJSONListener.onRemoteCallComplete(json);
        progressDialog.dismiss();
    }

}
