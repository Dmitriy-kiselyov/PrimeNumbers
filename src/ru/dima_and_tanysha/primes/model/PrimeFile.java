package ru.dima_and_tanysha.primes.model;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class PrimeFile {

    private final String mPath;
    private final long   mMaxCount;

    public PrimeFile(String path) throws IOException, IllegalArgumentException {
        mPath = path;

        ObjectInputStream in = new ObjectInputStream(new FileInputStream(path));

        try {
            String name = (String) in.readObject();
            if (!name.equals("primes"))
                throw new Exception();

            mMaxCount = in.readLong();
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Неверный формат файла!");
        }
    }

    public String getPath() {
        return mPath;
    }

    public long getMaxCount() {
        return mMaxCount;
    }
}
