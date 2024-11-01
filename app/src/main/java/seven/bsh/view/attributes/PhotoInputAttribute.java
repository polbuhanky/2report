package seven.bsh.view.attributes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.util.TypedValue;
import android.widget.Toast;

import java.io.File;

import seven.bsh.R;
import seven.bsh.services.BitmapService;
import seven.bsh.view.attributes.fields.FileInputField;
import seven.bsh.view.attributes.fields.PhotoInputField;
import seven.bsh.view.attributes.settings.PhotoInputSettings;
import seven.bsh.view.attributes.values.PhotoInputValue;
import seven.bsh.view.attributes.views.PhotoInputView;

public class PhotoInputAttribute extends FileInputAttribute implements DialogInterface.OnClickListener {
    public static final int REQUEST_CODE_IMAGE_CAPTURE = 100;
    public static final int REQUEST_CODE_GALLERY = 101;


    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //f
    //---------------------------------------------------------------------------

    public PhotoInputAttribute(Context context) {
        super(context, new PhotoInputSettings(), new PhotoInputValue(context));
    }

    //---------------------------------------------------------------------------
    //
    // HANDLERS
    //
    //---------------------------------------------------------------------------

    @Override
    public void onFileClick() {
        new AlertDialog.Builder(getContext())
                .setItems(R.array.photo_variants, this)
                .create()
                .show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case 0:
                ((OnPhotoFileClickListener) listener).onImageCaptureClick(this);
                break;

            case 1:
                ((OnPhotoFileClickListener) listener).onGalleryClick(this);
                break;

            default:
                super.onFileClick();
        }
    }

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    @Override
    public void initField() {
        setField(new PhotoInputField(getContext(), this));
    }

    @Override
    public void initView() {
        setView(new PhotoInputView(getContext(), this));
    }

    @Override
    public void setActivityResult(int requestCode, int resultCode, Intent data, File tempFile) {
        FileInputField field = (FileInputField) getField();
        if (requestCode == REQUEST_CODE_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            Bitmap bitmap = BitmapFactory.decodeFile(getRealPathFromIntentData(getContext(), uri));
            int orientation = 0;
            ExifInterface exif = null;
            try {
                exif = new ExifInterface(getRealPathFromIntentData(getContext(), uri));
                orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            bitmap = drawWaterMarkOnBitmap(bitmap, waterText, false, orientation);
            try {
                field.setFile(BitmapService.saveBitmapToFile(getContext(), bitmap));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        } else if (requestCode == REQUEST_CODE_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri imageUri = data.getData();
            if (imageUri == null) {
                return;
            }

            String path = getPath(getContext(), imageUri);
            if (path == null) {
                Toast.makeText(getContext(), R.string.attribute_file_error_remote, Toast.LENGTH_LONG).show();
                return;
            }

            tempFile = new File(path);
            field.setFile(tempFile);
        } else {
            super.setActivityResult(requestCode, resultCode, data, tempFile);
        }
    }

    public static String getRealPathFromIntentData(Context context, Uri selectedFile) {
        if ("file".equalsIgnoreCase(selectedFile.getScheme())) {
            return selectedFile.getPath();
        }

        String[] filePathColumn = {MediaStore.MediaColumns.DATA, MediaStore.Files.FileColumns.DATA};
        Cursor cursor = context.getContentResolver().query(selectedFile, filePathColumn, null, null, null);

        if (cursor == null) {
            Log.e("UTILS", "can't get real path " + selectedFile);
            return null;
        }

        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String realPath = cursor.getString(columnIndex);
        cursor.close();

        return realPath;
    }

    public Bitmap drawWaterMarkOnBitmap(Bitmap bitmap, String mText, boolean isFolder, int orientation) {
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
            } else if (orientation == 8) {
                Matrix matrix = new Matrix();
                matrix.postRotate(270);
                matrix.preScale(-1, 1);
                bitmap = Bitmap.createScaledBitmap(bitmap, x, y, true);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            }

            Canvas canvas = new Canvas(bitmap);

            if (!isFolder) {
                Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                paint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20, getContext().getResources().getDisplayMetrics()));
                paint.setShadowLayer(1f, 0f, 1f, Color.DKGRAY);
                Rect bounds = new Rect();
                paint.getTextBounds(mText, 0, mText.length(), bounds);
                x = (bitmap.getWidth() - bounds.width() - 25);
                y = (bitmap.getHeight() - bounds.height());
                Paint.FontMetrics fm = new Paint.FontMetrics();
                paint.setColor(Color.WHITE);
                paint.getFontMetrics(fm);
                int margin = 20;
//                canvas.drawRect(x - 50 - margin, y - 120 + fm.top - margin,
//                        x + paint.measureText(mText.split("\n")[0]) + margin, y - 65 + fm.bottom
//                                + margin, paint);
                paint.setColor(Color.parseColor("#039FE0"));
                canvas.drawText(mText.split("\n")[0], x - 50 - margin, y - 50 - margin, paint);
                try {
                    canvas.drawText(mText.split("\n")[1], x - 50 - margin, y - 120 - margin, paint);
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }

            } else {
                Drawable folderLogo = getContext().getResources().getDrawable(R.drawable.ic_folder_watermark);
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
    // INTERFACES
    //
    //---------------------------------------------------------------------------

    public interface OnPhotoFileClickListener extends OnFileClickListener {
        void onImageCaptureClick(PhotoInputAttribute target);

        void onGalleryClick(PhotoInputAttribute target);
    }
}
