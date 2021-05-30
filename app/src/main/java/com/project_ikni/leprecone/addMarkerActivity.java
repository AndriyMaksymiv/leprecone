package com.project_ikni.leprecone;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.project_ikni.leprecone.DB.DBHelp;

import java.io.ByteArrayOutputStream;

public class addMarkerActivity extends AppCompatActivity {
    ImageView imageView;
    EditText et_name, et_about;
    Button save, btnChoose, del;
    String name, about;

    private static final int CAM_REQUEST=1313;
    DBHelp hep;
    Bitmap bitmap;
    Cursor cursor1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_marker);
        Bundle extras = getIntent().getExtras();
        String result = null;
        if (extras != null) {
            result = extras.getString("key");
            result = result.split("\\(")[1];
            result = result.split("\\)")[0];
            System.out.println("yeah" + result);
        }



        findViewById(R.id.relativeLayout).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                return true;
            }
        });
        et_name = findViewById(R.id.name);
        et_about = findViewById(R.id.about);
        imageView = findViewById(R.id.img);
        save = findViewById(R.id.save);
        btnChoose = findViewById(R.id.get_photo);
        setEditTextMaxLength(et_name, 20);
        hep = new DBHelp(this);
        btnChoose.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                Intent i = new Intent(Intent.ACTION_PICK,
                                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                i.putExtra("crop", "true");
                                i.putExtra("aspectX", 100);
                                i.putExtra("aspectY", 100);
                                i.putExtra("outputX", 256);
                                i.putExtra("outputY", 256);
                                try {

                                    i.putExtra("return-data", true);
                                    startActivityForResult(
                                            Intent.createChooser(i, "Select Picture"), 0);
                                }catch (ActivityNotFoundException ex){
                                    ex.printStackTrace();
                                }
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(intent,CAM_REQUEST);
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(addMarkerActivity.this);
                builder.setMessage("Звідки вибрати фото?").setPositiveButton("Галерея", dialogClickListener)
                        .setNegativeButton("Камера", dialogClickListener).show();
            }
        });
        final String finalValue = result;
        save.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (et_name.getText().toString().trim().equals("") || et_about.getText().toString().trim().equals("")) {
                            Toast.makeText(getApplicationContext(), "Заповніть усі поля", Toast.LENGTH_LONG).show();
                        } else {
                            name = et_name.getText().toString();
                            about = et_about.getText().toString();

                            boolean insert = hep.add_marker(name, about, finalValue, imageViewToByte(imageView));

                            if (insert) {
                                Toast.makeText(addMarkerActivity.this, "Збережено", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0 && resultCode == Activity.RESULT_OK){
            try {
                Bundle bundle = data.getExtras();
                bitmap = bundle.getParcelable("data");
                imageView.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if(requestCode == CAM_REQUEST){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);
        }
    }

    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    public void setEditTextMaxLength(EditText editText, int length) {
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(length);
        editText.setFilters(FilterArray);
    }
}
