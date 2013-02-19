package org.beroot.android.activities;

import org.beroot.android.BoardViewPreview;
import org.beroot.android.R;
import org.beroot.android.db.DaoGame;
import org.beroot.android.db.DbGame;
import org.beroot.android.util.Perf;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyGamesAdapter extends BaseAdapter
{
  private DaoGame _daoGame;
  private LayoutInflater _inflater;

  public MyGamesAdapter(Context ctx, DaoGame daoGame)
  {
    _inflater = LayoutInflater.from(ctx);
    _daoGame = daoGame;
  }

  @Override
  public int getCount()
  {
    return _daoGame.getCount();
  }

  @Override
  public Object getItem(int i)
  {
    return _daoGame.getGameFromId(i + 1);
  }

  @Override
  public long getItemId(int arg0)
  {
    return arg0;
  }

  @Override
  public View getView(int position, View view, ViewGroup parentView)
  {
    ViewHolder vh = new ViewHolder();
    if (view == null)
    {
      view = _inflater.inflate(R.layout.itemsgf, null);
      vh._whitePlayer = (TextView) view.findViewById(R.id.fileWhitePlayerText);
      vh._blackPlayer = (TextView) view.findViewById(R.id.fileBlackPlayerText);
      vh._result = (TextView) view.findViewById(R.id.fileResultText);
      vh._boardView = (BoardViewPreview) view.findViewById(R.id.itemBoardView);
      view.setTag(vh);
    }
    else
    {
      vh = (ViewHolder) view.getTag();
    }

    Perf perf = new Perf();
    DbGame game = (DbGame) getItem(position);
    vh._boardView.setBoardSize(game.getBoardSize());
    vh._boardView.setStones(game.getStones());
    vh._result.setText(game.getResult());
    vh._whitePlayer.setText(game.getWhitePlayerName());
    vh._blackPlayer.setText(game.getBlackPlayerName());
    Log.d("beroot", "Load DbGame: " + perf.getTime() + " s");
    return view;
  }

  private class ViewHolder
  {
    TextView _whitePlayer;
    TextView _blackPlayer;
    TextView _result;
    BoardViewPreview _boardView;
  }
}
