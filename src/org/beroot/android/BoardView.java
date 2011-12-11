package org.beroot.android;

import org.beroot.android.goengine.Go;
import org.beroot.android.goengine.GobanSize;
import org.beroot.android.util.Point;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.drawable.shapes.PathShape;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

public class BoardView extends BoardViewCommon
{

  protected Bitmap _whiteStoneImage;
  protected Bitmap _blackStoneImage;
  
  private GestureDetector _gestureDetector;
  private ScaleGestureDetector _scaleDetector;

  private float mPosX;
  private float mPosY;

  private float mLastTouchX;
  private float mLastTouchY;

  private static final int INVALID_POINTER_ID = -1;

  private int mActivePointerId = INVALID_POINTER_ID;
  private float mScaleFactor = 1.0f;


  // --------------------------------------------------------------------------
  // Initialisation
  // --------------------------------------------------------------------------
  /**
   * Constructeur par défaut
   * 
   * @param pContext
   * @param pAttributeSet
   */
  public BoardView(Context context)
  {
    super(context);
    init(context);
  }

  public BoardView(Context context, AttributeSet attrs)
  {
    super(context, attrs);
    init(context);
  }

  protected void init(Context context)
  {
    super.init(context);
    _gestureDetector = new GestureDetector(context, new GobanGestureListener());
    _scaleDetector = new ScaleGestureDetector(context, new ScaleListener());
    setGobanSize(GobanSize.G19);
  }


  // --------------------------------------------------------------------------
  // Phase de jeu
  // --------------------------------------------------------------------------
  private boolean addStone(Point p)
  {
    if (isBlackTurn() && _go.isLegal(_go.pos(p.x, p.y), Go.BLACK))
    {
      _go.play(p.x, p.y, Go.BLACK);
      _moveCount++;
      return true;
    }
    else if (!isBlackTurn() && _go.isLegal(_go.pos(p.x, p.y), Go.WHITE))
    {
      _go.play(p.x, p.y, Go.WHITE);
      _moveCount++;
      return true;
    }
    return false;
  }

