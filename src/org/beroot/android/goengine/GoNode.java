package org.beroot.android.goengine;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GoNode
{
  // ------------------------------------------------------------------------
  // Données de jeu
  // ------------------------------------------------------------------------
  /**
   * Référence vers le noeud suivant
   */
  private GoNode _next;

  /**
   * Référence vers le noeud parent
   */
  private GoNode _prev = null;

  /**
   * Liste de variations du noeud courant
   */
  private List<GoNode> _variations;

  /**
   * Hashmap des propriétés
   */
  private Map<String, String> _props;

  /**
   * Liste de variations du noeud courant
   */
  private List<SgfProperty> _movesAndStones;

  // ------------------------------------------------------------------------
  // Manipulation de la liste chainée
  // ------------------------------------------------------------------------
  public void setNextNode(GoNode next)
  {
    _next = next;
    _next.setPrevNode(this);
  }

  public void setPrevNode(GoNode prev)
  {
    _prev = prev;
  }

  public GoNode getNextNode()
  {
    return _next;
  }

  public GoNode getPrevNode()
  {
    return _prev;
  }

  // ------------------------------------------------------------------------
  // Manipulation des variations
  // ------------------------------------------------------------------------
  public void addVariation(GoNode variation)
  {
    if (_variations == null)
    {
      //_variations = new ArrayList<GoNode>();
      _variations = new LinkedList<GoNode>();
    }
    _variations.add(variation);
  }

  public int getVariationCount()
  {
    if (_variations == null)
    {
      return 0;
    }
    return _variations.size();
  }

  public GoNode getVariation(int variationNumber)
  {
    if (_variations != null && variationNumber < _variations.size())
    {
      return _variations.get(variationNumber);
    }
    return null;
  }

  // ------------------------------------------------------------------------
  // Manipulation des propriétés
  // ------------------------------------------------------------------------
  public void addProp(String key, String value)
  {
    // Ajout d'un coup
    if (ISgf.MOVES_PROP.contains(key))
    {
      if (_movesAndStones == null)
      {
        _movesAndStones = new LinkedList<SgfProperty>();
      }
      _movesAndStones.add(new SgfProperty(key, value));
    }
    // Ajout d'une propriété
    else
    {
      if (_props == null)
      {
        _props = new HashMap<String, String>();
      }
      _props.put(key, value);
    }
  }

  public void addProp(String key, int value)
  {
    addProp(key, "" + value);
  }

  public String getProp(String key)
  {
    if (_props != null && _props.containsKey(key))
    {
      return _props.get(key);
    }
    return null;
  }

  public List<SgfProperty> getMovesAndStones()
  {
    return _movesAndStones;
  }
}
