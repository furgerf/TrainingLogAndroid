package ch.android.traininglog.settings;

import android.util.Log;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import ch.android.traininglog.R;
import ch.android.traininglog.main.BiodataEntryActivity;

final public class BiodataEntryViews {
	private final static String TAG = BiodataEntryViews.class.getSimpleName();

	private static BiodataEntryViews mInstance;

	private final static Object LOCK = new Object();
	
	// views
	private final NumberPicker numSleepDuration;

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
					Log.e(TAG, "couldnt save new value for series, " + newVal);
				break;
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
		
		
	}
}
