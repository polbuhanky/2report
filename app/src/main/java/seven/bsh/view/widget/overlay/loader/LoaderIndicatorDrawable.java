package seven.bsh.view.widget.overlay.loader;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import android.util.TypedValue;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.HashMap;

import seven.bsh.R;

public class LoaderIndicatorDrawable extends Drawable implements Animatable {
    private static final int DURATION = 1500;
    private static final int RADIUS = 8;

    private final Context mContext;
    private HashMap<ValueAnimator, ValueAnimator.AnimatorUpdateListener> mUpdateListeners;
    private ArrayList<ValueAnimator> mAnimators;
    private Rect mDrawBounds;
    private Paint mPaint;
    private float[] mTranslateX = new float[4];
    private float[] mTranslateY = new float[4];
    private boolean mHasAnimators;
    private int mAlpha = 255;
    private float mRadius;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public LoaderIndicatorDrawable(Context context, int width, int height) {
        mContext = context;
        Resources r = context.getResources();
        mRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, RADIUS, r.getDisplayMetrics());

        mUpdateListeners = new HashMap<>();
        mDrawBounds = new Rect(0, 0, width, height);

        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
    }

    //---------------------------------------------------------------------------
    //
    // HANDLERS
    //
    //---------------------------------------------------------------------------

    public ArrayList<ValueAnimator> onCreateAnimators() {
        ArrayList<ValueAnimator> animators = new ArrayList<>();
        int width = getWidth();
        int height = getHeight();
        float startX = mRadius;
        float startY = mRadius;
        float endX = width - startX;
        float endY = height - startY;
        ValueAnimator translateXAnim;
        ValueAnimator translateYAnim;

        for (int i = 0; i < 4; i++) {
            final int index = i;
            if (i == 0) {
                translateXAnim = ValueAnimator.ofFloat(startX, endX, endX, startX, startX);
                translateYAnim = ValueAnimator.ofFloat(startY, startY, endY, endY, startY);
            } else if (i == 1) {
                translateXAnim = ValueAnimator.ofFloat(endX, endX, startX, startX, endX);
                translateYAnim = ValueAnimator.ofFloat(startY, endY, endY, startY, startY);
            } else if (i == 2) {
                translateXAnim = ValueAnimator.ofFloat(endX, startX, startX, endX, endX);
                translateYAnim = ValueAnimator.ofFloat(endY, endY, startY, startY, endY);
            } else {
                translateXAnim = ValueAnimator.ofFloat(startX, startX, endX, endX, startX);
                translateYAnim = ValueAnimator.ofFloat(endY, startY, startY, endY, endY);
            }

            translateXAnim.setDuration(DURATION);
            translateXAnim.setInterpolator(new LinearInterpolator());
            translateXAnim.setRepeatCount(-1);
            addUpdateListener(translateXAnim, new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mTranslateX[index] = (float) animation.getAnimatedValue();
                    postInvalidate();
                }
            });

            translateYAnim.setDuration(DURATION);
            translateYAnim.setInterpolator(new LinearInterpolator());
            translateYAnim.setRepeatCount(-1);
            addUpdateListener(translateYAnim, new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mTranslateY[index] = (float) animation.getAnimatedValue();
                    postInvalidate();
                }
            });

            animators.add(translateXAnim);
            animators.add(translateYAnim);
        }
        return animators;
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        setDrawBounds(bounds);
    }

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    @Override
    public void draw(Canvas canvas) {
        for (int i = 0; i < 4; i++) {
            mPaint.setColor(ContextCompat.getColor(mContext, R.color.accent));

            canvas.save();
            canvas.translate(mTranslateX[i], mTranslateY[i]);
            canvas.drawCircle(0, 0, mRadius, mPaint);
            canvas.restore();
        }
    }

    @Override
    public void start() {
        ensureAnimators();
        if (mAnimators == null || isStarted()) {
            return;
        }

        startAnimators();
        invalidateSelf();
    }

    @Override
    public void stop() {
        stopAnimators();
    }

    @Override
    public boolean isRunning() {
        return !mAnimators.isEmpty() && mAnimators.get(0).isRunning();
    }

    public void addUpdateListener(ValueAnimator animator, ValueAnimator.AnimatorUpdateListener updateListener) {
        mUpdateListeners.put(animator, updateListener);
    }

    public void setDrawBounds(Rect drawBounds) {
        drawBounds = new Rect(drawBounds.left, drawBounds.top, drawBounds.right, drawBounds.bottom);
    }

    public void postInvalidate() {
        invalidateSelf();
    }

    //---------------------------------------------------------------------------
    //
    // PRIVATE METHODS
    //
    //---------------------------------------------------------------------------

    private void ensureAnimators() {
        if (!mHasAnimators) {
            mAnimators = onCreateAnimators();
            mHasAnimators = true;
        }
    }

    private void startAnimators() {
        for (ValueAnimator animator : mAnimators) {
            ValueAnimator.AnimatorUpdateListener updateListener = mUpdateListeners.get(animator);
            if (updateListener != null) {
                animator.addUpdateListener(updateListener);
            }

            animator.start();
        }
    }

    private void stopAnimators() {
        if (mAnimators == null) {
            return;
        }

        for (ValueAnimator animator : mAnimators) {
            if (animator != null && animator.isStarted()) {
                animator.removeAllUpdateListeners();
                animator.end();
            }
        }
    }

    private boolean isStarted() {
        for (ValueAnimator animator : mAnimators) {
            return animator.isStarted();
        }
        return false;
    }

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    public int getWidth() {
        return mDrawBounds.width();
    }

    public int getHeight() {
        return mDrawBounds.height();
    }

    public int getColor() {
        return mPaint.getColor();
    }

    public void setColor(int color) {
        mPaint.setColor(color);
    }

    @Override
    public void setAlpha(int alpha) {
        mAlpha = alpha;
    }

    @Override
    public int getAlpha() {
        return mAlpha;
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        // ignored
    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }
}
