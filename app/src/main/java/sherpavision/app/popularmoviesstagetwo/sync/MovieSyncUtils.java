package sherpavision.app.popularmoviesstagetwo.sync;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by aniket on 6/21/17.
 */

public class MovieSyncUtils {
    public static void startImmediateSync(Context context, String...params){
        Log.i("Aniket","Aniket2");

        Intent intentToSyncImmediately = new Intent(context, MovieSyncIntentService.class);
        intentToSyncImmediately.putExtra("params", params);
        context.startService(intentToSyncImmediately);
    }
}
