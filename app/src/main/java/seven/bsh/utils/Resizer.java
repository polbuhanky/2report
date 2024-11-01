package seven.bsh.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.util.Log;
import android.util.TypedValue;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import seven.bsh.R;

public class Resizer {
    private static final int BUFFER_SIZE = 8192;
    private static final String TAG = "Resizer";

    private static final int[][] SIZES = new int[][]{
        new int[]{0, 80},
        new int[]{1600, 100}, new int[]{1600, 80},
        new int[]{1200, 100}, new int[]{1200, 80},
        new int[]{1024, 100}, new int[]{1024, 80},
    };

    //---------------------------------------------------------------------------
    //
    // PRIVATE METHODS
    //
    //---------------------------------------------------------------------------

    /**
     * Ресайз картинки
     *
     * @param bitmap исходная картинка
     * @param size размер после обработки
     * @param quality качество после обработки
     * @param file исходный файл
     */
    private static void resizeImage(Bitmap bitmap, int size, int quality, File file, double longitude, double latitude, Context context) throws IOException {
        float width = bitmap.getWidth();
        float height = bitmap.getHeight();
        float k;

        if (size == 0) {
            k = 1;
        } else {
            if (width > height) {
                k = size / width;
            } else {
                k = size / height;
            }
        }
        if (k >= 1) {
            return;
        }

        int newWidth = (int) (width * k);
        int newHeight = (int) (height * k);

        file.delete();
        file.createNewFile();

        Matrix matrix = new Matrix();
        matrix.postScale(k, k);

        Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
        writeBitmap(newBitmap, file, quality, longitude, latitude, context);
    }

    private static void writeBitmap(Bitmap bitmap, File file, int quality, double longitude, double latitude, Context context) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        SimpleDateFormat watermarkDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault());
        ExifInterface exif = null;
        int orientation = 0;
        try {
            exif = new ExifInterface(file.getPath());
             orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String waterText= watermarkDateFormat.format(new Date()) + "\n" + longitude + " " + latitude;
        BufferedOutputStream bos = new BufferedOutputStream(fos, BUFFER_SIZE);
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, bos);
        drawWaterMarkOnBitmap(bitmap, waterText, false, orientation, context);
        bos.flush();
        bos.close();
        fos.close();
    }

    public static Bitmap drawWaterMarkOnBitmap(Bitmap bitmap, String mText, boolean isFolder, int orientation, Context context) {
        try {
            int x = (bitmap.getWidth());
            int y = (bitmap.getHeight());
            Bitmap.Config bitmapConfig = bitmap.getConfig();
            if (bitmapConfig == null) {
                bitmapConfig = Bitmap.Config.ARGB_8888;
            }
            bitmap = bitmap.copy(bitmapConfig, true);
            if (orientation == 6) {
                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                bitmap = Bitmap.createScaledBitmap(bitmap, x, y, true);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            }

            Canvas canvas = new Canvas(bitmap);

            if (!isFolder) {
                Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                paint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20, context.getResources().getDisplayMetrics()));
                paint.setShadowLayer(1f, 0f, 1f, Color.DKGRAY);
                Rect bounds = new Rect();
                paint.getTextBounds(mText, 0, mText.length(), bounds);
                x = (bitmap.getWidth() - bounds.width() - 25);
                y = (bitmap.getHeight() - bounds.height());
                Paint.FontMetrics fm = new Paint.FontMetrics();
                paint.setColor(Color.WHITE);
                paint.getFontMetrics(fm);
                int margin = 5;
                canvas.drawRect(x - 50 - margin, y - 50 + fm.top - margin,
                        x - 50 + paint.measureText(mText) + margin, y - 50 + fm.bottom
                                + margin, paint);

                paint.setColor(Color.parseColor("#039FE0"));
                canvas.drawText(mText, x - 50 - margin, y - 50 - margin, paint);
            } else {
                Drawable folderLogo = context.getResources().getDrawable(R.drawable.ic_folder_watermark);
                int imageSizeFolder = 400;
                int xOffset = 250;
                int yOffset = 500;
                folderLogo.setBounds(x - imageSizeFolder - xOffset, y - yOffset, x - xOffset, y - yOffset + imageSizeFolder);
                folderLogo.draw(canvas);
            }

            return bitmap;
        } catch (Exception exc) {
            exc.printStackTrace();
            return null;
        }
    }

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    /**
     * Обработка картинки
     *
     * @param file Файл картинки
     * @param limit Верхний порог веса картинки, после которого надо ее оптимизировать
     */
    public static File prepareImage(File file, long limit, double longitude, double latitude, Context context) {
        try {
            if (file.length() <= limit) {
                Log.i(TAG, "В оптимизации не нуждается");
                return file;
            }

            String fileName = file.getName();
            String filePath = file.getPath();
            Bitmap bitmap = BitmapFactory.decodeFile(filePath);
            if (!fileName.matches("\\.jpe?g$")) {
                filePath = filePath.replaceAll("\\.\\w+$", ".jpg");
                file = new File(filePath);
                if (!file.exists()) {
                    file.createNewFile();
                }
                writeBitmap(bitmap, file, 80, longitude, latitude, context);
            }

            int length = SIZES.length;
            for (int i = 0; i < length; i++) {
                Resizer.resizeImage(bitmap, SIZES[i][0], SIZES[i][1], file, longitude, latitude, context);
                if (file.length() <= limit) {
                    Log.i(TAG, "Итерация: " + (i + 1));
                    break;
                }
            }
        } catch (Exception ex) {
            Log.e(TAG, ex.toString(), ex);
        }
        return file;
    }
}
