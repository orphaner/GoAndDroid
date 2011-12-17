package org.beroot.android.activities;

import org.beroot.android.R;
import org.beroot.android.goengine.GoGame;
import org.beroot.android.util.StringUtils;

import android.os.Bundle;

/**
 * @author nicolas
 * 
 */
public class Board extends CommonGame
{
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.board);

    findViews();
    addButtonListener();

    String blackPlayerName = getIntent().getStringExtra(NewGame.BLACK_PLAYER_NAME);
    String whitePlayerName = getIntent().getStringExtra(NewGame.WHITE_PLAYER_NAME);
    _gg = new GoGame(getBoardSizeFromIntent(), 0, whitePlayerName, blackPlayerName);

    _boardView.setGoGame(_gg);

    setPlayerName(blackPlayerName, R.id.boardBlackPlayerText);
    setPlayerName(whitePlayerName, R.id.boardWhitePlayerText);
  }
  
  /**
   * Injecte la taille du goban dans la vue de jeu
   */
  private int getBoardSizeFromIntent()
  {
    String size = getIntent().getStringExtra(NewGame.BOARD_SIZE);
    if (StringUtils.isEmpty(size) || size.equals("13x13"))
    {
      return 13;
    }
    else if (size.equals("9x9"))
    {
      return 9;
    }
    else if (size.equals("19x19"))
    {
      return 19;
    }
    return 19;
  }
}
