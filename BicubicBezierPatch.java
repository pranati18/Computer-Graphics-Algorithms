import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import Jama.*;

public class BicubicBezierPatch {
	static String arr[][];
	static List<Double> Clistx = new ArrayList<Double>();
	static List<Double> Clisty = new ArrayList<Double>();
	static List<Double> Clistz = new ArrayList<Double>();
	static List<Double> listx = new ArrayList<Double>();
	static List<Double> listy = new ArrayList<Double>();
	static List<Double> listz = new ArrayList<Double>();
	
	
	static Matrix Qa;
	static Matrix Qb;
	static Matrix Qc;
	
	static List<Double> Slista = new ArrayList<Double>();
	static List<Double> Slistb = new ArrayList<Double>();
	static List<Double> Slistc = new ArrayList<Double>();
	static int check;
	static int check1=0;
	static int count;
	
	static int countbezier=0;

	public static int factorial(int n) {
		if (n < 1) {
			return 1;
		} else {
			return n * factorial(n - 1);
		}
	}
	public static double permutation(int i,int n){
		double denominator=((factorial(i))*(factorial((3-i))));
		double permutation=factorial(n)/denominator;
		return permutation;

	}



	public static void Bernstein(double diffu_num,double diffv_num,double listingx[][],double listingy[][],double listingz[][]){

		
		
		int k=3;//listingx.size()-1;
		//int n=3;
		double sumx=0;
		double sumy=0;
		double sumz=0;

		double u=0;
		double v=0;
		check=0;
		double diffu;
		double diffv;
		//int count=0;
		diffu= 1/(diffu_num-1);
		diffv= 1/(diffv_num-1);
		
		while(u<=1){
			v=0;
			while(v<=1){
				sumx=0;
				sumy=0;
				sumz=0;
				
			for(int a=0;a<=k;a++){
				for(int b=0;b<=k;b++){
				sumx=((listingx[a][b])*((permutation(a,k))*(Math.pow(1-u,k-a))*(Math.pow(u,a)))*((permutation(b,k))*(Math.pow(1-v,k-b))*(Math.pow(v,b))))+sumx ;
				sumy=((listingy[a][b])*((permutation(a,k))*(Math.pow(1-u,k-a))*(Math.pow(u,a)))*((permutation(b,k))*(Math.pow(1-v,k-b))*(Math.pow(v,b))))+sumy ;
				sumz=((listingz[a][b])*((permutation(a,k))*(Math.pow(1-u,k-a))*(Math.pow(u,a)))*((permutation(b,k))*(Math.pow(1-v,k-b))*(Math.pow(v,b))))+sumz ;
		
				}
				
			}
				
			listx.add(sumx);
			listy.add(sumy);
			listz.add(sumz);
			
		
			check++;
			
			
			v=v+diffv;
	
		}
			
			
			u=u+diffu;
		}
	}

	public static void smooth(double diffu_num,double diffv_num,double listingx[][],double listingy[][],double listingz[][]){
		 
		double diffu;
		double diffv;
		double u=0;
		double v=0;
		//int count=0;
		diffu= 1/(diffu_num-1);
		diffv= 1/(diffv_num-1);
		
		
	
		
		double M[][]= { {-1,3,-3, 1} , { 3,-6,3, 0},{-3,3,0,0},{1,0,0,0} };
	
		double  transposeM[][] =new double[4][4];
		for ( int c = 0 ; c < 4 ; c++ )
	      {
	         for ( int d = 0 ; d < 4 ; d++ )               
	            transposeM[d][c] = M[c][d];
	      }
		
		
		
		Matrix Mmat= new Matrix(M);
		Matrix MTmat= new Matrix(transposeM);
		
		Matrix Xmat= new Matrix(listingx);
		Matrix Ymat= new Matrix(listingy);
		Matrix Zmat= new Matrix(listingz);
		
		while(u<=1){
			v=0;
			while(v<=1){
		
				double S[][]={ {Math.pow(u,3)},{Math.pow(u,2)},{u}, {1} };
				double T[][]={ {(Math.pow(v,3)),(Math.pow(v,2)),v,1} };
				
				
				double DS[][]={ {3*Math.pow(u,2)},{(2*u)},{1}, {0} };//4x1
				double DT[][]={ {3*Math.pow(v,2),(2*v),1,0 }};//1x4
				
				
				Matrix Smat= new Matrix(S);
				Matrix Tmat=new Matrix(T);
				Matrix DSmat=new Matrix(DS);
				Matrix DTmat=new Matrix(DT);
				
				
				/*Matrix xsmat=Tmat.times(MTmat.times((Xmat).times(Mmat.times(DSmat))));
				Matrix ysmat=Tmat.times(MTmat.times((Ymat).times(Mmat.times(DSmat))));
				Matrix zsmat=Tmat.times(MTmat.times((Zmat).times(Mmat.times(DSmat))));
				
				
				Matrix xtmat=DTmat.times(MTmat.times(Xmat.times(Mmat.times(Smat))));
				Matrix ytmat=DTmat.times(MTmat.times(Ymat.times(Mmat.times(Smat))));
				Matrix ztmat=DTmat.times(MTmat.times(Zmat.times(Mmat.times(Smat))));*/
				
				Matrix xsmat=((((Tmat.times(MTmat)).times(Xmat)).times(Mmat)).times(DSmat));
				Matrix ysmat=((((Tmat.times(MTmat)).times(Ymat)).times(Mmat)).times(DSmat));
				Matrix zsmat=((((Tmat.times(MTmat)).times(Zmat)).times(Mmat)).times(DSmat));
				
				
				Matrix xtmat=((((DTmat.times(MTmat)).times(Xmat)).times(Mmat)).times(Smat));
				Matrix ytmat=((((DTmat.times(MTmat)).times(Ymat)).times(Mmat)).times(Smat));
				Matrix ztmat=((((DTmat.times(MTmat)).times(Zmat)).times(Mmat)).times(Smat));
				
				
				 Qa=(ysmat.times(ztmat)).minus((ytmat.times(zsmat)));
				 Qb=(zsmat.times(xtmat)).minus((ztmat.times(xsmat)));
				 Qc=(xsmat.times(ytmat)).minus((xtmat.times(ysmat)));		
				 
				 
				 Slista.add((Qa.get(0, 0)));    
				 Slistb.add((Qb.get(0, 0)));    
				 Slistc.add((Qc.get(0, 0)));    
			      
			    
				v=v+diffv;
				
			}
				
				u=u+diffu;
			}
		}
	
	
	
