import java.io.FileWriter;
import java.io.IOException;

public class Rover {

  public static void checking(String[][] map) {
    if (map[0][0].equals("X")) {
      try (FileWriter writer = new FileWriter("path-plan.txt", false)) {
        writer.write("Can not start a movement because start point is X");
        writer.flush();
      } catch (IOException ignored) {
        System.exit(0);
      }
      System.exit(0);
    }

    for (int i = 0; i < map.length; i++) {
      for (int j = 0; j < map[0].length; j++) {
        if (map[i][j].equals("X")) {}
      }
    }
  }

  public static void calculateRoverPath(String[][] map) {
    checking(map);
    int[][] adjacencyMatrix = createAdjacencyMatrix(recalculateMapInArray(map));
    int[] dimensions = new int[map.length * map[0].length];
    int[] unvisitedVertex = new int[map.length * map[0].length];
    int temp, minIndex, min;
    int INF = Integer.MAX_VALUE;
    int evency = 0;
    for (int i = 0; i < map.length * map[0].length; i++) {
      dimensions[i] = INF;
      unvisitedVertex[i] = 1;
    }
    dimensions[0] = 0; // start vertex
    do {
      minIndex = INF;
      min = INF;
      for (int i = 0; i < map.length * map[0].length; i++) {
        if ((unvisitedVertex[i] == 1) && (dimensions[i] < min)) {
          min = dimensions[i];
          minIndex = i;
        }
      }

      if (minIndex != INF) {
        for (int i = 0; i < map.length * map[0].length; i++) {
          if (adjacencyMatrix[minIndex][i] > 0) {
            /*if ((i - 1 >= 0 && adjacencyMatrix[minIndex][i - 1] > 0)
                || (i + 1 < adjacencyMatrix[0].length && adjacencyMatrix[minIndex][i + 1] > 0)) {
              temp = min + adjacencyMatrix[minIndex][i] + evency;
              if (temp < dimensions[i]) {
                dimensions[i] = temp;
                evency++;
                if (evency >= 2) {
                  evency = 0;
                }
              }
            } else {*/
            temp = min + adjacencyMatrix[minIndex][i];
            if (temp < dimensions[i]) {
              dimensions[i] = temp;
            }
            /*}*/
          }
        }
        unvisitedVertex[minIndex] = 0;
      }
    } while (minIndex < INF);

    int[] vertex = new int[map.length * map[0].length];
    int end = map.length * map[0].length - 1;
    vertex[0] = end + 1;
    int k = 1;
    int weight = dimensions[end];
    int weightTemp = dimensions[end];
    evency = 0;
    while (end != 0) {
      for (int i = 0; i < map.length * map[0].length; i++)
        if (adjacencyMatrix[i][end] != 0) {
          /*if ((end - 1 >= 0 && adjacencyMatrix[i][end - 1] > 0)
              || (end + 1 < adjacencyMatrix[0].length && adjacencyMatrix[i][end + 1] > 0)) {
            temp = weight - adjacencyMatrix[i][end] - evency;
            if (temp == dimensions[i]) {
              weight = temp;
              end = i;
              vertex[k] = i + 1;
              k++;
            }
          } else {*/
          temp = weight - adjacencyMatrix[i][end];
          if (temp == dimensions[i]) {
            weight = temp;
            end = i;
            vertex[k] = i + 1;
            k++;
            /*}*/
          }
        }
    }

    int[][] result = createMarksArray(recalculateMapInArray(map));
    String text = "";
    for (int i = k - 1; i >= 0; i--)
      if (i > 0)
        text = text + "[" + result[vertex[i] - 1][0] + "]" + "[" + result[vertex[i] - 1][1] + "]->";
      else
        text = text + "[" + result[vertex[i] - 1][0] + "]" + "[" + result[vertex[i] - 1][1] + "]";

    try (FileWriter writer = new FileWriter("path-plan.txt", false)) {
      writer.write(text + "\n" + "steps: " + (k - 1) + "\n" + "fuel: " + weightTemp);
      writer.flush();

    } catch (IOException ignored) {
      System.exit(0);
    }
  }

