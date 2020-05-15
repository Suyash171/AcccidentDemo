package com.example.acccidentdemo;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.acccidentdemo.drawing.CanvasView;
import com.gipl.imagepicker.ImagePickerDialog;
import com.gipl.imagepicker.ImageResult;
import com.gipl.imagepicker.PickerConfiguration;
import com.gipl.imagepicker.PickerListener;
import com.gipl.imagepicker.PickerResult;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import static com.example.acccidentdemo.MainActivity.getImage;

public class CustomEditor extends AppCompatActivity {

    private CanvasView canvas = null;
    private Button btnCircle, btnRect, btnOval, btnDraw, btnErase, btnUndo, btnRedo,btnSave,btnArrow;
    private SpeedDialView speedDialView;

    private Bitmap alteredBitmap;
    private PickerConfiguration pickerConfiguration = PickerConfiguration.build();
    private ImagePickerDialog imagePickerDialog;
    private static final int STORAGE_ACCESS_PERMISSION_REQUEST = 1234;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_custom_editor);

        canvas = (CanvasView) this.findViewById(R.id.canvas);
        btnCircle = findViewById(R.id.btn_Cirlce);
        btnDraw = findViewById(R.id.btnDraw);
        btnOval = findViewById(R.id.btnOval);
        btnRect = findViewById(R.id.btnRect);
        btnErase = findViewById(R.id.btnEarseer);
        btnUndo = findViewById(R.id.btnUndo);
        btnRedo = findViewById(R.id.btnRedo);
        btnSave = findViewById(R.id.btnSave);
        speedDialView = findViewById(R.id.speedDial);
        btnArrow = findViewById(R.id.btnArrow);


        Bitmap myLogo = BitmapFactory.decodeResource(getResources(), R.drawable.ic_human_body);
        Bitmap workingBitmap = Bitmap.createBitmap(myLogo);
        alteredBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);

        canvas.setImageBitmap(alteredBitmap);

        Glide.with(this)
                .asBitmap()
                .load(alteredBitmap)
                .centerCrop()
                .into(canvas);

        prepareImagePicker();

        speedDialView.addActionItem(new SpeedDialActionItem.Builder(R.id.action_add, R.drawable.ic_adjust_black_24dp).setLabel("Add Image").create());

        speedDialView.setOnActionSelectedListener(speedDialActionItem -> {
            switch (speedDialActionItem.getId()) {
                case R.id.action_add:
                    imagePickerDialog = ImagePickerDialog.display(getSupportFragmentManager(), pickerConfiguration.setSetCustomDialog(false));
                    return false; //true to keep the Speed Dial open
                default:
                    return false;
            }
        });


        btnSave.setOnClickListener(view -> {
            Bitmap bitmap = this.canvas.getBitmap();
            if (bitmap != null){
                Toast.makeText(this, "Saved...", Toast.LENGTH_SHORT).show();
            }

            canvas.setImageBitmap(canvas.getDrawing());
            // setSavePicture(mDrawingView.getDrawing());

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                setSavePicture(canvas.getDrawing());
            } else {
                startWriteStorageAccessPersmissionRequest();
            }
        });

        btnArrow.setOnClickListener(view -> {
            this.canvas.setDrawer(CanvasView.Drawer.ARROW);            // Draw Circle
            // Setter
            this.canvas.setBaseColor(Color.WHITE);
            // Getter
            // Setter
            this.canvas.setPaintStrokeColor(Color.RED);
            int backgroundColor = this.canvas.getBaseColor();
        });

        btnErase.setOnClickListener(view -> {
            // Change Mode
            this.canvas.setMode(CanvasView.Mode.ERASER);
        });

        btnUndo.setOnClickListener(view -> {
            this.canvas.undo();
        });

        btnRedo.setOnClickListener(view -> {
            this.canvas.redo();
        });

        btnCircle.setOnClickListener(view -> {
            this.canvas.setDrawer(CanvasView.Drawer.CIRCLE);            // Draw Circle
            // Setter
            this.canvas.setBaseColor(Color.WHITE);
            // Getter
            // Setter
            this.canvas.setPaintStrokeColor(Color.RED);
            int backgroundColor = this.canvas.getBaseColor();
        });

        btnRect.setOnClickListener(view -> {
            this.canvas.setDrawer(CanvasView.Drawer.RECTANGLE);            // Draw Circle
            // Setter
            this.canvas.setBaseColor(Color.WHITE);
            // Getter
            // Setter
            this.canvas.setPaintStrokeColor(Color.RED);
            int backgroundColor = this.canvas.getBaseColor();
        });

        btnOval.setOnClickListener(view -> {
            this.canvas.setDrawer(CanvasView.Drawer.ELLIPSE);
            // Setter
            this.canvas.setText("Canvas View");// Draw Circle
            // Setter
            this.canvas.setBaseColor(Color.WHITE);
            // Getter
            // Setter
            this.canvas.setPaintStrokeColor(Color.RED);
            int backgroundColor = this.canvas.getBaseColor();
        });

        btnDraw.setOnClickListener(view -> {
            this.canvas.setDrawer(CanvasView.Drawer.PEN);
            // Setter
            this.canvas.setBaseColor(Color.WHITE);
            // Getter
            // Setter
            this.canvas.setPaintStrokeColor(Color.RED);
            int backgroundColor = this.canvas.getBaseColor();
        });
    }


    /*

     */
    private void prepareImagePicker(){
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

                                Glide.with(CustomEditor.this)
                                        .asBitmap()
                                        .load(rotatedBitmap)
                                        .centerCrop()
                                        .into(canvas);
                                //canvas.setImageBitmap(rotatedBitmap);
                               // canvas.drawBitmap(rotatedBitmap);
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onReceiveImageList(ArrayList<ImageResult> sFilePath) {
                        super.onReceiveImageList(sFilePath);
                    }
                })
                .setSetCustomDialog(true);
    }

    private void startWriteStorageAccessPersmissionRequest() {
        ActivityCompat.requestPermissions((Activity) this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                STORAGE_ACCESS_PERMISSION_REQUEST);
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

