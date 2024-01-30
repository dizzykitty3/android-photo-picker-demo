package me.dizzykitty3.photopickerdemo;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

public class Utils {
    private static Toast currentToast;

    private Utils() {
        // Empty
    }

    public static void debugLog(@NonNull String event) {
        Log.d("me.dizzykitty3.photopickerdemo", event);
    }

    private static void makeToast(@NonNull Context context, @NonNull String text, @NonNull Boolean isDurationTimeLong) {
        cancelCurrentToast();
        currentToast = Toast.makeText(
                context,
                text,
                Boolean.TRUE.equals(isDurationTimeLong) ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
        currentToast.show();
    }

    private static void cancelCurrentToast() {
        if (currentToast != null) currentToast.cancel();
    }

    public static void makeToast(@NonNull Context context, @NonNull @StringRes Integer resId) {
        makeToast(context, context.getString(resId), false);
    }

    public static void makeToast(@NonNull Context context, @NonNull @StringRes Integer resId, @NonNull Boolean isDurationTimeLong) {
        makeToast(context, context.getString(resId), isDurationTimeLong);
    }

    public static void makeToast(@NonNull Context context, @NonNull Boolean isVideo, @NonNull Boolean isGif) {
        // Unsupported file type toast
        if (isVideo && isGif) {
            Utils.makeToast(context, R.string.toast_video_and_gif_not_supported);
        } else if (Boolean.TRUE.equals(isVideo)) {
            Utils.makeToast(context, R.string.toast_video_not_supported);
        } else if (Boolean.TRUE.equals(isGif)) {
            Utils.makeToast(context, R.string.toast_gif_not_supported);
        }
    }
}
