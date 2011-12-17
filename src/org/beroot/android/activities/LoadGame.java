package org.beroot.android.activities;

import org.beroot.android.R;
import org.beroot.android.goengine.Sgf;

import android.os.Bundle;

public class LoadGame extends CommonGame
{

  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.board);
    
    findViews();
    addButtonListener();

    String file = getIntent().getStringExtra("SGF");
    Sgf sgf = new Sgf("/sdcard/GoAndDroid/" + file);
    _gg = sgf.load();

    _boardView.setGoGame(_gg);
    setPlayerName(_gg.getBlackPlayerName(), R.id.boardBlackPlayerText);
    setPlayerName(_gg.getWhitePlayerName(), R.id.boardWhitePlayerText);
    updateComment();
  }
}
