package com.example.acccidentdemo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.gipl.imagepicker.ImagePicker;
import com.gipl.imagepicker.ImagePickerDialog;
import com.gipl.imagepicker.ImageResult;
import com.gipl.imagepicker.PickerConfiguration;
import com.gipl.imagepicker.PickerListener;
import com.gipl.imagepicker.PickerResult;
import com.mukesh.DrawingView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    DrawableImageView choosenImageView;
    Button choosePicture;
    Button savePicture;
    Button clear;
    Bitmap bmp;
    Bitmap alteredBitmap;

    PickerConfiguration pickerConfiguration = PickerConfiguration.build();
    ImagePickerDialog imagePickerDialog;
    //DrawingView choosenImageView;

    private static final int STORAGE_ACCESS_PERMISSION_REQUEST = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        choosenImageView = findViewById(R.id.imageView);
        //choosenImageView.setImageResource(R.drawable.ic_human_body);

        choosePicture = (Button) this.findViewById(R.id.ChoosePictureButton);
        savePicture = (Button) this.findViewById(R.id.SavePictureButton);
        clear = findViewById(R.id.clear);

        choosenImageView.setDrawingCacheEnabled(true);
        savePicture.setOnClickListener(this);
        choosePicture.setOnClickListener(this);
        clear.setOnClickListener(this);

        Bitmap myLogo = BitmapFactory.decodeResource(getResources(), R.drawable.ic_human_body);
        Bitmap workingBitmap = Bitmap.createBitmap(myLogo);
        alteredBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);

        choosenImageView.setNewImage(alteredBitmap, alteredBitmap);

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
                                rotatedBitmap = modifyOrientation(imageResult.getImageBitmap(), String.valueOf(new File(imageResult.getsImagePath())));
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        choosenImageView.setNewImage(alteredBitmap, rotatedBitmap);

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
    }


    @Override
    public void onClick(View view) {
        if (view == choosePicture) {
           /* Intent choosePictureIntent = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(choosePictureIntent, 0);*/

            // Open default dialog.
            imagePickerDialog = ImagePickerDialog.display(getSupportFragmentManager(), pickerConfiguration.setSetCustomDialog(false));

        } else if (view == savePicture) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                setSavePicture();
            } else {
                startWriteStorageAccessPersmissionRequest();
            }
        }else if (view == clear){
            choosenImageView.clear();
        }
    }

    private void setSavePicture(){
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
    private void startWriteStorageAccessPersmissionRequest() {
        ActivityCompat.requestPermissions((Activity) this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                STORAGE_ACCESS_PERMISSION_REQUEST);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == STORAGE_ACCESS_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setSavePicture();
            }
        }else{
            imagePickerDialog.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public static Bitmap getImage(Bitmap imagePath) {

        int width = imagePath.getWidth();
        int height = imagePath.getHeight();

        if (400 < width && 400 < height) {

            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) 400 / (float) 400;

            int finalWidth = 400;
            int finalHeight = 400;
            if (ratioMax > ratioBitmap) {
                finalWidth = (int) ((float) 400 * ratioBitmap);
            } else {
                finalHeight = (int) ((float) 400 / ratioBitmap);
            }
            imagePath = Bitmap.createScaledBitmap(imagePath, finalWidth, finalHeight, true);
            return imagePath;
        } else {
            return imagePath;
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

    public static Bitmap modifyOrientation(Bitmap bitmap, String image_absolute_path) throws IOException {
        ExifInterface ei = new ExifInterface(image_absolute_path);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotate(bitmap, 90);

            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotate(bitmap, 180);

            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotate(bitmap, 270);

            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                return flip(bitmap, true, false);

            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                return flip(bitmap, false, true);

            default:
                return bitmap;
        }
    }

    public static Bitmap rotate(Bitmap bitmap, float degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static Bitmap flip(Bitmap bitmap, boolean horizontal, boolean vertical) {
        Matrix matrix = new Matrix();
        matrix.preScale(horizontal ? -1 : 1, vertical ? -1 : 1);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

}
