import java.util.ArrayList;

public class Test {
	public static boolean flag = true;
	public static boolean rollback = false;

	public static void main(String[] args) {
		ArrayList<Integer[]> marks = new ArrayList<>();
//		int[][] trafficMatrix = new int[][] {
//				{ -1, 400, 130, 80 },
//				{ 13, -1, -1, -1 },
//				{ -1, -1, 300, 500 },
//				{ 188, 13, 300, 90 }
//		};
//		int[][] trafficMatrix = new int[][] {
//				{ -1, 400, 130, 80, 90 },
//				{ 13, -1, -1, -1, 90 },
//				{ -1, -1, 300, 500, 90 },
//				{ 188, 13, 300, 90, 90 }
//		};
//		int[][] trafficMatrix = new int[][] {
//				{ 300, -1, 1600, },
//				{ 1000, -1, -1, },
//				{ -1, 1700, -1, }
//		};
		int[][] trafficMatrix = new int[][] {
				{ 40, 58, -1, 0, -1 },
				{ -1, 2, 40, -1, -1 },
				{ -1, -1, -1, 68, 32 }
		};

		marks.add(new Integer[] { 1, 4 });
		trafficMatrix[marks.get(0)[0]][marks.get(0)[1]] = -2;
		marks = Potential.createLoop(trafficMatrix, marks);
		flag = true;
		rollback = false;
		Potential.printMarks(marks);
		Potential.processMarks(trafficMatrix, marks);
		System.out.println("Найденный цикл:");
		Potential.printMarks(marks);
		System.out.println(marks.size());
	}
}
