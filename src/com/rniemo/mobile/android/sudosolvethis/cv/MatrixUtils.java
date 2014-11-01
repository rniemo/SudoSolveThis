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
