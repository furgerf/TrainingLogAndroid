package ch.android.traininglog.settings;

import android.util.Log;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import ch.android.traininglog.R;
import ch.android.traininglog.main.BiodataEntryActivity;
import ch.android.traininglog.main.Index;

final public class BiodataEntryViews {
	private final static String TAG = BiodataEntryViews.class.getSimpleName();

	private static BiodataEntryViews mInstance;

	private final static Object LOCK = new Object();
	
	// views
	private final NumberPicker numSleepDuration;
	private final NumberPicker numSleepQuality;
	private final NumberPicker numFeeling;
	private final NumberPicker numRestingHeartRate;
	private final EditText txtOwnIndex;
	private final EditText txtWeight;
	private final MultiAutoCompleteTextView txtNiggle;
	private final MultiAutoCompleteTextView txtNote;
	

	// listeners
	private final OnValueChangeListener valueChangeListener = new OnValueChangeListener() {
		@Override
		public void onValueChange(final NumberPicker picker, final int oldVal,
				final int newVal) {
			// Log.i(TAG, "change of value detected: id " + picker.getId()
			// + " from " + oldVal + " to " + newVal);
			switch (picker.getId()) {
			case R.id.num_sleep_duration:
				if (!Settings.setSleepDuration(newVal))
					Log.e(TAG, "couldnt save new value for sleep duration, " + newVal);
				break;
			case R.id.num_sleep_quality:
				if (!Settings.setSleepQualityIndex(newVal))
					Log.e(TAG, "couldnt save new value for sleep quality, " + newVal);
			case R.id.num_feeling:
				if (!Settings.setFeelingIndex(newVal))
					Log.e(TAG, "couldnt save new value for feeling, " + newVal);
			default:
				Log.w(TAG,
						"unhandled change of value with id " + picker.getId()
								+ " from " + oldVal + " to " + newVal);
			}
		}
	};
//	private final OnCheckedChangeListener checkedChangeListener = new OnCheckedChangeListener() {
//		@Override
//		public void onCheckedChanged(final RadioGroup group, final int checkedId) {
//			String[] nums;
			// Log.i(TAG, "change of checked detected. new checked id: "
			// + checkedId);

//			if (!Settings.setKeyValue("cornersortime",
//					checkedId == R.id.radCorners, boolean.class))
//				Log.e(TAG, "couldnt save new value for cornersortime, "
//						+ (checkedId == R.id.radCorners));
//
//			switch (checkedId) {
//			case R.id.radCorners:
//				nums = new String[100 / 5];
//				for (int i = 1; i <= 100 / 5; i++)
//					nums[i - 1] = Integer.toString(i * 5);
//
//				numCornerTime.setMaxValue(nums.length - 1);
//				numCornerTime.setMinValue(0);
//				numCornerTime.setDisplayedValues(nums);
//				numCornerTime.setValue(Settings.getCornerCount() / 5 - 1);
//
//				if (!Settings.setKeyValue("cornersortime", true, boolean.class))
//					Log.e(TAG,
//							"couldnt save new value for cornersortime, " + true);
//				break;
//			case R.id.radTime:
//				nums = new String[100 / 5];
//				for (int i = 1; i <= 100 / 5; i++)
//					nums[i - 1] = Integer.toString(i * 5);
//
//				numCornerTime.setMaxValue(nums.length - 1);
//				numCornerTime.setMinValue(0);
//				numCornerTime.setDisplayedValues(nums);
//				numCornerTime.setValue(Settings.getCornerTime() / 5 - 1);
//
//				if (!Settings
//						.setKeyValue("cornersortime", false, boolean.class))
//					Log.e(TAG,
//							"couldnt save new value for cornersortime, " + false);
//				break;
//			default:
//				Log.e(TAG, "unexpected radioubutton id: " + checkedId);
//			}
//		}
//	};

	// public access
	public static void initialize() {
		synchronized (LOCK) {
			if (mInstance == null) {
				mInstance = new BiodataEntryViews();
			}
		}
	}

	// constructor, instantiates views
	private BiodataEntryViews() {
		// find views
		numSleepDuration = (NumberPicker) BiodataEntryActivity.getActivity().findViewById(
				R.id.num_sleep_duration);
		numSleepQuality = (NumberPicker) BiodataEntryActivity.getActivity().findViewById(
				R.id.num_sleep_quality);
		numFeeling = (NumberPicker) BiodataEntryActivity.getActivity().findViewById(
				R.id.num_feeling);
		numRestingHeartRate= (NumberPicker) BiodataEntryActivity.getActivity().findViewById(
				R.id.num_resting_hr);
		txtOwnIndex = (EditText) BiodataEntryActivity.getActivity().findViewById(
				R.id.txt_vo2_max);
		txtWeight = (EditText) BiodataEntryActivity.getActivity().findViewById(
				R.id.txt_weight);
		txtNiggle = (MultiAutoCompleteTextView) BiodataEntryActivity.getActivity().findViewById(
				R.id.txt_niggle);
		txtNote = (MultiAutoCompleteTextView) BiodataEntryActivity.getActivity().findViewById(
				R.id.txt_note);
		
		// set range, value, etc
		initializeViews();
	}

	// sets view properties
	private void initializeViews() {
		// sleep duration: 0, 0.5, ... 24
		final String[] sleepValues = new String[49];
		for (float i = 0; i < 49; i++)
			sleepValues[(int)i] = Float.valueOf(i /2).toString();
		numSleepDuration.setDisplayedValues(sleepValues);
		numSleepDuration.setMinValue(0);
		numSleepDuration.setMaxValue(sleepValues.length - 1);
		numSleepDuration.setValue(Settings.getSleepDuration());
		numSleepDuration.setOnValueChangedListener(valueChangeListener);
		numSleepDuration.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
		
		final String[] indexValues = new String[5];
		for (int i = 0; i < 5; i++)
			indexValues[i] = Index.values()[i].toString();
		numSleepQuality.setDisplayedValues(indexValues);
		numSleepQuality.setMinValue(0);
		numSleepQuality.setMaxValue(indexValues.length - 1);
		numSleepQuality.setValue(Settings.getSleepQualityIndex());
		numSleepQuality.setOnValueChangedListener(valueChangeListener);
		numSleepQuality.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

		numFeeling.setDisplayedValues(indexValues);
		numFeeling.setMinValue(0);
		numFeeling.setMaxValue(indexValues.length - 1);
		numFeeling.setValue(Settings.getFeelingIndex());
		numFeeling.setOnValueChangedListener(valueChangeListener);
		numFeeling.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
	}
}
