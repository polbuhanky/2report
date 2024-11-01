package seven.bsh.view.attributes.fields;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.iconics.view.IconicsButton;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import seven.bsh.R;
import seven.bsh.db.entity.QueueFile;
import seven.bsh.services.BitmapService;
import seven.bsh.view.attributes.FileInputAttribute;
import seven.bsh.view.attributes.values.FileInputValue;

import static seven.bsh.view.attributes.FileInputAttribute.EMPTY_FILE;

public class FileInputField extends BaseInputField implements View.OnClickListener {
    protected OnFileClickListener listener;
    protected ImageView actionBtn;
    protected IconicsButton restoreBtn;
    protected TextView fileLabel;
    protected File file;
    protected Context context;
    protected boolean fileUploaded;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public FileInputField(Context context, FileInputAttribute attribute) {
        super(context, attribute);
        this.context = context;
    }

    //---------------------------------------------------------------------------
    //
    // HANDLERS
    //
    //---------------------------------------------------------------------------

    @Override
    public void onClick(View view) {
        // clear focus hack
        View focusedChild = ((ViewGroup) view.getParent().getParent().getParent()).getFocusedChild();
        if (focusedChild != null) {
            focusedChild.clearFocus();
        }

        if (view != actionBtn) {
            setFile(new File(EMPTY_FILE));
            return;
        }

        if (file == null) {
            onAddClick();
        } else {
            setFile(null);
        }
    }

    protected void onAddClick() {
        if (listener != null) {
            listener.onFileClick();
        }
    }

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    @Override
    public View getView() {
        View view = View.inflate(getContext(), R.layout.partial_attribute_file, null);
        errorField = view.findViewById(R.id.error);

        TextView labelField = view.findViewById(R.id.label);
        labelField.setText(getTitleLabel());

        fileLabel = view.findViewById(R.id.file_label);
        fileLabel.setText(R.string.attribute_file_empty);

        actionBtn = view.findViewById(R.id.action_btn);
        actionBtn.setOnClickListener(this);

        restoreBtn = view.findViewById(R.id.restore_btn);
        restoreBtn.setOnClickListener(this);
        restoreBtn.setVisibility(View.GONE);

        FileInputValue value = (FileInputValue) getValue();
        if (value.getPath() != null) {
            fileLabel.setText(R.string.attribute_file_remote);
        }
        return view;
    }

    public void setActivityResult(int resultCode, Intent data, File tempFile) {
        if (resultCode == Activity.RESULT_OK) {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                Bitmap bitmap = BitmapFactory.decodeFile(tempFile.getPath());
                int orientation = 0;
                SimpleDateFormat watermarkDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault());
                ExifInterface exif = null;
                try {
                    exif = new ExifInterface(tempFile.getPath());
                    orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                bitmap = drawWaterMarkOnBitmap(bitmap, watermarkDateFormat.format(new Date()), false, orientation);

            if (data != null && data.hasExtra("file")) {
                setFile(new File(data.getStringExtra("file")));
            } else {
                try {
                    setFile(BitmapService.saveBitmapToFile(context, bitmap));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (fileUploaded) {
                restoreBtn.setVisibility(View.VISIBLE);
            }
            });
        }
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

    @Override
    public void prepareDataValue() {
        if (file == null) {
            return;
        }
        FileInputValue value = (FileInputValue) getValue();
        value.setPath(file.getAbsolutePath());
        value.setFile(file);
    }

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        if (this.file == file) {
            return;
        }

        this.file = file;
        if (file == null) {
            //actionBtn.setText(R.string.attribute_file_add);
            fileLabel.setText(R.string.attribute_file_empty);
            if (fileUploaded) {
                restoreBtn.setVisibility(View.VISIBLE);
            }
        } else {
            String fileName = file.getName();
            if (fileName.equals(EMPTY_FILE)) {
                fileLabel.setText(R.string.attribute_file_remote);
             //   actionBtn.setText(R.string.attribute_file_delete);
                restoreBtn.setVisibility(View.GONE);
            } else {
                fileLabel.setText(fileName);
               // actionBtn.setText(R.string.attribute_file_delete);
            }
        }
    }

    public void setListener(OnFileClickListener listener) {
        this.listener = listener;
    }

    public void setFileModel(QueueFile fileModel) {
        if (fileModel.getPath() == null) {
            fileLabel.setText(R.string.attribute_file_empty);
        } else {
            fileLabel.setText(fileModel.getFile().getName());
        }
    }

    //---------------------------------------------------------------------------
    //
    // INTERFACES
    //
    //---------------------------------------------------------------------------

    public interface OnFileClickListener {
        void onFileClick();
    }
}
