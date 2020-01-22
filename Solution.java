import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Scanner;

/*
 * https://www.hackerrank.com/challenges/two-pluses/problem
 */
class Point {

  // **** members ****
  int r;
  int c;

  /*
   * Constructor.
   */
  public Point(int r, int c) {
    this.r = r;
    this.c = c;
  }

  /*
   * Return a string for this point. Row is displayed first, then column is
   * displayed.
   */
  public String toString() {
    return "(" + this.r + "," + this.c + ")";
  }
}

/*
 * Defines a plus sign.
 */
class PlusSign {

  // **** members ****
  Point c;
  int size;
  int side;
  int arm;

  /*
   * Constructor.
   */
  public PlusSign(int r, int c, int size) {

    // **** center of plus sign ****
    this.c = new Point(r, c);

    // **** size of plus sign (1, 5, 9, 13, ...) ****
    this.size = size;

    // **** side of plus sign (1, 3, 5, 7, ...) ****
    this.side = (size / 2) + 1;

    // **** arm of plus sign (0, 1, 2, 3, ...) ****
    this.arm = (size - 1) / 4;
  }

  /*
   * Draw the plus sign on the map. Returns true is plus sign can be drawn;
   * otherwise returns false.
   */
  public boolean draw(char[][] map) {

    // **** draw center (if possible) ****
    if (map[this.c.r][this.c.c] == 'G') {
      map[this.c.r][this.c.c] = 'X';
    } else {
      return false;
    }

    // **** draw up (if possible) ****
    for (int row = this.c.r - 1; row >= this.c.r - this.arm; row--) {
      if (map[row][this.c.c] == 'G') {
        map[row][this.c.c] = 'X';
      } else {
        return false;
      }
    }

    // **** draw right (if possible) ****
    for (int col = this.c.c + 1; col <= this.c.c + this.arm; col++) {
      if (map[this.c.r][col] == 'G') {
        map[this.c.r][col] = 'X';
      } else {
        return false;
      }
    }

    // **** draw down (if possible) ****
    for (int row = this.c.r + 1; row <= this.c.r + this.arm; row++) {
      if (map[row][this.c.c] == 'G') {
        map[row][this.c.c] = 'X';
      } else {
        return false;
      }
    }

    // **** draw left (if possible) ****
    for (int col = this.c.c - 1; col >= this.c.c - this.arm; col--) {
      if (map[this.c.r][col] == 'G') {
        map[this.c.r][col] = 'X';
      } else {
        return false;
      }
    }

    // **** able to draw plus sign ****
    return true;
  }

  /*
   * Determine if the specified plus sign overlaps this plus sign.
   */
  public boolean overlap(char[][] map, PlusSign ps) {

    // **** draw this plus sign ****
    boolean drawn = this.draw(map);

    // **** draw plus sign ****
    drawn = ps.draw(map);

    // **** inform caller if plus signs overlap ****
    return !drawn;
  }

  /*
   * Generate a string with the contents of the specified plus sign.
   */
  public String toString() {

    // **** ****
    StringBuilder sb = new StringBuilder();

    // **** ****
    sb.append(" c: " + this.c.toString());
    sb.append(" size: " + this.size);
    sb.append(" side: " + this.side);
    sb.append(" arm: " + this.arm);

    // **** ****
    return sb.toString();
  }
}

/*
 *
 */
public class Solution {

  /*
   * Create a fresh map. It only contains 'G' and 'B' cells. 'B' == bad, 'G' ==
   * good, and 'X' == used by plus. Top left side of grid at (0, 0).
   */
  static char[][] freshMap(String[] grid) {

    // **** grid dimensions ****
    int R = grid.length;
    int C = grid[0].length();

    // **** create map ****
    char[][] map = new char[R][C];

    // **** initialize map with grid values ****
    for (int row = 0; row < R; row++) {
      String s = grid[row];
      for (int col = 0; col < C; col++) {
        map[row][col] = s.charAt(col);
      }
    }

    // **** return map ****
    return map;
  }

