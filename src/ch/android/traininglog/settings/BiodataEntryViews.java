package ch.android.traininglog.settings;

import android.text.Editable;
import android.text.TextWatcher;
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
	private final EditText txtVo2Max;
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
					Log.e(TAG, "couldnt save new value for sleep duration, "
							+ newVal);
				break;
			case R.id.num_sleep_quality:
				if (!Settings.setSleepQualityIndex(newVal))
					Log.e(TAG, "couldnt save new value for sleep quality, "
							+ newVal);
			case R.id.num_feeling:
				if (!Settings.setFeelingIndex(newVal))
					Log.e(TAG, "couldnt save new value for feeling, " + newVal);
			case R.id.num_resting_hr:
				if (!Settings.setRestingHr(newVal))
					Log.e(TAG, "couldnt save new value for resting hr, "
							+ newVal);
			default:
				Log.w(TAG,
						"unhandled change of value with id " + picker.getId()
								+ " from " + oldVal + " to " + newVal);
			}
		}
	};

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
		numSleepDuration = (NumberPicker) BiodataEntryActivity.getActivity()
				.findViewById(R.id.num_sleep_duration);
		numSleepQuality = (NumberPicker) BiodataEntryActivity.getActivity()
				.findViewById(R.id.num_sleep_quality);
		numFeeling = (NumberPicker) BiodataEntryActivity.getActivity()
				.findViewById(R.id.num_feeling);
		numRestingHeartRate = (NumberPicker) BiodataEntryActivity.getActivity()
				.findViewById(R.id.num_resting_hr);
		txtVo2Max = (EditText) BiodataEntryActivity.getActivity().findViewById(
				R.id.txt_vo2_max);
		txtWeight = (EditText) BiodataEntryActivity.getActivity().findViewById(
				R.id.txt_weight);
		txtNiggle = (MultiAutoCompleteTextView) BiodataEntryActivity
				.getActivity().findViewById(R.id.txt_niggle);
		txtNote = (MultiAutoCompleteTextView) BiodataEntryActivity
				.getActivity().findViewById(R.id.txt_note);

		// set range, value, etc
		initializeViews();
	}

	// sets view properties
	private void initializeViews() {
		// sleep duration: 0, 0.5, ... 24
		final String[] sleepValues = new String[49];
		for (float i = 0; i < 49; i++)
			sleepValues[(int) i] = Float.valueOf(i / 2).toString();
		numSleepDuration.setDisplayedValues(sleepValues);
		numSleepDuration.setMinValue(0);
		numSleepDuration.setMaxValue(sleepValues.length - 1);
		numSleepDuration.setValue(Settings.getSleepDuration());
		numSleepDuration.setOnValueChangedListener(valueChangeListener);
		numSleepDuration
				.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

		final String[] indexValues = new String[5];
		for (int i = 0; i < 5; i++)
			indexValues[i] = Index.values()[i].toString();
		numSleepQuality.setDisplayedValues(indexValues);
		numSleepQuality.setMinValue(0);
		numSleepQuality.setMaxValue(indexValues.length - 1);
		numSleepQuality.setValue(Settings.getSleepQualityIndex());
		numSleepQuality.setOnValueChangedListener(valueChangeListener);
		numSleepQuality
				.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

		numFeeling.setDisplayedValues(indexValues);
		numFeeling.setMinValue(0);
		numFeeling.setMaxValue(indexValues.length - 1);
		numFeeling.setValue(Settings.getFeelingIndex());
		numFeeling.setOnValueChangedListener(valueChangeListener);
		numFeeling
				.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

//		numRestingHeartRate.setMinValue(0);
//		numRestingHeartRate.setMaxValue(200);
		numRestingHeartRate.setValue(Settings.getRestingHr());
		numRestingHeartRate.setOnValueChangedListener(valueChangeListener);
		numRestingHeartRate
				.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

		txtVo2Max.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (!Settings.setVo2Max(Integer.parseInt(s.toString())))
					Log.e(TAG, "couldnt save new value for vo2 max, " + s);
			}
		});

		txtWeight.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (!Settings.setWeight(Integer.parseInt(s.toString())))
					Log.e(TAG, "couldnt save new value for weight, " + s);
			}
		});
		txtNiggle.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (!Settings.setNiggle(s.toString()))
					Log.e(TAG, "couldnt save new value for niggle, " + s);
			}
		});

		txtNote.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (!Settings.setNote(s.toString()))
					Log.e(TAG, "couldnt save new value for note, " + s);
			}
		});
	}
}
