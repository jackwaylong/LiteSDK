package com.longway.framework.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 图片解码器
 */
public class BitmapDecoder {

    private BitmapDecoder() {
    }

    public static int calculateInSampleSize(String url, int maxWidth,
                                            int maxHeight) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(url, options);
        if (options.outWidth == -1 || options.outHeight == -1) {
            updateOptions(url, options);
        }
        return getSampleSize(maxWidth, maxHeight, options);
    }

    private static void updateOptions(String url, Options options) {
        ExifInterface exifInterface;
        try {
            exifInterface = new ExifInterface(url);
            int width = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, ExifInterface.ORIENTATION_NORMAL);//获取图片的宽度
            int height = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, ExifInterface.ORIENTATION_NORMAL);//获取图片的高度
            options.outWidth = width;
            options.outHeight = height;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError error) {
            error.printStackTrace();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public static int[] getWidthAndheight(String url) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(url, options);
        int[] size = new int[2];
        if (options.outWidth == -1 || options.outHeight == -1) {
            updateOptions(url, options);
        }
        size[0] = options.outWidth;
        size[1] = options.outHeight;
        return size;
    }

    private static int getSampleSize(int maxWidth, int maxHeight,
                                     Options options) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (width > maxWidth || height > maxHeight) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) maxHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) maxWidth);
            }

            final float totalPixels = width * height;

            final float maxTotalPixels = maxWidth * maxHeight;

            while (totalPixels / (inSampleSize * inSampleSize) > maxTotalPixels) {
                inSampleSize *= 2;
            }
        }
        // 避免采样值小于1,即最大为原图
        if (inSampleSize < 1) {
            inSampleSize = 1;
        }
        return inSampleSize;
    }

    public static Bitmap getBitmap(int maxWidth, int maxHeight, String url) {

        Bitmap bitmap = getSafeBitmap(
                calculateInSampleSize(url, maxWidth, maxHeight), url);
        return bitmap;
    }

    public static Bitmap getSafeBitmap(int inSampleSize, String url) {
        Options options = new Options();
        Bitmap bitmap;
        for (; ; ) {
            try {
                options.inSampleSize = inSampleSize;
                bitmap = BitmapFactory.decodeFile(url, options);
                break;
            } catch (OutOfMemoryError e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                inSampleSize *= 2;
            } catch (Throwable ex) {
                printErrorStackInfo(ex);
            }
        }
        return bitmap;
    }

    private static void printErrorStackInfo(Throwable ex) {
        StringWriter writer = new StringWriter(0);
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Log.e("getSafeBitmap", writer.toString());
    }

    public static Bitmap getBitmap(int maxWidth, int maxHeight, Resources res,
                                   int resId) {

        Options options = new Options();
        options.inJustDecodeBounds = true;
        options.inSampleSize = getSampleSize(maxWidth, maxHeight, options);
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeResource(res, resId, options);
        return bitmap;
    }

    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        if (bitmap == null || bitmap.isRecycled()) {
            return null;
        }
        // 旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        // 创建新的图片
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Bitmap resizedBitmap = null;
        for (; ; ) {
            try {
                resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix,
                        true);
                break;
            } catch (OutOfMemoryError e) {
                // TODO Auto-generated catch block
                w *= 0.8;
                h *= 0.8;
                e.printStackTrace();
            } catch (Throwable ex) {
                printErrorStackInfo(ex);
            }

        }
        return resizedBitmap;
    }

}
