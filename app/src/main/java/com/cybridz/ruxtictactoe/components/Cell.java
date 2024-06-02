package com.cybridz.ruxtictactoe.components;

import android.widget.TextView;

public class Cell {
    private String id;
    private TextView view;

    public Cell(String id, TextView view) {
        this.id = id;
        this.view = view;
    }

    public String getId() {
        return id;
    }

    public TextView getView() {
        return view;
    }
}
