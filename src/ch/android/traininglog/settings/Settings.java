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
	private static final String KEY_SLEEP_QUALITY_INDEX = "sleepQualityIndex";
	private static final String KEY_FEELING_INDEX = "feelingIndex";
	
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
	public static int getSleepQualityIndex(){
		return Integer.parseInt(mPreferences.getString(KEY_SLEEP_QUALITY_INDEX, "2"));
	}
	public static boolean setSleepQualityIndex(final int sleepQualityIndex) {
		return setKeyValue(KEY_SLEEP_QUALITY_INDEX, sleepQualityIndex);
	}	
	public static int getFeelingIndex(){
		return Integer.parseInt(mPreferences.getString(KEY_FEELING_INDEX, "2"));
	}
	public static boolean setFeelingIndex(final int feelingIndex) {
		return setKeyValue(KEY_FEELING_INDEX, feelingIndex);
	}
	// don't instantiate, use static methods
	private Settings(){
		
	}

}
