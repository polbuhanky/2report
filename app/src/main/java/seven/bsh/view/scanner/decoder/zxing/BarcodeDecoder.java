package seven.bsh.view.scanner.decoder.zxing;

public interface BarcodeDecoder {
    String decode(byte[] image, int width, int height);
}