  private boolean isBlackTurn()
  {
    return _moveCount % 2 == 0;
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
  {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    if (getWidth() > 0)
    {
      // Récupération de la surface de travail
      final boolean isLandscape = getWidth() > getHeight();
      _gobanWidth = isLandscape ? getMeasuredHeight() : getMeasuredWidth();

      if (isLandscape)
      {
        setMeasuredDimension(getMeasuredHeight(), getMeasuredHeight());
      }
      else
      {
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
      }

      // Calcul des mesures
      Log.d(TAG, "Calcul des mesures");
      _globalCoef = _gobanWidth / _gobanSize.getSize();
      _activeCellWidth = _globalCoef;
      _lineWidth = _globalCoef * (_gobanSize.getSize() - 1);
      _backgroundWidth = _globalCoef * _gobanSize.getSize();
      _globalPadding = _globalCoef / 2;

      Bitmap.Config bitmapConfig = Bitmap.Config.ARGB_8888;
      _blackStoneImage = Bitmap.createBitmap((int) _activeCellWidth, (int) _activeCellWidth, bitmapConfig);
      final Canvas canvas2 = new Canvas(_blackStoneImage);
      final float stoneCenter = getWidth() / 2;
      float stoneRadius = stoneCenter - getWidth() / 30;
      Path path = new Path();
      path.addCircle(stoneCenter, stoneCenter, stoneRadius, Path.Direction.CW);
      PathShape shape = new PathShape(path, getWidth(), getWidth());
      shape.resize(_activeCellWidth, _activeCellWidth);
      Paint _xferModePaintSrc = new Paint();
      _xferModePaintSrc.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
      final Paint paint = new Paint(_xferModePaintSrc);
      paint.setStyle(Paint.Style.FILL);
      paint.setFlags(Paint.ANTI_ALIAS_FLAG);
      final float circleHighlight = stoneCenter - stoneCenter / 3, radiusHighlight = stoneCenter / 3;
      final int blackStoneColor = _resources.getColor(R.color.blackStoneColor);
      paint.setShader(new RadialGradient(circleHighlight, circleHighlight, radiusHighlight, _resources.getColor(R.color.blackStoneHighlightColor),
          blackStoneColor, Shader.TileMode.CLAMP));
      shape.draw(canvas2, paint);

      _whiteStoneImage = Bitmap.createBitmap((int) _activeCellWidth, (int) _activeCellWidth, bitmapConfig);
      canvas2.setBitmap(_whiteStoneImage);
      final int whiteStoneColor = _resources.getColor(R.color.whiteStoneColor);
      final float highlightEnd = stoneCenter + (float) Math.sqrt(stoneCenter * stoneCenter / 2);
      paint.setShader(new LinearGradient(circleHighlight, circleHighlight, highlightEnd, highlightEnd, whiteStoneColor, _resources
          .getColor(R.color.whiteStoneHighlightColor), Shader.TileMode.CLAMP));
      shape.draw(canvas2, paint);
      //Paint blackPaint = new Paint(paint);
    }
  }

  @Override
  public void onDraw(Canvas canvas)
  {
    canvas.save();
    canvas.translate(mPosX, mPosY);
    canvas.scale(mScaleFactor, mScaleFactor);

    super.onDraw(canvas);

    canvas.restore();
  }

  @Override
  protected void drawStones(Canvas canvas)
  {
    // Tracé des pierres
    float reduc = _globalCoef / 1.8f;

    for (int i = _go.boardMin; i < _go.boardMax; i++)
    {
      if (_go.onBoard(i))
      {
        if (_go._board[i] == Go.WHITE)
        {
          drawStone(canvas, reduc, _go.I(i), _go.J(i), Go.WHITE);
        }
        else if (_go._board[i] == Go.BLACK)
        {
          drawStone(canvas, reduc, _go.I(i), _go.J(i), Go.BLACK);
        }
      }
    }
  }

  @Override
  protected void drawStone(Canvas canvas, float reduc, int px, int py, byte color)
  {
    float x;
    float y;
    x = (px * _globalCoef) + _globalPadding - reduc;
    y = (py * _globalCoef) + _globalPadding - reduc;
    Matrix matrix = new Matrix();
    matrix.postTranslate(x, y);
    canvas.drawBitmap(color == Go.BLACK ? _blackStoneImage : _whiteStoneImage, matrix, null);
  }

  /**
   * Écoute des clics écran et gère les scrolls en cas de zoom.
   * 
   * Délègue le traitement :
   * - au gestureDetector pour l'ajout des pierres
   * - au scaleDectector pour le pinch to zoom
   */
  @Override
  public boolean onTouchEvent(MotionEvent event)
  {
    // performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
    _gestureDetector.onTouchEvent(event);
    _scaleDetector.onTouchEvent(event);

    final int action = event.getAction();
    int pointerIndex;
    switch (action & MotionEvent.ACTION_MASK)
    {
      case MotionEvent.ACTION_DOWN:
      {
        final float x = event.getX();
        final float y = event.getY();

        mLastTouchX = x;
        mLastTouchY = y;
        mActivePointerId = event.getPointerId(0);
        break;
      }

      case MotionEvent.ACTION_MOVE:
        pointerIndex = event.findPointerIndex(mActivePointerId);
        final float x = event.getX(pointerIndex);
        final float y = event.getY(pointerIndex);

        // Only move if the ScaleGestureDetector isn't processing a gesture.
        if (!_scaleDetector.isInProgress())
        {
          final float dx = x - mLastTouchX;
          final float dy = y - mLastTouchY;

          mPosX += dx;
          mPosY += dy;

          if (mScaleFactor == 1.0f)
          {
            mPosX = 0.0f;
            mPosY = 0.0f;
          }
          else
          {
            if (mPosX < 0.0f && Math.abs(mPosX) > getWidth() * (mScaleFactor - 1))
            {
              mPosX = -1 * getWidth() * (mScaleFactor - 1);
            }
            if (mPosX > 0.0f)
            {
              mPosX = 0.0f;
            }
            if (mPosY < 0.0f && Math.abs(mPosY) > getHeight() * (mScaleFactor - 1))
            {
              mPosY = -1 * getHeight() * (mScaleFactor - 1);
            }
            if (mPosY > 0.0f)
            {
              mPosY = 0.0f;
            }
          }
          invalidate();
        }

        mLastTouchX = x;
        mLastTouchY = y;

        break;

      case MotionEvent.ACTION_UP:
        mActivePointerId = INVALID_POINTER_ID;
        break;

      case MotionEvent.ACTION_CANCEL:
        mActivePointerId = INVALID_POINTER_ID;
        break;

      case MotionEvent.ACTION_POINTER_UP:
        pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
        final int pointerId = event.getPointerId(pointerIndex);
        if (pointerId == mActivePointerId)
        {
          // This was our active pointer going up. Choose a new
          // active pointer and adjust accordingly.
          final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
          mLastTouchX = event.getX(newPointerIndex);
          mLastTouchY = event.getY(newPointerIndex);
          mActivePointerId = event.getPointerId(newPointerIndex);
        }
        break;
    }

    invalidate();
    return true;
  }

  private final class GobanGestureListener extends GestureDetector.SimpleOnGestureListener
  {
    @Override
    public boolean onSingleTapUp(MotionEvent e)
    {
      if (!_scaleDetector.isInProgress())
      {
        Point p = coord2Point(e.getX(), e.getY());
        if (p.x < _gobanSize.getSize() && p.y < _gobanSize.getSize())
        {
          Log.d(TAG, "pressed - x: " + e.getX() + " - y: " + e.getY());
          Log.d(TAG, "point   - x: " + p.x + " - y: " + p.y);
          Log.d(TAG, "traced  - x: " + (int) (((p.x) * _globalCoef) + _globalPadding) + " - y: " + (int) (((p.y) * _globalCoef) + _globalPadding));
          if (addStone(p))
          {
            invalidate();
          }
        }
      }
      return true;
    }

    private Point coord2Point(final float x, final float y)
    {
      return new Point((int) (((x - mPosX) / (_activeCellWidth)) / mScaleFactor), (int) (((y - mPosY) / (_activeCellWidth)) / mScaleFactor));
    }
  }

  private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener
  {
    @Override
    public boolean onScale(ScaleGestureDetector detector)
    {
      mScaleFactor *= detector.getScaleFactor();

      // Don't let the object get too small or too large.
      mScaleFactor = Math.max(1f, Math.min(mScaleFactor, 3.0f));

      invalidate();
      return true;
    }
  }
}