	public static void main(String[] args) throws FileNotFoundException{
		
		//using for now
		double radius=0.1;
		double du=(double)11;
		double dv=(double)11;
		File filename = new File("patchPoints.txt");
		int flat=1;
		int smooth=0;

		//command line parsing logic
		if(args.length>0)
		{        
			
			for(int i = 0; i < args.length; i++)
			{       
				
				if((args[i]).equals("-f")){
					filename = new File(args[i+1]);
				}
				else if((args[i]).equalsIgnoreCase("-u"))
					{
					du = Double.parseDouble(args[i+1]);
					}
				else if((args[i]).equalsIgnoreCase("-v"))
				{
					dv = Double.parseDouble(args[i+1]);
				}
				else if((args[i]).equalsIgnoreCase("-r")){
					radius = Double.parseDouble(args[i+1]);
				}
				else if((args[i]).equals("-F"))
				{
					flat=1;
				}
				else if((args[i]).equals("-S"))
				{
					smooth=1;
					flat=0;
				}
			}
		}
		
		
		
		
		/*Code for making flat shading default*/
		/*if(flat==1){
			System.out.println("Call flat function");
		}
		else if(smooth==1){
			System.out.println("Call smooth function");
		}
		else{
			System.out.println("Call flat function by default");
		}*/
		
		
		
		Scanner sc = new Scanner(filename);
		Scanner sc1 = new Scanner(filename);

		/*code to read in the file and store in in an array*/
		int i=0;
		count=0;
		while(sc1.hasNext()){
			sc1.nextLine();
			count++;
		}
		arr =new String[count][4];
		while(sc.hasNext())
		{
			String val=sc.nextLine();
			String str[]=val.split(" ");
			
			for(int c1=0,j=0;c1<str.length;c1++,j++)
			{
				arr[i][j]=str[j];
			}
			i++;
		}

		
		
		for(int i1=0;i1<16;i1++){
			Clistx.add(Double.parseDouble(arr[i1][0]));
			Clisty.add(Double.parseDouble(arr[i1][1]));
			Clistz.add(Double.parseDouble(arr[i1][2]));
		}
		
	
		
		double arrx[][]=new double[4][4];
		double arry[][]=new double[4][4];
		double arrz[][]=new double[4][4];
		
		int count1=0;
		int count2=0;
		int count3=0;
		
	for(int j1=0;j1<4;j1++){
		for(int i1=0;i1<4;i1++){
			arrx[j1][i1]=Clistx.get(count1);
			count1++;
			}
		}
	
	for(int j1=0;j1<4;j1++){
		for(int i1=0;i1<4;i1++){
			arry[j1][i1]=Clisty.get(count2);
			count2++;
			}
		}
	for(int j1=0;j1<4;j1++){
		for(int i1=0;i1<4;i1++){
			arrz[j1][i1]=Clistz.get(count3);
			count3++;
			}
		}
	
	
	
	
		Bernstein(du, dv, arrx, arry, arrz);
		smooth(du, dv, arrx, arry, arrz);
		
		if(flat==1){
			createscriptsys(du, radius);
		}
		else if(smooth==1){
			createscriptsyssmooth(du,dv, radius);
		}
		else{
			createscriptsys(du, radius);
		}
		
		sc.close();
		sc1.close();
		
	}
	
