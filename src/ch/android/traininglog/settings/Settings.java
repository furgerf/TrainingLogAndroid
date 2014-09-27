package ch.android.traininglog.settings;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.Log;
import ch.android.traininglog.main.MainActivity;

final public class Settings {
	private final static String TAG = Settings.class.getSimpleName();
	// constants
	public static final String VERSION = "0.1";

	// keys
	private final static String KEY_SLEEP_DURATION = "sleepDuration";
	
	// preferences
	private static SharedPreferences mPreferences = PreferenceManager
			.getDefaultSharedPreferences(MainActivity.getActivity());

	private static boolean setKeyValue(final String key, final Object value) {
		final Editor editor = mPreferences.edit();

		Log.d(TAG, "Writing new value for setting " + key + ": " + value);
		
		editor.putString(key, value.toString());

		editor.commit();
		
		return true;
	}

//	public static SharedPreferences getSharedPreferences() {
//		return mPreferences;
//	}
	
	public static int getSleepDuration(){
		return Integer.parseInt(mPreferences.getString(KEY_SLEEP_DURATION, "16"));
	}
	public static boolean setSleepDuration(final int sleepDuration){
		return setKeyValue(KEY_SLEEP_DURATION, sleepDuration);
	}
	
	// don't instantiate, use static methods
	private Settings(){
		
	}
}
