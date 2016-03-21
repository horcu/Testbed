package com.example.hacz.gameboard;

import java.util.ArrayList;

/**
 * Created by hcummings on 3/21/2016.
 */
public class TileHelper {



    public  ArrayList<Integer> AddNeighboursIndices(int index, int lasIndex){
        ArrayList<Integer> neighbourIndices = new ArrayList<Integer>();

        //set index of spot before current tile's slot
        int beforeIndex = index -1 > 0 ? index - 1 : 0;

        //set index of spot after current tile's spot
        int afterIndex = index + 1 > lasIndex ? lasIndex : index + 1;

        //set the other neighbours that are either in the rows ahead
        //first position of any other in the same column [divisible by 6]
        int otherIndices1 = 1000;
        int otherIndices2 = 1000;
        int otherIndices3 = 1000;

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
        else if (index == (lasIndex)) {
           otherIndices1 = index - 10;
           otherIndices2 = index - 11;
       } // in the middlke somewhere
        else{
           otherIndices1 = index + 5;
            otherIndices2 = index + 6;
            otherIndices3 = index + 7;
       }
           //add all the neighbours indices to an arraylist
        if(beforeIndex != index)
           neighbourIndices.add(beforeIndex);
        if(afterIndex != index)
           neighbourIndices.add(afterIndex);

        if(otherIndices1 != 1000)
           neighbourIndices.add(otherIndices1);
        if(otherIndices2 != 1000)
           neighbourIndices.add(otherIndices2);
        if(otherIndices3 != 1000)
           neighbourIndices.add(otherIndices3);

       return neighbourIndices;
    }
}
