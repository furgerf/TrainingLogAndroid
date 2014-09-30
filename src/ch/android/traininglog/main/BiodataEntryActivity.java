package ch.android.traininglog.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import ch.android.traininglog.R;
import ch.android.traininglog.settings.views.BiodataEntryViews;
import ch.android.traininglog.xml.classes.BiodataEntry;
import ch.android.traininglog.xml.classes.EntryList;
import ch.android.traininglog.xml.main.XmlParser;

public class BiodataEntryActivity extends Activity {
	// fields
	private final ArrayList<BiodataEntry> mEntries = new ArrayList<BiodataEntry>();
	private final BiodataEntryViews mViews = new BiodataEntryViews(this);
	
	// event handling
	@SuppressLint("SimpleDateFormat")
	public void butOkClick(final View view) {
		final BiodataEntry be = new BiodataEntry();

		final Date date = Calendar.getInstance().getTime();
		
		be.EntryDate = new SimpleDateFormat("yyyy-MM-dd").format(date) + "T00:00:00+01:00";
		be.SleepDuration = mViews.getSleepDuration();
		
		//TODO
		
		mEntries.add(be);
		saveBiodataEntries();

		finish();
	}

	public void butCancelClick(final View view) {
		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.biodata_entry);

		new Thread() {
			public void run() {
				loadBiodataEntries();
			}
		}.start();
	}

	private void loadBiodataEntries() {
		final File file = new File(Environment.getExternalStorageDirectory(),
				"/TrainingLog/biodata.xml");
		final StringBuilder text = new StringBuilder();
		try {
			final BufferedReader br = new BufferedReader(new FileReader(file));
			String line;

			while ((line = br.readLine()) != null) {
				text.append(line);
				text.append('\n');
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		mEntries.clear();
		for (final BiodataEntry be : XmlParser.deserializeBiodataEntries(text
				.toString()))
			mEntries.add(be);
	}

	private void saveBiodataEntries() {
		final File file = new File(Environment.getExternalStorageDirectory(),
				"/TrainingLog/biodata.xml");
		try {
			final BufferedWriter bw = new BufferedWriter(new FileWriter(file));

			bw.write(XmlParser.serializeBiodataEntries(new EntryList(
					(BiodataEntry[]) mEntries.toArray())));
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
