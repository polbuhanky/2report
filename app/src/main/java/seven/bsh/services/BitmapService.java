package seven.bsh.services;


import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.os.Environment;
import android.util.Base64;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import seven.bsh.R;

public class BitmapService {
    private static List<Intent> addIntentsToList(Context context, List<Intent> list, Intent intent) {
        List<ResolveInfo> resInfo = context.getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resInfo) {
            String packageName = resolveInfo.activityInfo.packageName;
            Intent targetedIntent = new Intent(intent);
            targetedIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            targetedIntent.setPackage(packageName);
            list.add(targetedIntent);
        }
        return list;
    }


    public static Bitmap modifyOrientation(Bitmap bitmap, String image_absolute_path) throws IOException {
        try {
            ExifInterface ei = new ExifInterface(image_absolute_path);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return rotate(bitmap, 90);

                case ExifInterface.ORIENTATION_ROTATE_180:
                    return rotate(bitmap, 180);

                case ExifInterface.ORIENTATION_ROTATE_270:
                    return rotate(bitmap, 270);

                case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                    return flip(bitmap, true, false);

                case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                    return flip(bitmap, false, true);

                default:
                    return bitmap;
            }
        } catch (Exception exc) {
            return bitmap;
        }
    }

    private static Bitmap rotate(Bitmap bitmap, float degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    private static Bitmap flip(Bitmap bitmap, boolean horizontal, boolean vertical) {
        Matrix matrix = new Matrix();
        matrix.preScale(horizontal ? -1 : 1, vertical ? -1 : 1);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int maxSize) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > maxSize || width > maxSize)
            if (height >= width)
                inSampleSize = height / maxSize;
            else
                inSampleSize = width / maxSize;

        return inSampleSize;
    }

    public static String getBitmapBytesEncodedBase64(Bitmap bitmap) {
        ByteArrayOutputStream blob = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, blob);
        byte[] b = blob.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public static File saveBitmapToFile(Context context, Bitmap bitmap) throws Exception {
        File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), String.valueOf(System.currentTimeMillis()) + ".jpeg");
        return saveBitmapToFile(f, bitmap);
    }

    public static File saveBitmapToFile(File file, Bitmap bitmap) throws Exception {
        if (file.createNewFile()) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos);
            byte[] bitmapData = bos.toByteArray();

            //write the bytes in file
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapData);
            fos.flush();
            fos.close();

            return file;
        } else
            throw new Exception("Не удалось создать файл");
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}
