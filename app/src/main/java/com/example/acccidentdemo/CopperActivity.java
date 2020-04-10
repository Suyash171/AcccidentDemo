package com.example.acccidentdemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mukesh.DrawingView;
import com.pes.androidmaterialcolorpickerdialog.ColorPicker;


public class CopperActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private Button saveButton, penButton, eraserButton, penColorButton, backgroundColorButton,
            loadButton, clearButton;
    private DrawingView drawingView;
    private SeekBar penSizeSeekBar, eraserSizeSeekBar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cropper);

        initializeUI();
        setListeners();

    }

    private void setListeners() {
        saveButton.setOnClickListener(this);
        penButton.setOnClickListener(this);
        eraserButton.setOnClickListener(this);
        penColorButton.setOnClickListener(this);
        backgroundColorButton.setOnClickListener(this);
        penSizeSeekBar.setOnSeekBarChangeListener(this);
        eraserSizeSeekBar.setOnSeekBarChangeListener(this);
        loadButton.setOnClickListener(this);
        clearButton.setOnClickListener(this);
    }

    private void initializeUI() {
        drawingView = findViewById(R.id.scratch_pad);
        saveButton = findViewById(R.id.save_button);
        loadButton = findViewById(R.id.load_button);
        penButton = findViewById(R.id.pen_button);
        eraserButton = findViewById(R.id.eraser_button);
        penColorButton = findViewById(R.id.pen_color_button);
        backgroundColorButton = findViewById(R.id.background_color_button);
        penSizeSeekBar = findViewById(R.id.pen_size_seekbar);
        eraserSizeSeekBar = findViewById(R.id.eraser_size_seekbar);
        clearButton = findViewById(R.id.clear_button);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.save_button:
                drawingView.saveImage(Environment.getExternalStorageDirectory().toString(), "test",
                        Bitmap.CompressFormat.JPEG, 100);
                break;
            case R.id.load_button:
                drawingView.loadImage(BitmapFactory.decodeResource(getResources(), R.drawable.ic_human_body));
                Log.d("saveImage", "quality cannot better that 100");
                break;
            case R.id.pen_button:
                drawingView.initializePen();
                break;
            case R.id.eraser_button:
                drawingView.initializeEraser();
                break;
            case R.id.clear_button:
                drawingView.clear();
                break;
            case R.id.pen_color_button:
                final ColorPicker colorPicker = new ColorPicker(CopperActivity.this, 100, 100, 100);
                colorPicker.setCallback(
                        color -> {
                            drawingView.setPenColor(color);
                            colorPicker.dismiss();
                        });
                colorPicker.show();
                break;
            case R.id.background_color_button:
                final ColorPicker backgroundColorPicker = new ColorPicker(CopperActivity.this, 100, 100, 100);
                backgroundColorPicker.setCallback(
                        color -> {
                            drawingView.setBackgroundColor(color);
                            backgroundColorPicker.dismiss();
                        });
                backgroundColorPicker.show();
                break;
            default:
                break;

        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        int seekBarId = seekBar.getId();
        if (seekBarId == R.id.pen_size_seekbar) {
            drawingView.setPenSize(i);
        } else if (seekBarId == R.id.eraser_size_seekbar) {
            drawingView.setEraserSize(i);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
