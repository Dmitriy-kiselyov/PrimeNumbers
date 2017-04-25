package ru.dima_and_tanysha.primes.model;

public class PrimeFile {

    private final String mPath;
    private final String mFileName;
    private final int    mPrimeCount;
    private final int    mTotalCount;

    public PrimeFile(String fileName, int primeCount, int totalCount) {
        mFileName = fileName;
        mPath = fileName;
        mPrimeCount = primeCount;
        mTotalCount = totalCount;
    }

    public String getPath() {
        return mPath;
    }

    public String getFileName() {
        return mFileName;
    }

    public int getPrimeCount() {
        return mPrimeCount;
    }

    public int getTotalCount() {
        return mTotalCount;
    }

    @Override
    public String toString() {
        return mFileName;
    }
}
