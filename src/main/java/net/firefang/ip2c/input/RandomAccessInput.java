package net.firefang.ip2c.input;

import java.io.IOException;

public interface RandomAccessInput {
    public int readInt() throws IOException;

    public short readShort() throws IOException;

    public void readFully(byte[] var1) throws IOException;

    public void seek(long var1) throws IOException;
}