  public static int[][] recalculateMapInArray(String[][] map) {
    int[][] integerMap = new int[map.length][map[0].length];
    try {
      for (int i = 0; i < map.length; i++) {
        for (int j = 0; j < map[0].length; j++) {
          if (map[i][j].equals("X")) integerMap[i][j] = Integer.MAX_VALUE;
          else integerMap[i][j] = Integer.parseInt(map[i][j]);
        }
      }
    } catch (NumberFormatException ex) {
      try (FileWriter writer = new FileWriter("path-plan.txt", false)) {
        writer.write("Map number format error");
        writer.flush();
      } catch (IOException ignored) {
        System.exit(0);
      }
      System.exit(0);
    }

    return integerMap;
  }

  public static int[][] createAdjacencyMatrix(int[][] map) {

    int[][] adjacencyMatrix = new int[map.length * map[0].length][map.length * map[0].length];

    for (int i = 0; i < map.length; i++) {
      for (int j = 0; j < map[0].length; j++) {
        if (map[i][j] == Integer.MAX_VALUE) {

        } else {
          if (j + 1 < map[0].length)
            if (map[i][j + 1] == Integer.MAX_VALUE) {
              adjacencyMatrix[i * map[0].length + j][i * map[0].length + j + 1] = 0;
              adjacencyMatrix[i * map[0].length + j + 1][i * map[0].length + j] = 0;
            } else {
              adjacencyMatrix[i * map[0].length + j][i * map[0].length + j + 1] =
                      Math.abs(Math.abs(map[i][j]) - map[i][j + 1]) + 1;
              adjacencyMatrix[i * map[0].length + j + 1][i * map[0].length + j] =
                      Math.abs(Math.abs(map[i][j]) - map[i][j + 1]) + 1;
            }
          if (i + 1 < map.length)
            if (map[i + 1][j] == Integer.MAX_VALUE) {
              adjacencyMatrix[i * map[0].length + j][i * map[0].length + j + map[0].length] = 0;
              adjacencyMatrix[i * map[0].length + j + map[0].length][i * map[0].length + j] = 0;
            } else {
              adjacencyMatrix[i * map[0].length + j][i * map[0].length + j + map[0].length] =
                      Math.abs(Math.abs(map[i][j]) - map[i + 1][j]) + 1;
              adjacencyMatrix[i * map[0].length + j + map[0].length][i * map[0].length + j] =
                      Math.abs(Math.abs(map[i][j]) - map[i + 1][j]) + 1;
            }
          if (i + 1 < map.length && j + 1 < map[0].length)
            if (map[i + 1][j + 1] == Integer.MAX_VALUE) {
              adjacencyMatrix[i * map[0].length + j][i * map[0].length + j + map[0].length + 1] = 0;
              adjacencyMatrix[i * map[0].length + j + map[0].length + 1][i * map[0].length + j] = 0;
            } else {
              adjacencyMatrix[i * map[0].length + j][i * map[0].length + j + map[0].length + 1] =
                      Math.abs(Math.abs(map[i][j]) - map[i + 1][j + 1]) + 1;
              adjacencyMatrix[i * map[0].length + j + map[0].length + 1][i * map[0].length + j] =
                      Math.abs(Math.abs(map[i][j]) - map[i + 1][j + 1]) + 1;
            }
          if (j - 1 >= 0 && i + 1 < map.length)
            if (map[i + 1][j - 1] == Integer.MAX_VALUE) {
              adjacencyMatrix[i * map[0].length + j][i * map[0].length + j + map[0].length - 1] = 0;
              adjacencyMatrix[i * map[0].length + j + map[0].length - 1][i * map[0].length + j] = 0;
            } else {
              adjacencyMatrix[i * map[0].length + j][i * map[0].length + j + map[0].length - 1] =
                      Math.abs(Math.abs(map[i][j]) - map[i + 1][j - 1]) + 1;
              adjacencyMatrix[i * map[0].length + j + map[0].length - 1][i * map[0].length + j] =
                      Math.abs(Math.abs(map[i][j]) - map[i + 1][j - 1]) + 1;
            }
        }
      }
    }

    return adjacencyMatrix;
  }

  public static int[][] createMarksArray(int[][] map) {
    int[][] result = new int[map.length * map[0].length][2];
    int count = 0;
    for (int i = 0; i < map.length; i++) {
      for (int j = 0; j < map[0].length; j++) {
        result[count][0] = i;
        result[count][1] = j;
        count++;
      }
    }
    return result;
  }
}
