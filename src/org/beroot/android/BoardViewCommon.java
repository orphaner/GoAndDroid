package org.beroot.android;

import org.beroot.android.goengine.Go;
import org.beroot.android.goengine.GoConstants;
import org.beroot.android.goengine.GobanSize;
import org.beroot.android.util.Point;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

public abstract class BoardViewCommon extends View
{
  protected static final String TAG = "beroot";

  // --------------------------------------------------------------------------
  // Objets utilisés dans la phase de doPaint()
  // --------------------------------------------------------------------------
  protected static Paint _gobanBackgroundImage;
  protected int _gobanWidth;
  protected float _activeCellWidth;
  protected static final Paint _linePaint;
  protected static final Paint _linePaintBig;
  protected static final Paint _whiteStonePaint;
  protected static final Paint _blackStonePaint;
  protected int _globalCoef;
  protected int _lineWidth;
  protected int _backgroundWidth;
  protected int _globalPadding;

  // --------------------------------------------------------------------------
  // Objets de jeu
  // --------------------------------------------------------------------------
  protected GobanSize _gobanSize;
  protected Go _go;
  protected int _moveCount = 0;

  // --------------------------------------------------------------------------
  // Objets généraux
  // --------------------------------------------------------------------------
  protected Resources _resources;

  protected float _hoshiSize = 5;

  static
  {
    // Couleur des lignes et hoshis du goban
    _linePaint = new Paint();
    _linePaint.setAntiAlias(true);
    _linePaint.setARGB(255, 0, 0, 0);
    _linePaint.setStrokeWidth(0.8f);

    _linePaintBig = new Paint();
    _linePaintBig.setAntiAlias(true);
    _linePaintBig.setARGB(255, 0, 0, 0);
    _linePaintBig.setStrokeWidth(2f);

    _blackStonePaint = new Paint();
    _blackStonePaint.setAntiAlias(true);
    _blackStonePaint.setARGB(255, 0, 0, 0);
    _blackStonePaint.setStrokeWidth(0.8f);

    _whiteStonePaint = new Paint();
    _whiteStonePaint.setAntiAlias(true);
    _whiteStonePaint.setARGB(255, 255, 255, 255);
    _whiteStonePaint.setStrokeWidth(0.8f);
  }

  public BoardViewCommon(Context context, AttributeSet attrs, int defStyle)
  {
    super(context, attrs, defStyle);
  }

  public BoardViewCommon(Context context, AttributeSet attrs)
  {
    super(context, attrs);
  }

  public BoardViewCommon(Context context)
  {
    super(context);
  }

  protected void init(Context context)
  {
    _resources = getResources();
  }

  /**
   * 
   */
  protected void initDrawObjects()
  {
    // Image de fond du goban
    if (_gobanBackgroundImage == null)
    {
      _gobanBackgroundImage = new Paint();
      _gobanBackgroundImage.setShader(new BitmapShader(BitmapFactory.decodeResource(_resources, R.drawable.board), Shader.TileMode.MIRROR,
          Shader.TileMode.MIRROR));
    }
  }

  public void setGo(Go go)
  {
    _go = go;
  }

  public void setGobanSize(GobanSize gobanSize)
  {
    _gobanSize = gobanSize;
    _go = new Go(_gobanSize.getSize());
    initDrawObjects();
    postInvalidate();
  }

  @Override
  public void onDraw(Canvas canvas)
  {
    super.onDraw(canvas);
    
    // Image de fond
    canvas.drawRect(0, 0, _backgroundWidth, _backgroundWidth, _gobanBackgroundImage);

    // Tracé des lignes
    boolean isFirstOrLast;
    for (int i = 0; i < _gobanSize.getSize(); i++)
    {
      isFirstOrLast = (i == 0 || i == _gobanSize.getSize() - 1);
      canvas.drawLine((i * _globalCoef) + _globalPadding, _globalPadding, (i * _globalCoef) + _globalPadding, _lineWidth + _globalPadding,
          (isFirstOrLast ? _linePaintBig : _linePaint));
      canvas.drawLine(_globalPadding, (i * _globalCoef) + _globalPadding, _lineWidth + _globalPadding, (i * _globalCoef) + _globalPadding,
          (isFirstOrLast ? _linePaintBig : _linePaint));
    }

    // Tracé des hoshis
    for (Point hoshi : GoConstants.getHoshis(_gobanSize))
    {
      canvas.drawCircle(((hoshi.x - 1) * _globalCoef) + _globalPadding, ((hoshi.y - 1) * _globalCoef) + _globalPadding, _hoshiSize, _linePaint);
    }
    
    drawStones(canvas);
  }

  /**
   * Trace une pierre sur le goban
   * 
   * @param canvas
   * @param reduc
   * @param px
   * @param py
   * @param stone
   */
  protected abstract void drawStone(Canvas canvas, float reduc, int px, int py, byte color);
  
  protected abstract void drawStones(Canvas canvas);
}
