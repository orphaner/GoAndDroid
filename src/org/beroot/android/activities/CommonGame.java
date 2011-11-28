package org.beroot.android.activities;

import org.beroot.android.BoardView;
import org.beroot.android.R;
import org.beroot.android.goengine.GoGame;
import org.beroot.android.util.StringUtils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public abstract class CommonGame extends Activity
{
  /**
   * Référence vers la vue de jeu
   */
  protected BoardView _boardView = null;

  /**
   * 
   */
  protected TextView _boardCommentary;

  /**
   * 
   */
  protected GoGame _gg;

  /**
   * 
   */
  protected Button _nextButton;

  /**
   * 
   */
  protected Button _endButton;

  /**
   * 
   */
  protected Button _prevButton;

  /**
   * 
   */
  protected Button _beginButton;
  
  protected Context _context;

  /**
   * 
   */
  protected void findViews()
  {
    _boardView = (BoardView) findViewById(R.id.boardView);
    _boardCommentary = (TextView) findViewById(R.id.boardCommentary);
    _nextButton = (Button) findViewById(R.id.boardNextButton);
    _endButton = (Button) findViewById(R.id.boardEndButton);
    _prevButton = (Button) findViewById(R.id.boardPrevButton);
    _beginButton = (Button) findViewById(R.id.boardBeginButton);
  }

  /**
   * 
   */
  protected void addButtonListener()
  {
    _nextButton.setOnClickListener(new NextButtonListener());
    _endButton.setOnClickListener(new EndButtonListener());
    _prevButton.setOnClickListener(new PrevButtonListener());
    _beginButton.setOnClickListener(new BeginButtonListener());
  }

  /**
   * 
   */
  protected void updateComment()
  {
    String comment = _gg.getComment();
    if (StringUtils.isEmpty(comment))
    {
      _boardCommentary.setText("");
      _boardCommentary.invalidate(); // TODO utile ?
    }
    else
    {
      _boardCommentary.setText(comment);
      _boardCommentary.invalidate(); // TODO utile ?
    }
  }

  /**
   * @param playerName
   * @param textViewId
   */
  protected void setPlayerName(String playerName, int textViewId)
  {
    TextView textView = (TextView) findViewById(textViewId);
    if (textView != null)
    {
      textView.setText(playerName);
    }
  }

  
  private class NextButtonListener implements OnClickListener
  {
    @Override
    public void onClick(View v)
    {
      if (_gg.hasNextNode())
      {
        _gg.nextNode();
      }
      updateComment();
      _boardView.setGo(_gg.getGoEngine());
      _boardView.invalidate();
    } 
  }
  
  private class EndButtonListener implements OnClickListener
  {
    @Override
    public void onClick(View v)
    {
      while (_gg.hasNextNode())
      {
        _gg.nextNode();
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(ACTIVITY_SERVICE);
        MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);

        Log.i("beroot", " memoryInfo.availMem " + memoryInfo.availMem + "\n" );
        Log.i("beroot", " memoryInfo.lowMemory " + memoryInfo.lowMemory + "\n" );
        Log.i("beroot", " memoryInfo.threshold " + memoryInfo.threshold + "\n" );

      }
      updateComment();
      _boardView.setGo(_gg.getGoEngine());
      _boardView.invalidate();
    }
  }
  
  private class PrevButtonListener implements OnClickListener
  {
    @Override
    public void onClick(View v)
    {
      if (_gg.canUndo())
      {
        _gg.undo();
      }
      updateComment();
      _boardView.setGo(_gg.getGoEngine());
      _boardView.invalidate();
    }
  }
  
  private class BeginButtonListener implements OnClickListener
  {
    @Override
    public void onClick(View v)
    {
    }
  }
}
