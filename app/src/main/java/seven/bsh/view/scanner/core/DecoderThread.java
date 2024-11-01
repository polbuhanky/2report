package seven.bsh.view.scanner.core;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.concurrent.CountDownLatch;

import seven.bsh.R;
import seven.bsh.view.scanner.decoder.zxing.BarcodeDecoder;

public class DecoderThread extends Thread {
    private final CountDownLatch mHandlerInitLatch;
    private Handler mDecoderHandler;
    private Handler mUiHandler;
    private BarcodeDecoder mDecoder;

    public DecoderThread(Handler uiHandler, BarcodeDecoder decoder) {
        mHandlerInitLatch = new CountDownLatch(1);
        mUiHandler = uiHandler;
        mDecoder = decoder;
    }

    @Override
    public void run() {
        Looper.prepare();
        mDecoderHandler = new DecoderHandler(mUiHandler, mDecoder);
        mHandlerInitLatch.countDown();
        Looper.loop();
    }

    public void shutdown() {
        final Message message = Message.obtain(getDecoderHandler(), R.id.qr_message_stop);
        if (message != null) {
            message.sendToTarget();
        }
    }

    Handler getDecoderHandler() {
        try {
            mHandlerInitLatch.await();
        } catch (InterruptedException ignored) {
            // ignored
        }
        return mDecoderHandler;
    }

    public void submitBarcodeRecognitionTask(byte[] bytes, int width, int height) {
        final Message message = Message.obtain(getDecoderHandler(), R.id.qr_message_decode_data, width, height, bytes);
        if (message != null) {
            message.sendToTarget();
        }
    }
}
