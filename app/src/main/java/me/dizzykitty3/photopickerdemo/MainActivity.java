package me.dizzykitty3.photopickerdemo;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import me.dizzykitty3.photopickerdemo.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private boolean isFirstOpen = true;
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
        initPhotoPicker();
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
            Utils.debugLog("click: select photo button");
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageAndVideo.INSTANCE)
                    .build());
        });
        buttonSelectPhoto.setOnLongClickListener(v -> {
            Utils.debugLog("long click: select photo button");
            if (isFirstOpen) {
                Utils.makeToast(this, R.string.toast_first_file_only, true);
                isFirstOpen = false;
            }
            pickMultipleMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageAndVideo.INSTANCE)
                    .build());
            return true; // setOnLongClickListener() expects a boolean return type
        });

        buttonClear.setOnClickListener(v -> {
            Utils.debugLog("click: clear button");
            changeVisibilityWhenCleared();
        });
        buttonClear.setOnLongClickListener(v -> {
            Utils.debugLog("long click: clear button");
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageAndVideo.INSTANCE)
                    .build());
            return true; // setOnLongClickListener() expects a boolean return type
        });

        textViewAppName.setOnLongClickListener(v -> {
            Utils.debugLog("long click: app name text");
            Utils.makeToast(this, R.string.toast_hello_there);
            return true; // setOnLongClickListener() expects a boolean return type
        });
    }

    private void initPhotoPicker() {
        initSingleSelectPhotoPicker();
        initMultiSelectPhotoPicker();
    }

    private void initSingleSelectPhotoPicker() {
        pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
            if (uri == null) {
                Utils.debugLog("no media selected");
                return;
            }

            // Skip video files
            Utils.debugLog("selected uri = " + uri);
            final String mimeType = getContentResolver().getType(uri);
            Utils.debugLog("uri mime type = " + mimeType);
            if (mimeType == null) return;
            if (mimeType.startsWith("video")) {
                Utils.debugLog("selected file is a video");
                Utils.makeToast(this, true, false);
                return;
            }
            if (mimeType.contains("gif")) {
                Utils.debugLog("selected file is a gif");
                Utils.makeToast(this, false, true);
            }
            imageViewPhotoShown.setImageURI(uri);
            onPhotoSelected();
        });
    }

    private void initMultiSelectPhotoPicker() {
        pickMultipleMedia = registerForActivityResult(new ActivityResultContracts.PickMultipleVisualMedia(100), uris -> {
            if (uris.isEmpty()) {
                Utils.debugLog("no media selected");
                return;
            }
            boolean isVideo = false;
            boolean isGif = false;
            boolean isShowToast = false;

            final int urisCount = uris.size();
            Utils.debugLog("number of items selected = " + urisCount);

            // TODO Reduce the total number of break and continue statements
            for (int i = 0; i < urisCount; i++) {
                Uri uri = uris.get(i);

                // Skip video files
                Utils.debugLog("selected number = " + i + "uri = " + uri);
                final String mimeType = getContentResolver().getType(uri);
                Utils.debugLog("uri number = " + i + "mime type = " + mimeType);
                if (mimeType == null) continue;
                if (mimeType.contains("video")) {
                    Utils.debugLog("selected file is a video");
                    isVideo = true;
                    isShowToast = true;
                    continue;
                }
                if (mimeType.contains("gif")) {
                    Utils.debugLog("selected file is a gif");
                    isGif = true;
                    isShowToast = true;
                    continue;
                }
                isShowToast = false;
                imageViewPhotoShown.setImageURI(uri);
                onPhotoSelected();
            }
            if (isShowToast) Utils.makeToast(this, isVideo, isGif);
        });
    }

    /**
     * @see #changeVisibilityWhenCleared()
     */
    private void changeVisibilityWhenPhotoSelected() {
        imageViewPhotoShown.setVisibility(View.VISIBLE);
        buttonClear.setVisibility(View.VISIBLE);
        buttonSelectPhoto.setVisibility(View.GONE);
        textViewNoDataHint.setVisibility(View.GONE);
    }

    /**
     * @see #changeVisibilityWhenPhotoSelected()
     */
    private void changeVisibilityWhenCleared() {
        imageViewPhotoShown.setVisibility(View.GONE);
        buttonClear.setVisibility(View.GONE);
        buttonSelectPhoto.setVisibility(View.VISIBLE);
        textViewNoDataHint.setVisibility(View.VISIBLE);
    }

    private void setPhotoScale() {
        imageViewPhotoShown.setScaleX(0.8F);
        imageViewPhotoShown.setScaleY(0.8F);
    }

    private void onPhotoSelected() {
        changeVisibilityWhenPhotoSelected();
        setPhotoScale();
    }
}