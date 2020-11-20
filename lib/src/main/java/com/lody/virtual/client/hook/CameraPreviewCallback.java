package com.lody.virtual.client.hook;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.util.Log;

import com.lody.virtual.helper.ImageTool;

import java.io.ByteArrayOutputStream;

public class CameraPreviewCallback implements Camera.PreviewCallback {
    public Camera.PreviewCallback pc;

    public void onPreviewFrame(byte[] data, Camera camera) {
        PreviewCallbackHandler(camera);
    }

    public CameraPreviewCallback(Camera.PreviewCallback previewCallback) {
        this.pc = previewCallback;
    }

    public static Bitmap DecodeNV21(byte[] data, Camera camera) {
        Camera.Size size = camera.getParameters().getPreviewSize();
        try {
            YuvImage image = new YuvImage(data, 17, size.width, size.height, null);
            if (image == null) {
                return null;
            }
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            image.compressToJpeg(new Rect(0, 0, size.width, size.height), 80, stream);
            Bitmap bmp = BitmapFactory.decodeByteArray(stream.toByteArray(), 0, stream.size());
            stream.close();
            return bmp;
        } catch (Exception ex) {
            Log.e("Sys", "Error:" + ex.getMessage());
            return null;
        }
    }

    public void PreviewCallbackHandler(Camera camera) {
        if (this.pc != null && camera != null) {
            Camera.Size previewSize = camera.getParameters().getPreviewSize();
            this.pc.onPreviewFrame(ImageTool.getTestYuvImg(previewSize.width, previewSize.height), camera);
        }
    }
}
