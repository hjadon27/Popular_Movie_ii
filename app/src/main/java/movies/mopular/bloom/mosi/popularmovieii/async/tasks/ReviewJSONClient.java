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

/**
 * Created by DELL on 01-01-2016.
 */
public class ReviewJSONClient extends AsyncTask<String, Void, JSONObject> {

    ProgressDialog progressDialog ;
    GetReviewJSONListener getJSONListener;
    Context curContext;
    HttpURLConnection urlConnection;
    BufferedReader reader = null;
    String jasonString;
    public static List<JSONObject> json_review_list;

    public ReviewJSONClient(Context context, GetReviewJSONListener listener){
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
                url = new URL(MovieDetailActivity.reviewUrl);
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

    void makeJason(String jasonString){
        try {
            json_review_list = new ArrayList<>();
            JSONObject jsonRootObject = new JSONObject(jasonString);

            //Get the instance of JSONArray that contains JSONObjects
            JSONArray jsonArray = jsonRootObject.optJSONArray("results");

            //Iterate the jsonArray and print the info of JSONObjects
            for(int i=0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                    json_review_list.add(jsonObject);
                Log.d(" Review Added ###### : ", jsonObject.optString("author").toString());
            }
        } catch (JSONException e) {e.printStackTrace();}
    }

    @Override
    protected void onPostExecute(JSONObject json ) {
        getJSONListener.onRemoteCallComplete(json);
        progressDialog.dismiss();
    }
}
