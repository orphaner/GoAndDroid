package org.beroot.android.goengine;

public class GoGame
{
  // ------------------------------------------------------------------------
  // Données de jeu
  // ------------------------------------------------------------------------
  private GoNode _first;

  private GoNode _current;

  private Go _goEngine;

  // ------------------------------------------------------------------------
  // Données de jeu
  // ------------------------------------------------------------------------
  public void setFirstNode(GoNode first)
  {
    _first = first;
    _current = _first;
    boolean playerTurnSet = false;

    // Taille du goban
    String sz = _current.getProp(ISgf.SZ);
    if (sz != null)
    {
      int size = Integer.parseInt(sz);
      _goEngine = new Go(size);
    }

    // Affectation du joueur qui doit jouer
    String pl = _current.getProp(ISgf.PL);
    if (pl != null)
    {
      if (pl.equals("W"))
      {
        _goEngine.setFirstPlayer(Go.WHITE);
        playerTurnSet = true;
      }
      else if (pl.equals("B"))
      {
        _goEngine.setFirstPlayer(Go.BLACK);
        playerTurnSet = true;
      }
    }

    // Pierres de handicap
    String ha = _current.getProp(ISgf.HA);
    if (ha != null)
    {
      int handicap = Integer.parseInt(ha);
      if (!playerTurnSet && handicap > 1)
      {
        _goEngine.setFirstPlayer(Go.WHITE); // TODO déporter dans goEngine ?
      }
    }
    else if (!playerTurnSet)
    {
      _goEngine.setFirstPlayer(Go.BLACK); // TODO déporter dans goEngine ?
    }

    // TODO multi AB/AW/AE prendre en compte l'ordre !!!
    addStones(_current.getProp(ISgf.AB), Go.BLACK);
    addStones(_current.getProp(ISgf.AW), Go.WHITE);
    addStones(_current.getProp(ISgf.AE), Go.EMPTY);
  }

  private void addStones(String stone, byte color)
  {
    if (stone != null)
    {
      for (String move : stone.split("\\|"))
      {
        if (move.length() == 2)
        {
          _goEngine.addStone(move, color);
        }
        else if (move.length() == 5)
        {
          String moves[] = move.split(":");
          char x1 = moves[0].charAt(0);
          char y1 = moves[0].charAt(1);
          char x2 = moves[1].charAt(0);
          char y2 = moves[1].charAt(1);

          for (char c1 = x1; c1 <= x2; c1++)
          {
            for (char c2 = y1; c2 <= y2; c2++)
            {
              System.out.println("" + c1 + c2);
            }
          }
        }
      }
    }
  }

  public String getBlackPlayerName()
  {
    return _first.getProp(ISgf.PB);
  }

  public String getWhitePlayerName()
  {
    return _first.getProp(ISgf.PW);
  }

  public String getComment()
  {
    return _current.getProp(ISgf.C);
  }

  public boolean hasNextNode()
  {
    return _current.getNextNode() != null;
  }

  public void nextNode()
  {
    fetchNextNode();
    String move = null;
    if (_goEngine.getPlayerTurn() == Go.BLACK)
    {
      move = _current.getProp(ISgf.B);
    }
    else if (_goEngine.getPlayerTurn() == Go.WHITE)
    {
      move = _current.getProp(ISgf.W);
    }

    if (move != null)
    {
      _goEngine.play(move, _goEngine.getPlayerTurn());
    }
    else
    {
      addStones(_current.getProp(ISgf.AW), Go.WHITE);
      addStones(_current.getProp(ISgf.AB), Go.BLACK);
      addStones(_current.getProp(ISgf.AE), Go.EMPTY);
    }

  }

  private void fetchNextNode()
  {
    if (hasNextNode())
    {
      _current = _current.getNextNode();
    }
  }

  public void nextVariation(int variationNumber)
  {
    if (_current != null)
    {
      GoNode result = _current.getVariation(variationNumber);
      _current = result;
    }
  }

  public Go getGoEngine()
  {
    return _goEngine;
  }

  public void undo()
  {
    if (canUndo())
    {
      _current = _current.getPrevNode();
    }
  }

  public boolean canUndo()
  {
    return _current != null && _current.getPrevNode() != null;
  }
}
