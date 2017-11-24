package net.firefang.ip2c.input;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class MemoryMappedRandoeAccessFile implements RandomAccessInput {

    private ByteBuffer m_roBuf;

    public MemoryMappedRandoeAccessFile(String string, String string2) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile(string, string2);
        FileChannel fileChannel = randomAccessFile.getChannel();
        this.m_roBuf = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, (int) fileChannel.size());
    }

    public int readInt() throws IOException {
        return this.m_roBuf.getInt();
    }

    public short readShort() throws IOException {
        return this.m_roBuf.getShort();
    }

    public void readFully(byte[] arrby) throws IOException {
        this.m_roBuf.get(arrby);
    }

    public void seek(long l) throws IOException {
        this.m_roBuf.position((int) l);
    }
}
