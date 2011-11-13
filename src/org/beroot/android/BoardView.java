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
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class BoardView extends View
{
  private static final String TAG = "beroot";

  // --------------------------------------------------------------------------
  // Objets utilisés dans la phase de doPaint()
  // --------------------------------------------------------------------------
  private static final Paint _gobanBackgroundImage = new Paint();
  private static Bitmap _whiteStoneImage;
  private static Bitmap _blackStoneImage;
  private static int _gobanWidth;
  private float _activeCellWidth;
  private static Paint _linePaint;
  private static Paint _linePaintBig;
  private static int _globalCoef;
  private static int _lineWidth;
  private static int _backgroundWidth;
  private static int _globalPadding;

  // --------------------------------------------------------------------------
  // Objets généraux
  // --------------------------------------------------------------------------
  private static Resources _resources;
  private final GestureDetector _gestureDetector;

  private static int _moveCount = 0;

  // --------------------------------------------------------------------------
  // Objets de jeu
  // --------------------------------------------------------------------------
  private static GobanSize _gobanSize;
  private static Go _go;
  private static List<Point> _plays = new ArrayList<Point>();

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
    _resources = getResources();
    _gestureDetector = new GestureDetector(context, new GobanGestureListener());
    setGobanSize(GobanSize.G13);
  }

  public BoardView(Context context, AttributeSet attrs)
  {
    super(context, attrs);
    _resources = getResources();
    _gestureDetector = new GestureDetector(context, new GobanGestureListener());
    setGobanSize(GobanSize.G13);
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
  private final class GobanGestureListener extends GestureDetector.SimpleOnGestureListener
  {
    public boolean onSingleTapUp(MotionEvent e)
    {
      Point p = coord2Point(e.getX(), e.getY());
      if (p.x < _gobanSize.getSize() && p.y < _gobanSize.getSize())
      {
        Log.d(TAG, "pressed - x: " + e.getX() + " - y: " + e.getY());
        Log.d(TAG, "point   - x: " + p.x + " - y: " + p.y);
        Log.d(TAG, "traced  - x: " + (int) (((p.x) * _globalCoef) + _globalPadding) + " - y: " + (int) (((p.y) * _globalCoef) + _globalPadding));
        if (addStone(p))
        {
          postInvalidate();
        }
      }
      return true;
    }
  }

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

  private Point coord2Point(final float x, final float y)
  {
    return new Point((int) (x / (_activeCellWidth)), (int) (y / (_activeCellWidth)));
  }

  private void doDraw(Canvas canvas)
  {
    // Récupération de la surface de travail
    final boolean isLandscape = getWidth() > getHeight();
    _gobanWidth = isLandscape ? getHeight() : getWidth();

    // Calcul des mesures
    Log.d(TAG, "Calcul des mesures");
    _globalCoef = _gobanWidth / _gobanSize.getSize();
    _activeCellWidth = _globalCoef;
    _lineWidth = _globalCoef * (_gobanSize.getSize() - 1);
    _backgroundWidth = _globalCoef * _gobanSize.getSize();
    _globalPadding = _globalCoef / 2;

    // Image de fond
    Log.d(TAG, "image de fond");
    canvas.drawRect(0, 0, _backgroundWidth, _backgroundWidth, _gobanBackgroundImage);

    // Tracé des lignes
    Log.d(TAG, "Tracé des lignes");
    
    boolean isFirstOrLast;
    for (int i = 0; i < _gobanSize.getSize(); i++)
    {
      isFirstOrLast = (i == 0 || i == _gobanSize.getSize() - 1);
      canvas.drawLine((i * _globalCoef) + _globalPadding, _globalPadding, (i * _globalCoef) + _globalPadding, _lineWidth + _globalPadding, (isFirstOrLast ? _linePaintBig : _linePaint));
      canvas.drawLine(_globalPadding, (i * _globalCoef) + _globalPadding, _lineWidth + _globalPadding, (i * _globalCoef) + _globalPadding, (isFirstOrLast ? _linePaintBig : _linePaint));
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
   * Écoute des clics écran. Délègue le traitement au gestureDetector
   */
  @Override
  public boolean onTouchEvent(MotionEvent e)
  {
    // performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
    _gestureDetector.onTouchEvent(e);
    return true;
  }

  @Override
  public void onDraw(Canvas canvas)
  {
    doDraw(canvas);
  }
}
