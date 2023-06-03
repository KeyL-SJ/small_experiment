package main;

import java.util.ArrayList;
import java.util.List;

public class Data {
	public static List datas = new ArrayList();

	static void init() {
		int[][] data1 = {{-1, 0}, {0, 0}, {1, 0}, {1, 1}};
		datas.add(data1);

		int[][] data2 = {{-1, 0}, {0, 0}, {1, 0}, {2, 0}};
		datas.add(data2);

		int[][] data3 = {{-1, 0}, {-1, 1}, {0, 0}, {1, 0}};
		datas.add(data3);

		int[][] data4 = {{-1, 0}, {0, 0}, {0, 1}, {1, 1}};
		datas.add(data4);

		int[][] data5 = {{0, 0}, {0, 1}, {1, 0}, {1, 1}};
		datas.add(data5);

		int[][] data6 = {{-1, 1}, {0, 0}, {0, 1}, {1, 0}};
		datas.add(data6);

		int[][] data7 = {{-1, 0}, {0, 0}, {0, 1}, {1, 0}};
		datas.add(data7);
	}
}
