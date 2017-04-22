package ru.dima_and_tanysha.primes.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Created by Pussy_penetrator on 12.03.2017.
 */
public class Model {

    public static final double RADIUS_MINIMUM = 0.00001;
    public static final double RADIUS_MAXIMUM = 10000;

    private double          mRadius    = 2;
    private BooleanProperty mOnlyPrime = new SimpleBooleanProperty(true);

    private PrimeFile mPrimeFile;
    private int[]     mPrimes;
    private boolean[] mPrimesInBoolean;

    public void setPrimeFile(PrimeFile file) {
        if (mPrimeFile != file) {
            mPrimeFile = file;
            mPrimes = null;
            mPrimesInBoolean = null;
        }
    }

    public int[] getPrimes() {
        if (mPrimeFile == null)
            return null;

        if (mPrimes == null) {
            try (ObjectInputStream in = new ObjectInputStream(
                    getClass().getClassLoader().getResourceAsStream(mPrimeFile.getPath()))) {
                mPrimes = (int[]) in.readObject();
            }
            catch (IOException | ClassNotFoundException e) {
                return null;
            }
        }
        return mPrimes;
    }

    public int getTotalCount() {
        if (mPrimeFile == null)
            return 0;
        return mPrimeFile.getTotalCount();
    }

    public boolean[] getPrimesInBoolean() {
        if (mPrimesInBoolean == null) {
            int[] primes = getPrimes();
            if (primes == null)
                return null;

            mPrimesInBoolean = new boolean[getTotalCount() + 1];
            for (int p : primes)
                mPrimesInBoolean[p] = true;
        }

        return mPrimesInBoolean;
    }

    public double getRadius() {
        return mRadius;
    }

    public void setRadius(double radius) throws IllegalArgumentException {
        if (radius < RADIUS_MINIMUM || radius > RADIUS_MAXIMUM)
            throw new IllegalArgumentException();
        mRadius = radius;
    }

    public double getOuterRadius() {
        if (getTotalCount() == 0)
            return 0;
        if (getTotalCount() == 1)
            return 1;

        long totalCount = getTotalCount() - 1;
        long layer = 0;
        long count = 0;
        for (long i = 1; count < totalCount; i += 6) {
            count += i;
            layer++;
        }

        return layer * mRadius * 2 - mRadius;
    }

    public double setAutoRadius(double outerRadius) {
        if (getTotalCount() == 0)
            return 0;
        if (getTotalCount() == 1)
            return outerRadius;

        long totalCount = getTotalCount() - 1;
        long layer = 0;
        long count = 0;
        for (long i = 1; count < totalCount; i += 6) {
            count += i;
            layer++;
        }

        count = layer * 2 - 1;
        mRadius = outerRadius / count;
        return mRadius;
    }

    public boolean isOnlyPrime() {
        return mOnlyPrime.get();
    }

    public void setOnlyPrime(boolean onlyPrime) {
        this.mOnlyPrime.set(onlyPrime);
    }

    public BooleanProperty onlyPrimeProperty() {
        return mOnlyPrime;
    }
}
