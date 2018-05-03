package barbapplications.sft;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
//to push


public class ImagePreviewActivity extends AppCompatActivity {

    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_CAMERA_PERMISSION = 3;
    static final int REQUEST_STORAGE_PERMISSION = 4;

    //static final String IMAGE_PATH = "image_path";  
    private String mCurrentPhotoPath = null;

    private String mCaseID;

    ImageView imageView;
    EditText editComment;
    Button myButton;
    Button doneButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);

        mCaseID = getIntent().getStringExtra("case_id");

        imageView = (ImageView) findViewById(R.id.imageView);
        editComment = (EditText) findViewById(R.id.editComment);
        myButton = (Button) findViewById(R.id.buttonSubmit);
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // send image and comment to the server here
                String url = "https://csi445.ddns.net/csi/new_case_image.php";
                SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("Response", response);
                                // and  then show camera again
                                dispatchTakePictureIntent();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                smr.addStringParam("case_id", mCaseID);
                smr.addStringParam("comment", editComment.getText().toString());
                smr.addFile("image", mCurrentPhotoPath);

                RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext());
                mRequestQueue.add(smr);
            }
        });

        doneButton = (Button) findViewById(R.id.buttonDone);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(ImagePreviewActivity.this, HomeScreenActivity.class));
            }
        });

        dispatchTakePictureIntent();
    }

    private void startCamera() {
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            if (ActivityCompat.checkSelfPermission(ImagePreviewActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(ImagePreviewActivity.this, android.Manifest.permission.CAMERA)) {
                    // show alert here
                }
                else {
                    ActivityCompat.requestPermissions(ImagePreviewActivity.this, new String[]{android.Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                }
                return;
            }
            dispatchTakePictureIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            }
        }
        else if (requestCode == REQUEST_STORAGE_PERMISSION) {
            dispatchTakePictureIntent();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            imageView.setImageBitmap(BitmapFactory.decodeFile(mCurrentPhotoPath));

        }
        else
            super.onActivityResult(requestCode, resultCode, data);
    }

    private void dispatchTakePictureIntent() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERMISSION);
            }
            return;
        }
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            }
            catch (IOException ex) {
                // error
                mCurrentPhotoPath = null;
            }

            if (photoFile != null) {
                Uri photUri = FileProvider.getUriForFile(this,
                        "barbapplications.sft.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photUri);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
}
