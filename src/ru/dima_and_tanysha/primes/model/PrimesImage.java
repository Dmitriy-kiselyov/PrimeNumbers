package ru.dima_and_tanysha.primes.model;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

public class PrimesImage extends Canvas {

    private WritableImage mWritableImage;
    private int           mImageWidth;
    private int           mImageHeight;

    private int[][] mPrimeCount;
    private byte[]  mImageData;
    private double  mRadius;

    private Model mModel;

    public PrimesImage() {
        super();
        setupSizeListeners();
    }

    public PrimesImage(double width, double height) {
        super(width, height);
        setupSizeListeners();
    }

    private void setupSizeListeners() {
        this.widthProperty().addListener((observable, oldValue, newValue) -> onDraw());
        this.heightProperty().addListener((observable, oldValue, newValue) -> onDraw());
    }

    public void setModel(Model model) {
        mModel = model;
        clearCanvas();
        //Model listeners
        mModel.filterProperty().addListener((observable, oldValue, newValue) -> onDraw());
    }

    private void clearCanvas() {
        GraphicsContext context = this.getGraphicsContext2D();
        context.setFill(Color.WHITE);
        context.fillRect(0, 0, this.getWidth(), this.getHeight());
    }

    public void updateAndRedraw() {
        mImageWidth = mModel.getImageWidth();
        mImageHeight = mModel.getImageHeight();
        mWritableImage = new WritableImage(mImageWidth, mImageHeight);
        mImageData = new byte[mImageWidth * mImageHeight * 3];
        mRadius = getRadius();
        mPrimeCount = countPrimes(mRadius);

        onDraw();
    }

    private void onDraw() {
        clearCanvas();

        if (mImageData == null)
            return;

        int max = max(mPrimeCount);
        double norm = 255.0 / max;

        //count byte data
        int index = 0;
        int filter = mModel.getFilter();
        for (int i = 0; i < mImageWidth; i++) {
            for (int j = 0; j < mImageHeight; j++) {
                int color = (int) (255 - mPrimeCount[i][j] * norm);

                if (color > filter) {
                    mImageData[index++] = (byte) 255;
                    mImageData[index++] = (byte) 255;
                    mImageData[index++] = (byte) 255;
                } else {
                    mImageData[index++] = (byte) color;
                    mImageData[index++] = (byte) color;
                    mImageData[index++] = (byte) color;
                }
            }
        }

        //draw
        GraphicsContext context = this.getGraphicsContext2D();
        PixelFormat<ByteBuffer> pixelFormat = PixelFormat.getByteRgbInstance();
        mWritableImage.getPixelWriter().setPixels(0, 0, mImageWidth, mImageHeight,
                                                  pixelFormat, mImageData, 0, mImageWidth * 3);

        context.drawImage(mWritableImage, 0, 0);
    }

    private double getRadius() {
        double outerRadius = Math.min(mImageWidth, mImageHeight) / 2 - 5;

        if (mModel.getTotalCount() == 0)
            return 0;
        if (mModel.getTotalCount() == 1)
            return outerRadius;

        long totalCount = mModel.getTotalCount() - 1;
        long layer = 0;
        long count = 0;
        for (long i = 1; count < totalCount; i += 6) {
            count += i;
            layer++;
        }

        count = layer * 2 - 1;
        return outerRadius / count;
    }

    private int max(int[][] m) {
        int max = 0;
        for (int i = 0; i < m.length; i++)
            for (int j = 0; j < m[i].length; j++)
                max = Math.max(max, m[i][j]);
        return max;
    }

    private int[][] countPrimes(double radius) {
        double centerX = mImageWidth / 2;
        double centerY = mImageHeight / 2;

        int[][] m = new int[mImageWidth][mImageHeight];

        //draw circles
        int[] primes = mModel.getPrimes();
        int start = 2;
        int end = 7;
        int count = 6;
        double dist = radius * 2;

        for (int prime : primes) {
            while (prime > end) {
                count += 6;
                start = end + 1;
                end = start + count - 1;
                dist += radius * 2;
            }

            int pos = prime - start;
            double angle = 2 * Math.PI / count * pos;
            double x = centerX + Math.cos(angle) * dist;
            double y = centerY - Math.sin(angle) * dist;

            m[(int) x][(int) y]++;
        }

        return m;
    }

    public boolean saveImage() {
        // Write to file system as a .png image
        try {
            String imageName = "primes_" + mModel.getPrimeFile().getPrimeCount() +
                               " (" + mImageWidth + "x" + mImageHeight + ").png";
            File outFile = new File(mModel.getSaveImagePath() + "/" + imageName);
            ImageIO.write(SwingFXUtils.fromFXImage(mWritableImage, null),
                          "png", outFile);
            return true;
        }
        catch (IOException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }

}
