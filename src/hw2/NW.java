package hw2;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Random;

public class NW {
	public static void main(String[] args) {
		Random rnd = new Random();
		float[] ans = new float[1000];
		for (int n = 1; n < 1001; n++) {
			System.out.println("n = " + n);
			int[][] ST = new int[n + 1][n + 1];
			for (int m = 0; m < 100; m++) {
				int[] a = new int[n];
				int[] b = new int[n];
				for (int i = 0; i < n; i++) {
					a[i] = rnd.nextInt(4);
					b[i] = rnd.nextInt(4);
				}
				for (int i = 1; i < n + 1; i++) {
					for (int j = 1; j < n + 1; j++) {
						ST[i][j] = Math.max(
								ST[i - 1][j - 1]
										+ 1
										- Math.abs((int) Math.signum(a[i - 1]
												- b[j - 1])),
								Math.max(ST[i - 1][j], ST[i][j - 1]));
					}
				}
				ans[n - 1] += ST[n][n];
			}
			ans[n - 1] /= 100;
		}
		try {
			PrintWriter writer = new PrintWriter("ansPS2-1.txt", "UTF-8");
			for (int n = 0; n < 1000; n++) {
				writer.println(ans[n]);
			}
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
