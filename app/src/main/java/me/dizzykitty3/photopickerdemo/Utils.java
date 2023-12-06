package me.dizzykitty3.photopickerdemo;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

public class Utils {
    private static Toast currentToast;

    private Utils() {
    }

    public static void debugLog(@NonNull String event) {
        Log.d("me.dizzykitty3.photopickerdemo", event);
    }

    public static void makeToast(@NonNull Context context, @NonNull String text, @Nullable Boolean durationTimeIsLong) {
        cancelToast();
        if (durationTimeIsLong == null || !durationTimeIsLong) {
            currentToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        } else {
            currentToast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        }
        currentToast.show();
    }

    private static void cancelToast() {
        if (currentToast != null) {
            currentToast.cancel();
        }
    }

    public static void makeToast(@NonNull Context context, @NonNull @StringRes Integer resId) {
        final String text = context.getString(resId);
        makeToast(context, text, false);
    }

    public static void makeToast(@NonNull Context context, @NonNull @StringRes Integer resId, @SuppressWarnings("unused") @NonNull Boolean durationTimeIsLong) {
        final String text = context.getString(resId);
        makeToast(context, text, true);
    }

    public static void makeToast(@NonNull Context context, @NonNull Boolean isVideo, @NonNull Boolean isGif) {
        // unsupported file type toast
        if (isVideo && isGif) {
            Utils.makeToast(context, R.string.toast_video_and_gif_not_supported);
        } else if (Boolean.TRUE.equals(isVideo)) {
            Utils.makeToast(context, R.string.toast_video_not_supported);
        } else if (Boolean.TRUE.equals(isGif)) {
            Utils.makeToast(context, R.string.toast_gif_not_supported);
        }
    }
}
