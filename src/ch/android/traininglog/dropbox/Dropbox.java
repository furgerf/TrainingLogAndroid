package ch.android.traininglog.dropbox;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;
import ch.android.traininglog.main.BiodataEntryActivity;
import ch.android.traininglog.settings.Settings;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AppKeyPair;

public class Dropbox {
	// static variables
	private final static String TAG = Dropbox.class.getSimpleName();
	private final static String APP_KEY;
	private final static String APP_SECRET;

	private final static HashMap<File, PostDropboxAction> mFileActions = new HashMap<File, PostDropboxAction>();

	private static Dropbox mInstance;

	// instance variables
	private DropboxAPI<AndroidAuthSession> mDBApi;
	private Queue<AsyncTask<Void, Long, Boolean>> mTaskQueue = new LinkedList<AsyncTask<Void, Long, Boolean>>();
	private boolean mGettingAccessToken;

	static {
		// load app key/secret
		BufferedReader br = null;
		final String[] data = new String[2];
		try {
			String path = Environment.getExternalStorageDirectory()
					+ "/dropbox_access_data";
			br = new BufferedReader(new FileReader(path));

			data[0] = br.readLine();
			data[1] = br.readLine();
		} catch (IOException e) {
			Log.e(TAG,
					"Error while reading application key/secret. Aborting...",
					e);
			BiodataEntryActivity.getActivity().finish();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException e) {
				Log.e(TAG, "Error while closing file", e);
			}
		}
		APP_KEY = data[0];
		APP_SECRET = data[1];
		Log.d(TAG, "Loaded dropbox application key and secret");

		// create instance
		mInstance = new Dropbox();

		if (Settings.getAccessToken().isEmpty()) {
			// authenticate
			Log.i(TAG, "Getting access token");
			AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
			AndroidAuthSession session = new AndroidAuthSession(appKeys);
			mInstance.mDBApi = new DropboxAPI<AndroidAuthSession>(session);

			// start auth activity and make sure we process the response
			// correctly
			mInstance.mGettingAccessToken = true;

			Log.i(TAG, "Starting authentification activity");
			mInstance.mDBApi.getSession().startOAuth2Authentication(
					BiodataEntryActivity.getActivity());
		} else {
			Log.d(TAG, "Using stored access token!");
			AppKeyPair appKeyPair = new AppKeyPair(APP_KEY, APP_SECRET);
			AndroidAuthSession session = new AndroidAuthSession(appKeyPair);
			session.setOAuth2AccessToken(Settings.getAccessToken());
			mInstance.mDBApi = new DropboxAPI<AndroidAuthSession>(session);
		}
		Log.d(TAG, "Dropbox instance created");
	}

	public static void resumeDropbox() {
		mInstance.resume();
	}

	private void resume() {
		// did we just try to authenticate?
		if (mGettingAccessToken)
			finishAuthentication();

		// are we doing something atm?
		while (mInstance.mDBApi.getSession().isLinked()
				&& !mTaskQueue.isEmpty())
			mTaskQueue.remove().execute();
	}

	private void finishAuthentication() {
		if (mDBApi.getSession().authenticationSuccessful()) {
			try {
				// Required to complete auth, sets the access token on the
				// session
				mDBApi.getSession().finishAuthentication();

				String accessToken = mDBApi.getSession().getOAuth2AccessToken();

				// store access token
				Settings.setAccessToken(accessToken);

				mGettingAccessToken = false;
				Log.d(TAG, "Access token received! :)");
			} catch (IllegalStateException e) {
				Log.e(TAG, "Error during authentification", e);
			}
		} else {
			Log.e(TAG, "Authentification unsuccessful!!");
		}
	}

	public static void uploadFile(File file, PostDropboxAction action) {
		mFileActions.put(file, action);
		final AsyncTask<Void, Long, Boolean> task = new UploadFile(file,
				mInstance.mDBApi);
		Log.i(TAG, "Starting file upload task");
		if (mInstance.mDBApi.getSession().isLinked())
			task.execute();
		else
			mInstance.mTaskQueue.add(task);
	}

	public static void downloadFile(File file, PostDropboxAction action) {
		mFileActions.put(file, action);
		final AsyncTask<Void, Long, Boolean> task = new DownloadFile(file,
				mInstance.mDBApi);
		Log.i(TAG, "Starting file download task");
		if (mInstance.mDBApi.getSession().isLinked())
			task.execute();
		else
			mInstance.mTaskQueue.add(task);
	}

	public static void onDropboxActionComplete(final File file) {
		if (!mFileActions.containsKey(file)) {
			Log.e(TAG, "Couldnt find key " + file.getName()
					+ ", aborting action!");
		} else if (mFileActions.get(file) != null)
			executePostDropboxAction(file, mFileActions.get(file));
	}

	private static void executePostDropboxAction(final File file,
			final PostDropboxAction action) {
		switch (action.type) {
		case LOAD_BIODATA_ENTRIES:
			BiodataEntryActivity.getActivity().loadBiodataEntries(file);
			break;
		case NONE:
			break;
		case TOAST:
			Toast.makeText(BiodataEntryActivity.getActivity(),
					action.getMessage(), Toast.LENGTH_LONG).show();
			break;
		default:
			Log.w(TAG, "Unknown action: " + mFileActions.get(file).toString());
			break;
		}

		if (action.nextAction != null)
			executePostDropboxAction(file, action.nextAction);
	}
}
