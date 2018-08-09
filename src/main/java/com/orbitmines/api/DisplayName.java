package com.orbitmines.api;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class DisplayName {

    private final String initialName;
    private String name;
    private String prefix;
    private String suffix;

    public DisplayName(String name) {
        this.initialName = name;
        this.name = name;
    }

    public String getInitialName() {
        return initialName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public void clearPrefix() {
        this.prefix = null;
    }

    public void clearSuffix() {
        this.suffix = null;
    }

    public void clearName() {
        this.name = initialName;
    }

    public void clear(boolean name) {
        clearPrefix();
        clearSuffix();

        if (name)
            clearName();
    }

    public String getDisplayName(boolean onlyName) {
        return onlyName ? initialName /* name, and then change methods to OMPlayer#getRealName? */ : (prefix != null ? prefix : "") + name + (suffix != null ? suffix : "");
    }
}
