package sherpavision.app.popularmoviesstagetwo.sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

public class MovieSyncIntentService extends IntentService{
    public MovieSyncIntentService(){
        super("MovieSyncIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.i("Aniket","Aniket3");

        String[] params = intent.getStringArrayExtra("params");
        MovieSyncTask.syncMovies(this, params);
    }
}