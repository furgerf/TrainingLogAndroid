package ch.android.traininglog.main;

import android.app.Activity;
import android.os.Bundle;
import ch.android.traininglog.R;
import ch.android.traininglog.settings.BiodataEntryViews;

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
	}
}
