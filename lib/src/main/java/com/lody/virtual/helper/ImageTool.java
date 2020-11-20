package com.lody.virtual.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;

import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.util.Log;

import java.io.File;

public class ImageTool {
    public static byte[] rgb2YCbCr420(int[] pixels, int width, int height) {
        int len = width * height;
        byte[] yuv = new byte[((len * 3) / 2)];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int rgb = pixels[(i * width) + j] & ViewCompat.MEASURED_SIZE_MASK;
                int r = rgb & MotionEventCompat.ACTION_MASK;
                int g = (rgb >> 8) & MotionEventCompat.ACTION_MASK;
                int b = (rgb >> 16) & MotionEventCompat.ACTION_MASK;
                int y = (((((r * 66) + (g * 129)) + (b * 25)) + 128) >> 8) + 16;
                int u = (((((r * -38) - (g * 74)) + (b * 112)) + 128) >> 8) + 128;
                int v = (((((r * 112) - (g * 94)) - (b * 18)) + 128) >> 8) + 128;
                if (y < 16) {
                    y = 16;
                } else if (y > MotionEventCompat.ACTION_MASK) {
                    y = MotionEventCompat.ACTION_MASK;
                }
                if (u < 0) {
                    u = 0;
                } else if (u > MotionEventCompat.ACTION_MASK) {
                    u = MotionEventCompat.ACTION_MASK;
                }
                if (v < 0) {
                    v = 0;
                } else if (v > MotionEventCompat.ACTION_MASK) {
                    v = MotionEventCompat.ACTION_MASK;
                }
                yuv[(i * width) + j] = (byte) y;
                yuv[((((i >> 1) * width) + len) + (j & -2)) + 0] = (byte) u;
                yuv[((((i >> 1) * width) + len) + (j & -2)) + 1] = (byte) v;
            }
        }
        return yuv;
    }


    public static byte[] getTestYuvImg(int width, int height) {

        if(!new File("/sdcard/scan.jpg").exists())
        {
            return null;
        }
        Bitmap bitmap = getBitmap("/sdcard/scan.jpg");

        Bitmap resizeBitmap = ImageTool.ResizeBitMap(bitmap, width, height);
        ImageTool.saveBitmap(resizeBitmap, "resizeBitmap");
        Bitmap newBitmap = ImageTool.MergeImg(ImageTool.GetVoidBitMap(width, height), resizeBitmap);
        ImageTool.saveBitmap(newBitmap, "newBitmap");
        return ImageTool.getYUVByBitmap(newBitmap, width, height);
    }


    public static Bitmap getBitmap(String imgPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = false;
        newOpts.inPurgeable = true;
        newOpts.inInputShareable = true;
        newOpts.inSampleSize = 1;
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
        return BitmapFactory.decodeFile(imgPath, newOpts);
    }

    public static void decodeYUV420SP(byte[] rgbBuf, byte[] yuv420sp, int width, int height) {
        int frameSize = width * height;
        if (rgbBuf == null) {
            throw new NullPointerException("buffer 'rgbBuf' is null");
        } else if (rgbBuf.length < frameSize * 3) {
            throw new IllegalArgumentException("buffer 'rgbBuf' size " + rgbBuf.length + " < minimum " + (frameSize * 3));
        } else if (yuv420sp == null) {
            throw new NullPointerException("buffer 'yuv420sp' is null");
        } else if (yuv420sp.length < (frameSize * 3) / 2) {
            throw new IllegalArgumentException("buffer 'yuv420sp' size " + yuv420sp.length + " < minimum " + ((frameSize * 3) / 2));
        } else {
            int j = 0;
            int yp = 0;
            while (j < height) {
                int uvp;
                int uvp2 = frameSize + ((j >> 1) * width);
                int u = 0;
                int v = 0;
                int i = 0;
                while (true) {
                    uvp = uvp2;
                    if (i >= width) {
                        break;
                    }
                    int y = (yuv420sp[yp] & MotionEventCompat.ACTION_MASK) - 16;
                    if (y < 0) {
                        y = 0;
                    }
                    if ((i & 1) == 0) {
                        uvp2 = uvp + 1;
                        v = (yuv420sp[uvp] & MotionEventCompat.ACTION_MASK) - 128;
                        u = (yuv420sp[uvp2] & MotionEventCompat.ACTION_MASK) - 128;
                        uvp2++;
                    } else {
                        uvp2 = uvp;
                    }
                    int y1192 = y * 1192;
                    int r = y1192 + (v * 1634);
                    int g = (y1192 - (v * 833)) - (u * 400);
                    int b = y1192 + (u * 2066);
                    if (r < 0) {
                        r = 0;
                    } else if (r > 262143) {
                        r = 262143;
                    }
                    if (g < 0) {
                        g = 0;
                    } else if (g > 262143) {
                        g = 262143;
                    }
                    if (b < 0) {
                        b = 0;
                    } else if (b > 262143) {
                        b = 262143;
                    }
                    rgbBuf[yp * 3] = (byte) (r >> 10);
                    rgbBuf[(yp * 3) + 1] = (byte) (g >> 10);
                    rgbBuf[(yp * 3) + 2] = (byte) (b >> 10);
                    i++;
                    yp++;
                }
                j++;
                uvp2 = uvp;
            }
        }
    }

    public static byte[] getRGBByBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] pixels = new int[(width * height)];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        return convertColorToByte(pixels);
    }

    public static byte[] getYUVByBitmap(Bitmap bitmap, int newWidth, int newHeight) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] pixels = new int[(width * height)];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        return rgb2YCbCr420(pixels, width, height);
    }

    public static byte[] convertColorToByte(int[] color) {
        if (color == null) {
            return null;
        }
        byte[] data = new byte[(color.length * 3)];
        for (int i = 0; i < color.length; i++) {
            data[i * 3] = (byte) ((color[i] >> 16) & MotionEventCompat.ACTION_MASK);
            data[(i * 3) + 1] = (byte) ((color[i] >> 8) & MotionEventCompat.ACTION_MASK);
            data[(i * 3) + 2] = (byte) (color[i] & MotionEventCompat.ACTION_MASK);
        }
        return data;
    }

    public static Bitmap GetVoidBitMap(int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.eraseColor(Color.parseColor("#000000"));
        return bitmap;
    }

    public static Bitmap MergeImg(Bitmap baseBitmap, Bitmap topBitmap) {
        if (baseBitmap == null) {
            return null;
        }
        int width = baseBitmap.getWidth();
        int height = baseBitmap.getHeight();
        int paddingLeft = (width - topBitmap.getWidth()) / 2;
        int paddingTop = (height - topBitmap.getHeight()) / 2;
        Bitmap newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawBitmap(baseBitmap, 0.0f, 0.0f, null);
        canvas.drawBitmap(topBitmap, (float) paddingLeft, (float) paddingTop, null);
        canvas.save(31);
        canvas.restore();
        return newBitmap;
    }

    public static Bitmap ResizeBitMap(Bitmap bitmap, int width, int height) {
        float scale;
        int oldWidth = bitmap.getWidth();
        int oldHeight = bitmap.getHeight();
        int newWidth = (int) (((double) width) * 0.6d);
        int newHeight = (int) (((double) height) * 0.6d);
        Log.i("wwbs", "oldWidth:" + oldWidth + ",oldHeight:" + oldHeight);
        float scaleWidth = ((float) newWidth) / ((float) oldWidth);
        float scaleHeight = ((float) newHeight) / ((float) oldHeight);
        if (scaleHeight < scaleWidth) {
            scale = scaleHeight;
        } else {
            scale = scaleWidth;
        }
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        Bitmap frontBitmap = Bitmap.createBitmap(bitmap, 0, 0, oldWidth, oldHeight, matrix, true);
        saveBitmap(frontBitmap, "test");
        return frontBitmap;
    }

    public static void saveBitmap(Bitmap bitmap, String fileName) {
    }

    public static void saveBitmap(Bitmap bitmap) {
        saveBitmap(bitmap, "xx");
    }



}
