package org.beroot.android.activities;

import org.beroot.android.R;
import org.beroot.android.goengine.GobanSize;
import org.beroot.android.goengine.Sgf;
import org.beroot.android.util.Perf;

import android.os.Bundle;
import android.util.Log;

public class LoadGame extends CommonGame
{

  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.board);
    
    findViews();
    addButtonListener();

    Perf perf = new Perf();
    String file = getIntent().getStringExtra("SGF");
    Sgf sgf = new Sgf("/sdcard/GoAndDroid/" + file);
    _gg = sgf.load();
    Log.d("beroot", "Load SGF: " + perf.getTime() + "s");

    _boardView.setGobanSize(GobanSize.G19);
    _boardView.setGo(_gg.getGoEngine());
    setPlayerName(_gg.getBlackPlayerName(), R.id.boardBlackPlayerText);
    setPlayerName(_gg.getWhitePlayerName(), R.id.boardWhitePlayerText);
    updateComment();
  }
}
