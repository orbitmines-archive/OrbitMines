package net.firefang.ip2c;

import java.util.StringTokenizer;

public class LineInfo
        implements Cloneable {
    private long startIP;
    private long endIP;
    private String id2c;
    private String id3c;
    private String name;
    private int lineNum;

    public LineInfo(long l, long l2, String string, String string2, String string3, int n) {
        this.endIP = l2;
        this.id2c = string;
        this.id3c = string2;
        this.lineNum = n;
        this.name = string3;
        this.startIP = l;
    }

    public LineInfo(String string, int n, int n2) {
        this.lineNum = n2;
        StringTokenizer stringTokenizer = new StringTokenizer(string, "\",");
        if (n == 1) {
            this.startIP = Long.parseLong(stringTokenizer.nextToken());
            this.endIP = Long.parseLong(stringTokenizer.nextToken());
            stringTokenizer.nextToken();
            stringTokenizer.nextToken();
            this.id2c = stringTokenizer.nextToken();
            this.id3c = stringTokenizer.nextToken();
            if (stringTokenizer.hasMoreElements()) {
                int n3 = string.lastIndexOf(34, string.length() - 2);
                this.name = string.substring(n3 + 1, string.length() - 1);
            }
        } else if (n == 0) {
            this.startIP = Long.parseLong(stringTokenizer.nextToken());
            this.endIP = Long.parseLong(stringTokenizer.nextToken());
            this.id2c = stringTokenizer.nextToken();
            this.id3c = "";
            this.name = "";
            if (stringTokenizer.hasMoreElements()) {
                this.id3c = stringTokenizer.nextToken();
                if (stringTokenizer.hasMoreElements()) {
                    int n4 = string.lastIndexOf(34, string.length() - 2);
                    this.name = string.substring(n4 + 1, string.length() - 1);
                }
            }
        }
    }

    public long getStartIP() {
        return this.startIP;
    }

    public long getEndIP() {
        return this.endIP;
    }

    public String getId2c() {
        return this.id2c;
    }

    public String getId3c() {
        return this.id3c;
    }

    public String getName() {
        return this.name;
    }

    public int getLineNum() {
        return this.lineNum;
    }

    public void setEndIP(long l) {
        this.endIP = l;
    }

    public void setStartIP(long l) {
        this.startIP = l;
    }

    public boolean equals(Object object) {
        if (object == null || object.getClass() != this.getClass()) {
            return false;
        }
        LineInfo lineInfo = (LineInfo) object;
        return this.startIP == lineInfo.getStartIP() && this.endIP == lineInfo.getEndIP() && this.id2c != null && this.id2c.equals(lineInfo.getId2c()) && this.id3c != null && this.id3c.equals(this.getId3c()) && this.name.equals(lineInfo.getName());
    }

    public int hashCode() {
        int n = 0;
        if (this.id2c != null) {
            n = this.id2c.hashCode();
        }
        if (this.id3c != null) {
            n = 42 * n + this.id3c.hashCode();
        }
        if (this.name != null) {
            n = 15 * n + this.name.hashCode();
        }
        n = (int) ((long) n + this.startIP);
        n = (int) ((long) n + this.endIP);
        return n;
    }

    public String toString() {
        return "Line #" + this.lineNum + ": " + this.startIP + "," + this.endIP + "," + this.id2c + "," + this.id3c + "," + this.name;
    }

    public Object clone() {
        return new LineInfo(this.startIP, this.endIP, this.id2c, this.id3c, this.name, this.lineNum);
    }
}