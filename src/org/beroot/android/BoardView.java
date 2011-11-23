package org.beroot.android;

import java.util.ArrayList;
import java.util.List;

import org.beroot.android.goengine.Go;
import org.beroot.android.goengine.GoConstants;
import org.beroot.android.goengine.GobanSize;
import org.beroot.android.util.Point;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
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
import android.view.View;

public class BoardView extends View
{
  private static final String TAG = "beroot";

  // --------------------------------------------------------------------------
  // Objets utilisés dans la phase de doPaint()
  // --------------------------------------------------------------------------
  private final Paint _gobanBackgroundImage = new Paint();
  private Bitmap _whiteStoneImage;
  private Bitmap _blackStoneImage;
  private int _gobanWidth;
  private float _activeCellWidth;
  private Paint _linePaint;
  private Paint _linePaintBig;
  private int _globalCoef;
  private int _lineWidth;
  private int _backgroundWidth;
  private int _globalPadding;

  // --------------------------------------------------------------------------
  // Objets généraux
  // --------------------------------------------------------------------------
  private Resources _resources;
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
  // Objets de jeu
  // --------------------------------------------------------------------------
  private GobanSize _gobanSize;
  private Go _go;
  private int _moveCount = 0;
  private List<Point> _plays = new ArrayList<Point>();

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

  private void init(Context context)
  {
    _resources = getResources();
    _gestureDetector = new GestureDetector(context, new GobanGestureListener());
    _scaleDetector = new ScaleGestureDetector(context, new ScaleListener());
    setGobanSize(GobanSize.G19);
  }

  public void setGobanSize(GobanSize gobanSize)
  {
    _gobanSize = gobanSize;
    _go = new Go(_gobanSize.getSize());
    initDrawObjects();
    postInvalidate();
  }

  /**
   * 
   */
  private void initDrawObjects()
  {
    // Image de fond du goban
    _gobanBackgroundImage
        .setShader(new BitmapShader(BitmapFactory.decodeResource(_resources, R.drawable.board), Shader.TileMode.MIRROR, Shader.TileMode.MIRROR));

    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = false;
    options.inDither = false;
    options.inScaled = false;
    options.inSampleSize = 1;
    options.inPreferredConfig = Bitmap.Config.ARGB_8888;

    float desiredScale = 16.5f / _gobanSize.getSize();
    Matrix matrix = new Matrix();
    matrix.postScale(desiredScale, desiredScale);

    // Image des pierres blanches
    _whiteStoneImage = BitmapFactory.decodeResource(getResources(), R.drawable.whitestone, options);
    _whiteStoneImage = Bitmap.createBitmap(_whiteStoneImage, 0, 0, _whiteStoneImage.getWidth(), _whiteStoneImage.getHeight(), matrix, true);

    // Image des pierres noires
    _blackStoneImage = BitmapFactory.decodeResource(getResources(), R.drawable.blackstone, options);
    _blackStoneImage = Bitmap.createBitmap(_blackStoneImage, 0, 0, _blackStoneImage.getWidth(), _blackStoneImage.getHeight(), matrix, true);

    // Couleur des lignes et hoshis du goban
    _linePaint = new Paint();
    _linePaint.setAntiAlias(true);
    _linePaint.setARGB(255, 0, 0, 0);
    _linePaint.setStrokeWidth(0.8f);

    _linePaintBig = new Paint();
    _linePaintBig.setAntiAlias(true);
    _linePaintBig.setARGB(255, 0, 0, 0);
    _linePaintBig.setStrokeWidth(2f);
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
      _plays.add(p);
      return true;
    }
    else if (!isBlackTurn() && _go.isLegal(_go.pos(p.x, p.y), Go.WHITE))
    {
      _go.play(p.x, p.y, Go.WHITE);
      _moveCount++;
      _plays.add(p);
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
    super.onDraw(canvas);

    canvas.save();
    canvas.translate(mPosX, mPosY);
    canvas.scale(mScaleFactor, mScaleFactor);

    // Image de fond
    Log.d(TAG, "image de fond");
    canvas.drawRect(0, 0, _backgroundWidth, _backgroundWidth, _gobanBackgroundImage);

    // Tracé des lignes
    Log.d(TAG, "Tracé des lignes");

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
    Log.d(TAG, "Tracé des hoshis");
    for (Point hoshi : GoConstants.getHoshis(_gobanSize))
    {
      canvas.drawCircle(((hoshi.x - 1) * _globalCoef) + _globalPadding, ((hoshi.y - 1) * _globalCoef) + _globalPadding, 5, _linePaint);
    }

    // Tracé des pierres
    Log.d(TAG, "Tracé des pierres");
    float reduc = _globalCoef / 1.8f;

    for (int i = _go.boardMin; i < _go.boardMax; i++)
    {
      if (_go.onBoard(i))
      {
        if (_go._board[i] == Go.WHITE)
        {
          drawStone(canvas, reduc, _go.I(i), _go.J(i), _whiteStoneImage);
        }
        else if (_go._board[i] == Go.BLACK)
        {
          drawStone(canvas, reduc, _go.I(i), _go.J(i), _blackStoneImage);
        }
      }
    }

    canvas.restore();
    Log.d(TAG, "Fin du tracé");
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
  private void drawStone(Canvas canvas, float reduc, int px, int py, Bitmap stone)
  {
    float x;
    float y;
    x = (px * _globalCoef) + _globalPadding - reduc;
    y = (py * _globalCoef) + _globalPadding - reduc;
    Log.d(TAG, "drawStone - B(" + px + "," + py + ") - coord(" + x + "," + y + ")");
    Matrix matrix = new Matrix();
    matrix.postTranslate(x, y);
    canvas.drawBitmap(stone, matrix, null);
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
