package ru.dima_and_tanysha.primes.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;

import java.nio.ByteBuffer;

public class CanvasMatrixStrategy implements CanvasStrategy {

    private WritableImage mWritableImage;
    private int mImageWidth;
    private int mImageHeight;

    private int[][] mPrimeCount;

    private Model mModel;
    private PrimesCanvas mCanvas;

    public CanvasMatrixStrategy(Model model) {
        super();
        mModel = model;
        setupListeners();
    }

    private void setupListeners() {
        mModel.filterProperty().addListener((observable, oldValue, newValue) -> refresh());
    }

    @Override
    public void init(PrimesCanvas canvas) {
        mCanvas = canvas;
    }

    @Override
    public void onDrawStart() {
        mImageWidth = mModel.getImageWidth();
        mImageHeight = mModel.getImageHeight();
        mWritableImage = new WritableImage(mImageWidth, mImageHeight);
        mPrimeCount = new int[mImageWidth][mImageHeight];
    }

    @Override
    public void onDraw(double x, double y, double radius) {
        mPrimeCount[(int) x][(int) y]++;
    }

    @Override
    public void onDrawEnd() {
        int max = max(mPrimeCount);
        if (max == 0)
            return;
        double norm = 255.0 / max;

        //count byte data
        int index = 0;
        int filter = mModel.getFilter();
        byte[] imageData = new byte[mImageWidth * mImageHeight * 3];

        for (int i = 0; i < mImageWidth; i++) {
            for (int j = 0; j < mImageHeight; j++) {
                int color = (int) (255 - mPrimeCount[i][j] * norm);

                if (color > filter) {
                    imageData[index++] = (byte) 255;
                    imageData[index++] = (byte) 255;
                    imageData[index++] = (byte) 255;
                } else {
                    imageData[index++] = (byte) color;
                    imageData[index++] = (byte) color;
                    imageData[index++] = (byte) color;
                }
            }
        }

        //draw
        GraphicsContext context = mCanvas.getGraphicsContext2D();
        PixelFormat<ByteBuffer> pixelFormat = PixelFormat.getByteRgbInstance();
        mWritableImage.getPixelWriter().setPixels(0, 0, mImageWidth, mImageHeight, pixelFormat, imageData, 0, mImageWidth * 3);
        context.drawImage(mWritableImage, 0, 0);
    }

    private int max(int[][] m) {
        int max = 0;
        for (int[] row : m)
            for (int value : row)
                max = Math.max(max, value);
        return max;
    }

    @Override
    public void refresh() {
        if (mPrimeCount != null)
            onDrawEnd();
    }

    @Override
    public Image prepareImage() {
        return mWritableImage;
    }

    @Override
    public String prepareImageName() {
        String imageName = "primes_" + mModel.getShowToNumber() + " (" + mImageWidth + "x" + mImageHeight + ")";
        if (mModel.getFilter() != mModel.MAX_FILTER)
            imageName += " filter=" + mModel.getFilter();
        imageName += ".png";

        return imageName;
    }
}
