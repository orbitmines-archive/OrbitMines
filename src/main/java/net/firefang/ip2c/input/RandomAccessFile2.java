package net.firefang.ip2c.input;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

public class RandomAccessFile2 extends RandomAccessFile implements RandomAccessInput {

    public RandomAccessFile2(String string, String string2) throws FileNotFoundException {
        super(string, string2);
    }
}
