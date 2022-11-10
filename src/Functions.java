import java.util.Arrays;

public class Functions {
	public static void initMatrix(int[][] a) {
		for (int i = 0; i < a.length; i++)
			Arrays.fill(a[i], -1);
	}

	public static void deleteZeros(int[][] a) {
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[i].length; j++) {
				if (a[i][j] == 0)
					a[i][j] = -1;
			}
		}
	}

	public static int[][] duplicate(int[][] a) {
		int[][] aCopy = new int[a.length][a[0].length];

		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[i].length; j++) {
				aCopy[i][j] = a[i][j];
			}
		}
		return aCopy;
	}

	public static int[] searchIJMin(int[][] a) {
		int min = 999999999;
		int[] ij = new int[2];

		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[i].length; j++) {
				if (a[i][j] != -1 && a[i][j] < min) {
					min = a[i][j];
					ij[0] = i;
					ij[1] = j;
				}
			}
		}
		a[ij[0]][ij[1]] = -1;
		return ij;
	}
}
