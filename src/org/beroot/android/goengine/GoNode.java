package org.beroot.android.goengine;

import java.util.ArrayList;
import java.util.HashMap;
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
  private GoNode _prev;

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
      _variations = new ArrayList<GoNode>();
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
    // TODO remplir _movesAndStones
    if (_props == null)
    {
      _props = new HashMap<String, String>();
    }
    _props.put(key, value);
  }

  public String getProp(String key)
  {
    if (_props != null && _props.containsKey(key))
    {
      return _props.get(key);
    }
    return null;
  }
}
