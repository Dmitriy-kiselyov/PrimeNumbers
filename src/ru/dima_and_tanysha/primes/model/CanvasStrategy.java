package ru.dima_and_tanysha.primes.model;

import javafx.scene.image.WritableImage;

public interface CanvasStrategy {

    void init(PrimesCanvas canvas);

    void onDrawStart();

    void onDraw(double x, double y, double radius);

    void onDrawEnd();

    void refresh();

    WritableImage prepareImage();

    String prepareImageName();

}