	public static void createscriptsys(double du,double radius){

		System.out.println("#Inventor V2.0 ascii");
		System.out.println("ShapeHints {");
		System.out.println("vertexOrdering        COUNTERCLOCKWISE");
		  
		System.out.println("}");
		

		System.out.println("Separator { \n"+"Coordinate3 { \n"+"\tpoint [ \n");

		for(int i=0;i<listx.size();i++){

			System.out.print("\t"+new DecimalFormat("#0.000000").format(listx.get(i))+" "+new DecimalFormat("#0.000000").format(listy.get(i))+" "
					+new DecimalFormat("#0.000000").format(listz.get(i))+",\n");


		}
		System.out.println("\t\t]\n"+"}");
		System.out.println("IndexedFaceSet {\n"+"coordIndex [");

		int temp=0;
		int change=0;
		int it;
	
		while(change<((((du*du)-1)-du)-1)){
		for(it=change;it<(((du+change)-1));it++){
			
			
			System.out.print("\t"+it+", "+(int)(it+(du))+", "+(it+1)+", -1,\n");
			System.out.print("\t"+(it+1)+", "+(int)(it+(du))+", "+(int)(it+(du)+1)+", -1,\n");
		
		}
		change=it+1;
		}
		
		System.out.print("  ]\n"+"}\n");
		
		for(int i=0;i<count;i++){
			System.out.print("Separator {\n"+"LightModel {\n"+"model PHONG\n"+"}\n"
					+"Material {\n"+"\tdiffuseColor 1.0 1.0 1.0\n"+"}\n"+"Transform {\n"+"\t translation ");
			System.out.print(arr[i][0]+" "+arr[i][1] +" "+arr[i][2]+"\n"+"}\n"+"Sphere {\n"+"\tradius "+radius+"\n"+
					"}\n"+"}");
		}
		System.out.println("\n\n"+"}");	
	}

	public static void createscriptsyssmooth(double du, double dv, double radius){

		System.out.println("#Inventor V2.0 ascii");
		System.out.println("ShapeHints {");
		System.out.println("vertexOrdering        COUNTERCLOCKWISE");
		  
		System.out.println("}");
		

		System.out.println("Separator { \n"+"Coordinate3 { \n"+"\tpoint [ \n");

		for(int i=0;i<listx.size();i++){

			System.out.print("\t\t"+new DecimalFormat("#0.000000").format(listx.get(i))+" "+new DecimalFormat("#0.000000").format(listy.get(i))+" "
					+new DecimalFormat("#0.000000").format(listz.get(i))+",\n");


		}
		System.out.println("\t\t]\n"+"}");
		System.out.println("IndexedFaceSet {\n"+"coordIndex [");

		int temp=0;
		int change=0;
		int it;
	
		while(change<((((du*du)-1)-du)-1)){
		for(it=change;it<(((du+change)-1));it++){
			
			
			System.out.print("\t"+it+", "+(int)(it+(du))+", "+(it+1)+", -1,\n");
			System.out.print("\t"+(it+1)+", "+(int)(it+(du))+", "+(int)(it+(du)+1)+", -1,\n");
		
		}
		change=it+1;
		}
		
		System.out.print("  ]\n"+"}\n");
		
		
		System.out.println("NormalBinding {");
		System.out.println("value        PER_VERTEX_INDEXED");	  
			System.out.println("}");

			 System.out.println("Normal {"); 
			   System.out.println("vector [");
		
			  for(int i=0;i<(du*dv);i++){
				  System.out.println("\t"+new DecimalFormat("#0.000000").format(Slista.get(i))+" "+new DecimalFormat("#0.000000").format(Slistb.get(i))+" "+new DecimalFormat("#0.000000").format(Slistc.get(i))+",");
			  }
				   
			   
			   System.out.println(" ]");
		System.out.println("}");
		
		
		for(int i=0;i<count;i++){
			System.out.print("Separator {\n"+"LightModel {\n"+"model PHONG\n"+"}\n"
					+"Material {\n"+"\tdiffuseColor 1.0 1.0 1.0\n"+"}\n"+"Transform {\n"+"\t translation ");
			System.out.print(arr[i][0]+" "+arr[i][1] +" "+arr[i][2]+"\n"+"}\n"+"Sphere {\n"+"\tradius "+radius+"\n"+
					"}\n"+"}");
		}
		System.out.println("\n\n"+"}");	
	}

	
}



