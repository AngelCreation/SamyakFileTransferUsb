package com.example.sdoshi.usbdatatransfer;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button getFile,copyFile, copyCV, copyPasteFile, getUri, copyOnMyUsbFile;
    private static final int READ_REQUEST_CODE = 42;
    private static final int WRITE_REQUEST_CODE = 43;
    private static final int GET_URI_FOR_USB = 44;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    Uri uri = null;
    private static final String MY_CV_PATH = Environment.getDataDirectory().getAbsolutePath() + File.separator +"Shreyansh" + File.separator + "cv_shreyansh_doshi.pdf";
    private static Uri usbUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getFile = findViewById(R.id.getFile);
        copyFile = findViewById(R.id.copyFile);
        copyCV = findViewById(R.id.copyCV);
        copyPasteFile = findViewById(R.id.copyPasteFile);
//        getUri = findViewById(R.id.getUri);
//        copyOnMyUsbFile = findViewById(R.id.copyOnMyUsbFile);

        verifyStoragePermissions(MainActivity.this);

        getFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performFileSearch();
            }
        });

        copyFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copySearchedFile();
            }
        });

        copyCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "Data Directory Path: " + Environment.getDataDirectory().getAbsolutePath(), Toast.LENGTH_LONG).show();
//                Toast.makeText(MainActivity.this, "MyFilePath: " + MY_CV_PATH, Toast.LENGTH_LONG).show();

                //1
//                boolean copied = copyFileFromPath(MY_CV_PATH,Environment.getExternalStorageDirectory().getAbsolutePath());
//                if(copied){
//                    Toast.makeText(MainActivity.this, "File copied successfully", Toast.LENGTH_LONG).show();
//                } else {
//                    Toast.makeText(MainActivity.this, "Error while copying file", Toast.LENGTH_LONG).show();
//                }

                copySearchedFile();
//                Uri uri = Uri.fromFile(new File(Environment.getDataDirectory().getAbsolutePath()));

            }
        });

        copyPasteFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "ext path: " + Environment.getExternalStorageDirectory() + "/mnt/media_rw/76EE-8201" , Toast.LENGTH_SHORT).show();
                boolean copied = copyAndPasteFile();
                if(copied){
                    Toast.makeText(MainActivity.this, "File copied successfully", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, "Error while copying file", Toast.LENGTH_LONG).show();
                }
            }
        });

//        getUri.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getUsbUri();
//            }
//        });

//        copyOnMyUsbFile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(usbUri != null){
//                    boolean copied = copyFileFromPathAndDestUri(usbUri);
//                    if(copied){
//                        Toast.makeText(MainActivity.this, "File copied successfully", Toast.LENGTH_LONG).show();
//                    } else {
//                        Toast.makeText(MainActivity.this, "Error while copying file", Toast.LENGTH_LONG).show();
//                    }
//                } else {
//                    Toast.makeText(MainActivity.this, "Please select storage location", Toast.LENGTH_LONG).show();
//                }
//
//            }
//        });


    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    public void performFileSearch() {

        // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file
        // browser.
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

        // Filter to only show results that can be "opened", such as a
        // file (as opposed to a list of contacts or timezones)
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // Filter to show only images, using the image MIME data type.
        // If one wanted to search for ogg vorbis files, the type would be "audio/ogg".
        // To search for all documents available via installed storage providers,
        // it would be "*/*".
        intent.setType("*/*");

        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    public void copySearchedFile(){
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_TITLE,"File Name");
        startActivityForResult(intent, WRITE_REQUEST_CODE);
    }

