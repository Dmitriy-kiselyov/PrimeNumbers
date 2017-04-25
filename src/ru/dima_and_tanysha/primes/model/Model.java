package ru.dima_and_tanysha.primes.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.io.IOException;
import java.io.ObjectInputStream;

public class Model {

    public final int MIN_FILTER = 100;
    public final int MAX_FILTER = 255;

    private PrimeFile mPrimeFile;
    private int[]     mPrimes;
    private boolean[] mPrimesInBoolean;

    private IntegerProperty mFilter = new SimpleIntegerProperty(MAX_FILTER);

    public PrimeFile getPrimeFile() {
        return mPrimeFile;
    }

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

    public int getFilter() {
        return mFilter.get();
    }

    public void setFilter(int filter) {
        this.mFilter.set(filter);
    }

    public IntegerProperty filterProperty() {
        return mFilter;
    }
}
