package com.example.sdoshi.usbdatatransfer;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;


public class SocketConnection extends AsyncTask<String, Void, Boolean> {
    String host = "10.100.112.91";
    int port = 6667;
    int len;
    Socket socket = new Socket();
    byte buf[] = new byte[1024];
    private static final String MY_CV_PATH = File.separator + "Shreyansh" + File.separator + "cv_shreyansh_doshi.pdf";
//    private static final String MY_CV_PATH = File.separator + "Shreyansh" + File.separator + "abc.mp3";

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected synchronized Boolean doInBackground(String... strings) {
        try {
//                socket.bind(null);

//            Log.e("Request", strings[0]);
            socket.connect((new InetSocketAddress(host, port)), 1000);

            if (!socket.isConnected()) {
                socket.close();
                return null;
            }

            BufferedInputStream bis = null;
            File sd = Environment.getExternalStorageDirectory();
            if (sd.canWrite()) {
                String from = MY_CV_PATH;
                int end = from.toString().lastIndexOf("/");
                String str1 = from.toString().substring(0, end);
                String str2 = from.toString().substring(end + 1, from.length());
                File source = new File(Environment.getExternalStorageDirectory() + str1, str2);
//                DataOutputStream request = new DataOutputStream(socket.getOutputStream());
                OutputStream outputStream = socket.getOutputStream();
                try {
                    byte[] mybytearray = new byte[(int) source.length()];
                    bis = new BufferedInputStream(new FileInputStream(source));
                    bis.read(mybytearray, 0, mybytearray.length);
                    outputStream.write(mybytearray, 0, mybytearray.length);
//                    while ((len = bis.read(buf)) != -1) {
//                        outputStream.write(buf, 0, len);
//                    }

                    Log.e("hellllooooo..","file sent successfully....");
                    outputStream.flush();
                    outputStream.close();
                    return true;

                } catch (NullPointerException e) {
                    e.printStackTrace();
                    return false;
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                } finally {
                    try {
                        if (bis != null) bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (socket != null) {
                        if (socket.isConnected()) {
                            try {
                                socket.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

            }

            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

        @Override
    protected void onPostExecute(Boolean s) {
        super.onPostExecute(s);
    }
}


