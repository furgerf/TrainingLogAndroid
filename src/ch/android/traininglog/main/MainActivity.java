package ch.android.traininglog.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import ch.android.traininglog.R;
import ch.android.traininglog.dropbox.Dropbox;

public class MainActivity extends Activity {

	private static MainActivity mInstance;
	
	public static MainActivity getActivity(){
		return mInstance;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		Dropbox.resumeDropbox();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mInstance = this;		

		// don't load main layout
//		setContentView(R.layout.activity_main);
		
		// autostart biodata entry
		final Intent intent = new Intent(this, BiodataEntryActivity.class);
		startActivity(intent);
//		BiodataSettingsViews.initialize();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
