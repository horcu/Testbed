package com.example.hacz.gameboard;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;

import java.util.ArrayList;

/**
 * Created by hcummings on 3/21/2016.
 */
public class GameTile extends CardView {
    private ArrayList<Integer> neighboursIndices;

    public GameTile(Context context) {
        super(context);
    }

    public GameTile(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GameTile(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ArrayList<Integer> getNeighboursIndices() {
        return neighboursIndices;
    }

    public void setNeighboursIndices(ArrayList<Integer> neighboursIndices) {
        this.neighboursIndices = neighboursIndices;
    }
}
