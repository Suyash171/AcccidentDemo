package com.example.acccidentdemo;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.acccidentdemo.drawing.CanvasView;
import com.example.acccidentdemo.text.CustomDialogClass;
import com.example.acccidentdemo.text.DragableEditTextView;
import com.example.acccidentdemo.text.TextEditorDialogFragment;
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
import java.util.HashMap;

import static com.example.acccidentdemo.MainActivity.getImage;

public class CustomEditor extends AppCompatActivity implements CustomDialogClass.ISelectedValue {

    private CanvasView canvas = null;
    private Button btnCircle, btnRect, btnOval, btnDraw, btnErase, btnUndo, btnRedo, btnSave, btnArrow,btnText;
    private SpeedDialView speedDialView;

    private Bitmap alteredBitmap;
    private PickerConfiguration pickerConfiguration = PickerConfiguration.build();
    private ImagePickerDialog imagePickerDialog;
    private static final int STORAGE_ACCESS_PERMISSION_REQUEST = 1234;
    private HashMap<Integer, String> editTextSelected = new HashMap<>();
    private ViewGroup llroot;

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
        btnText = findViewById(R.id.btn_ad_text);

        Bitmap myLogo = BitmapFactory.decodeResource(getResources(), R.drawable.ic_human_body);
        Bitmap workingBitmap = Bitmap.createBitmap(myLogo);
        alteredBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);

        canvas.setImageBitmap(alteredBitmap);

        /*btnText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llroot = (ViewGroup) v.getParent();
                TextEditorDialogFragment textEditorDialogFragment = TextEditorDialogFragment.show(CustomEditor.this);
                textEditorDialogFragment.setOnTextEditorListener((inputText, colorCode) -> {
                    //final TextStyleBuilder styleBuilder = new TextStyleBuilder();
                    //styleBuilder.withTextColor(colorCode);

                    Toast.makeText(CustomEditor.this, inputText, Toast.LENGTH_SHORT).show();
                    TextView textView = new TextView(CustomEditor.this);
                    textView.setText(inputText);
                    textView.setTextSize(10);
                    textView.setTextColor(Color.RED);
                    textView.setOnLongClickListener(new MyLongClickListner());
                    textView.setOnDragListener(new MyDragListner());
                    llroot.addView(textView);

                });
            }
        });*/

        Glide.with(this)
                .asBitmap()
                .load(alteredBitmap)
                .centerCrop()
                .into(canvas);

        prepareImagePicker();

        speedDialView.addActionItem(new SpeedDialActionItem.Builder(R.id.action_add, R.drawable.ic_adjust_black_24dp).setLabel("Add Image").create());
        speedDialView.addActionItem(new SpeedDialActionItem.Builder(R.id.action_add_text, R.drawable.ic_adjust_black_24dp).setLabel("Add Text").create());
        speedDialView.addActionItem(new SpeedDialActionItem.Builder(R.id.action_add_brush, R.drawable.ic_adjust_black_24dp).setLabel("Add Brush").create());

        speedDialView.setOnActionSelectedListener(speedDialActionItem -> {
            switch (speedDialActionItem.getId()) {
                case R.id.action_add:
                    imagePickerDialog = ImagePickerDialog.display(getSupportFragmentManager(), pickerConfiguration.setSetCustomDialog(false));
                    return false; //true to keep the Speed Dial open

                case R.id.action_add_text:
                    TextEditorDialogFragment textEditorDialogFragment = TextEditorDialogFragment.show(this);
                    textEditorDialogFragment.setOnTextEditorListener((inputText, colorCode) -> {
                        //final TextStyleBuilder styleBuilder = new TextStyleBuilder();
                        //styleBuilder.withTextColor(colorCode);

                      /*  Toast.makeText(CustomEditor.this, inputText, Toast.LENGTH_SHORT).show();
                        TextView textView = new TextView(CustomEditor.this);
                        textView.setText(inputText);
                        textView.setTextSize(10);
                        textView.setTextColor(Color.RED);
                        textView.setOnLongClickListener(new MyLongClickListner());
                        textView.setOnDragListener(new MyDragListner());
                        llroot.addView(textView);*/

                        //ViewGroup relativeParent = (ViewGroup) canvas.getParent();
                        CustomEdittext edttxt = new CustomEdittext(canvas.getContext());
                        // Change Mode
                        this.canvas.setMode(CanvasView.Mode.TEXT);
                        // SetterSetter
                        this.canvas.setText(inputText);
                        // Getter
                        //String text = this.canvas.getText();
                        edttxt.setOnLongClickListener(new MyLongClickListner());
                        edttxt.setOnDragListener(new MyDragListner());

                        //edttxt.setOnLongClickListener(new NewCropperActivity.MyLongClickListner());
                        // mPhotoEditor.addText(inputTex, styleBuilder);
                        //mTxtCurrentTool.setText(R.string.label_text);
                    });
                    return false; //true to keep the Speed Dial open
                case R.id.action_add_brush:
                    CustomDialogClass customDialog = new CustomDialogClass(this);
                    customDialog.setiSelectedValue(this);
                    customDialog.show();

                    return false;
                default:
                    return false;
            }
        });


        btnSave.setOnClickListener(view -> {
            Bitmap bitmap = this.canvas.getBitmap();
            if (bitmap != null) {
                Toast.makeText(this, "Saved...", Toast.LENGTH_SHORT).show();
            }

            // canvas.setImageBitmap(canvas.getDrawing());
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
            this.canvas.setMode(CanvasView.Mode.DRAW);
            this.canvas.setDrawer(CanvasView.Drawer.CIRCLE);            // Draw Circle
            // Setter
            this.canvas.setBaseColor(Color.WHITE);
            // Getter
            // Setter
            this.canvas.setPaintStrokeColor(Color.RED);
            int backgroundColor = this.canvas.getBaseColor();
        });

        btnRect.setOnClickListener(view -> {
            this.canvas.setMode(CanvasView.Mode.DRAW);
            this.canvas.setDrawer(CanvasView.Drawer.RECTANGLE);            // Draw Circle
            // Setter
            this.canvas.setBaseColor(Color.WHITE);
            // Getter
            // Setter
            this.canvas.setPaintStrokeColor(Color.RED);
            int backgroundColor = this.canvas.getBaseColor();
        });

        btnOval.setOnClickListener(view -> {
            this.canvas.setMode(CanvasView.Mode.DRAW);
            this.canvas.setDrawer(CanvasView.Drawer.ELLIPSE);
            // Setter
            // Setter
            this.canvas.setBaseColor(Color.WHITE);
            // Getter
            // Setter
            this.canvas.setPaintStrokeColor(Color.RED);
            int backgroundColor = this.canvas.getBaseColor();
        });

        btnDraw.setOnClickListener(view -> {
            this.canvas.setMode(CanvasView.Mode.DRAW);
            this.canvas.setDrawer(CanvasView.Drawer.PEN);
            // Setter
            this.canvas.setBaseColor(Color.WHITE);
            // Getter
            // Setter
            this.canvas.setPaintStrokeColor(Color.RED);
            int backgroundColor = this.canvas.getBaseColor();
        });


    }

    @Override
    public void seekBarValue(int value) {
        this.canvas.setDrawer(CanvasView.Drawer.PEN);
        // Setter
        this.canvas.setBaseColor(Color.WHITE);
        // Getter
        // Setter
        this.canvas.setPaintStrokeWidth(value);
        this.canvas.setPaintStrokeColor(Color.RED);
        int backgroundColor = this.canvas.getBaseColor();
        // Setter
    }

    public class MyLongClickListner implements View.OnLongClickListener {

        @Override
        public boolean onLongClick(View v) {

            // Create a new ClipData.Item from the ImageView object's tag
            ClipData.Item item = new ClipData.Item((CharSequence) v.getTag());
            // Create a new ClipData using the tag as a label, the plain text MIME type, and
            // the already-created item. This will create a new ClipDescription object within the
            // ClipData, and set its MIME type entry to "text/plain"
            String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
            ClipData data = new ClipData(v.getTag().toString(), mimeTypes, item);
            // Instantiates the drag shadow builder.
            View.DragShadowBuilder dragshadow = new View.DragShadowBuilder(v);
            // Starts the drag
            v.startDrag(data        // data to be dragged
                    , dragshadow   // drag shadow builder
                    , v           // local data about the drag and drop operation
                    , 0          // flags (not currently used, set to 0)
            );
            return true;
        }

    }

    public class MyDragListner implements View.OnDragListener {

        @Override
        public boolean onDrag(View v, DragEvent event) {
            // Defines a variable to store the action type for the incoming event
            int action = event.getAction();
            // Handles each of the expected events
            switch (action) {

                case DragEvent.ACTION_DRAG_STARTED:
                    // Determines if this View can accept the dragged data
                    if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                        // if you want to apply color when drag started to your view you can uncomment below lines
                        // to give any color tint to the View to indicate that it can accept data.
                        // v.getBackground().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);
                        // Invalidate the view to force a redraw in the new tint
                        //  v.invalidate();
                        // returns true to indicate that the View can accept the dragged data.
                        return true;
                    }
                    // Returns false. During the current drag and drop operation, this View will
                    // not receive events again until ACTION_DRAG_ENDED is sent.
                    return false;

                case DragEvent.ACTION_DRAG_ENTERED:
                    // Applies a GRAY or any color tint to the View. Return true; the return value is ignored.
                    v.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
                    // Invalidate the view to force a redraw in the new tint
                    v.invalidate();
                    return true;

                case DragEvent.ACTION_DRAG_LOCATION:
                    // Ignore the event
                    return true;

                case DragEvent.ACTION_DRAG_EXITED:
                    // Re-sets the color tint to blue. Returns true; the return value is ignored.
                    // view.getBackground().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);
                    //It will clear a color filter .
                    v.getBackground().clearColorFilter();
                    // Invalidate the view to force a redraw in the new tint
                    v.invalidate();
                    return true;

                case DragEvent.ACTION_DROP:
                    // Gets the item containing the dragged data
                    ClipData.Item item = event.getClipData().getItemAt(0);
                    // Gets the text data from the item.
                    String dragData = item.getText().toString();
                    // Displays a message containing the dragged data.
                    Toast.makeText(CustomEditor.this, "Dragged data is " + dragData, Toast.LENGTH_SHORT).show();
                    // Turns off any color tints
                    v.getBackground().clearColorFilter();
                    // Invalidates the view to force a redraw
                    v.invalidate();

                    View vw = (View) event.getLocalState();
                    ViewGroup owner = (ViewGroup) vw.getParent();
                    owner.removeView(vw); //remove the dragged view
                    //caste the view into LinearLayout as our drag acceptable layout is LinearLayout
                    LinearLayout container = (LinearLayout) v;
                    container.addView(vw);//Add the dragged view
                    vw.setVisibility(View.VISIBLE);//finally set Visibility to VISIBLE
                    // Returns true. DragEvent.getResult() will return true.
                    return true;

                case DragEvent.ACTION_DRAG_ENDED:
                    // Turns off any color tinting
                    v.getBackground().clearColorFilter();
                    // Invalidates the view to force a redraw
                    v.invalidate();
                    // Does a getResult(), and displays what happened.
                    if (event.getResult())
                        Toast.makeText(CustomEditor.this, "The drop was handled.", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(CustomEditor.this, "The drop didn't work.", Toast.LENGTH_SHORT).show();
                    // returns true; the value is ignored.
                    return true;
                // An unknown action type was received.
                default:
                    Log.e("DragDrop Example", "Unknown action type received by OnDragListener.");
                    break;
            }
            return false;
        }
    }


    /*

     */
    private void prepareImagePicker() {
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

