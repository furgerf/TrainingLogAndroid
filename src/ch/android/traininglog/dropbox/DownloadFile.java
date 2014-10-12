package ch.android.traininglog.dropbox;

import java.io.File;
import java.io.FileOutputStream;

import android.os.AsyncTask;
import android.util.Log;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.DropboxFileInfo;
import com.dropbox.client2.android.AndroidAuthSession;

public class DownloadFile extends AsyncTask<Void, Long, Boolean> {
	private final static String TAG = DownloadFile.class.getSimpleName();
    private Exception mException;
    private File mFile;
    
	private DropboxAPI<AndroidAuthSession> mDBApi;
	
	public DownloadFile(File file, DropboxAPI<AndroidAuthSession> DBApi){
		mFile = file;
		mDBApi = DBApi;
	}

    protected Boolean doInBackground(Void... params) {
        try {
        	Log.d(TAG, "Starting download of " + mFile.getName());
        	FileOutputStream outputStream = new FileOutputStream(mFile);
        	DropboxFileInfo info = mDBApi.getFile(mFile.getName(), null, outputStream, null);
        	
        	Log.i(TAG, "Downloaded file, rev is: " + info.getMetadata().rev);
        } catch (Exception e) {
            mException = e;
        }

        return null;
    }

    protected void onPostExecute(Boolean bool) {
    	if (mException == null){
    		Dropbox.onDropboxActionComplete(mFile);
    	}else{
    		Log.e(TAG, "Error during file download", mException);
    	}
    }
}
