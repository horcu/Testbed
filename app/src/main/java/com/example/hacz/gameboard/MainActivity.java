package com.example.hacz.gameboard;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.hacz.gameboard.widget.LeBubbleTextView;
import com.github.ivbaranov.mli.MaterialLetterIcon;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    AutoFitGridLayout grid;
    LeBubbleTextView infoView;
    int playerTileLocationIndex = 71;
    String playerTurn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        grid = (AutoFitGridLayout) findViewById(R.id.grid);
        infoView = (LeBubbleTextView) findViewById(R.id.infoView);
        infoView.setVisibility(View.GONE);
        TileHelper tHelper = new TileHelper();

        final View cardView = getLayoutInflater().inflate(R.layout.tile, grid, false);

        for(int i = 0; i < 60; i ++ ) {

            final GameTile card = (GameTile) getLayoutInflater().inflate(R.layout.peezcard, grid, false); // new CardView(this);
            card.setTag(R.string.view_index, i);
            card.setCardElevation(1);
            card.setBackground(new ColorDrawable(Color.parseColor("#ffffff")));
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = 150;
            card.setForegroundGravity(Gravity.CENTER);
            card.setLayoutParams(params);
            ArrayList<Integer> nIndicdes = tHelper.AddNeighboursIndices(i,grid.getChildCount()-1);
            card.setNeighboursIndices(nIndicdes);

            card.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                public void onGlobalLayout() {
                    card.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                    int[] locations = new int[2];
                    card.getLocationOnScreen(locations);
                    card.setTag(R.string.view_top,locations[0]);
                    card.setTag(R.string.view_left,locations[1]);
                }
            });

            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   Integer moveToIndex =  (Integer) card.getTag(R.string.view_index);
                   Integer moveFromIndex =  grid.getIndexOfMyTile();

                    if(moveFromIndex == moveToIndex)
                        return;

                    if(areNeighbours(moveFromIndex, moveToIndex)){
                          View newHome =  moveTile(moveFromIndex,moveToIndex);
                        if(newHome == null)
                            Toast.makeText(getApplicationContext(), "cant move right now", Toast.LENGTH_SHORT).show();
                        else
                          AlignInfoView(newHome);
                          setInfoTextString(newHome);
                        }
                }
            });

            if(i == grid.getIndexOfMyTile()) {

                final int finalI = i;

                card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) { //todo use the index to best tell where the arrow should be and where to position the bubble text box
                        AlignInfoView(card);
                        Toast.makeText(getApplicationContext(),"I clicked at: " + finalI, Toast.LENGTH_SHORT).show();
                    }
                });

                cardView.findViewById(R.id.letter_icon).setOnClickListener(PlayerTileOnClickHandler(card));
                card.addView(cardView);
            }
            if (grid != null) {
                grid.addView(card);
            }
        }
    }

    @NonNull
    private View.OnClickListener PlayerTileOnClickHandler(final CardView card) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlignInfoView(card);

                infoView.setVisibility(View.VISIBLE);
                setInfoTextString(card);

                infoView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        infoView.setVisibility(View.GONE);
                    }
                });
            }
        };
    }

    private void setInfoTextString(View card) {
        RelativeLayout rel = (RelativeLayout) infoView.getChildAt(1);
        TextView tv = (TextView) rel.getChildAt(0);
        if(card !=null)
        tv.setText("spot " + card.getTag(R.string.view_index ));
    }

    private void AlignInfoView(View v) {
        //get arrowdirectrion

        //get where to anchor the infoview top bottom left or right.. below solution is for right side alignment
        int transX = ((int)v.getTag(R.string.view_top) + 210);
        int transY = ((int)v.getTag(R.string.view_left)) - 260 ;

        infoView.setTranslationY(transY);
        infoView.setTranslationX(transX);
    }

    private View moveTile(Integer moveFrom, Integer moveTo) {

        GameTile moveFromCard = null;
        GameTile moveToCard = null;
        try {
            //remove the card from its old spot
            moveFromCard = (GameTile) grid.getChildAt(moveFrom);
            moveToCard = (GameTile) grid.getChildAt(moveTo);

            RelativeLayout tileContainer = (RelativeLayout) moveFromCard.getChildAt(0);
            MaterialLetterIcon icon = (MaterialLetterIcon) tileContainer.getChildAt(0);

            if(icon == null)
                return null ;

            icon.setDrawingCacheEnabled(true);
            MaterialLetterIcon cloneIcon = GetTileIcon(icon.getLetter(), icon.getShapeColor(), icon.getDrawingCacheBackgroundColor()); // new MaterialLetterIcon(this);
            icon.setDrawingCacheEnabled(false);

            RelativeLayout cloneContainer = new RelativeLayout(getApplicationContext());
            cloneContainer.setLayoutParams(tileContainer.getLayoutParams());

            cloneIcon.setLetter("P");
            cloneIcon.setLayoutParams(icon.getLayoutParams());
            cloneIcon.setLetterColor(Color.LTGRAY);
            cloneIcon.setOnClickListener(PlayerTileOnClickHandler(moveToCard));

            cloneContainer.addView(cloneIcon);
            tileContainer.removeView(icon);

            YoYo.with(Techniques.FadeOut).duration(700).playOn(infoView);

            MaterialLetterIcon visitedIcon = new MaterialLetterIcon(getApplicationContext());

            visitedIcon.setLayoutParams(icon.getLayoutParams());
            visitedIcon.setShapeColor(Color.LTGRAY);
            visitedIcon.setLetterColor(Color.WHITE);

            tileContainer.addView(visitedIcon);

            YoYo.with(Techniques.FadeIn).duration(700).playOn(infoView);

            //shade next available spots
            ShadeNextAvailbleSpots(moveToCard);

            //move the card to its new spot
            moveToCard.addView(cloneContainer);
            YoYo.with(Techniques.Pulse).duration(700).playOn(cloneContainer);
            grid.setIndexOfMyTile(moveTo);

            return moveToCard;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return moveFromCard;
    }

    private void ShadeNextAvailbleSpots(GameTile moveToCard) {

        ArrayList<Integer> neighboursIndices = moveToCard.getNeighboursIndices();

        for(int i = 0; i < neighboursIndices.size(); i ++) {
            Integer index = neighboursIndices.get(i);
            GameTile gt = (GameTile) grid.getChildAt(index);
            gt.setBackground(new ColorDrawable(Color.parseColor("#e4e4e4")));
        }
    }

    private boolean areNeighbours(int moveFromIndex, int moveToIndex) {

        GameTile tileFrom = (GameTile) grid.getChildAt(moveFromIndex);
        return  tileFrom.getNeighboursIndices().contains(moveToIndex);
    }

    private MaterialLetterIcon GetTileIcon(String label, final int shapeColor, final int backgroundColor) {
        MaterialLetterIcon icon = new MaterialLetterIcon(this);
        icon.setLetterSize(17);
        icon.setLetterTypeface(Typeface.SANS_SERIF);
        icon.setShapeType(MaterialLetterIcon.SHAPE_CIRCLE);
        icon.setBackgroundColor(backgroundColor);
        icon.setShapeColor(shapeColor);
        icon.setLetter(label);
        icon.setLetterColor(Color.LTGRAY);

        return icon;
    }
}