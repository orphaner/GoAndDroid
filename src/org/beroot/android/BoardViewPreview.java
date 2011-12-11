package org.beroot.android;

import org.beroot.android.goengine.Go;
import org.beroot.android.util.Perf;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;

public class BoardViewPreview extends BoardViewCommon
{
  private String stones;

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
    _go = new Go(19);
    _hoshiSize = 1.5f;
  }
  
  public void setStones(String stones)
  {
    this.stones = stones;
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
  {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    Log.d("beroot", "mesure: " + getWidth() + " / " + getHeight());

    if (getWidth() > 0)
    {
      //setMeasuredDimension(getMeasuredHeight(), getMeasuredHeight());
      setMeasuredDimension(100, 100);
      _gobanWidth = getMeasuredHeight();
      _globalCoef = _gobanWidth / _gobanSize.getSize();
      _activeCellWidth = _globalCoef;
      _lineWidth = _globalCoef * (_gobanSize.getSize() - 1);
      _backgroundWidth = _globalCoef * _gobanSize.getSize();
      _globalPadding = _globalCoef / 2;
    }
  }

  @Override
  protected void drawStones(Canvas canvas)
  {
    Perf perf = new Perf();
    String splii[];
    for (String s : stones.split("\\|"))
    {
      splii = s.split(":");
      byte color = Byte.parseByte(splii[0]);
      int pos = Integer.parseInt(splii[1]);
      drawStone(canvas, 0, _go.I(pos), _go.J(pos), color);
    }
    Log.d("beroot", "Preview.drawStones: " + perf.getTime() + " s" + " - " + stones.length());
  }

  @Override
  protected void drawStone(Canvas canvas, float reduc, int px, int py, byte color)
  {
    float x;
    float y;
    x = (px * _globalCoef) + _globalPadding ;
    y = (py * _globalCoef) + _globalPadding ;
    canvas.drawCircle(x, y, 2.2f, color == Go.BLACK ? _blackStonePaint : _whiteStonePaint);
  }
}
