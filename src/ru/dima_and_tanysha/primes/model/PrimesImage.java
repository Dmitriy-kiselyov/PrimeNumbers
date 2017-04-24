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

    private static final int IMAGE_WIDTH  = 1000;
    private static final int IMAGE_HEIGHT = 1000;

    private WritableImage mWritableImage;

    private int[][] mPrimeCount;
    private byte mImageData[] = new byte[IMAGE_WIDTH * IMAGE_HEIGHT * 3];

    private Model mModel;

    public PrimesImage() {
        super();
        setupListeners();
    }

    public PrimesImage(double width, double height) {
        super(width, height);
        setupListeners();
    }

    private void setupListeners() {
        this.widthProperty().addListener((observable, oldValue, newValue) -> clearCanvas());
        this.heightProperty().addListener((observable, oldValue, newValue) -> clearCanvas());
    }

    public void setModel(Model model) {
        mModel = model;
        mWritableImage = new WritableImage(IMAGE_WIDTH, IMAGE_HEIGHT);
        clearCanvas();
    }

    private void clearCanvas() {
        GraphicsContext context = this.getGraphicsContext2D();
        context.setFill(Color.WHITE);
        context.fillRect(0, 0, this.getWidth(), this.getHeight());
    }

    public void redraw() {
        onDraw();
    }

    private void onDraw() {
        clearCanvas();

        mModel.setAutoRadius(Math.min(IMAGE_WIDTH, IMAGE_HEIGHT) / 2 - 5);
        double radius = mModel.getRadius();

        mPrimeCount = countPrimes(radius);
        int max = max(mPrimeCount);
        double norm = 255.0 / max;

        //count byte data
        int index = 0;
        for (int i = 0; i < IMAGE_WIDTH; i++) {
            for (int j = 0; j < IMAGE_HEIGHT; j++) {
                int color = (int) (255 - mPrimeCount[i][j] * norm);
                mImageData[index++] = (byte) color;
                mImageData[index++] = (byte) color;
                mImageData[index++] = (byte) color;
            }
        }

        //draw
        GraphicsContext context = this.getGraphicsContext2D();
        PixelFormat<ByteBuffer> pixelFormat = PixelFormat.getByteRgbInstance();
        mWritableImage.getPixelWriter().setPixels(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT,
                                                  pixelFormat, mImageData, 0, IMAGE_WIDTH * 3);

        context.drawImage(mWritableImage, 0, 0);
    }

    private int max(int[][] m) {
        int max = 0;
        for (int i = 0; i < m.length; i++)
            for (int j = 0; j < m[i].length; j++)
                max = Math.max(max, m[i][j]);
        return max;
    }

    private int[][] countPrimes(double radius) {
        double centerX = IMAGE_WIDTH / 2;
        double centerY = IMAGE_HEIGHT / 2;

        int[][] m = new int[IMAGE_WIDTH][IMAGE_HEIGHT];

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

    public boolean saveImage(String path) {
        // Write snapshot to file system as a .png image
        File outFile = new File(path);
        try {
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
