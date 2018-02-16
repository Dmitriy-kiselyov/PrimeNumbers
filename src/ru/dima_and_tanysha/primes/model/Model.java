package ru.dima_and_tanysha.primes.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Model {

    public final int MIN_FILTER = 100;
    public final int MAX_FILTER = 255;
    public final int MIN_SIZE = 100;
    public final int MAX_SIZE = 8000;

    private PrimeFile mPrimeFile;
    private long mShowToNumber;

    private int mImageWidth = 1000;
    private int mImageHeight = 1000;
    private IntegerProperty mFilter = new SimpleIntegerProperty(MAX_FILTER);
    private StringProperty mSaveImagePath = new SimpleStringProperty("");

    public PrimeFile getPrimeFile() {
        return mPrimeFile;
    }

    public void setPrimeFile(PrimeFile file) {
        if (mPrimeFile != file) {
            mPrimeFile = file;
        }
    }

    public long getShowToNumber() {
        return mShowToNumber;
    }

    public void setShowToNumber(long showToNumber) {
        mShowToNumber = showToNumber;
    }

    public long getMaxCount() {
        if (mPrimeFile == null)
            return 0;
        return mPrimeFile.getMaxCount();
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

    public int getImageWidth() {
        return mImageWidth;
    }

    public void setImageWidth(int imageWidth) throws IllegalArgumentException {
        if (!isValidSize(imageWidth))
            throw new IllegalArgumentException("Size is not valid");
        mImageWidth = imageWidth;
    }

    public int getImageHeight() {
        return mImageHeight;
    }

    public void setImageHeight(int imageHeight) throws IllegalArgumentException {
        if (!isValidSize(imageHeight))
            throw new IllegalArgumentException("Size is not valid");
        mImageHeight = imageHeight;
    }

    private boolean isValidSize(int size) {
        return MIN_SIZE <= size && size <= MAX_SIZE;
    }

    public String getSaveImagePath() {
        return mSaveImagePath.get();
    }

    public void setSaveImagePath(String saveImagePath) {
        this.mSaveImagePath.set(saveImagePath);
    }

    public StringProperty saveImagePathProperty() {
        return mSaveImagePath;
    }
}
