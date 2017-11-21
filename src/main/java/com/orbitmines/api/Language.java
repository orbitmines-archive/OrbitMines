package com.orbitmines.api;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public enum Language {

    DUTCH,
    ENGLISH;

    public Language next() {
        Language[] values = Language.values();

        return values.length == ordinal() +1 ? values[0] : values[ordinal() + 1];
    }
}
