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
	private static final String KEY_RESTING_HR = "restingHr";
	private static final String KEY_VO2_MAX = "vo2Max";
	private static final String KEY_WEIGHT = "weight";
	private static final String KEY_NIGGLE = "niggle";
	private static final String KEY_NOTE = "note";
	
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
	public static int getRestingHr(){
		return Integer.parseInt(mPreferences.getString(KEY_RESTING_HR, "52"));
	}
	public static boolean setRestingHr(final int restingHr) {
		return setKeyValue(KEY_RESTING_HR, restingHr);
	}
	public static int getVo2Max(){
		return Integer.parseInt(mPreferences.getString(KEY_VO2_MAX, "73"));
	}
	public static boolean setVo2Max(final int vo2Max) {
		return setKeyValue(KEY_VO2_MAX, vo2Max);
	}
	public static int getWeight(){
		return Integer.parseInt(mPreferences.getString(KEY_WEIGHT, "64.5"));
	}
	public static boolean setWeight(final int weight) {
		return setKeyValue(KEY_WEIGHT, weight);
	}
	public static String getNiggle(){
		return mPreferences.getString(KEY_NIGGLE, "");
	}
	public static boolean setNiggle(final String niggle) {
		return setKeyValue(KEY_NIGGLE, niggle);
	}
	public static String getNote(){
		return mPreferences.getString(KEY_NOTE, "");
	}
	public static boolean setNote(final String note) {
		return setKeyValue(KEY_NOTE, note);
	}
	// don't instantiate, use static methods
	private Settings(){
		
	}

}