//    public void getUsbUri(){
//        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
//        startActivityForResult(intent, GET_URI_FOR_USB);
//    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {

        // The ACTION_OPEN_DOCUMENT intent was sent with the request code
        // READ_REQUEST_CODE. If the request code seen here doesn't match, it's the
        // response to some other intent, and the code below shouldn't run at all.

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using resultData.getData().

            if (resultData != null) {
                uri = resultData.getData();
            }
        } else if(requestCode == WRITE_REQUEST_CODE && resultCode == Activity.RESULT_OK){
//            copyFile(uri, resultData.getData());
            boolean copied = copyFileFromPathAndDestUri(resultData.getData());
            if(copied){
                Toast.makeText(MainActivity.this, "File copied successfully", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(MainActivity.this, "Error while copying file", Toast.LENGTH_LONG).show();
            }
        } /*else if(requestCode == GET_URI_FOR_USB && resultCode == Activity.RESULT_OK){
            if (resultData != null) {
                usbUri  = DocumentsContract.buildDocumentUriUsingTree(resultData.getData(),
                        DocumentsContract.getTreeDocumentId(resultData.getData()));
                Toast.makeText(MainActivity.this, "usbUri" + usbUri.getPath(), Toast.LENGTH_SHORT).show();
            }
        }*/
    }

    private void copyFile(Uri src, Uri destUri) {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        try {
            bis = new BufferedInputStream(getContentResolver().openInputStream(src));
            bos = new BufferedOutputStream(getContentResolver().openOutputStream(destUri));
            byte[] buf = new byte[1024];
            bis.read(buf);
            do {
                bos.write(buf);
            } while(bis.read(buf) != -1);
        } catch (NullPointerException e) {
            Toast.makeText(this, "file not found to copy", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (IOException e) {
            Toast.makeText(this, "io exception in making file", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) bis.close();
                if (bos != null) bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //1
    public boolean copyFileFromPath(String from, String to) {
        try {
            File sd = Environment.getExternalStorageDirectory();
            if (sd.canWrite()) {
                int end = from.toString().lastIndexOf("/");
                String str1 = from.toString().substring(0, end);
                String str2 = from.toString().substring(end+1, from.length());
                File source = new File(Environment.getExternalStorageDirectory() + str1, str2);
                File destination= new File(to, str2);
//                Log.e("Str1::" ,"" + str1);
//                Log.e("Str2::" ,"" + str2);
//                Log.e("Source Exists::" ,"" + source.exists());
//                Log.e("Files dir::" ,"" + getFilesDir().getAbsolutePath());
//                Log.e("sd::" ,"" + Environment.getExternalStorageDirectory());
                if (source.exists()) {
                    FileChannel src = new FileInputStream(source).getChannel();
                    FileChannel dst = new FileOutputStream(destination).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            }
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    public boolean copyFileFromPathAndDestUri(Uri destUri) {

                BufferedInputStream bis = null;
                BufferedOutputStream bos = null;
            File sd = Environment.getExternalStorageDirectory();
            if (sd.canWrite()) {
                String from = MY_CV_PATH;
                int end = from.toString().lastIndexOf("/");
                String str1 = from.toString().substring(0, end);
                String str2 = from.toString().substring(end+1, from.length());
                File source = new File(Environment.getExternalStorageDirectory() + str1, str2);
                try {
                    bis = new BufferedInputStream(new FileInputStream(source));
                    bos = new BufferedOutputStream(getContentResolver().openOutputStream(destUri));
                    byte[] buf = new byte[1024];
                    bis.read(buf);
                    do {
                        bos.write(buf);
                    } while(bis.read(buf) != -1);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    return false;
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                } finally {
                    try {
                        if (bis != null) bis.close();
                        if (bos != null) bos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return true;

    }
    
    public boolean copyAndPasteFile(){
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        File sd = Environment.getExternalStorageDirectory();
        if (sd.canWrite()) {
            String from = MY_CV_PATH;
            int end = from.toString().lastIndexOf("/");
            String str1 = from.toString().substring(0, end);
            String str2 = from.toString().substring(end+1, from.length());
            File source = new File(Environment.getExternalStorageDirectory() + str1, str2);
            File dest = new File(Environment.getExternalStorageDirectory() , str2);
            try {
                bis = new BufferedInputStream(new FileInputStream(source));
                bos = new BufferedOutputStream(new FileOutputStream(dest));
                byte[] buf = new byte[1024];
                bis.read(buf);
                do {
                    bos.write(buf);
                } while(bis.read(buf) != -1);
            } catch (NullPointerException e) {
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } finally {
                try {
                    if (bis != null) bis.close();
                    if (bos != null) bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

}