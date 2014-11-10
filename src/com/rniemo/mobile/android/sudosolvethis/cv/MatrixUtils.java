package com.rniemo.mobile.android.sudosolvethis.cv;

public class MatrixUtils {

	public static double[][] transpose(double[][] mat){
        double[][] temp = new double[mat[0].length][mat.length];
        for (int i = 0; i < mat.length; i++){
            for (int j = 0; j < mat[0].length; j++){
                temp[j][i] = mat[i][j];
            }
        }
        return temp;
	}
	
    public static double[][] mult(double[][] m, double[][] n) {
        int mrows = m.length;
        int mcols = m[0].length;
        int ncols = m[0].length;
        double[][] ret = new double[mrows][ncols];
        for (int i = 0; i < mrows; i++){
            for (int j = 0; j < ncols; j++){
                for (int k = 0; k < mcols; k++){
                    ret[i][j] += m[i][k] * n[k][j];
                }
            }
        }
        return ret;
    }
	public static void printMatrix(double[][] mat){
		double sum = 0;
		for(int x = 0; x < mat.length; x++){
			for(int y = 0; y < mat.length; y++){
				System.out.print(mat[x][y] + " ");
				sum += mat[x][y];
			}
			System.out.println();
		}
		System.out.println("sum: " + sum);
	}
	
}
