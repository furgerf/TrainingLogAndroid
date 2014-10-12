package ch.android.traininglog.dropbox;

import android.util.Log;

public class PostDropboxAction {
	private final static String TAG = PostDropboxAction.class.getSimpleName();
	public final PostDropboxActionTypes type;

	public final PostDropboxAction nextAction;

	private String mMessage;

	public String getMessage() {
		if (type != PostDropboxActionTypes.TOAST) {
			Log.e(TAG, "Illegal call to message! Action Type is " + type);
		}

		return mMessage;
	}

	public static PostDropboxAction None(final PostDropboxAction nextAction) {
		return new PostDropboxAction(PostDropboxActionTypes.NONE, nextAction);
	}

	public static PostDropboxAction LoadBiodataEntries(
			final PostDropboxAction nextAction) {
		return new PostDropboxAction(
				PostDropboxActionTypes.LOAD_BIODATA_ENTRIES, nextAction);
	}

	public static PostDropboxAction Toast(final PostDropboxAction nextAction,
			final String message) {
		final PostDropboxAction action = new PostDropboxAction(
				PostDropboxActionTypes.TOAST, nextAction);
		action.mMessage = message;
		return action;
	}

	private PostDropboxAction(final PostDropboxActionTypes type,
			final PostDropboxAction nextAction) {
		this.type = type;
		this.nextAction = nextAction;
	}
}
