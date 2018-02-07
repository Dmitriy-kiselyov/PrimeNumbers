package ru.dima_and_tanysha.primes.model;

import javafx.geometry.Rectangle2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * Created by Pussy_penetrator on 30.03.2017.
 */
public class CanvasCircleStrategy implements CanvasStrategy {

    private Model mModel;
    private PrimesCanvas mCanvas;

    private int mWidth;
    private int mHeight;
    private long mToNumber;
    private Image mImage;

    public CanvasCircleStrategy(Model model) {
        mModel = model;
    }

    @Override
    public void init(PrimesCanvas canvas) {
        mCanvas = canvas;
    }

    @Override
    public void onDrawStart() {
        mWidth = mModel.getImageWidth();
        mHeight = mModel.getImageWidth();
        mToNumber = mModel.getShowToNumber();

        GraphicsContext context = mCanvas.getGraphicsContext2D();
        context.setFill(Color.BLACK);
    }

    @Override
    public void onDraw(double x, double y, double radius) {
        GraphicsContext context = mCanvas.getGraphicsContext2D();
        context.fillOval(x - radius, y - radius, radius * 2, radius * 2);
    }

    @Override
    public void onDrawEnd() {
        SnapshotParameters params = new SnapshotParameters();
        Rectangle2D viewport = new Rectangle2D(0, 0, mWidth, mHeight);
        params.setViewport(viewport);

        mImage = mCanvas.snapshot(params, null); //todo ret rid of snapshot (crops image)
    }

    @Override
    public void refresh() {
        mCanvas.getGraphicsContext2D().drawImage(mImage, 0, 0);
    }

    @Override
    public Image prepareImage() {
        return mImage;
    }

    @Override
    public String prepareImageName() {
        String imageName = "primes_" + mToNumber + " (" + mWidth + "x" + mHeight + ")";
        imageName += ".png";

        return imageName;
    }
}
