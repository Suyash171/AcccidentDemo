package com.example.acccidentdemo;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.acccidentdemo.drawing.Drawing;
import com.gipl.imagepicker.ImagePickerDialog;
import com.gipl.imagepicker.ImageResult;
import com.gipl.imagepicker.PickerConfiguration;
import com.gipl.imagepicker.PickerListener;
import com.gipl.imagepicker.PickerResult;
import com.larswerkman.holocolorpicker.ColorPicker;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import static com.example.acccidentdemo.MainActivity.getImage;


public class NewCropperActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, ColorPicker.OnColorChangedListener {

    private Drawing mDrawingView;
    //private ImageView mDrawingImageView;
    private Button mClearButton;
    private Button mSubmitButton;
    private Button mPickPhoto;
    private Button mColorButton;
    private TextView mThicknessText;
    Bitmap alteredBitmap;
    private SeekBar mThicknessBar;

    private PickerConfiguration pickerConfiguration = PickerConfiguration.build();
    private ImagePickerDialog imagePickerDialog;
    private static final int STORAGE_ACCESS_PERMISSION_REQUEST = 1234;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_cropper);


        mDrawingView = (Drawing) findViewById(R.id.drawing_view);
        //mDrawingImageView = (ImageView) findViewById(R.id.drawing_image_view);
        mClearButton = (Button) findViewById(R.id.clear_button);
        mSubmitButton = (Button) findViewById(R.id.submit_button);
        mColorButton = (Button) findViewById(R.id.color_button);
        mPickPhoto = findViewById(R.id.pick_photo);

        mThicknessText = (TextView) findViewById(R.id.thickness_value);
        mThicknessBar = (SeekBar) findViewById(R.id.thickness_bar);
        mThicknessBar.setMax(50);

        Bitmap myLogo = BitmapFactory.decodeResource(getResources(), R.drawable.ic_human_body);
        Bitmap workingBitmap = Bitmap.createBitmap(myLogo);
        alteredBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);

        mDrawingView.setImageBitmap(alteredBitmap);

        mClearButton.setOnClickListener(this);
        mSubmitButton.setOnClickListener(this);
        mColorButton.setOnClickListener(this);
        mPickPhoto.setOnClickListener(this);
        mThicknessBar.setOnSeekBarChangeListener(this);

        pickerConfiguration = PickerConfiguration.build()
                .setTextColor(Color.parseColor("#000000"))
                .setIconColor(Color.parseColor("#000000"))
                .setCameraImageId(R.drawable.ic_menu_camera)
                .setBackGroundColor(Color.parseColor("#ffffff"))
                .setIsDialogCancelable(false)
                .enableMultiSelect(false)
                .setPickerDialogListener(new PickerListener() {
                    @Override
                    public void onCancelClick() {
                        super.onCancelClick();
                    }
                })
                .setImagePickerResult(new PickerResult() {
                    @Override
                    public void onImageGet(ImageResult imageResult) {
                        super.onImageGet(imageResult);

                        Bitmap bitmap = getImage(imageResult.getImageBitmap());
                        Bitmap rotatedBitmap = null;
                        try {
                            if (new File(imageResult.getsImagePath()).exists()) {
                                Log.d("Exits", "file");
                                rotatedBitmap = MainActivity.modifyOrientation(imageResult.getImageBitmap(), String.valueOf(new File(imageResult.getsImagePath())));
                                mDrawingView.setImageBitmap(rotatedBitmap);
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                      /*  Uri imageFileUri = Uri.parse(imageResult.getsImagePath());
                        try {
                            BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
                            bmpFactoryOptions.inJustDecodeBounds = true;
                            bmp = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageFileUri), null, bmpFactoryOptions);

                            bmpFactoryOptions.inJustDecodeBounds = false;
                            bmp = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageFileUri), null, bmpFactoryOptions);

                            alteredBitmap = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }*/
                    }

                    @Override
                    public void onReceiveImageList(ArrayList<ImageResult> sFilePath) {
                        super.onReceiveImageList(sFilePath);
                    }
                })
                .setSetCustomDialog(true);

        updateThickness((int) mDrawingView.getThickness());

    }

    private void updateThickness(int value) {
        mThicknessText.setText("Thickness : " + value);
        mThicknessBar.setProgress(value);
        mDrawingView.setDrawingThickness(value);
    }

    @Override
    public void onClick(View v) {
        if (v == mClearButton) {
            mDrawingView.resetDrawing();

            //if (mDrawingImageView.getVisibility() == View.VISIBLE) {
            mSubmitButton.setVisibility(View.VISIBLE);
            // mDrawingImageView.setVisibility(View.GONE);
            mDrawingView.setImageBitmap(null);
            mThicknessText.setVisibility(View.VISIBLE);
            mThicknessBar.setVisibility(View.VISIBLE);
            mColorButton.setVisibility(View.VISIBLE);
            // }
        } else if (v == mSubmitButton) {
            mSubmitButton.setVisibility(View.GONE);
            // mDrawingImageView.setVisibility(View.VISIBLE);
            mDrawingView.setImageBitmap(mDrawingView.getDrawing());
           // setSavePicture(mDrawingView.getDrawing());
            mThicknessText.setVisibility(View.GONE);
            mThicknessBar.setVisibility(View.GONE);
            mColorButton.setVisibility(View.GONE);

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                setSavePicture(mDrawingView.getDrawing());
            } else {
                startWriteStorageAccessPersmissionRequest();
            }

        } else if (v == mColorButton) {
            ColorPicker colorPicker = new ColorPicker(this);
            colorPicker.setOldCenterColor(mDrawingView.getDrawingColor());
            colorPicker.setOnColorChangedListener(this);

            new AlertDialog.Builder(this).setView(colorPicker).show();
        } else if (v == mPickPhoto) {
            imagePickerDialog = ImagePickerDialog.display(getSupportFragmentManager(), pickerConfiguration.setSetCustomDialog(false));
        }
    }

    private void startWriteStorageAccessPersmissionRequest() {
        ActivityCompat.requestPermissions((Activity) this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                STORAGE_ACCESS_PERMISSION_REQUEST);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
        updateThickness(progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onColorChanged(int color) {
        mDrawingView.setDrawingColor(color);
    }


    private void setSavePicture(Bitmap alteredBitmap) {
        if (alteredBitmap != null) {
            ContentValues contentValues = new ContentValues(3);
            contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, "Draw On Me");

            Uri imageFileUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            try {
                OutputStream imageFileOS = getContentResolver().openOutputStream(imageFileUri);
                alteredBitmap.compress(Bitmap.CompressFormat.JPEG, 90, imageFileOS);
                Toast t = Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT);
                t.show();

            } catch (Exception e) {
                Log.v("EXCEPTION", e.getMessage());
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (imagePickerDialog != null)
                imagePickerDialog.onActivityResult(requestCode, resultCode, data);
        }
    }
}
