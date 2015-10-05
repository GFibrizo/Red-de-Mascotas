package utils;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by fabrizio on 04/10/15.
 */
public class UploadTask extends AsyncTask <Void, Void, Integer> {


    Activity activity;
    String sourceFileUri;
    String upLoadServerUri;

    public UploadTask(Activity activity, String sourceFileUri, String upLoadServerUri) {
        this.activity = activity;
        this.sourceFileUri = sourceFileUri;
        this.upLoadServerUri = upLoadServerUri;
    }

    @Override
    protected Integer doInBackground(Void... params) {

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = null;
        sourceFile = new File(sourceFileUri);
        String renameFile = sourceFile.getName();

        int serverResponseCode = 0;
        Log.e("ENTRA", "ENTRA");

        if (!sourceFile.isFile()) {
            Log.e("uploadFile", "Source File not exist : " + sourceFileUri);
            return 0;

        } else {
            try {
                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(upLoadServerUri);

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

                conn.setRequestProperty("uploaded_file", renameFile);

                dos = new DataOutputStream(conn.getOutputStream());

                // add parameters
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"type\""
                        + lineEnd);
                dos.writeBytes(lineEnd);

                // assign value
                dos.writeBytes("Your value");
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + lineEnd);

                // send image
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name='uploaded_file';filename='"
                        + renameFile + "'" + lineEnd);

                dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.i("uploadFile", "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);

                if (serverResponseCode == 200) {
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            String message = "File Upload Completed.";
                            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {
                ex.printStackTrace();

                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(activity, "MalformedURLException : : check script url.", Toast.LENGTH_SHORT).show();
                    }
                });

                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {
                e.printStackTrace();

                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(activity, "Got Exception : see logcat ", Toast.LENGTH_SHORT).show();
                    }
                });

                Log.e("Upload file Exception", "Exception : " + e.getMessage(), e);
            }
            return serverResponseCode;

        }

    }
}
