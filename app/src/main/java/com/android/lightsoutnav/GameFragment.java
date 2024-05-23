package com.android.lightsoutnav;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;

public class GameFragment extends Fragment {
    private final String GAME_STATE = "gameState";
    private LightsOutGame mGame;
    private GridLayout mLightGrid;
    private int mLightOnColor;
    private int mLightOffColor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View parentView = inflater.inflate(R.layout.fragment_game, container, false);

        // Add the same click handler to all grid buttons
        mLightGrid = parentView.findViewById(R.id.light_grid);
        for (int i = 0; i < mLightGrid.getChildCount(); i++) {
            Button gridButton = (Button) mLightGrid.getChildAt(i);
            gridButton.setOnClickListener(this::onLightButtonClick);
        }

        Button newGameBtn = parentView.findViewById(R.id.new_game_button);
        newGameBtn.setOnClickListener(v -> startGame());

        // Load preferred "on" button color
        SharedPreferences sharedPref = this.requireActivity().getPreferences(Context.MODE_PRIVATE);
        int onColorId = sharedPref.getInt("color", R.color.yellow);

        mLightOnColor = ContextCompat.getColor(this.requireActivity(), onColorId);
        mLightOffColor = ContextCompat.getColor(this.requireActivity(), R.color.black);

        mGame = new LightsOutGame();

        if (savedInstanceState == null) {
            startGame();
        } else {
            String gameState = savedInstanceState.getString(GAME_STATE);
            mGame.setState(gameState);;
            setButtonColors();
        }

        return parentView;
    }

    @Override
    public void onSaveInstanceState (@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(GAME_STATE, mGame.getState());
    }

    private void startGame() {
        mGame.newGame();
        setButtonColors();
    }

    private void onLightButtonClick (View view) {

        // Find the button's row and col
        int buttonIndex = mLightGrid.indexOfChild(view);
        int row = buttonIndex / LightsOutGame.GRID_SIZE;
        int col = buttonIndex % LightsOutGame.GRID_SIZE;

        mGame.selectLight(row, col);
        setButtonColors();

        // Congratulate the user if the game is over
        if (mGame.isGameOver()) {
            Toast.makeText(this.requireActivity(), R.string.congrats, Toast.LENGTH_SHORT).show();
        }
    }

    public void setButtonColors() {

        for (int buttonIndex = 0; buttonIndex < mLightGrid.getChildCount(); buttonIndex++) {
            Button gridButton = (Button) mLightGrid.getChildAt(buttonIndex);

            // Find the button's row and col
            int row = buttonIndex / LightsOutGame.GRID_SIZE;
            int col = buttonIndex % LightsOutGame.GRID_SIZE;

            if (mGame.isLightOn(row, col)) {
                gridButton.setBackgroundColor(mLightOnColor);
            } else {
                gridButton.setBackgroundColor(mLightOffColor);
            }
        }
    }
}


















