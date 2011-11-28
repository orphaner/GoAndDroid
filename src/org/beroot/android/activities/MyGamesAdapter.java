package org.beroot.android.activities;

import java.util.List;

import org.beroot.android.BoardView;
import org.beroot.android.R;
import org.beroot.android.goengine.GoGame;
import org.beroot.android.goengine.GobanSize;
import org.beroot.android.goengine.Sgf;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyGamesAdapter extends BaseAdapter
{
  private List<String> _sgfs;
  private LayoutInflater _inflater;

  public MyGamesAdapter(Context ctx, List<String> sgfs)
  {
    _inflater = LayoutInflater.from(ctx);
    _sgfs = sgfs;
  }

  @Override
  public int getCount()
  {
    return _sgfs.size();
  }

  @Override
  public Object getItem(int i)
  {
    return _sgfs.get(i);
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
      vh._boardView = (BoardView) view.findViewById(R.id.itemBoardView);
      vh._boardView.setGobanSize(GobanSize.G19);
      
      //Sgf sgf = new Sgf("/sdcard/GoAndDroid/" + _sgfs.get(position));
      //GoGame _gg = sgf.load();
      //vh._whitePlayer.setText(_gg.getWhitePlayerName());
      //vh._blackPlayer.setText(_gg.getBlackPlayerName());
      //while (_gg.hasNextNode())
      //{
      //  _gg.nextNode();
      //}
      //vh._boardView.setGo(_gg.getGoEngine());
      
      view.setTag(vh);
    }
    else
    {
      vh = (ViewHolder) view.getTag();
    }
    //vh._sgfFileName.setText(_sgfs.get(position));
    return view;
  }

  private class ViewHolder
  {
    TextView _whitePlayer;
    TextView _blackPlayer;
    BoardView _boardView;
  }
}
