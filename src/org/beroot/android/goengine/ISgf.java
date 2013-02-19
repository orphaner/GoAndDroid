package org.beroot.android.goengine;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public interface ISgf
{
  /**
   * Add Black
   * Property type: setup
   * Property value: list of stone
   */
  public static final String AB = "AB";

  /**
   * Add Empty
   * Property type: setup
   * Property value: list of point
   */
  public static final String AE = "AE";

  /**
   * Annotation
   * Property type: game-info
   * Property value: simpletext
   */
  public static final String AN = "AN";

  /**
   * Application
   * Property type: root
   * Property value: composed simplete
   */
  public static final String AP = "AP";

  /**
   * Arrow
   * Property type: -
   * Property value: list of composed
   */
  public static final String AR = "AR";

  /**
   * Who adds stones
   * Property type: - (LOA)
   * Property value: simpletext
   */
  public static final String AS = "AS";

  /**
   * Add White
   * Property type: setup
   * Property value: list of stone
   */
  public static final String AW = "AW";

  /**
   * Black
   * Property type: move
   * Property value: move
   */
  public static final String B = "B";

  /**
   * Black time left
   * Property type: move
   * Property value: real
   */
  public static final String BL = "BL";

  /**
   * Bad move
   * Property type: move
   * Property value: double
   */
  public static final String BM = "BM";

  /**
   * Black rank
   * Property type: game-info
   * Property value: simpletext
   */
  public static final String BR = "BR";

  /**
   * Black team
   * Property type: game-info
   * Property value: simpletext
   */
  public static final String BT = "BT";

  /**
   * Comment
   * Property type: -
   * Property value: text
   */
  public static final String C = "C";

  /**
   * Charset
   * Property type: root
   * Property value: simpletext
   */
  public static final String CA = "CA";

  /**
   * Copyright
   * Property type: game-info
   * Property value: simpletext
   */
  public static final String CP = "CP";

  /**
   * Circle
   * Property type: -
   * Property value: list of point
   */
  public static final String CR = "CR";

  /**
   * Dim points
   * Property type: - (inherit)
   * Property value: elist of point
   */
  public static final String DD = "DD";

  /**
   * Even position
   * Property type: -
   * Property value: double
   */
  public static final String DM = "DM";

  /**
   * Doubtful
   * Property type: move
   * Property value: none
   */
  public static final String DO = "DO";

  /**
   * Date
   * Property type: game-info
   * Property value: simpletext
   */
  public static final String DT = "DT";

  /**
   * Event
   * Property type: game-info
   * Property value: simpletext
   */
  public static final String EV = "EV";

  /**
   * Fileformat
   * Property type: root
   * Property value: number (range: 1-
   */
  public static final String FF = "FF";

  /**
   * Figure
   * Property type: -
   * Property value: none | composed n
   */
  public static final String FG = "FG";

  /**
   * Good for Black
   * Property type: -
   * Property value: double
   */
  public static final String GB = "GB";

  /**
   * Game comment
   * Property type: game-info
   * Property value: text
   */
  public static final String GC = "GC";

  /**
   * Game
   * Property type: root
   * Property value: number (range: 1-
   */
  public static final String GM = "GM";

  /**
   * Game name
   * Property type: game-info
   * Property value: simpletext
   */
  public static final String GN = "GN";

  /**
   * Good for White
   * Property type: -
   * Property value: double
   */
  public static final String GW = "GW";

  /**
   * Handicap
   * Property type: game-info (Go)
   * Property value: number
   */
  public static final String HA = "HA";

  /**
   * Hotspot
   * Property type: -
   * Property value: double
   */
  public static final String HO = "HO";

  /**
   * Initial pos.
   * Property type: game-info (LOA)
   * Property value: simpletext
   */
  public static final String IP = "IP";

  /**
   * Interesting
   * Property type: move
   * Property value: none
   */
  public static final String IT = "IT";

  /**
   * Invert Y-axis
   * Property type: game-info (LOA)
   * Property value: simpletext
   */
  public static final String IY = "IY";

  /**
   * Komi
   * Property type: game-info (Go)
   * Property value: real
   */
  public static final String KM = "KM";

  /**
   * Ko
   * Property type: move
   * Property value: none
   */
  public static final String KO = "KO";

  /**
   * Label
   * Property type: -
   * Property value: list of composed
   */
  public static final String LB = "LB";

  /**
   * Line
   * Property type: -
   * Property value: list of composed
   */
  public static final String LN = "LN";

  /**
   * Mark
   * Property type: -
   * Property value: list of point
   */
  public static final String MA = "MA";

  /**
   * set move number
   * Property type: move
   * Property value: number
   */
  public static final String MN = "MN";

  /**
   * Nodename
   * Property type: -
   * Property value: simpletext
   */
  public static final String N = "N";

  /**
   * OtStones Black
   * Property type: move
   * Property value: number
   */
  public static final String OB = "OB";

  /**
   * Opening
   * Property type: game-info
   * Property value: text
   */
  public static final String ON = "ON";

  /**
   * Overtime
   * Property type: game-info
   * Property value: simpletext
   */
  public static final String OT = "OT";

  /**
   * OtStones White
   * Property type: move
   * Property value: number
   */
  public static final String OW = "OW";

  /**
   * Player Black
   * Property type: game-info
   * Property value: simpletext
   */
  public static final String PB = "PB";

  /**
   * Place
   * Property type: game-info
   * Property value: simpletext
   */
  public static final String PC = "PC";

  /**
   * Player to play
   * Property type: setup
   * Property value: color
   */
  public static final String PL = "PL";

  /**
   * Print move mode
   * Property type: - (inherit)
   * Property value: number
   */
  public static final String PM = "PM";

  /**
   * Player White
   * Property type: game-info
   * Property value: simpletext
   */
  public static final String PW = "PW";

  /**
   * Result
   * Property type: game-info
   * Property value: simpletext
   */
  public static final String RE = "RE";

  /**
   * Round
   * Property type: game-info
   * Property value: simpletext
   */
  public static final String RO = "RO";

  /**
   * Rules
   * Property type: game-info
   * Property value: simpletext
   */
  public static final String RU = "RU";

  /**
   * Markup
   * Property type: - (LOA)
   * Property value: point
   */
  public static final String SE = "SE";

  /**
   * Selected
   * Property type: -
   * Property value: list of point
   */
  public static final String SL = "SL";

  /**
   * Source
   * Property type: game-info
   * Property value: simpletext
   */
  public static final String SO = "SO";

  /**
   * Square
   * Property type: -
   * Property value: list of point
   */
  public static final String SQ = "SQ";

  /**
   * Style
   * Property type: root
   * Property value: number (range: 0-
   */
  public static final String ST = "ST";

  /**
   * Setup type
   * Property type: game-info (LOA)
   * Property value: simpletext
   */
  public static final String SU = "SU";

  /**
   * Size
   * Property type: root
   * Property value: (number | compose
   */
  public static final String SZ = "SZ";

  /**
   * Territory Black
   * Property type: - (Go)
   * Property value: elist of point
   */
  public static final String TB = "TB";

  /**
   * Tesuji
   * Property type: move
   * Property value: double
   */
  public static final String TE = "TE";

  /**
   * Timelimit
   * Property type: game-info
   * Property value: real
   */
  public static final String TM = "TM";

  /**
   * Triangle
   * Property type: -
   * Property value: list of point
   */
  public static final String TR = "TR";

  /**
   * Territory White
   * Property type: - (Go)
   * Property value: elist of point
   */
  public static final String TW = "TW";

  /**
   * Unclear pos
   * Property type: -
   * Property value: double
   */
  public static final String UC = "UC";

  /**
   * User
   * Property type: game-info
   * Property value: simpletext
   */
  public static final String US = "US";

  /**
   * Value
   * Property type: -
   * Property value: real
   */
  public static final String V = "V";

  /**
   * View
   * Property type: - (inherit)
   * Property value: elist of point
   */
  public static final String VW = "VW";

  /**
   * White
   * Property type: move
   * Property value: move
   */
  public static final String W = "W";

  /**
   * White time left
   * Property type: move
   * Property value: real
   */
  public static final String WL = "WL";

  /**
   * White rank
   * Property type: game-info
   * Property value: simpletext
   */
  public static final String WR = "WR";

  /**
   * White team
   * Property type: game-info
   * Property value: simpletext
   */
  public static final String WT = "WT";

  /**
   * Liste des propriétés représentant des coups ou des ajouts/suppression de pierres
   */
  public static final Set<String> MOVES_PROP = new HashSet<String>(Arrays.asList(new String[] { W, B, AB, AW, AE }));

  /**
   * Liste des propriétés représentant des marques à afficher sur le goban et/ou les pierres
   */
  public static final Set<String> MARKS_PROP = new HashSet<String>(Arrays.asList(new String[] { LB, SQ, CR, AR, LN, MA, SQ, TR }));
}
