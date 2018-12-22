package com.util;

import java.util.ArrayList;
import java.util.HashSet;

import net.sf.json.JSONArray;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

public class Test {

	private static Log logger = LogFactory.getLog(Test.class);
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		// Create a real matrix with two rows and three columns, using a factory
		// method that selects the implementation class for us.
		double[][] matrixData = { {1d,2d,3d}, {2d,5d,3d}};
		RealMatrix m = MatrixUtils.createRealMatrix(matrixData);

		// One more with three rows, two columns, this time instantiating the
		// RealMatrix implementation class directly.
		double[][] matrixData2 = { {1d,2d}, {2d,5d}, {1d, 7d}};
		RealMatrix n = new Array2DRowRealMatrix(matrixData2);

		// Note: The constructor copies  the input double[][] array in both cases.

		// Now multiply m by n
		RealMatrix p = m.multiply(n);

		// Invert p, using LU decomposition
		RealMatrix pInverse = new LUDecomposition(p).getSolver().getInverse();
	
		System.out.println(m.toString());
		System.out.println(n.toString());
		System.out.println(p.toString());
		System.out.println(pInverse.toString());
		System.out.println(p.multiply(pInverse).toString());
		System.out.println(pInverse.multiply(p).toString());
		
		// csu test 1
		logger.info("csu test 1");
		logger.info(p.equals(p.copy()));
		
		// csu test 2
		logger.info("csu test 2");
		HashSet<Long> data = new HashSet<Long>();
		data.add(new Long(1));
		logger.info(data.contains(new Long(1)));
		logger.info(data.contains(new Long(100)));
		logger.info(data.contains(Long.valueOf(1)));
		
		// csu test 3
		logger.info("csu test 3");
		logger.info("value: " + JSONArray.fromObject(new ArrayList<String>()));
		
	}

}
