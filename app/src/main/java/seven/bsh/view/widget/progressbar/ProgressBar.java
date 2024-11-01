package seven.bsh.view.widget.progressbar;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.util.Locale;

import seven.bsh.R;
import seven.bsh.utils.Converter;

import static android.graphics.Bitmap.Config.ARGB_8888;

public class ProgressBar extends View {
    private static final String KEY_USER_PROGRESS = "ProgressBar.userProgress";
    private static final String KEY_DRAWN_PROGRESS = "ProgressBar.drawnProgress";
    private static final String TAG = "ProgressBar";

    private Paint mProgressPaint;
    private Paint mStripePaint;
    private Paint mBgPaint;
    private Paint mStrokePaint;
    private Paint mTilePaint;
    private Canvas mProgressCanvas;
    private Bitmap mProgressBitmap;
    private Bitmap mStripeTile;
    private int mUserProgress;
    private int mDrawnProgress;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public ProgressBar(Context context) {
        super(context);
        init();
    }

    public ProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    //---------------------------------------------------------------------------
    //
    // HANDLERS
    //
    //---------------------------------------------------------------------------

    @Override
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(TAG, super.onSaveInstanceState());
        bundle.putInt(KEY_USER_PROGRESS, mUserProgress);
        bundle.putInt(KEY_DRAWN_PROGRESS, mDrawnProgress);
        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            mUserProgress = bundle.getInt(KEY_USER_PROGRESS);
            mDrawnProgress = bundle.getInt(KEY_DRAWN_PROGRESS);
            state = bundle.getParcelable(TAG);
        }

        super.onRestoreInstanceState(state);
        updateBootstrapState();
        setProgress(mUserProgress);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (h != oldh) {
            mStripeTile = null;
        }

        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Resources resources = getContext().getResources();
        float w = getWidth();
        float h = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, resources.getDisplayMetrics());

        if (w <= 0) {
            return;
        }

        if (mProgressBitmap == null) {
            mProgressBitmap = Bitmap.createBitmap((int) w, (int) h, ARGB_8888);
        }

        if (mProgressCanvas == null) {
            mProgressCanvas = new Canvas(mProgressBitmap);
        }

        mProgressCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        float ratio = mDrawnProgress / 100f;
        int lineEnd = (int) (w * ratio);
        float offset = (h * 2);

        if (mStripeTile == null) {
            mStripeTile = createTile(h, mStripePaint, mProgressPaint);
        }

        float start = -offset;

        while (start < lineEnd) {
            mProgressCanvas.drawBitmap(mStripeTile, start, 0, mTilePaint);
            start += mStripeTile.getWidth();
        }

        mProgressCanvas.drawRect(lineEnd, 0, w, h, mBgPaint);
        mProgressCanvas.drawRect(lineEnd, 0, w, h, mStrokePaint);

        float corners = Converter.dip2Px(getContext(), 2);
        Bitmap round = createRoundedBitmap(mProgressBitmap, corners);
        canvas.drawBitmap(round, 0, 0, mTilePaint);
    }

    //---------------------------------------------------------------------------
    //
    // PRIVATE METHODS
    //
    //---------------------------------------------------------------------------

    private void init() {
        mTilePaint = new Paint();

        mProgressPaint = new Paint();
        mProgressPaint.setStyle(Paint.Style.FILL);
        mProgressPaint.setAntiAlias(true);

        mStripePaint = new Paint();
        mStripePaint.setStyle(Paint.Style.FILL);
        mStripePaint.setAntiAlias(true);

        mBgPaint = new Paint();
        mBgPaint.setStyle(Paint.Style.FILL);
        mBgPaint.setColor(ContextCompat.getColor(getContext(), R.color.light_gray));

        mStrokePaint = new Paint();
        mStrokePaint.setStyle(Paint.Style.STROKE);
        mStrokePaint.setColor(ContextCompat.getColor(getContext(), R.color.gray));
        mStrokePaint.setStrokeWidth(Converter.dip2Px(getContext(), 1));

        mUserProgress = 0;
        mDrawnProgress = mUserProgress;

        updateBootstrapState();
        setProgress(mUserProgress);
    }

    private static Bitmap createTile(float h, Paint stripePaint, Paint progressPaint) {
        Bitmap bm = Bitmap.createBitmap((int) h * 2 - 2, (int) h, ARGB_8888);
        Canvas tile = new Canvas(bm);

        float x = 0;
        Path path = new Path();

        path.moveTo(x, h);
        path.lineTo(x, 0);
        path.lineTo(h, 0);
        tile.drawPath(path, stripePaint);

        path.reset();
        path.moveTo(x - 1, h);
        path.lineTo(x + h - 1, 0);
        path.lineTo(x + (h * 2) - 1, 0);
        path.lineTo(x + h - 1, h);
        tile.drawPath(path, progressPaint);

        x += h;
        path.reset();
        path.moveTo(x - 2, h);
        path.lineTo(x + h - 2, h);
        path.lineTo(x + h - 2, 0);
        tile.drawPath(path, stripePaint);

        return bm;
    }

    private static Bitmap createRoundedBitmap(Bitmap bitmap, float cornerRadius) {
        Bitmap roundedBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), ARGB_8888);
        Canvas canvas = new Canvas(roundedBitmap);

        final Paint paint = new Paint();
        final Rect frame = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        // prepare canvas for transfer
        paint.setAntiAlias(true);
        paint.setColor(0xFFFFFFFF);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawRoundRect(new RectF(frame), cornerRadius, cornerRadius, paint);

        // draw bitmap
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, frame, frame, paint);

        return roundedBitmap;
    }

    private void updateBootstrapState() {
        mProgressPaint.setColor(0xFF203D69);
        mStripePaint.setColor(0xFF274A75);
        invalidateDrawCache();
        invalidate();
    }

    private void invalidateDrawCache() {
        mStripeTile = null;
        mProgressBitmap = null;
        mProgressCanvas = null;
    }

    //---------------------------------------------------------------------------
    //
    // PUBLIC ACCESSORS
    //
    //---------------------------------------------------------------------------

    public void setProgress(int progress) {
        if (progress < 0 || progress > 100) {
            Locale locale = Locale.getDefault();
            String s = String.format(
                locale,
                "Invalid value '%d' - progress must be an integer in the range 0-100",
                progress
            );
            throw new IllegalArgumentException(s);
        }

        mUserProgress = progress;
        mDrawnProgress = progress;
        invalidate();
    }
}
