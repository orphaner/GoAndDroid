package org.beroot.android.activities;

import org.beroot.android.BoardView;
import org.beroot.android.R;
import org.beroot.android.goengine.Go;
import org.beroot.android.util.StringUtils;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * @author nicolas
 * 
 */
public class Board extends Activity
{
  /**
   * Référence vers la vue de jeu
   */
  private BoardView _boardView = null;

  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.board);

    _boardView = (BoardView) findViewById(R.id.boardView);

    setBoardSize();
    setName(NewGame.BLACK_PLAYER_NAME, R.id.boardBlackPlayerText);
    setName(NewGame.WHITE_PLAYER_NAME, R.id.boardWhitePlayerText);
  }

  /**
   * Injecte la taille du goban dans la vue de jeu
   */
  private void setBoardSize()
  {
    String size = getIntent().getStringExtra(NewGame.BOARD_SIZE);
    if (StringUtils.isEmpty(size) || size.equals("13x13"))
    {
      _boardView.setGo(new Go(13));
    }
    else if (size.equals("9x9"))
    {
      _boardView.setGo(new Go(9));
    }
    else if (size.equals("19x19"))
    {
      _boardView.setGo(new Go(19));
    }
  }

  /**
   * @param key
   * @param textViewId
   */
  private void setName(String key, int textViewId)
  {
    String name = getIntent().getStringExtra(key);
    TextView textView = (TextView) findViewById(textViewId);
    if (textView != null)
    {
      textView.setText(name);
    }
  }
}
