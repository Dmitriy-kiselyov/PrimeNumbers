package ru.dima_and_tanysha.primes.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import ru.dima_and_tanysha.primes.util.PrimesReader;

import javax.imageio.ImageIO;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;

public class PrimesCanvas extends Canvas {

    private BooleanProperty mHasImage = new SimpleBooleanProperty(false);

    private int mImageWidth;
    private int mImageHeight;

    private Model mModel;
    private CanvasStrategy mStrategy;

    public void setModel(Model model) {
        mModel = model;
        clearCanvas();
    }

    public void setStrategy(CanvasStrategy strategy) {
        mStrategy = strategy;
        mStrategy.init(this);
        setupSizeListeners();
    }

    private void setupSizeListeners() {
        Runnable run = () -> {
            clearCanvas();
            mStrategy.refresh();
        };

        this.widthProperty().addListener((observable, oldValue, newValue) -> run.run());
        this.heightProperty().addListener((observable, oldValue, newValue) -> run.run());
    }

    private void clearCanvas() {
        GraphicsContext context = this.getGraphicsContext2D();
        context.setFill(Color.WHITE);
        context.fillRect(0, 0, this.getWidth(), this.getHeight());
    }

    public void updateAndRedraw() {
        mImageWidth = mModel.getImageWidth();
        mImageHeight = mModel.getImageHeight();

        mHasImage.setValue(true);

        draw();
    }

    private double getRadius() {
        double outerRadius = Math.min(mImageWidth, mImageHeight) / 2 - 5;

        if (mModel.getShowToNumber() < 2)
            return 0;

        long totalCount = mModel.getShowToNumber() + 1;
        long layer = 0;
        long count = 0;
        for (long i = 1; count < totalCount; i += 6) {
            count += i;
            layer++;
        }

        count = layer * 2 - 1;
        return outerRadius / count;
    }

    protected void draw() {
        if (!mHasImage.get())
            return;

        clearCanvas();
        mStrategy.onDrawStart();

        //count byte data
        double centerX = mImageWidth / 2;
        double centerY = mImageHeight / 2;
        double radius = getRadius();

        try (PrimesReader in = new PrimesReader(mModel.getPrimeFile())) {
            long start = 2;
            long end = 7;
            long count = 6;
            double dist = radius * 2;

            while (true) {
                long prime = in.next();
                if (prime > mModel.getShowToNumber())
                    break;

                while (prime > end) {
                    count += 6;
                    start = end + 1;
                    end = start + count - 1;
                    dist += radius * 2;
                }

                long pos = prime - start;
                double angle = 2 * Math.PI / count * pos;
                double x = centerX + Math.cos(angle) * dist;
                double y = centerY - Math.sin(angle) * dist;

                mStrategy.onDraw(x, y, radius);
            }
        }
        catch (EOFException e) {
            //EOF
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            mStrategy.onDrawEnd();
        }
    }

    public boolean saveImage() {
        // Write to file system as a .png image
        try {
            Image image = mStrategy.prepareImage();
            String imageName = mStrategy.prepareImageName();

            File outFile = new File(mModel.getSaveImagePath() + "/" + imageName);
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", outFile);
            return true;
        }
        catch (IOException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }

    public boolean hasImage() {
        return mHasImage.get();
    }

    public BooleanProperty hasImageProperty() {
        return mHasImage;
    }
}
