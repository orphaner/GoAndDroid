package org.beroot.android.goengine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author nicolas
 *
 */
public class GoGame
{
  // ------------------------------------------------------------------------
  // Données de jeu
  // ------------------------------------------------------------------------
  /**
   * Référence vers la racine de l'arbre
   */
  private GoNode _first;

  /**
   * Référence vers le noeud courant
   */
  private GoNode _current;

  /**
   * Moteur de jeu pour la racine de l'arbre
   */
  private Go _goEngineInitial;

  /**
   * Moteur de jeu à jour pour le noeud courant
   */
  private Go _goEngine;

  // ------------------------------------------------------------------------
  // Manipulation de l'arbre
  // ------------------------------------------------------------------------
  /**
   * Affecte le noeud racine de l'arbre.
   * Cré le GoEngine et renseigne les valeurs de base.
   */
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

    _goEngineInitial = _goEngine.clone();
    addMovesAndStones();
  }

  /**
   * Possible d'avancer vers le noeud suivant ?
   * @return
   */
  public boolean hasNextNode()
  {
    return _current.getNextNode() != null;
  }

  /**
   * Avance au noeud suivant
   */
  public void nextNode()
  {
    if (hasNextNode())
    {
      _current = _current.getNextNode();
    }
    addMovesAndStones();
  }

  /**
   * Navigue vers une variation du noeud courant
   * @param variationNumber
   */
  public void nextVariation(int variationNumber)
  {
    if (_current != null)
    {
      GoNode result = _current.getVariation(variationNumber);
      _current = result;
    }
  }
  
  /**
   * Annule le dernier coup
   */
  public void undo()
  {
    if (canUndo())
    {
      GoNode prev;
      List<SgfProperty> list = new ArrayList<SgfProperty>();
      prev = _current.getPrevNode();
      while (prev != null)
      {
        if (prev.getMovesAndStones() != null)
        {
          list.addAll(prev.getMovesAndStones());
        }
        prev = prev.getPrevNode();
      }
      Collections.reverse(list);
      _goEngine = _goEngineInitial.clone();
      for (SgfProperty sp : list)
      {
        addms(sp);
      }
      _current = _current.getPrevNode();
    }
  }

  /**
   * Possible d'annuler le dernier coup
   * @return
   */
  public boolean canUndo()
  {
    return _current != null && _current.getPrevNode() != null;
  }

  /**
   * Retourne au début de l'arbre de jeu
   */
  public void goToFirstNode()
  {
    _goEngine = _goEngineInitial.clone();
    _current = _first;
    addMovesAndStones();
  }

  // ------------------------------------------------------------------------
  // Gestion d'ajout de coup et de pierres
  // ------------------------------------------------------------------------
  /**
   * Ajoute les coups et pierres du noeud courant
   */
  private void addMovesAndStones()
  {
    if (_current.getMovesAndStones() == null)
    {
      return;
    }
    for (SgfProperty sgfProp : _current.getMovesAndStones())
    {
      addms(sgfProp);
    }
  }

  /**
   * Ajout des informations de coup ou d'ajout de pierre de la SgfProperty
   * @param sgfProp informations de coup ou d'ajout de pierre
   */
  private void addms(SgfProperty sgfProp)
  {
    if (sgfProp.prop.equals(ISgf.B))
    {
      _goEngine.play(sgfProp.value, Go.BLACK);
    }
    else if (sgfProp.prop.equals(ISgf.W))
    {
      _goEngine.play(sgfProp.value, Go.WHITE);
    }
    else if (sgfProp.prop.equals(ISgf.AW))
    {
      addStones(sgfProp.value, Go.WHITE);
    }
    else if (sgfProp.prop.equals(ISgf.AB))
    {
      addStones(sgfProp.value, Go.BLACK);
    }
    else if (sgfProp.prop.equals(ISgf.AE))
    {
      addStones(sgfProp.value, Go.EMPTY);
    }
  }

  /**
   * Ajout de pierre. Expansion d'une liste de coups compressés.
   * @param stone
   * @param color
   */
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
              _goEngine.addStone("" + c1 + c2, color);
            }
          }
        }
      }
    }
  }

  // ------------------------------------------------------------------------
  // Récupération de propriété du noeud courant
  // ------------------------------------------------------------------------
  /**
   * Retourne le nom du joueur noir
   * @return
   */
  public String getBlackPlayerName()
  {
    return _first.getProp(ISgf.PB);
  }

  /**
   * Retourne le nom du joueur blanc
   * @return
   */
  public String getWhitePlayerName()
  {
    return _first.getProp(ISgf.PW);
  }

  /**
   * Retourne le commentaire
   * @return
   */
  public String getComment()
  {
    return _current.getProp(ISgf.C);
  }

  /**
   * Retourne le résultat de la partie
   * @return
   */
  public String getResult()
  {
    return _first.getProp(ISgf.RE);
  }

  // ------------------------------------------------------------------------
  // Méthodes diverses
  // ------------------------------------------------------------------------
  /**
   * Retourne le moteur de jeu
   */
  public Go getGoEngine()
  {
    return _goEngine;
  }
  
  /**
   * Retourne la liste des pierres de la position finale pour enregistrement en BDD
   * @return
   */
  public String getStonesForBDD()
  {
    StringBuilder sb = new StringBuilder();
    while (hasNextNode())
    {
      nextNode();
    }
    for (int i = _goEngine.boardMin; i < _goEngine.boardMax; i++)
    {
      if (_goEngine.onBoard(i))
      {
        if (_goEngine._board[i] == Go.WHITE)
        {
          sb.append(Go.WHITE);
          sb.append(":");
          sb.append(i);
          sb.append("|");
        }
        else if (_goEngine._board[i] == Go.BLACK)
        {
          sb.append(Go.BLACK);
          sb.append(":");
          sb.append(i);
          sb.append("|");
        }
      }
    }
    return sb.substring(0, sb.length() - 1);
  }

  public int getBoardSize()
  {
    return _goEngine.getBoardSize();
  }
}
