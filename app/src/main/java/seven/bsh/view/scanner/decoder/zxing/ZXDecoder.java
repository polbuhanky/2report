package seven.bsh.view.scanner.decoder.zxing;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;

public class ZXDecoder implements BarcodeDecoder {
    final static Collection<BarcodeFormat> QR_CODE_FORMATS = EnumSet.of(BarcodeFormat.QR_CODE);
    protected Map<DecodeHintType, Object> hints = new EnumMap<>(DecodeHintType.class);
    private MultiFormatReader reader;

    public ZXDecoder() {
        reader = new MultiFormatReader();
        Collection<BarcodeFormat> decodeFormats = EnumSet.noneOf(BarcodeFormat.class);
        decodeFormats.addAll(QR_CODE_FORMATS);
        hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);
        hints.put(DecodeHintType.CHARACTER_SET, "utf-8");
        hints.put(DecodeHintType.TRY_HARDER, true);
        reader.setHints(hints);
    }

    public String decode(final byte[] image, final int width, final int height) {
        Result result = null;
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(new PlanarRotatedYUVLuminanceSource(image, width, height, 0, 0, width, height, true)));

        try {
            result = reader.decodeWithState(bitmap);
        } catch (Throwable ignored) {
        }

        reader.reset();

        if (result != null) {
            return result.getText();
        }

        return null;
    }
}
