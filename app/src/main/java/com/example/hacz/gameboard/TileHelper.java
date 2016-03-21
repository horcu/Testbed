package com.example.hacz.gameboard;

import java.util.ArrayList;

/**
 * Created by hcummings on 3/21/2016.
 */
public class TileHelper {

    private int otherIndices1;;
    private int otherIndices2;
    private int otherIndices3;

    public  ArrayList<Integer> AddNeighboursIndices(int index, int totalCount){
        ArrayList<Integer> neighbourIndices = new ArrayList<Integer>();

        //set index of spot before current tile's slot
        int beforeIndex = index -1 > 0 ? index - 1 : 0;

        //set index of spot after current tile's spot
        int afterIndex = index + 1 > totalCount ? totalCount : index + 1;

        //set the other neighbours that are either in the rows ahead
        //first position of any other in the same column [divisible by 6]

       if(index == 0 || ((index + 6) % 6)== 0 ) {
           otherIndices2 = index + 6;
           otherIndices3 = index + 7;
       }
        //5th position [end of row] and all others in same column [remainder of index minus 5 is divisible by 6]
        else if (index == 5 || ((index - 5) % 6)== 0 ) {
           otherIndices1 = index + 5;
           otherIndices2 = index + 6;
       }
        //last spot
        else if (index == (totalCount)) {
           otherIndices1 = index - 10;
           otherIndices2 = index - 11;

           //add all the neighbours indices to an arraylist
           neighbourIndices.add(beforeIndex);
           neighbourIndices.add(afterIndex);
           neighbourIndices.add(otherIndices1);
           neighbourIndices.add(otherIndices2);
           neighbourIndices.add(otherIndices3);
       }
       return neighbourIndices;
    }
}
