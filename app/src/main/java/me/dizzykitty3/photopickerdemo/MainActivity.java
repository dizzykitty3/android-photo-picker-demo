package me.dizzykitty3.photopickerdemo;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import me.dizzykitty3.photopickerdemo.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;
    private ActivityResultLauncher<PickVisualMediaRequest> pickMultipleMedia;
    private TextView textViewNoDataHint;
    private TextView textViewAppName;
    private Button buttonSelectPhoto;
    private Button buttonClear;
    private ImageView imageViewPhotoShown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        initBinding();
        initVisibility();
        initButtonAction();
        initSingleSelectPhotoPicker();
        initMultiSelectPhotoPicker();
    }

    private void initBinding() {
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        textViewNoDataHint = binding.textNoDataHint;
        textViewAppName = binding.textAppName;
        buttonSelectPhoto = binding.buttonSelectPhoto;
        buttonClear = binding.buttonClear;
        imageViewPhotoShown = binding.imagePhotoShown;
    }

    private void initVisibility() {
        buttonSelectPhoto.setVisibility(View.VISIBLE);
        buttonClear.setVisibility(View.GONE);
    }

    private void initButtonAction() {
        buttonSelectPhoto.setOnClickListener(v -> {
            userEventDebugLog("click: select photo button");
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageAndVideo.INSTANCE)
                    .build());
        });
        buttonSelectPhoto.setOnLongClickListener(v -> {
            userEventDebugLog("long click: select photo button");
            makeToast("Shows the first photo only");
            pickMultipleMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageAndVideo.INSTANCE)
                    .build());
            return true; // setOnLongClickListener() expects a boolean return type
        });

        buttonClear.setOnClickListener(v -> {
            userEventDebugLog("click: clear button");
            changeVisibility(false);
        });
        buttonClear.setOnLongClickListener(v -> {
            userEventDebugLog("long click: clear button");
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageAndVideo.INSTANCE)
                    .build());
            return true; // setOnLongClickListener() expects a boolean return type
        });

        textViewAppName.setOnLongClickListener(v -> {
            userEventDebugLog("long click: app name text");
            makeToast("Hello there :D");
            return true; // setOnLongClickListener() expects a boolean return type
        });
    }

    private void initSingleSelectPhotoPicker() {
        pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
            if (uri == null) {
                userEventDebugLog("no media selected");
                return;
            }

            // skip video files [reuse this code]
            userEventDebugLog("selected URI: " + uri);
            final String mimeType = getContentResolver().getType(uri);
            userEventDebugLog("uri mime type = " + mimeType);
            if (mimeType == null) return;
            if (mimeType.startsWith("video")) {
                userEventDebugLog("selected file is a video");
                makeToast("Sorry! Video is not supported yet.");
                return;
            }
            if (mimeType.contains("gif")) {
                userEventDebugLog("selected file is a gif");
                makeToast("Sorry! GIF is not supported yet.");
            }
            imageViewPhotoShown.setImageURI(uri);
            setPhotoScale();
        });
    }

    private void initMultiSelectPhotoPicker() {
        pickMultipleMedia = registerForActivityResult(new ActivityResultContracts.PickMultipleVisualMedia(5), uris -> {
            if (uris.isEmpty()) {
                userEventDebugLog("no media selected");
                return;
            }
            userEventDebugLog("number of items selected: " + uris.size());
            final Uri uri = uris.get(0);

            // skip video files [reuse this code]
            userEventDebugLog("selected URI: " + uri);
            final String mimeType = getContentResolver().getType(uri);
            userEventDebugLog("uri mime type = " + mimeType);
            if (mimeType == null) return;
            if (mimeType.startsWith("video")) {
                userEventDebugLog("selected file is a video");
                makeToast("Sorry! Video is not supported yet.");
                return;
            }
            if (mimeType.contains("gif")) {
                userEventDebugLog("selected file is a gif");
                makeToast("Sorry! GIF is not supported yet.");
            }
            imageViewPhotoShown.setImageURI(uri);
            setPhotoScale();
        });
    }

    private void changeVisibility(boolean isPhotoShown) {
        if (!isPhotoShown) {
            imageViewPhotoShown.setVisibility(View.GONE);
            buttonClear.setVisibility(View.GONE);
            buttonSelectPhoto.setVisibility(View.VISIBLE);
            textViewNoDataHint.setVisibility(View.VISIBLE);
            return;
        }
        imageViewPhotoShown.setVisibility(View.VISIBLE);
        buttonClear.setVisibility(View.VISIBLE);
        buttonSelectPhoto.setVisibility(View.GONE);
        textViewNoDataHint.setVisibility(View.GONE);
    }

    private void setPhotoScale() {
        changeVisibility(true);
        imageViewPhotoShown.setScaleX(0.8F);
        imageViewPhotoShown.setScaleY(0.8F);
    }

    private void makeToast(@NonNull String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    private void userEventDebugLog(@NonNull String event) {
        Log.d("me.dizzykitty3.photopickerdemo", event);
    }
}