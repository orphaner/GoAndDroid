package org.beroot.android.activities;

import org.beroot.android.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class NewGame extends Activity
{
  public static final String BOARD_SIZE = "BOARD_SIZE";
  public static final String BLACK_PLAYER_NAME = "BLACK_PLAYER_NAME";
  public static final String WHITE_PLAYER_NAME = "WHITE_PLAYER_NAME";

  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.newgame);

    OnClickListener startGameListener = new OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        Intent boardIntent = new Intent(getBaseContext(), Board.class);
        
        // Taille du goban
        Spinner boardSizeSpinner = (Spinner) findViewById(R.id.boardSize);
        String selectedSize = (String) boardSizeSpinner.getSelectedItem();
        boardIntent.putExtra(NewGame.BOARD_SIZE, selectedSize);
        
        // Nom des joueurs
        TextView blackTextView = (TextView) findViewById(R.id.blackNameText);
        TextView whiteTextView = (TextView) findViewById(R.id.whiteNameText);
        boardIntent.putExtra(NewGame.BLACK_PLAYER_NAME, blackTextView.getText().toString());
        boardIntent.putExtra(NewGame.WHITE_PLAYER_NAME, whiteTextView.getText().toString());
        
        // Démarre l'activité de jeu
        startActivity(boardIntent);
      }
    };

    Button newGameButton = (Button) findViewById(R.id.startGameButton);
    newGameButton.setOnClickListener(startGameListener);
    
    declareLayoutListener(R.id.blackPlayerLayout, R.id.blackNameText);
    declareLayoutListener(R.id.whitePlayerLayout, R.id.whiteNameText);
  }

  private void declareLayoutListener(int layoutId, final int textId)
  {
    LinearLayout layout = (LinearLayout) findViewById(layoutId);

    OnClickListener blackNameListener = new OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        TextView textView = (TextView) findViewById(textId);
        getAlert(textView).show();
      }
    };
    layout.setOnClickListener(blackNameListener);
  }

  private AlertDialog.Builder getAlert(final TextView textView)
  {
    AlertDialog.Builder alert = new AlertDialog.Builder(this);

    alert.setTitle("Player Name");

    // Set an EditText view to get user input 
    final EditText input = new EditText(this);
    input.setText(textView.getText());
    alert.setView(input);

    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface dialog, int whichButton)
      {
        textView.setText(input.getText());
      }
    });

    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface dialog, int whichButton)
      {
        // Canceled.
      }
    });
    return alert;
  }
}
