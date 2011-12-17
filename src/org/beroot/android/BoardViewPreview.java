package org.beroot.android;

import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import org.beroot.android.goengine.Go;
import org.beroot.android.util.Perf;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;

public class BoardViewPreview extends BoardViewCommon
{
  private Go _go;
  private List<InternalStone> _stones;

  public BoardViewPreview(Context context)
  {
    super(context);
    init(context);
  }

  public BoardViewPreview(Context context, AttributeSet attrs, int defStyle)
  {
    super(context, attrs, defStyle);
    init(context);
  }

  public BoardViewPreview(Context context, AttributeSet attrs)
  {
    super(context, attrs);
    init(context);
  }

  public void init(Context context)
  {
    super.init(context);
    _hoshiSize = 1.5f;
  }

  private class InternalStone
  {
    public byte color;
    public int pos;
  }

  public void setStones(String stones)
  {
    _stones = new LinkedList<InternalStone>();
    String split[];
    StringTokenizer st = new StringTokenizer(stones, "\\|");
    InternalStone iStone;
    while (st.hasMoreTokens())
    {
      iStone = new InternalStone();
      split = st.nextToken().split(":");
      iStone.color = Byte.parseByte(split[0]);
      int pos = Integer.parseInt(split[1]);
      iStone.pos = pos;
      _stones.add(iStone);
    }
    setDrawingCacheEnabled(false);
  }

  public void setBoardSize(int boardSize)
  {
    _boardSize = boardSize;
    _go = new Go(boardSize);
    calculateMeasure();
  }

  private void calculateMeasure()
  {
    _globalCoef = _gobanWidth / _boardSize;
    _activeCellWidth = _globalCoef;
    _lineWidth = _globalCoef * (_boardSize - 1);
    _backgroundWidth = _globalCoef * _boardSize;
    _globalPadding = _globalCoef / 2;
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
  {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    Log.d("beroot", "mesure: " + getWidth() + " / " + getHeight());

    setMeasuredDimension(100, 100);
    _gobanWidth = 100;
    calculateMeasure();
  }

  @Override
  protected void drawStones(Canvas canvas)
  {
    Perf perf = new Perf();
    float stoneSize = 2.2f * (19 / _boardSize);
    float px, py;
    for (InternalStone iStone : _stones)
    {
      px = (_go.I(iStone.pos) * _globalCoef) + _globalPadding;
      py = (_go.J(iStone.pos) * _globalCoef) + _globalPadding;
      canvas.drawCircle(px, py, stoneSize, iStone.color == Go.BLACK ? _blackStonePaint : _whiteStonePaint);
    }
    Log.d("beroot", "Preview.drawStones: " + perf.getTime() + " s" + " - " + _stones.size());
  }

  @Override
  protected void drawStone(Canvas canvas, float reduc, int px, int py, byte color)
  {
  }

  @Override
  public void onDraw(Canvas canvas)
  {
    super.onDraw(canvas);
    setDrawingCacheEnabled(true);
  }
}
