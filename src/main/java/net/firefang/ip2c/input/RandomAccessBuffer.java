package net.firefang.ip2c.input;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class RandomAccessBuffer implements RandomAccessInput {

    private byte[] m_buffer;
    private int m_offset;

    public RandomAccessBuffer(String string) throws IOException {
        File file = new File(string);
        byte[] arrby = new byte[(int) file.length()];
        FileInputStream fileInputStream = new FileInputStream(file);
        DataInputStream dataInputStream = new DataInputStream(fileInputStream);
        dataInputStream.readFully(arrby);
        fileInputStream.close();
        this.m_buffer = arrby;
        this.m_offset = 0;
    }

    public RandomAccessBuffer(byte[] arrby) {
        this.m_buffer = arrby;
        this.m_offset = 0;
    }

    public int readInt() throws IOException {
        byte by = this.m_buffer[this.m_offset++];
        byte by2 = this.m_buffer[this.m_offset++];
        byte by3 = this.m_buffer[this.m_offset++];
        byte by4 = this.m_buffer[this.m_offset++];
        return by << 24 & -16777216 | by2 << 16 & 16711680 | by3 << 8 & 65280 | by4 << 0 & 255;
    }

    public short readShort() throws IOException {
        byte by = this.m_buffer[this.m_offset++];
        byte by2 = this.m_buffer[this.m_offset++];
        return (short) (by << 8 & 65280 | by2 << 0 & 255);
    }

    public void readFully(byte[] arrby) throws IOException {
        System.arraycopy(this.m_buffer, this.m_offset, arrby, 0, arrby.length);
        this.m_offset += arrby.length;
    }

    public void seek(long l) throws IOException {
        this.m_offset = (int) l;
    }
}

