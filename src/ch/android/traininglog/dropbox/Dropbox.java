package ch.android.traininglog.dropbox;

import java.io.File;

import android.util.Log;
import android.widget.Toast;
import ch.android.traininglog.main.MainActivity;
import ch.android.traininglog.settings.Settings;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;

public class Dropbox {
	// static variables
	private final static String TAG = Dropbox.class.getSimpleName();

	private static Dropbox mInstance;

	private static final boolean USE_OAUTH1 = false;

	// instance variables
	private final String mKey;
	private final String mSecret;

	private final DropboxAPI<AndroidAuthSession> mApi;

	// public access
	public static String downloadFile(final String name) {
		if (!isInitialized())
			return null;
		mInstance.connect();

		// TODO

		return null;
	}

	public static boolean uploadFile(final String name, final String data) {
		if (!isInitialized())
			return false;
		mInstance.connect();

        UploadFile upload = new UploadFile(MainActivity.getActivity(), mInstance.mApi, "/Logs/", new File(name));
        upload.execute();
        
		return true;
	}

	public static void initialize(final String key, final String secret) {
		mInstance = new Dropbox(key, secret);
	}

	// constructor
	private Dropbox(final String key, final String secret) {
		mKey = key;
		mSecret = secret;

		// We create a new AuthSession so that we can use the Dropbox API.
		AndroidAuthSession session = buildSession();
		mApi = new DropboxAPI<AndroidAuthSession>(session);
	}

	// private methods
	private static boolean isInitialized() {
		return mInstance != null;
	}

	private void connect() {
		// This logs you out if you're logged in, or vice versa
		if (mApi.getSession().isLinked()) {
			logOut();
		} else {
			// Start the remote authentication
			if (USE_OAUTH1) {
				mApi.getSession().startAuthentication(
						MainActivity.getActivity());
			} else {
				mApi.getSession().startOAuth2Authentication(
						MainActivity.getActivity());
			}
		}
	}

	private void logOut() {
		// Remove credentials from the session
		mApi.getSession().unlink();

		// Clear our stored keys
		clearKeys();
	}

	/**
	 * Shows keeping the access keys returned from Trusted Authenticator in a
	 * local store, rather than storing user name & password, and
	 * re-authenticating each time (which is not to be done, ever).
	 */
	private void storeAuth(AndroidAuthSession session) {
		// Store the OAuth 2 access token, if there is one.
		String oauth2AccessToken = session.getOAuth2AccessToken();
		if (oauth2AccessToken != null) {
			Settings.setAccessKey("oauth2:");
			Settings.setAccessSecret(oauth2AccessToken);
			return;
		}
		// Store the OAuth 1 access token, if there is one. This is only
		// necessary if
		// you're still using OAuth 1.
		AccessTokenPair oauth1AccessToken = session.getAccessTokenPair();
		if (oauth1AccessToken != null) {
			Settings.setAccessKey(oauth1AccessToken.key);
			Settings.setAccessSecret(oauth1AccessToken.secret);
			return;
		}
	}

	public static void resumeDropbox() {
		if (mInstance == null)
			return;

		AndroidAuthSession session = mInstance.mApi.getSession();

		// The next part must be inserted in the onResume() method of the
		// activity from which session.startAuthentication() was called, so
		// that Dropbox authentication completes properly.
		if (session.authenticationSuccessful()) {
			try {
				// Mandatory call to complete the auth
				session.finishAuthentication();

				// Store it locally in our app for later use
				mInstance.storeAuth(session);
			} catch (IllegalStateException e) {
				Toast.makeText(
						MainActivity.getActivity(),
						"Couldn't authenticate with Dropbox:"
								+ e.getLocalizedMessage(), Toast.LENGTH_LONG)
						.show();
				Log.i(TAG, "Error authenticating", e);
			}
		}
	}

	private void clearKeys() {
		Settings.setAccessKey("");
		Settings.setAccessSecret("");
		//TODO maybe clear more settings...
	}

	private AndroidAuthSession buildSession() {
		AppKeyPair appKeyPair = new AppKeyPair(Settings.getDropboxKey(),
				Settings.getDropboxSecret());

		AndroidAuthSession session = new AndroidAuthSession(appKeyPair);
		loadAuth(session);
		return session;
	}

	/**
	 * Shows keeping the access keys returned from Trusted Authenticator in a
	 * local store, rather than storing user name & password, and
	 * re-authenticating each time (which is not to be done, ever).
	 */
	private void loadAuth(AndroidAuthSession session) {
		if (mKey.equals("oauth2:")) {
			// If the key is set to "oauth2:", then we can assume the token is
			// for OAuth 2.
			session.setOAuth2AccessToken(mSecret);
		} else {
			// Still support using old OAuth 1 tokens.
			session.setAccessTokenPair(new AccessTokenPair(mKey, mSecret));
		}
	}
}
