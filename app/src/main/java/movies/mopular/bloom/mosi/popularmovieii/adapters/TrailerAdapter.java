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
import movies.mopular.bloom.mosi.popularmovieii.async.tasks.JSONClient;

/**
 * Created by DELL on 31-12-2015.
 */
public class TrailerAdapter extends BaseAdapter {
    private Context mContext;

    private static LayoutInflater inflater = null;

    public TrailerAdapter(Context c) {
        mContext = c;
    }

    @Override
    public int getCount() {
        return JSONClient.json_trailer_list.size();
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
        View rowView = inflater.inflate(R.layout.trailer_layout_row,null,true);
        TextView trailerText =(TextView) rowView.findViewById(R.id.tv_trailer_text_row);
        JSONObject trailerJson = JSONClient.json_trailer_list.get(position);
        trailerText.setText(trailerJson.optString("name").toString());
        Log.d(" Trailore at Adaptor ", trailerJson.optString("name").toString());
        return rowView;
    }
}
