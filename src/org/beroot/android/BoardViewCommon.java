package org.beroot.android;

import org.beroot.android.goengine.GoConstants;
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
  protected static final Paint _blackCirclePaint;
  protected static final Paint _whiteCirclePaint;
  protected static final Paint _blackTextPaint;
  protected static final Paint _whiteTextPaint;
  protected int _globalCoef;
  protected int _lineWidth;
  protected int _backgroundWidth;
  protected int _globalPadding;

  // --------------------------------------------------------------------------
  // Objets de jeu
  // --------------------------------------------------------------------------
  protected int _boardSize;
  protected int _moveCount = 0;

  // --------------------------------------------------------------------------
  // Objets généraux
  // --------------------------------------------------------------------------
  protected Resources _resources;

  protected float _hoshiSize = 3;

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

    _blackCirclePaint = new Paint();
    _blackCirclePaint.setAntiAlias(true);
    _blackCirclePaint.setARGB(220, 230, 230, 230);
    _blackCirclePaint.setStrokeWidth(2.3f);

    _whiteCirclePaint = new Paint();
    _whiteCirclePaint.setAntiAlias(true);
    _whiteCirclePaint.setARGB(150, 0, 0, 0);
    _whiteCirclePaint.setStrokeWidth(2.3f);

    _blackTextPaint = new Paint();
    _blackTextPaint.setAntiAlias(true);
    _blackTextPaint.setARGB(255, 230, 230, 230);
    _blackTextPaint.setTextAlign(Paint.Align.CENTER);

    _whiteTextPaint = new Paint();
    _whiteTextPaint.setAntiAlias(true);
    _whiteTextPaint.setARGB(255, 0, 0, 0);
    _whiteTextPaint.setTextAlign(Paint.Align.CENTER);

//    _whiteTextPaint = new Paint();
//    _whiteTextPaint.setColor(0xFFFFFFFF);
//    _whiteTextPaint.setAntiAlias(true);
//    _whiteTextPaint.setTextAlign(Paint.Align.CENTER);
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
    initDrawObjects();
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

  @Override
  public void onDraw(Canvas canvas)
  {
    super.onDraw(canvas);

    // Image de fond
    canvas.drawRect(0, 0, _backgroundWidth, _backgroundWidth, _gobanBackgroundImage);

    // Tracé des lignes
    boolean isFirstOrLast;
    for (int i = 0; i < _boardSize; i++)
    {
      isFirstOrLast = (i == 0 || i == _boardSize - 1);
      canvas.drawLine((i * _globalCoef) + _globalPadding, _globalPadding, (i * _globalCoef) + _globalPadding, _lineWidth + _globalPadding,
          (isFirstOrLast ? _linePaintBig : _linePaint));
      canvas.drawLine(_globalPadding, (i * _globalCoef) + _globalPadding, _lineWidth + _globalPadding, (i * _globalCoef) + _globalPadding,
          (isFirstOrLast ? _linePaintBig : _linePaint));
    }

    // Tracé des hoshis
    for (Point hoshi : GoConstants.getHoshis(_boardSize))
    {
      canvas.drawCircle(((hoshi.x - 1) * _globalCoef) + _globalPadding, ((hoshi.y - 1) * _globalCoef) + _globalPadding, _hoshiSize, _linePaint);
    }

    drawStones(canvas);
  }

  /**
   * Trace toutes les pierres sur le goban
   * 
   * @param canvas
   */
  protected abstract void drawStones(Canvas canvas);
}
