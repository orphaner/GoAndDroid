package org.beroot.android.goengine;

/**
 * @author Nicolas
 * 
 *         Représentation d'une chaine
 */
public class StringData
{
  // ------------------------------------------------------------------------
  // Données de jeu
  // ------------------------------------------------------------------------
  /**
   * Color of string, BLACK or WHITE
   */
  public byte color;

  /**
   * Number of stones in string.
   */
  public int size = 1;

  /**
   * Coordinates of "origin", i.e. "upper left" stone.
   */
  public int origin;

  /**
   * Number of liberties.
   */
  public int libertyCount = 0;

  /**
   * Number of neighbor strings
   */
  public int neighborCount = 0;

  /**
   * General purpose mark.
   */
  public int mark = 0;

  /**
   * Liste des pierres de la chaine
   */
  public int[] stones;

  /**
   * Liste des libertées de la chaine
   */
  public int[] liberties;

  /**
   * Liste des chaines voisines adverses de la chaine
   */
  public int[] neighbors;

  // ------------------------------------------------------------------------
  // Constructeur
  // ------------------------------------------------------------------------
  /**
   * Constructeur de création d'une chaine
   * 
   * @param boardSize taille du goban
   * @param pos position initiale de la chaine
   * @param color couleur de la chaine
   */
  public StringData(int boardSize, int pos, byte color)
  {
    // Initialisation des structures
    stones = new int[boardSize];
    liberties = new int[boardSize];
    neighbors = new int[boardSize];
    resetLiberties();

    // Initialisation des valeurs
    this.color = color;
    origin = pos;
    stones[0] = pos;
  }

  // ------------------------------------------------------------------------
  // Logique
  // ------------------------------------------------------------------------
  /**
	 * 
	 *
	 */
  public void resetLiberties()
  {
    libertyCount = 0;
    int i = 0;
    while (liberties[i] > 0)
    {
      liberties[i] = 0;
      i++;
    }
  }

  /**
   * 
   * @param pos
   */
  public void addStone(int pos)
  {
    stones[size] = pos;
    size++;
  }

  /**
   * 
   * @param pos
   */
  public void addLiberty(int pos)
  {
    liberties[libertyCount] = pos;
    libertyCount++;
  }

  /**
   * 
   * @param stringNumber
   */
  public void addNeighbor(int stringNumber)
  {
    neighbors[neighborCount] = stringNumber;
    neighborCount++;
  }

  /**
   * 
   * @param neighbor
   */
  public void removeNeighbor(int neighbor)
  {
    for (int i = 0; i < neighborCount; i++)
    {
      if (neighbors[i] == neighbor)
      {
        neighbors[i] = neighbors[neighborCount - 1];
        neighbors[neighborCount - 1] = 0;
        break;
      }
    }
    neighborCount--;
  }
}
