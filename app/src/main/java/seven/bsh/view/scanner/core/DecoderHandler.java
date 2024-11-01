package seven.bsh.view.scanner.core;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import seven.bsh.R;
import seven.bsh.view.scanner.decoder.zxing.BarcodeDecoder;

public class DecoderHandler extends Handler {
    private static final String TAG = DecoderHandler.class.getSimpleName();

    private Handler mHandler;
    private BarcodeDecoder mDecoder;

    public DecoderHandler(Handler handler, BarcodeDecoder decoder) {
        mHandler = handler;
        mDecoder = decoder;
    }

    @Override
    public void handleMessage(Message message) {
        if (message.what == R.id.qr_message_decode_data) {
            decode((byte[]) message.obj, message.arg1, message.arg2);
        }
    }

    private void decode(byte[] data, int width, int height) {
        try {
            final String result = mDecoder.decode(data, width, height);

            if (!TextUtils.isEmpty(result)) {
                Message.obtain(mHandler, R.id.qr_message_decode_result_ok, result).sendToTarget();
            } else {
                Message.obtain(mHandler, R.id.qr_message_decode_result_nodata).sendToTarget();
            }
        } catch (Throwable err) {
            Log.e(TAG, err.getMessage(), err);
            Message.obtain(mHandler, R.id.qr_message_decode_result_nodata).sendToTarget();
        }
    }
}
