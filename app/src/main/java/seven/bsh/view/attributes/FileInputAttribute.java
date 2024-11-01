package seven.bsh.view.attributes;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;

import java.io.File;

import seven.bsh.db.entity.QueueFile;
import seven.bsh.view.attributes.fields.FileInputField;
import seven.bsh.view.attributes.settings.FileInputSettings;
import seven.bsh.view.attributes.settings.IInputAttributeSettings;
import seven.bsh.view.attributes.values.FileInputValue;
import seven.bsh.view.attributes.values.IDataValue;
import seven.bsh.view.attributes.views.FileInputView;

public class FileInputAttribute extends InputAttributeItem implements
    FileInputField.OnFileClickListener {
    public static final String EMPTY_FILE = "";

    protected OnFileClickListener listener;
    public String waterText;


    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public FileInputAttribute(Context context) {
        super(context, new FileInputSettings(), new FileInputValue(context));
    }

    public FileInputAttribute(Context context, IInputAttributeSettings settings, IDataValue value) {
        super(context, settings, value);
    }

    //---------------------------------------------------------------------------
    //
    // HANDLERS
    //
    //---------------------------------------------------------------------------

    @Override
    public void onFileClick() {
        if (listener != null) {
            listener.onFileClick(this);
        }
    }

    //---------------------------------------------------------------------------
    //
    // PRIVATE METHODS
    //
    //---------------------------------------------------------------------------


    public static String getPath(final Context context, final Uri uri) {
        // DocumentProvider
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.parseLong(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    private interface PhotosDownloadedCallback {
        void allPhotosDownloaded();
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    public void parseFileModel(QueueFile fileModel) {
        FileInputView view = (FileInputView) getView();
        if (view == null) {
            File file = fileModel.getFile();
            FileInputValue value = (FileInputValue) getValue();
            value.setFile(file);
            value.setPath(file.getName());
        } else {
            FileInputField field = (FileInputField) getField();
            field.setFileModel(fileModel);
            field.setFile(fileModel.getFile());
        }
    }

    @Override
    public View createFieldView() {
        initField();
        FileInputField field = (FileInputField) getField();
        field.setListener(this);
        layout = field.getView();
        return layout;
    }

    public void setActivityResult(int requestCode, int resultCode, Intent data, File tempFile) {
        FileInputField field = (FileInputField) getField();
        field.setActivityResult(resultCode, data, tempFile);
    }

    @Override
    protected void initField() {
        setField(new FileInputField(getContext(), this));
    }

    @Override
    protected void initView() {
        setView(new FileInputView(getContext(), this));
    }

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    public File getFile() {
        FileInputField field = (FileInputField) getField();
        return field.getFile();
    }

    public void setOnFileClickListener(OnFileClickListener listener) {
        this.listener = listener;
    }

    //---------------------------------------------------------------------------
    //
    // INTERFACES
    //
    //---------------------------------------------------------------------------

    public interface OnFileClickListener {
        void onFileClick(FileInputAttribute target);
    }
}
