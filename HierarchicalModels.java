import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import Jama.Matrix;

public class HierarchicalModels {
	static List<Double> listx = new ArrayList<Double>();
	static List<Double> listy = new ArrayList<Double>();
	static List<Double> listz = new ArrayList<Double>();

public static void main(String[] args) throws FileNotFoundException{
		
	
	//θ1 = -51°,   θ2 = 39°,   θ3 = 65°,   L1 = 4,    L2 = 3,    L3 = 2.5   
		//using for now
		double theta1=(double)-51;
		double theta2=(double)39;
		double theta3=(double)65;
		double L1=(double)4;
		double L2=(double)3;
		double L3=(double)2.5;
		
		
		//command line parsing logic
		if(args.length>0)
		{        
			
			for(int i = 0; i < args.length; i++)
			{       
				if((args[i]).equalsIgnoreCase("-t")){
					theta1 = Double.parseDouble(args[i+1]);
					}
				else if((args[i]).equalsIgnoreCase("-u")){
					theta2 = Double.parseDouble(args[i+1]);
				}
				else if((args[i]).equalsIgnoreCase("-v")){
					theta3 = Double.parseDouble(args[i+1]);
				}
				else if((args[i]).equalsIgnoreCase("-l")){
					L1 = Double.parseDouble(args[i+1]);
				}
				else if((args[i]).equals("-m")){
					L2 = Double.parseDouble(args[i+1]);
				}
				else if((args[i]).equals("-n")){
					L3 = Double.parseDouble(args[i+1]);
				}
				
			}
		}
		
		
		
		// convert them in radians
		theta1 = Math.toRadians(theta1);
		theta2 = Math.toRadians(theta2);
		theta3 = Math.toRadians(theta3);
		
		
		 
		 
		
		 
		
		
		 
		
		
		double[][] coordind={{ 0, 1, 2, 0, -1},{ 0, 2, 3, 0, -1},{ 7, 6, 5, 7, -1},{7, 5, 4, 7, -1},{0, 3, 7, 0, -1},{ 0, 7, 4, 0, -1},{1, 5, 6, 1, -1},{ 1, 6, 2, 1, -1},{ 0, 4, 5, 0, -1},{0, 5, 1, 0, -1},{ 3, 2, 6, 3, -1},{ 3, 6, 7, 3, -1}};
		
		double[][] P0 = {{2.000000,2.000000 ,1.000000},{-2.000000,2.000000,1.000000},{-2.000000,-2.000000,1.000000},{2.000000,-2.000000,1.000000}
	     ,{2.000000,2.000000 ,0.000000},{-2.000000,2.000000,0.000000},{-2.000000,-2.000000,0.000000},{2.000000,-2.000000,0.000000}};
		
		double TzL0[][]= { {1,0,0, 0} , { 0,1,0, 0}, {0,0,1,1},{0,0,0,1} };
		double Rzth1[][]= { {Math.cos(theta1),-Math.sin(theta1),0, 0},{ Math.sin(theta1),Math.cos(theta1),0, 0},{0,0,1,0},{0,0,0,1} };
		Matrix TzL0Mat= new Matrix(TzL0);
		Matrix Rzth1Mat= new Matrix(Rzth1);
		Matrix M1mat=TzL0Mat.times(Rzth1Mat);
//		double[][] M1matmat = M1mat.getArray();
//		
//		
//		for(int i = 0; i < M1matmat.length; i++) {
//		    for(int j = 0; j < M1matmat[i].length; j++) {        
//	        System.out.print( " "+i+" "+j+" :"+" " + M1matmat[i][j] );
//		    }
//		}
		
		double P1[][]={ {0.5, 0.5, L1, 1},{-0.5, 0.5, L1, 1},{-0.5, -0.5, L1, 1} , {0.5, -0.5, L1, 1},{0.5, 0.5, 0, 1},{-0.5, 0.5, 0, 1},{-0.5, -0.5, 0, 1},{0.5, -0.5, 0, 1} };
		Matrix P1Mat= new Matrix(P1);
		
		
		//Apply transformation matrix M to first link  model (P1): P1’ = M*P1
		
		Matrix ModelP1=(M1mat.times(P1Mat.transpose())).transpose();
		
		
		//M = M(Tz(L1)Ry(θ2))
		
		double TzL1[][]= { {1,0,0, 0} , { 0,1,0, 0},{0,0,1,L1},{0,0,0,1} };
		double Ryth2[][]= { {Math.cos(theta2),0,Math.sin(theta2),0},{0,1,0,0},{ -Math.sin(theta2),0,Math.cos(theta2), 0},{0,0,0,1} };
		Matrix TzL1Mat= new Matrix(TzL1);
		Matrix Ryth2Mat= new Matrix(Ryth2);
		Matrix M2mat=M1mat.times(TzL1Mat.times(Ryth2Mat));

		//P2’ = MP2
		
		double P2[][]={ {0.5, 0.5, L2, 1},{-0.5, 0.5, L2, 1},{-0.5, -0.5, L2, 1} , {0.5, -0.5, L2, 1},{0.5, 0.5, 0, 1},{-0.5, 0.5, 0, 1},{-0.5, -0.5, 0, 1},{0.5, -0.5, 0, 1} };
		Matrix P2Mat= new Matrix(P2);
		
		Matrix ModelP2=(M2mat.times(P2Mat.transpose())).transpose();
		
		//M = M(Tz(L2)Ry(θ3))
		
		
		double TzL2[][]= { {1,0,0, 0} , { 0,1,0, 0},{0,0,1,L2},{0,0,0,1} };
		double Ryth3[][]= { {Math.cos(theta3),0,Math.sin(theta3),0},{0,1,0,0},{ -Math.sin(theta3),0,Math.cos(theta3), 0},{0,0,0,1} };
		Matrix TzL2Mat= new Matrix(TzL2);
		Matrix Ryth3Mat= new Matrix(Ryth3);
		Matrix M3mat=M2mat.times(TzL2Mat.times(Ryth3Mat));
		
		//P3’ = MP3
		double P3[][]={ {0.5, 0.5, L3, 1},{-0.5, 0.5, L3, 1},{-0.5, -0.5, L3, 1} , {0.5, -0.5, L3, 1},{0.5, 0.5, 0, 1},{-0.5, 0.5, 0, 1},{-0.5, -0.5, 0, 1},{0.5, -0.5, 0, 1} };		
		Matrix P3Mat= new Matrix(P3);
		
		Matrix ModelP3=(M3mat.times(P3Mat.transpose())).transpose();
		
		//M = MTz(L3) Extract translation vector from M as the position for drawing sphere at end of arm 42
		
		double TzL3[][]= { {1,0,0, 0} , { 0,1,0, 0},{0,0,1,L3},{0,0,0,1} };
		Matrix TzL3Mat= new Matrix(TzL3);
		Matrix M4mat=M3mat.times(TzL3Mat);
		
		double[][] M4 = M4mat.getArray();
		
		double[][] modelP1 = ModelP1.getArray();
		double[][] modelP2 = ModelP2.getArray();
		double[][] modelP3 = ModelP3.getArray();
		
		
	
	/*for(int i = 0; i < modelP1.length; i++) {
		    for(int j = 0; j < modelP1[i].length; j++) {        
	        System.out.print( " "+i+" "+j+" :"+" " + modelP1[i][j] );
		    }
		}
		
	System.out.println("");
	for(int i = 0; i < modelP2.length; i++) {
		    for(int j = 0; j < modelP2[i].length; j++) {        
		        System.out.print( " " + modelP2[i][j] );
		    }
		}
	
	System.out.println("");
	for(int i = 0; i < modelP3.length; i++) {
		    for(int j = 0; j < modelP3[i].length; j++) {        
		        System.out.print( " "+i+" "+j+" :"+ modelP3[i][j] );
		    }
		}
		
		System.out.println("ModelP1:"+ModelP1 );
		System.out.println("ModelP2:"+ModelP2 );
		System.out.println("ModelP3:"+ModelP3 );
		
		System.out.println("@@@@@:"+modelP1.length );
		
		System.out.println("");
		for(int i = 0; i < M4.length; i++) {
			    for(int j = 0; j < M4[i].length; j++) {        
			        System.out.print( " " + M4[i][j] );
			    }
			}*/
		
		createscriptsys(P0, modelP1, modelP2, modelP3, coordind,M4);
}

public static void createscriptsys(double P0[][],double modelP1[][],double modelP2[][],double modelP3[][],double coordind[][],double[][] M4){

	System.out.println("#Inventor V2.0 ascii");
	System.out.println("Separator { \n"+"Coordinate3 { \n"+"\tpoint [ \n");

	for(int i=0;i<P0.length;i++){

		System.out.print("\t"+new DecimalFormat("#0.000000").format(P0[i][0])+" "+new DecimalFormat("#0.000000").format(P0[i][1])+" "
				+new DecimalFormat("#0.000000").format(P0[i][2])+",\n");
		

	}
	
	System.out.println("\t\t]\n"+"}");
	System.out.println("IndexedLineSet {\n"+"coordIndex [");

	for(int i=0;i<coordind.length;i++){

		System.out.print("\t"+(int)coordind[i][0]+", "+(int)coordind[i][1]+", "
				+(int)coordind[i][2]+", "+(int)coordind[i][3]+",\n");
		

	}
	
	System.out.print("  ]\n"+"}\n");
	
	System.out.println("\n\n"+"}");	
	
	System.out.println("Separator { \n"+"Coordinate3 { \n"+"\tpoint [ \n");
	
	for(int i=0;i<modelP1.length;i++){

		System.out.print("\t"+new DecimalFormat("#0.000000").format(modelP1[i][0])+" "+new DecimalFormat("#0.000000").format(modelP1[i][1])+" "
				+new DecimalFormat("#0.000000").format(modelP1[i][2])+",\n");
		

	}
	
	System.out.println("\t\t]\n"+"}");
	
	
	System.out.println("IndexedLineSet {\n"+"coordIndex [");

	for(int i=0;i<coordind.length;i++){

		System.out.print("\t"+(int)coordind[i][0]+", "+(int)coordind[i][1]+", "
				+(int)coordind[i][2]+", "+(int)coordind[i][3]+",\n");
		

	}
	
System.out.print("  ]\n"+"}\n");
	
	System.out.println("\n\n"+"}");	
	
	
	
	
System.out.println("Separator { \n"+"Coordinate3 { \n"+"\tpoint [ \n");
	
	for(int i=0;i<modelP2.length;i++){

		System.out.print("\t"+new DecimalFormat("#0.000000").format(modelP2[i][0])+" "+new DecimalFormat("#0.000000").format(modelP2[i][1])+" "
				+new DecimalFormat("#0.000000").format(modelP2[i][2])+",\n");
		

	}
	
	
	System.out.println("\t\t]\n"+"}");
	
	
	System.out.println("IndexedLineSet {\n"+"coordIndex [");

	for(int i=0;i<coordind.length;i++){

		System.out.print("\t"+(int)coordind[i][0]+", "+(int)coordind[i][1]+", "
				+(int)coordind[i][2]+", "+(int)coordind[i][3]+",\n");
		

	}
	
System.out.print("  ]\n"+"}\n");
	
	System.out.println("\n\n"+"}");	
	
	
	
	
System.out.println("Separator { \n"+"Coordinate3 { \n"+"\tpoint [ \n");
	
	for(int i=0;i<modelP3.length;i++){

		System.out.print("\t"+new DecimalFormat("#0.000000").format(modelP3[i][0])+" "+new DecimalFormat("#0.000000").format(modelP3[i][1])+" "
				+new DecimalFormat("#0.000000").format(modelP3[i][2])+",\n");
		

	}
	
	System.out.println("\t\t]\n"+"}");
	
	
	System.out.println("IndexedLineSet {\n"+"coordIndex [");

	for(int i=0;i<coordind.length;i++){

		System.out.print("\t"+(int)coordind[i][0]+", "+(int)coordind[i][1]+", "
				+(int)coordind[i][2]+", "+(int)coordind[i][3]+",\n");
		

	}
	
System.out.print("  ]\n"+"}\n");
	
	System.out.println("\n\n"+"}");	
	
	
	
	
	System.out.println("Separator {");
	System.out.println("LightModel {");
	System.out.println("model PHONG");
	System.out.println("	}");
	System.out.println("	Material {");
	System.out.println("    diffuseColor 1.0 1.0 1.0 ");
	System.out.println("}");
	System.out.println("Sphere {");
	System.out.println("  radius  0.20 ");
	System.out.println("}");
	System.out.println("	}");
	
	System.out.println("Separator {");
	System.out.println("LightModel {");
	System.out.println("model PHONG");
	System.out.println("	}");
	System.out.println("	Material {");
	System.out.println("    diffuseColor 1.0 1.0 1.0 ");
	System.out.println("}");
	
	System.out.println("Transform {");
			System.out.println("	translation "+new DecimalFormat("#0.000000").format(M4[0][3])+" "+new DecimalFormat("#0.000000").format(M4[1][3])+" "+new DecimalFormat("#0.000000").format(M4[2][3]));
					System.out.println("}");
	System.out.println("Sphere {");
	System.out.println("  radius  0.20 ");
	System.out.println("}");
	System.out.println("	}");
	


}





}
