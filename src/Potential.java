import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Potential {
	public static boolean flag = true;
	public static boolean rollback = false;
	private static final int MIN_SIZE_MARKS = 3;
	private static ArrayList<Integer[]> optionsZeros = new ArrayList<>();

	public static void main(String[] args) {
		int[] reserves;
		int[] needs;
		int[][] costMatrix;
		int[][] trafficMatrix;

		reserves = new int[] { 1900, 1000, 1700 };
		needs = new int[] { 1300, 1700, 1600 };
		costMatrix = new int[][] {
				{ 3637, 3043, 4386 },
				{ 3793, 3165, 4711 },
				{ 4509, 3714, 5607 }
		};
//		reserves = new int[] { 10, 20, 30 };
//		needs = new int[] { 15, 20, 25 };
//		costMatrix = new int[][] {
//				{ 5, 3, 1 },
//				{ 3, 2, 4 },
//				{ 4, 1, 2 }
//		};
//		reserves = new int[] { 1300, 1700, 1600 };
//		needs = new int[] { 1300, 1700, 1600 };
//		costMatrix = new int[][] {
//				{ 3637, 3043, 4386 },
//				{ 3793, 3165, 4711 },
//				{ 4509, 3714, 5607 }
//		};
//		reserves = new int[] { 30, 25, 20, 5 };
//		needs = new int[] { 20, 15, 25, 20 };
//		costMatrix = new int[][] {
//				{ 4, 5, 3, 6 },
//				{ 7, 2, 1, 5 },
//				{ 6, 1, 4, 2 },
//				{ 5, 2, 7, 1 },
//		};
//		reserves = new int[] { 110, 190, 90 };
//		needs = new int[] { 80, 60, 170, 80 };
//		costMatrix = new int[][] {
//				{ 8, 1, 9, 7 },
//				{ 4, 6, 2, 12 },
//				{ 3, 5, 8, 9 }
//		};
//		reserves = new int[] { 80, 60, 100 };
//		needs = new int[] { 40, 60, 40, 50, 50 };
//		costMatrix = new int[][] {
//				{ 6, 4, 3, 4, 2 },
//				{ 3, 6, 4, 9, 2 },
//				{ 3, 1, 2, 2, 6 }
//		};

		trafficMatrix = NorthwestCorner.method(reserves, needs);
//		trafficMatrix = MinimalElement.method(reserves, needs, costMatrix);
		method(trafficMatrix, costMatrix);
	}

	private static boolean isDegenerate(int[][] trafficMatrix) {
		int count = 0;

		for (int i = 0; i < trafficMatrix.length; i++) {
			for (int j = 0; j < trafficMatrix[i].length; j++) {
				if (trafficMatrix[i][j] != -1)
					count++;
			}
		}
		return count != trafficMatrix.length + trafficMatrix[0].length - 1;
	}

	private static void correctDegenerate(int[][] trafficMatrix, ArrayList<Integer[]> marks) {
		if (optionsZeros.size() != 0) {
			for (int i = 0; i < optionsZeros.size(); i++) {
				trafficMatrix[optionsZeros.get(i)[0]][optionsZeros.get(i)[1]] = 0;
			}
			optionsZeros = new ArrayList<>();
		} else {
			for (int i = 0; i < trafficMatrix.length; i++) {
				for (int j = 0; j < trafficMatrix[i].length; j++) {
					if (trafficMatrix[i][j] == -1) {
						marks.add(new Integer[]{i, j});
						trafficMatrix[i][j] = -2;
						marks = createLoop(trafficMatrix, marks);
						flag = true;
						rollback = false;
						if (marks.size() == 0) {
							trafficMatrix[i][j] = 0;
							if (!isDegenerate(trafficMatrix))
								return;
							else {
								marks = new ArrayList<>();
								continue;
							}
						}
						trafficMatrix[i][j] = -1;
						marks = new ArrayList<>();
					}
				}
			}
		}
	}

	private static void searchUV(int[] u, int[] v, int[][] trafficMatrix, int[][] costMatrix) {
		int[] uMark = new int[u.length];
		int[] vMark = new int[v.length];

		uMark[0] = 1;
		while (uMark[0] != 1 || uMark[1] != 1 || uMark[2] != 1
				|| vMark[0] != 1 || vMark[1] != 1 || vMark[2] != 1) {
			for (int i = 0; i < trafficMatrix.length; i++) {
				for (int j = 0; j < trafficMatrix[i].length; j++) {
					if (trafficMatrix[i][j] == -1)
						continue;
					if (uMark[i] == 0 && vMark[j] == 1) {
						u[i] = costMatrix[i][j] - v[j];
						uMark[i] = 1;
					} else if (uMark[i] == 1 && vMark[j] == 0) {
						v[j] = costMatrix[i][j] - u[i];
						vMark[j] = 1;
					}
				}
			}
		}
		System.out.println("u: " + Arrays.toString(u));
		System.out.println("v: " + Arrays.toString(v));
	}

	private static void searchScores(int[][] trafficMatrix, int[][] costMatrix, int[][] scoresMatrix,
									 int[] u, int[] v) {
		for (int i = 0; i < trafficMatrix.length; i++) {
			for (int j = 0; j < trafficMatrix[i].length; j++) {
				if (trafficMatrix[i][j] == -1) {
					scoresMatrix[i][j] = costMatrix[i][j] - (u[i] + v[j]);
				}
			}
		}
		System.out.println("scoresMatrix:");
		printMatrix(scoresMatrix);
	}

	private static void searchIJMin(int[][] scoresMatrix, int[] ijMin) {
		int min = 999999999;

		for (int i = 0; i < scoresMatrix.length; i++) {
			for (int j = 0; j < scoresMatrix[i].length; j++) {
				if (scoresMatrix[i][j] < min) {
					min = scoresMatrix[i][j];
					ijMin[0] = i;
					ijMin[1] = j;
				}
			}
		}
	}

	public static int F(int[][] trafficMatrix, int [][] costMatrix) {
		int sum = 0;

		for (int i = 0; i < trafficMatrix.length; i++) {
			for (int j = 0; j < trafficMatrix[i].length; j++) {
				if (trafficMatrix[i][j] == -1)
					continue;
				sum += trafficMatrix[i][j] * costMatrix[i][j];
			}
		}
		return sum;
	}

	public static ArrayList<Integer[]> createLoop(int[][] trafficMatrix, ArrayList<Integer[]> marks) {
		marks = left(trafficMatrix, marks);
		if (!flag)
			return marks;
		marks = down(trafficMatrix, marks);
		if (!flag)
			return marks;
		marks = right(trafficMatrix, marks);
		if (!flag)
			return marks;
		marks = up(trafficMatrix, marks);
		return marks;
	}

	private static ArrayList<Integer[]> left(int[][] trafficMatrix, ArrayList<Integer[]> marks) {
		for (int j = marks.get(marks.size() - 1)[1] - 1; j > -1; j--) {
			if (trafficMatrix[marks.get(marks.size() - 1)[0]][j] != -1) {
				if (Objects.equals(marks.get(0)[0], marks.get(marks.size() - 1)[0])
						&& Objects.equals(marks.get(0)[1], j)
						&& marks.size() > MIN_SIZE_MARKS) {
					flag = false;
					rollback = false;
					return marks;
				}
				else {
					Integer[] newElement = new Integer[] { marks.get(marks.size() - 1)[0], j };
					if (!isOnMarks(marks, newElement)) {
						marks.add(new Integer[]{marks.get(marks.size() - 1)[0], j});
						marks = createLoop(trafficMatrix, marks);
						if (!flag)
							break;
					}
					else {
						break;
					}
				}
			}
		}
		return marks;
	}

	private static ArrayList<Integer[]> down(int[][] trafficMatrix, ArrayList<Integer[]> marks) {
		for (int i = marks.get(marks.size() - 1)[0] + 1; i < trafficMatrix.length; i++) {
			if (trafficMatrix[i][marks.get(marks.size() - 1)[1]] != -1) {
				if (Objects.equals(marks.get(0)[0], i)
						&& Objects.equals(marks.get(0)[1], marks.get(marks.size() - 1)[1])
						&& marks.size() > MIN_SIZE_MARKS) {
					flag = false;
					rollback = false;
					return marks;
				}
				else {
					Integer[] newElement = new Integer[] { i, marks.get(marks.size() - 1)[1] };
					if (!isOnMarks(marks, newElement)) {
						marks.add(new Integer[]{i, marks.get(marks.size() - 1)[1]});
						marks = createLoop(trafficMatrix, marks);
						if (!flag)
							break;
					}
					else {
						break;
					}
				}
			}
		}
		return marks;
	}

	private static ArrayList<Integer[]> right(int[][] trafficMatrix, ArrayList<Integer[]> marks) {
		for (int j = marks.get(marks.size() - 1)[1] + 1; j < trafficMatrix[0].length; j++) {
			if (trafficMatrix[marks.get(marks.size() - 1)[0]][j] != -1) {
				if (Objects.equals(marks.get(0)[0], marks.get(marks.size() - 1)[0])
						&& Objects.equals(marks.get(0)[1], j)
						&& marks.size() > MIN_SIZE_MARKS) {
					flag = false;
					rollback = false;
					return marks;
				}
				else {
					Integer[] newElement = new Integer[] { marks.get(marks.size() - 1)[0], j };
					if (!isOnMarks(marks, newElement)) {
						marks.add(new Integer[]{marks.get(marks.size() - 1)[0], j});
						marks = createLoop(trafficMatrix, marks);
						if (!flag)
							break;
					}
					else {
						break;
					}
				}
			}
		}
		return marks;
	}

	private static ArrayList<Integer[]> up(int[][] trafficMatrix, ArrayList<Integer[]> marks) {
		for (int i = marks.get(marks.size() - 1)[0] - 1; i > -1; i--) {
			if (trafficMatrix[i][marks.get(marks.size() - 1)[1]] != -1) {
				if (Objects.equals(marks.get(0)[0], i)
						&& Objects.equals(marks.get(0)[1], marks.get(marks.size() - 1)[1])
						&& marks.size() > MIN_SIZE_MARKS) {
					flag = false;
					rollback = false;
					return marks;
				}
				else {
					Integer[] newElement = new Integer[] { i, marks.get(marks.size() - 1)[1] };
					if (!isOnMarks(marks, newElement)) {
						marks.add(new Integer[]{i, marks.get(marks.size() - 1)[1]});
						marks = createLoop(trafficMatrix, marks);
						if (rollback)
							marks.remove(marks.size() - 1);
						return marks;
					}
					else {
						break;
					}
				}
			}
		}
		marks.remove(marks.size() - 1);
		rollback = true;
		return marks;
	}

	private static boolean isOnMarks(ArrayList<Integer[]> marks, Integer[] newElement) {
		for (int i = 0; i < marks.size(); i++) {
			if (Objects.equals(marks.get(i)[0], newElement[0])
					&& Objects.equals(marks.get(i)[1], newElement[1]))
				return true;
		}
		return false;
	}

	public static void processMarks(int[][] trafficMatrix, ArrayList<Integer[]> marks) {
		ArrayList<Integer> columns = new ArrayList<>();
		ArrayList<Integer> indexes = new ArrayList<>();
		int min = 999999;
		int max = -999999;
		int iMin = 0;
		int iMax = 0;

		for (int i = 0; i < trafficMatrix.length; i++) {
			for (int j = 0; j < marks.size(); j++) {
				if (marks.get(j)[0] == i) {
					columns.add(marks.get(j)[1]);
					indexes.add(j);
				}
			}
			if (columns.size() == 1) {
				if (indexes.get(0) != 0)
					marks.remove((int) indexes.get(0));
			}
			else if (columns.size() % 2 == 1) {
				for (int j = 0; j < columns.size(); j++) {
					if (columns.get(j) < min) {
						min = columns.get(j);
						iMin = j;
					}
					if (columns.get(j) > max) {
						max = columns.get(j);
						iMax = j;
					}
				}
				if (iMin < iMax) {
					columns.remove(iMax);
					columns.remove(iMin);
					indexes.remove(iMax);
					indexes.remove(iMin);
				}
				else {
					columns.remove(iMin);
					columns.remove(iMax);
					indexes.remove(iMin);
					indexes.remove(iMax);
				}
				for (int j = indexes.size() - 1; j > -1; j--) {
					if (indexes.get(j) != 0)
						marks.remove((int) indexes.get(j));
				}
			}
			columns = new ArrayList<>();
			indexes = new ArrayList<>();
			min = 999999;
			max = -999999;
			iMin = 0;
			iMax = 0;
		}
	}

	private static int searchMin(int[][] trafficMatrix, ArrayList<Integer[]> marks) {
		int min = 999999999;

		for (int i = 1; i < marks.size(); i += 2) {
			if (trafficMatrix[marks.get(i)[0]][marks.get(i)[1]] < min) {
				min = trafficMatrix[marks.get(i)[0]][marks.get(i)[1]];
			}
		}
		return min;
	}

	private static void optimizeTrafficMatrix(int[][] trafficMatrix, ArrayList<Integer[]> marks) {
		int min = searchMin(trafficMatrix, marks);
		trafficMatrix[marks.get(0)[0]][marks.get(0)[1]] = 0;

		for (int i = 0; i < marks.size(); i++) {
			if (i % 2 == 0) {
				if (trafficMatrix[marks.get(i)[0]][marks.get(i)[1]] == 0 && min == 0)
					optionsZeros.add(new Integer[] { marks.get(i)[0], marks.get(i)[1] });
				trafficMatrix[marks.get(i)[0]][marks.get(i)[1]] += min;
			}
			else
				trafficMatrix[marks.get(i)[0]][marks.get(i)[1]] -= min;
		}
		Functions.deleteZeros(trafficMatrix);
	}

	public static void method(int[][] trafficMatrix, int[][] costMatrix) {
		int[] u = new int[trafficMatrix.length];
		int[] v = new int[trafficMatrix[0].length];
		int[][] scoresMatrix = new int[trafficMatrix.length][trafficMatrix[0].length];
		int[] ijMin = new int[2];
		ArrayList<Integer[]> marks = new ArrayList<>();

		System.out.println("\n\ntrafficMatrix:");
		printMatrix(trafficMatrix);
		if (isDegenerate(trafficMatrix)) {
			System.out.println("Матрица вырожденная\nИдет исправление...");
			correctDegenerate(trafficMatrix, marks);
			marks = new ArrayList<>();
			System.out.println("trafficMatrix:");
			printMatrix(trafficMatrix);
		}
		System.out.println("Матрица невырожденная");
		searchUV(u, v, trafficMatrix, costMatrix);
		searchScores(trafficMatrix, costMatrix, scoresMatrix, u, v);
		searchIJMin(scoresMatrix, ijMin);
		System.out.println("Минимальная оценка: " + scoresMatrix[ijMin[0]][ijMin[1]]);
		System.out.println("Индексы минимальной оценки: " + ijMin[0] + " " + ijMin[1]);
		if (scoresMatrix[ijMin[0]][ijMin[1]] >= 0) {
			System.out.println("Найдено оптимальное решение");
			System.out.println("Общие затраты на доставку всей продукции составляют " +
					F(trafficMatrix, costMatrix) + " ден.ед.");
		}
		else {
			System.out.println("Найдено опорное решение");
			System.out.println("Общие затраты на доставку всей продукции составляют " +
					F(trafficMatrix, costMatrix) + " ден.ед.");
			marks.add(new Integer[] { ijMin[0], ijMin[1] });
			trafficMatrix[marks.get(0)[0]][marks.get(0)[1]] = -2;
			marks = createLoop(trafficMatrix, marks);
			flag = true;
			rollback = false;
			processMarks(trafficMatrix, marks);
			System.out.println("Найденный цикл:");
			printMarks(marks);
			optimizeTrafficMatrix(trafficMatrix, marks);
			method(trafficMatrix, costMatrix);
		}
	}

	public static void printMatrix(int [][] matrix) {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				System.out.printf("%4d\t", matrix[i][j]);
			}
			System.out.println();
		}
	}

	public static void printMarks(ArrayList<Integer[]> marks) {
		for (int i = 0; i < marks.size(); i++)
			System.out.println(Arrays.toString(marks.get(i)));
	}
}
