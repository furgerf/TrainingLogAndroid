package ch.android.traininglog.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

import android.R.xml;
import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.widget.TextView;
import ch.android.traininglog.R;
import ch.android.traininglog.settings.BiodataEntryViews;
import ch.android.traininglog.xml.BiodataEntry;
import ch.android.traininglog.xml.EntryList;
import ch.android.traininglog.xml.XmlParser;

public class BiodataEntryActivity extends Activity {

	private static BiodataEntryActivity mInstance;
	
	public static BiodataEntryActivity getActivity(){
		return mInstance;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.biodata_entry);
		
		mInstance = this;
		
		BiodataEntryViews.initialize();
		
		EntryList foo = new EntryList();
		foo.BiodataEntryArray = new BiodataEntry[1];
		BiodataEntry bar = new BiodataEntry();
		//bar.EntryDate = Calendar.getInstance().getTime();
		bar.Feeling = Index.Bad;
		bar.SleepQuality = Index.Fantastic;
		bar.Niggles = "niggle";
		bar.Note = "note";
		bar.OwnIndex = 73;
		bar.RestingHR = 52;
		bar.SleepDuration = "8.5";
		bar.Weight = 64.4;
		
		foo.BiodataEntryArray[0] = bar;
		
		String res = XmlParser.serializeBiodataEntries(foo);
		
		
		
		File sdcard = Environment.getExternalStorageDirectory();
		File file = new File(sdcard, "/TrainingLog/biodata.xml");
		StringBuilder text = new StringBuilder();
		try {
		    BufferedReader br = new BufferedReader(new FileReader(file));
		    String line;

		    while ((line = br.readLine()) != null) {
		        text.append(line);
		        text.append('\n');
		    }
		    br.close();
		}
		catch (IOException e) {
		    //You'll need to add proper error handling here
		}
		
		BiodataEntry[] entries = XmlParser.deserializeBiodataEntries(text.toString());
		EntryList asdf = new EntryList();
		asdf.BiodataEntryArray = entries;
		
		file = new File(sdcard, "/TrainingLog/biodata2.xml");
		try {
		    BufferedWriter br = new BufferedWriter(new FileWriter(file));
		    br.write(XmlParser.serializeBiodataEntries(asdf));
		    br.flush();
		    br.close();
		}
		catch (IOException e) {
		    //You'll need to add proper error handling here
		}
		
		
		
		text.append("ldsfj");
	}
}
