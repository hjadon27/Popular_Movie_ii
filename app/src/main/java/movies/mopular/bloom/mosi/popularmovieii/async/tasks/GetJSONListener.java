package movies.mopular.bloom.mosi.popularmovieii.async.tasks;

import org.json.JSONObject;

/**
 * Created by DELL on 29-12-2015.
 */
public interface GetJSONListener {
    public void onRemoteCallComplete(JSONObject jsonFromNet);
}
