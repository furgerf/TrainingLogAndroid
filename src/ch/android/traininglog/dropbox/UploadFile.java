package ch.android.traininglog.dropbox;

import java.io.File;
import java.io.FileInputStream;

import android.os.AsyncTask;
import android.util.Log;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.Entry;
import com.dropbox.client2.android.AndroidAuthSession;

public class UploadFile extends AsyncTask<Void, Long, Boolean> {
	private final static String TAG = UploadFile.class.getSimpleName();
	private Exception mException;
	private File mFile;

	private DropboxAPI<AndroidAuthSession> mDBApi;

	public UploadFile(File file, DropboxAPI<AndroidAuthSession> DBApi) {
		mFile = file;
		mDBApi = DBApi;
	}

	protected Boolean doInBackground(Void... params) {
		try {
			Log.d(TAG, "Starting upload of " + mFile.getName());
			FileInputStream inputStream;
			inputStream = new FileInputStream(mFile);
			Entry response = mDBApi.putFileOverwrite(mFile.getName(), inputStream,
					mFile.length(), null);
			Log.i(TAG, "Uploaded file, rev is: " + response.rev);
		} catch (Exception e) {
			mException = e;
		}

		return null;
	}

	protected void onPostExecute(Boolean bool) {
		if (mException == null) {
			Dropbox.onDropboxActionComplete(mFile);
		} else {
			Log.e(TAG, "Error during file upload", mException);
		}
	}
}
