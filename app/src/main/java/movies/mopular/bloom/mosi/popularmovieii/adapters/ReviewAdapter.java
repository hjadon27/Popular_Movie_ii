package movies.mopular.bloom.mosi.popularmovieii.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONObject;

import movies.mopular.bloom.mosi.popularmovieii.R;
import movies.mopular.bloom.mosi.popularmovieii.async.tasks.ReviewJSONClient;

/**
 * Created by DELL on 01-01-2016.
 */
public class ReviewAdapter extends BaseAdapter {
    private Context mContext;

    public ReviewAdapter(Context c) {
        mContext = c;
    }
    private static LayoutInflater inflater = null;

    @Override
    public int getCount() {
        return ReviewJSONClient.json_review_list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        inflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.review_layout_row,null,true);
        TextView reviewContentText =(TextView) rowView.findViewById(R.id.tv_review_content);
        TextView  reviewAuthorText =(TextView) rowView.findViewById(R.id.tv_review_author);
        JSONObject reviewJson = ReviewJSONClient.json_review_list.get(position);
        reviewContentText.setText(reviewJson.optString("content").toString());
        reviewAuthorText.setText(reviewJson.optString("author").toString());
        Log.d(" Review Movie id ", (ReviewJSONClient.json_review_list.get(position)).optString("id").toString());
        Log.d(" Review author " , reviewJson.optString("author").toString());
        return rowView;
    }
}
