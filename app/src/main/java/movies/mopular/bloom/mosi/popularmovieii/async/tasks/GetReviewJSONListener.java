package movies.mopular.bloom.mosi.popularmovieii.async.tasks;

import org.json.JSONObject;

/**
 * Created by DELL on 01-01-2016.
 */
public interface GetReviewJSONListener {
    public void onRemoteCallComplete(JSONObject jsonFromNet);
}
