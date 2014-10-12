package ch.android.traininglog.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContextWrapper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import ch.android.traininglog.R;
import ch.android.traininglog.dropbox.Dropbox;
import ch.android.traininglog.dropbox.PostDropboxAction;
import ch.android.traininglog.settings.views.BiodataEntryViews;
import ch.android.traininglog.xml.classes.BiodataEntry;
import ch.android.traininglog.xml.classes.EntryList;
import ch.android.traininglog.xml.main.XmlParser;

public class BiodataEntryActivity extends Activity {
	// fields
	private final static String TAG = BiodataEntryActivity.class
			.getSimpleName();
	private final ArrayList<BiodataEntry> mEntries = new ArrayList<BiodataEntry>();
	private BiodataEntryViews mViews;
	private final static String BIODATA_LOG_NAME = "/biodata.xml";
	private static String APP_PATH;

	private static BiodataEntryActivity mInstance;

	public static BiodataEntryActivity getActivity() {
		return mInstance;
	}

	// event handling
	@SuppressLint("SimpleDateFormat")
	public void butOkClick(final View view) {
		// create new entry
		final BiodataEntry be = new BiodataEntry();
		be.EntryDate = new SimpleDateFormat("yyyy-MM-dd").format(Calendar
				.getInstance().getTime()) + "T00:00:00+01:00";
		be.SleepDuration = mViews.getSleepDuration();
		be.SleepQuality = mViews.getSleepQuality();
		be.Feeling = mViews.getFeeling();
		be.RestingHR = mViews.getRestingHeartRate();
		be.OwnIndex = mViews.getVo2Max();
		be.Weight = mViews.getWeight();
		be.Niggles = mViews.getNiggle();
		be.Note = mViews.getNote();
		Log.d(TAG, "New Biodata Entry created");

		// add entry to entries and to log
		mEntries.add(be);
		final BiodataEntry[] entries = Arrays.copyOf(mEntries.toArray(),
				mEntries.size(), BiodataEntry[].class);

		final File file = new File(APP_PATH + BIODATA_LOG_NAME);
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(file));
			bw.write(XmlParser.serializeBiodataEntries(new EntryList(entries)));
			bw.close();
		} catch (IOException e) {
			Log.e(TAG, "Error while writing to file", e);
		} finally {
			if (bw != null)
				try {
					bw.close();
				} catch (IOException e) {
					Log.e(TAG, "Error while closing BufferedWriter", e);
				}

			Log.d(TAG, "Biodata Entries written to file");
		}

		Log.d(TAG, "Triggering upload of Biodata log");
		Dropbox.uploadFile(file, PostDropboxAction.Toast(null, "New Biodata Log uploaded!"));

		finish();
	}

	public void butCancelClick(final View view) {
		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mInstance = this;

		setContentView(R.layout.biodata_entry);
		mViews = new BiodataEntryViews(this);
		APP_PATH = new ContextWrapper(this).getFilesDir().getPath();

		Log.d(TAG,
				"BiodataEntryActivity created, downloading Biodata Entries...");
		Dropbox.downloadFile(new File(APP_PATH + BIODATA_LOG_NAME), PostDropboxAction.LoadBiodataEntries(PostDropboxAction.Toast(null, "Biodata Log downloaded!")));
	}

	@Override
	protected void onResume() {
		super.onResume();

		Log.d(TAG, "Resuming dropbox");
		Dropbox.resumeDropbox();
	}

	public void loadBiodataEntries(final File file) {
		Log.d(TAG, "Loading Biodata Entries from " + file.getPath());

		final StringBuilder text = new StringBuilder();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			String line;

			while ((line = br.readLine()) != null) {
				text.append(line);
				text.append('\n');
			}
		} catch (IOException e) {
			Log.e(TAG, "Error while reading file", e);
		} finally {
			if (br != null)
				try {
					br.close();
				} catch (IOException e) {
					Log.e(TAG, "Error while closing BufferedReader", e);
				}
		}

		mEntries.clear();
		if (text.length() > 0)
			for (final BiodataEntry be : XmlParser
					.deserializeBiodataEntries(text.toString()))
				mEntries.add(be);
	}

	public void saveBiodataEntries(final File file) {
		Log.d(TAG, "Saving Biodata Entries to " + file.getPath());
		
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(file));

			bw.write(XmlParser.serializeBiodataEntries(new EntryList(
					(BiodataEntry[]) mEntries.toArray())));
		} catch (IOException e) {
			Log.e(TAG, "Error while writing to file", e);
		} finally {
			if (bw != null)
				try {
					bw.close();
				} catch (IOException e) {
					Log.e(TAG, "Error while closing BufferedWriter", e);
				}
		}
	}
}
