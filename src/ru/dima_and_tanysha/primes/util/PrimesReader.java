package ru.dima_and_tanysha.primes.util;

import ru.dima_and_tanysha.primes.model.PrimeFile;

import java.io.*;

public class PrimesReader implements Closeable {

    private ObjectInputStream mIn;
    private PrimeFile         mPrimeFile;
    private long              mPrime;

    public PrimesReader(PrimeFile primeFile) throws IOException, ClassNotFoundException {
        mIn = new ObjectInputStream(new FileInputStream(primeFile.getPath()));
        mPrimeFile = primeFile;

        mIn.readObject();
        mIn.readLong();
        mPrime = 0;
    }

    public long current() {
        return mPrime;
    }

    public long next() throws IOException {
        if (mPrime < 3)
            mPrime = mIn.readByte();
        else
            mPrime += (mIn.readByte() & 0xFF) * 2;
        return mPrime;
    }

    public long getMaxCount() {
        return mPrimeFile.getMaxCount();
    }

    @Override
    public void close() throws IOException {
        mIn.close();
    }
}