  /*
   * Compute and return the size of the largest plus sign at the specified
   * location. The contents of the map are NOT updated.
   */
  static int plusSize(char[][] map, int r, int c) {

    // **** ****
    int R = map.length;
    int C = map[0].length;

    // **** check if this cell is NOT available ****
    if (map[r][c] != 'G') {
      return 0;
    }

    // **** [0] == up, [1] == right, [2] == down and [3] == left ****
    int[] span = new int[4];

    // **** check up ([0] == up) ****
    for (int row = (r - 1); row >= 0; row--) {
      if (map[row][c] == 'G') {
        span[0]++;
      } else {
        break;
      }
    }

    // **** check right ([1] == right) ****
    for (int col = c + 1; col < C; col++) {
      if (map[r][col] == 'G') {
        span[1]++;
      } else {
        break;
      }
    }

    // **** check down ([2] == down) ****
    for (int row = r + 1; row < R; row++) {
      if (map[row][c] == 'G') {
        span[2]++;
      } else {
        break;
      }
    }

    // **** check left ([3] == left ) ****
    for (int col = c - 1; col >= 0; col--) {
      if (map[r][col] == 'G') {
        span[3]++;
      } else {
        break;
      }
    }

    // **** compute size from span array (minimum common value) ****
    int size = span[0];
    for (int i = 0; i < span.length; i++) {
      if (span[i] < size) {
        size = span[i];
      }
    }

    // **** take into account the four sides and the center (if needed) ****
    if (size != 0) {
      size *= 4;
      size += 1;
    }

    // **** return the size of the plus sign *****
    return size;
  }

  /*
   * Complete the twoPluses function below.
   */
  static int twoPluses(String[] grid) {

    // **** instantiate array list to hold all plus signs ****
    ArrayList<PlusSign> al = new ArrayList<PlusSign>();

    // **** ****
    int result = 1;

    // **** ****
    int R = grid.length;
    int C = grid[0].length();

    // **** create a fresh map ****
    char[][] map = freshMap(grid);

    // **** populate array list with all plus signs ****
    for (int c = 1; c < (C - 1); c++) {
      for (int r = 1; r < (R - 1); r++) {

        // **** compute plus sign at the specified location (y = row, x = column) ****
        int size = plusSize(map, r, c);

        // **** loop instantiating and adding plus signs to the array based on (r,c)
        while (size >= 5) {

          // ***** instantiate a new plus sign ****
          PlusSign plusSign = new PlusSign(r, c, size);

          // **** add plus sign to the array list ****
          al.add(plusSign);

          // **** decrement size ****
          size -= 4;
        }
      }
    }

    // **** check the number of plus signs ****
    switch (al.size()) {
    case 0:
      return 1;

    case 1:
      return al.get(0).size;

    default:

      // **** continue ****

      break;
    }

    // **** loop finding non-overlaping areas ****
    for (int i = 0; i < (al.size() - 1); i++) {
      for (int j = (i + 1); j < al.size(); j++) {

        // **** refresh the map ****
        map = freshMap(grid);

        // **** determine if these plus signs overlap ****
        boolean overlap = al.get(i).overlap(map, al.get(j));
        if (!overlap) {

          // **** check and update result ****
          if (result < al.get(i).size * al.get(j).size) {
            result = al.get(i).size * al.get(j).size;
          }
        }
      }
    }

    // **** update result (if needed) ****
    if (al.size() == 1) {
      result = al.get(0).size;
    }

    // **** update result (if needed) ****
    if ((result == 1) && (al.size() == 2)) {
      if (al.get(0).size >= al.get(1).size) {
        result = al.get(0).size;
      } else {
        result = al.get(1).size;
      }
    }

    // **** return result ****
    return result;
  }

  // **** open scanner ****
  private static final Scanner scanner = new Scanner(System.in);

  /*
   * Test scaffolding.
   */
  public static void main(String[] args) throws IOException {

    // **** ****
    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(System.out));

    // **** read n and m ****
    String[] nm = scanner.nextLine().split(" ");
    int n = Integer.parseInt(nm[0]);
    // int m = Integer.parseInt(nm[1]);

    // **** read matrix ****
    String[] grid = new String[n];
    for (int i = 0; i < n; i++) {
      String gridItem = scanner.nextLine();
      grid[i] = gridItem;
    }

    // **** generate result ****
    int result = twoPluses(grid);

    // **** write result ****
    bufferedWriter.write(String.valueOf(result));
    bufferedWriter.newLine();

    // **** close buffered writter ****
    bufferedWriter.close();

    // **** close scanner ****
    scanner.close();
  }
}