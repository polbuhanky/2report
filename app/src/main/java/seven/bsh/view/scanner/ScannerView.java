package seven.bsh.view.scanner;

import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import seven.bsh.R;
import seven.bsh.view.scanner.core.DecoderThread;
import seven.bsh.view.scanner.decoder.zxing.BarcodeDecoder;
import seven.bsh.view.scanner.decoder.zxing.ZXDecoder;

public class ScannerView extends FrameLayout implements CameraView.CameraViewListener {
    public final static long DEFAULT_SAMECODE_RESCAN_PROTECTION_TIME_MS = 5000;
    public final static long DEFAULT_DECODE_THROTTLE_MS = 800;

    protected CameraView camera;
    protected ScannerViewEventListener scannerViewEventListener;
    protected BarcodeDecoder decoder;

    private volatile String mLastDataDecoded;
    private volatile long mLastDataDecodedTimestamp;
    private volatile long mLastDataSubmittedTimestamp;
    private DecoderThread mDecoderThread;
    private DecoderResultHandler mDecoderResultHandler;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public ScannerView(final Context context) {
        super(context);
        initUI();
    }

    protected void initUI() {
        final View root = LayoutInflater.from(getContext()).inflate(getScannerLayoutResource(), this);
        camera = root.findViewById(R.id.zxscanlib_camera);
        camera.setListener(this);
        decoder = new ZXDecoder();
    }

    public ScannerView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        initUI();
    }

    public ScannerView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initUI();
    }

    @TargetApi(21)
    public ScannerView(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initUI();
    }

    //---------------------------------------------------------------------------
    //
    // HANDLERS
    //
    //---------------------------------------------------------------------------

    public void onCameraReady(Camera camera) {
        if (scannerViewEventListener != null) {
            scannerViewEventListener.onScannerReady();
        }
    }

    public void onCameraError() {
        if (scannerViewEventListener != null) {
            scannerViewEventListener.onScannerFailure();
        }
    }

    public void onPreviewData(final byte[] bytes, final int i, final Camera.Size size) {
        final long currentTime = System.currentTimeMillis();
        if (mDecoderThread != null && currentTime - mLastDataSubmittedTimestamp > DEFAULT_DECODE_THROTTLE_MS) {
            mLastDataSubmittedTimestamp = currentTime;
            mDecoderThread.submitBarcodeRecognitionTask(bytes, size.width, size.height);
        }
    }

    //---------------------------------------------------------------------------
    //
    // PRIVATE METHODS
    //
    //---------------------------------------------------------------------------

    private void shutodownThreadingSubsystem() {
        if (mDecoderThread != null) {
            mDecoderThread.shutdown();
        }

        mDecoderResultHandler = null;
        mDecoderThread = null;
    }

    private void processRecognizedBarcode(String data) {
        if (TextUtils.isEmpty(mLastDataDecoded) || !mLastDataDecoded.equalsIgnoreCase(data) ||
            (System.currentTimeMillis() - mLastDataDecodedTimestamp) > DEFAULT_SAMECODE_RESCAN_PROTECTION_TIME_MS) {
            mLastDataDecoded = data;
            mLastDataDecodedTimestamp = System.currentTimeMillis();
            notifyBarcodeRead(data);
        }
    }

    protected void notifyBarcodeRead(final String data) {
        if (scannerViewEventListener != null && !TextUtils.isEmpty(data)) {
            scannerViewEventListener.onCodeScanned(data);
        }
    }

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    /**
     * Starts scanner, using device default camera
     */
    public void startScanner() {
        mLastDataDecoded = null;
        initThreadingSubsystem();
        camera.start();
    }

    private void initThreadingSubsystem() {
        mDecoderResultHandler = new DecoderResultHandler();
        mDecoderThread = new DecoderThread(mDecoderResultHandler, decoder);
        mDecoderThread.start();
    }

    /**
     * Stops currently running scanner
     */
    public void stopScanner() {
        shutodownThreadingSubsystem();
        camera.stop();
    }

    //---------------------------------------------------------------------------
    //
    // PUBLIC ACCESSORS
    //
    //---------------------------------------------------------------------------

    public CameraView getCamera() {
        return camera;
    }

    public void setScannerViewEventListener(final ScannerViewEventListener scannerViewEventListener) {
        this.scannerViewEventListener = scannerViewEventListener;
    }

    protected int getScannerLayoutResource() {
        return R.layout.qr_view_scanner;
    }

    public interface ScannerViewEventListener {
        void onScannerReady();

        void onScannerFailure();

        void onCodeScanned(final String data);
    }

    class DecoderResultHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == R.id.qr_message_decode_result_ok) {
                processRecognizedBarcode((String) msg.obj);
            }
        }
    }
}
