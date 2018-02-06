package ru.dima_and_tanysha.primes.model;

import javafx.scene.image.Image;

public interface CanvasStrategy {

    void init(PrimesCanvas canvas);

    void onDrawStart();

    void onDraw(double x, double y, double radius);

    void onDrawEnd();

    void refresh();

    Image prepareImage();

    String prepareImageName();

}
