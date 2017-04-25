//package ru.dima_and_tanysha.primes.model;
//
//import javafx.scene.canvas.Canvas;
//import javafx.scene.canvas.GraphicsContext;
//import javafx.scene.paint.Color;
//
///**
// * Created by Pussy_penetrator on 30.03.2017.
// */
//public class PrimesCanvas extends Canvas {
//
//    private Model mModel;
//
//    private double mLastPressX, mLastPressY;
//    private double mCenterX, mCenterY;
//
//    public PrimesCanvas() {
//        super();
//        setupListeners();
//    }
//
//    public PrimesCanvas(double width, double height) {
//        super(width, height);
//        setupListeners();
//    }
//
//    private void setupListeners() {
//        this.widthProperty().addListener((observable, oldValue, newValue) -> clearCanvas());
//        this.heightProperty().addListener((observable, oldValue, newValue) -> clearCanvas());
//
//        setOnMousePressed(event -> {
//            mLastPressX = event.getX();
//            mLastPressY = event.getY();
//        });
//
//        setOnMouseReleased(event -> {
//            mLastPressX = -1;
//            mLastPressY = -1;
//        });
//
//        setOnMouseDragged(event -> {
//            if (mLastPressX == -1)
//                return;
//
//            mCenterX -= mLastPressX - event.getX();
//            mCenterY -= mLastPressY - event.getY();
//
//            mLastPressX = event.getX();
//            mLastPressY = event.getY();
//
//            onDraw();
//        });
//
//        setOnScroll(event -> {
//            double scroll = event.getDeltaY();
//            double dX = event.getX() - mCenterX;
//            double dY = event.getY() - mCenterY;
//
//            try {
//                if (scroll < 0) {
//                    mModel.setRadius(mModel.getRadius() * 0.9);
//
//                    mCenterX += dX * 0.1;
//                    mCenterY += dY * 0.1;
//                }
//                if (scroll > 0) {
//                    mModel.setRadius(mModel.getRadius() * 1.1);
//
//                    mCenterX -= dX * 0.1;
//                    mCenterY -= dY * 0.1;
//                }
//                onDraw();
//            }
//            catch (IllegalArgumentException e) {
//                //do nothing
//            }
//
//        });
//    }
//
//    public void setModel(Model model) {
//        mModel = model;
//        clearCanvas();
//    }
//
//    private void clearCanvas() {
//        GraphicsContext context = this.getGraphicsContext2D();
//        context.setFill(Color.GRAY);
//        context.fillRect(0, 0, this.getWidth(), this.getHeight());
//    }
//
//    private void fillCircle(GraphicsContext context, double x, double y, double radius) {
//        context.fillOval(x - radius, y - radius, radius * 2, radius * 2);
//    }
//
//    public void updateAndRedraw() {
//        mCenterX = getWidth() / 2;
//        mCenterY = getHeight() / 2;
//
//        mModel.setAutoRadius(Math.min(this.getWidth(), this.getHeight()) / 2 - 5);
//
//        onDraw();
//    }
//
//    private void onDraw() {
//        clearCanvas();
//        this.getGraphicsContext2D().setLineWidth(0);
//
//        double radius, outerRadius;
//        radius = mModel.getRadius();
//        outerRadius = mModel.getOuterRadius();
//
//        if (mModel.isOnlyPrime()) {
//            drawOnlyPrimes(radius, outerRadius);
//        } else
//            drawAll(radius);
//    }
//
//    private void drawAll(double radius) {
//        GraphicsContext context = this.getGraphicsContext2D();
//
//        //updateAndRedraw center circle
//        context.setFill(Color.WHITE);
//        fillCircle(context, mCenterX, mCenterY, radius);
//
//        boolean[] isPrime = mModel.getPrimesInBoolean();
//        int curNumber = 2;
//
//        loop:
//        for (long circleCount = 1; ; circleCount++) {
//            long count = circleCount * 6;
//            double dist = radius * 2 * circleCount;
//            for (long i = 0; i < count; i++) {
//                if (curNumber >= isPrime.length)
//                    break loop;
//
//                double angle = Math.PI / 3 / circleCount * i;
//                double x = mCenterX + Math.cos(angle) * dist;
//                double y = mCenterY - Math.sin(angle) * dist;
//
//                if (isPrime[curNumber])
//                    context.setFill(Color.BLACK);
//                else
//                    context.setFill(Color.WHITE);
//
//                fillCircle(context, x, y, radius);
//                curNumber++;
//            }
//        }
//    }
//
//    private void drawOnlyPrimes(double radius, double outerRadius) {
//        GraphicsContext context = this.getGraphicsContext2D();
//
//        //updateAndRedraw background
//        context.setFill(Color.WHITE);
//        fillCircle(context, mCenterX, mCenterY, outerRadius);
//
//        //updateAndRedraw circles
//        context.setFill(Color.BLACK);
//        int[] primes = mModel.getPrimes();
//        int start = 2;
//        int end = 7;
//        int count = 6;
//        double dist = radius * 2;
//
//        for (int prime : primes) {
//            while (prime > end) {
//                count += 6;
//                start = end + 1;
//                end = start + count - 1;
//                dist += radius * 2;
//            }
//
//            int pos = prime - start;
//            double angle = 2 * Math.PI / count * pos;
//            double x = mCenterX + Math.cos(angle) * dist;
//            double y = mCenterY - Math.sin(angle) * dist;
//
//            fillCircle(context, x, y, radius);
//        }
//    }
//
//}
