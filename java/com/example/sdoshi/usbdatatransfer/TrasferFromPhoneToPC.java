package com.example.sdoshi.usbdatatransfer;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ExecutionException;

public class TrasferFromPhoneToPC extends AppCompatActivity {

//    private String TAG = "TrasferFromPhoneToPC";
//    Socket client;
//    FileInputStream fileInputStream;
//    BufferedInputStream bufferedInputStream;
//    OutputStream outputStream;
//    private static final String MY_CV_PATH = Environment.getExternalStorageDirectory() + Environment.getDataDirectory().getAbsolutePath() + File.separator +"Shreyansh" + File.separator + "cv_shreyansh_doshi.pdf";
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private Button btnSong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trasfer_from_phone_to_pc);

        verifyStoragePermissions(TrasferFromPhoneToPC.this);
        btnSong = findViewById(R.id.btnSong);

        btnSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Boolean dataPassed = new SocketConnection().execute("").get();
                    if(dataPassed != null){
                        if(dataPassed){
                            Toast.makeText(TrasferFromPhoneToPC.this, "Data sent succesfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(TrasferFromPhoneToPC.this, "Error in sendiong data", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(TrasferFromPhoneToPC.this, "Can't connect to host", Toast.LENGTH_SHORT).show();
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });


//        File file = new File(MY_CV_PATH); //create file instance
//
//        try {
//            client = new Socket("10.100.112.91", 4444);
//            byte[] mybytearray = new byte[(int) file.length()]; //create a byte array to file
//            fileInputStream = new FileInputStream(file);
//            bufferedInputStream = new BufferedInputStream(fileInputStream);
//            bufferedInputStream.read(mybytearray, 0, mybytearray.length); //read the file
//            outputStream = client.getOutputStream();
//            outputStream.write(mybytearray, 0, mybytearray.length); //write file to the output stream byte by byte
//            outputStream.flush();
//
//            bufferedInputStream.close();
//            outputStream.close();
//            client.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
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

}
