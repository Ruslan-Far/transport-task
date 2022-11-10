import java.util.Arrays;

public class MinimalElement {
	public static int[][] method(int[] reserves, int[] needs, int[][] costMatrix) {
		int[][] trafficMatrix = new int[reserves.length][needs.length];
		int[][] costMatrixDup = Functions.duplicate(costMatrix);
		int[] ij;

		Functions.initMatrix(trafficMatrix);
		while (reserves[0] != 0 || reserves[1] != 0 || reserves[2] != 0
					|| needs[0] != 0 || needs[1] != 0 || needs[2] != 0) {
			ij = Functions.searchIJMin(costMatrixDup);
			trafficMatrix[ij[0]][ij[1]] = Math.min(reserves[ij[0]], needs[ij[1]]);
			if (trafficMatrix[ij[0]][ij[1]] == reserves[ij[0]]) {
				needs[ij[1]] = needs[ij[1]] - reserves[ij[0]];
				reserves[ij[0]] = 0;
			}
			else {
				reserves[ij[0]] = reserves[ij[0]] - needs[ij[1]];
				needs[ij[1]] = 0;
			}
		}
		System.out.println("reserves: " + Arrays.toString(reserves));
		System.out.println("needs: " + Arrays.toString(needs));
		Functions.deleteZeros(trafficMatrix);
		return trafficMatrix;
	}
}
