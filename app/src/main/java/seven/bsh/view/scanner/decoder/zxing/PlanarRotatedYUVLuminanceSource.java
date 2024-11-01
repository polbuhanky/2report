package seven.bsh.view.scanner.decoder.zxing;

import com.google.zxing.LuminanceSource;
import com.google.zxing.PlanarYUVLuminanceSource;

public class PlanarRotatedYUVLuminanceSource extends LuminanceSource {
    private final byte[] yuvData;
    private final int dataWidth;
    private final int dataHeight;
    private final int left;
    private final int top;

    public PlanarRotatedYUVLuminanceSource(byte[] yuvData, int dataWidth, int dataHeight, int left, int top, int width,
                                           int height, boolean reverseHorizontal) {
        super(width, height);

        if (left + width <= dataWidth && top + height <= dataHeight) {
            this.yuvData = yuvData;
            this.dataWidth = dataWidth;
            this.dataHeight = dataHeight;
            this.left = left;
            this.top = top;

            if (reverseHorizontal) {
                reverseHorizontal(width, height);
            }
        } else {
            throw new IllegalArgumentException("Crop rectangle does not fit within image data.");
        }
    }

    private void reverseHorizontal(int width, int height) {
        byte[] yuvData = this.yuvData;
        int y = 0;

        for (int rowStart = top * dataWidth + left; y < height; rowStart += dataWidth) {
            int middle = rowStart + width / 2;
            int x1 = rowStart;

            for (int x2 = rowStart + width - 1; x1 < middle; --x2) {
                byte temp = yuvData[x1];
                yuvData[x1] = yuvData[x2];
                yuvData[x2] = temp;
                x1++;
            }

            y++;
        }
    }

    public byte[] getRow(int y, byte[] row) {
        if (y >= 0 && y < getHeight()) {
            int width = getWidth();

            if (row == null || row.length < width) {
                row = new byte[width];
            }

            int offset = (y + top) * dataWidth + left;
            System.arraycopy(yuvData, offset, row, 0, width);
            return row;
        } else {
            throw new IllegalArgumentException("Requested row is outside the image: " + y);
        }
    }

    public byte[] getMatrix() {
        int width = getWidth();
        int height = getHeight();

        if (width == dataWidth && height == dataHeight) {
            return yuvData;
        } else {
            int area = width * height;
            byte[] matrix = new byte[area];
            int inputOffset = top * dataWidth + left;

            if (width == dataWidth) {
                System.arraycopy(yuvData, inputOffset, matrix, 0, area);
                return matrix;
            } else {
                for (int y = 0; y < height; ++y) {
                    int outputOffset = y * width;
                    System.arraycopy(yuvData, inputOffset, matrix, outputOffset, width);
                    inputOffset += dataWidth;
                }
                return matrix;
            }
        }
    }

    public boolean isCropSupported() {
        return true;
    }

    public LuminanceSource crop(int left, int top, int width, int height) {
        return new PlanarYUVLuminanceSource(yuvData, dataWidth, dataHeight, this.left + left,
            this.top + top, width, height, false
        );
    }

    public boolean isRotateSupported() {
        return true;
    }

    public LuminanceSource rotateCounterClockwise() {
        return new PlanarRotatedYUVLuminanceSource(
            rotateYUV420Degree90(yuvData, dataWidth, dataHeight),
            dataWidth, dataHeight, left, top, getWidth(), getHeight(), false
        );
    }

    public LuminanceSource rotateCounterClockwise45() {
        return new PlanarRotatedYUVLuminanceSource(
            rotateYUV420Degree90(yuvData, dataWidth, dataHeight),
            dataWidth, dataHeight, left, top, getWidth(), getHeight(), false
        );
    }

    private byte[] rotateYUV420Degree90(byte[] data, int imageWidth, int imageHeight) {
        byte[] yuv = new byte[imageWidth * imageHeight * 3 / 2];
        // Rotate the Y luma
        int i = 0;
        for (int x = 0; x < imageWidth; x++) {
            for (int y = imageHeight - 1; y >= 0; y--) {
                yuv[i] = data[y * imageWidth + x];
                i++;
            }
        }

        // Rotate the U and V color components
        i = imageWidth * imageHeight * 3 / 2 - 1;

        for (int x = imageWidth - 1; x > 0; x -= 2) {
            for (int y = 0; y < imageHeight / 2; y++) {
                yuv[i] = data[(imageWidth * imageHeight) + (y * imageWidth) + x];
                i--;
                yuv[i] = data[(imageWidth * imageHeight) + (y * imageWidth) + (x - 1)];
                i--;
            }
        }

        return yuv;
    }

    public int[] renderThumbnail() {
        int width = getWidth() / 2;
        int height = getHeight() / 2;
        int[] pixels = new int[width * height];
        int inputOffset = top * dataWidth + left;

        for (int y = 0; y < height; ++y) {
            int outputOffset = y * width;
            for (int x = 0; x < width; ++x) {
                int grey = yuvData[inputOffset + x * 2] & 255;
                pixels[outputOffset + x] = -16777216 | grey * 65793;
            }

            inputOffset += dataWidth * 2;
        }

        return pixels;
    }

    public int getThumbnailWidth() {
        return getWidth() / 2;
    }

    public int getThumbnailHeight() {
        return getHeight() / 2;
    }
}
