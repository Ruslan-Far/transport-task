import java.util.Arrays;

public class NorthwestCorner {
	public static int[][] method(int[] reserves, int[] needs) {
		int[][] trafficMatrix = new int[reserves.length][needs.length];

		Functions.initMatrix(trafficMatrix);
		for (int i = 0, j = 0; i != trafficMatrix.length && j != trafficMatrix[i].length;) {
			trafficMatrix[i][j] = Math.min(reserves[i], needs[j]);
			if (trafficMatrix[i][j] == reserves[i]) {
				needs[j] = needs[j] - reserves[i];
				reserves[i] = 0;
				i++;
			}
			else {
				reserves[i] = reserves[i] - needs[j];
				needs[j] = 0;
				j++;
			}
		}
		System.out.println("reserves: " + Arrays.toString(reserves));
		System.out.println("needs: " + Arrays.toString(needs));
		Functions.deleteZeros(trafficMatrix);
		return trafficMatrix;
	}
}
