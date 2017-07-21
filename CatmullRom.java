import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class CatmullRom {
	static String arr[][];
	static List<Double> Clistx = new ArrayList<Double>();
	static List<Double> Clisty = new ArrayList<Double>();
	static List<Double> Clistz = new ArrayList<Double>();
	static List<Double> listx = new ArrayList<Double>();
	static List<Double> listy = new ArrayList<Double>();
	static List<Double> listz = new ArrayList<Double>();
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
	public static double permutation(int i){
		double denominator=((factorial(i))*(factorial((3-i))));
		double permutation=factorial(3)/denominator;
		return permutation;

	}



	public static void Bezier(double diffu,List<Double> listingx,List<Double> listingy,List<Double> listingz){

		
		
		int k=3;//listingx.size()-1;
		double sumx=0;
		double sumy=0;
		double sumz=0;

		double u=0;
		check=0;

		while(u<=1){
			
			for(int a=0;a<=k;a++){
				sumx=(listingx.get(a))*(permutation(a))*(Math.pow(1-u,k-a))*(Math.pow(u,a))+sumx ;
				sumy=(listingy.get(a))*(permutation(a))*(Math.pow(1-u,k-a))*(Math.pow(u,a))+sumy ;
				sumz=(listingz.get(a))*(permutation(a))*(Math.pow(1-u,k-a))*(Math.pow(u,a))+sumz ;
			}
			
			listx.add(sumx);
			listy.add(sumy);
			listz.add(sumz);
			sumx=0;
			sumy=0;
			sumz=0;
			check++;
			
			u=u+diffu;

		}
	}


	public static void main(String[] args) throws FileNotFoundException{
		
		try
		{	
			//using for now
			double radius=0.1;
			double du=(double)0.09;
			File filename = new File("cpts_in.txt");
			

			//command line parsing logic
			if(args.length>0)
			{        
				
				for(int i = 0; i < args.length; i++)
				{       
					
					if((args[i]).equalsIgnoreCase("-f")){
						filename = new File(args[i+1]);
					}
					else if((args[i]).equalsIgnoreCase("-u"))
						{
						du = Double.parseDouble(args[i+1]);
						}
					else if((args[i]).equalsIgnoreCase("-r")){
						radius = Double.parseDouble(args[i+1]);
					}
				}
			}

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

				for(int count1=0,j=0;count1<str.length;count1++,j++)
				{
					arr[i][j]=str[j];
				}
				i++;
			}

			/*adding first point*/
			Clistx.add(Double.parseDouble(arr[2][0]));
			Clisty.add(Double.parseDouble(arr[2][1]));
			Clistz.add(Double.parseDouble(arr[2][2]));

			double Cx=0;
			double Cy=0;
			double Cz=0;
			double x=(double)1/3;

			/*calculating control point for the first tangent*/

			Cx=Double.parseDouble(arr[2][0])+x*Double.parseDouble(arr[0][0]);
			Cy=Double.parseDouble(arr[2][1])+x*Double.parseDouble(arr[0][1]);
			Cz=Double.parseDouble(arr[2][2])+x*Double.parseDouble(arr[0][2]);
			
			Clistx.add(Cx);
			Clisty.add(Cy);
			Clistz.add(Cz);

			/*if there are more than 2 points*/
			if(count>4){
				double h=0.5;
				int counttan=0;

				//calculating tangents
				double tanvx;
				double tanvy;
				double tanvz;
				List<Double> tanx = new ArrayList<Double>();
				List<Double> tany = new ArrayList<Double>();
				List<Double> tanz = new ArrayList<Double>();

				for(int k=3;k<count-1;k++){
					tanvx=h*(Double.parseDouble(arr[k+1][0])-Double.parseDouble(arr[k-1][0]));
					tanvy=h*(Double.parseDouble(arr[k+1][1])-Double.parseDouble(arr[k-1][1]));
					tanvz=h*(Double.parseDouble(arr[k+1][2])-Double.parseDouble(arr[k-1][2]));


					tanx.add(tanvx);
					tany.add(tanvy);
					tanz.add(tanvz);
					counttan++;


				}
				//done calculating tangents



				//calculating the + and - Control points
				for(int k=3,j=0;k<=count-2;k++,j++){
					//  for(int j=0;j<=tanx.size()-1;j++){

					Cx=Double.parseDouble(arr[k][0])-x*tanx.get(j);
					Cy=Double.parseDouble(arr[k][1])-x*tany.get(j);
					Cz=Double.parseDouble(arr[k][2])-x*tanz.get(j);
					Clistx.add(Cx);
					Clisty.add(Cy);
					Clistz.add(Cz);
					
					

					//adding the points after negative control points
					Clistx.add(Double.parseDouble(arr[k][0]));
					Clisty.add(Double.parseDouble(arr[k][1]));
					Clistz.add(Double.parseDouble(arr[k][2]));

					//adding the points before positive control points
					Clistx.add(Double.parseDouble(arr[k][0]));
					Clisty.add(Double.parseDouble(arr[k][1]));
					Clistz.add(Double.parseDouble(arr[k][2]));


					Cx=Double.parseDouble(arr[k][0])+x*tanx.get(j);
					Cy=Double.parseDouble(arr[k][1])+x*tany.get(j);
					Cz=Double.parseDouble(arr[k][2])+x*tanz.get(j);
					Clistx.add(Cx);
					Clisty.add(Cy);
					Clistz.add(Cz);

					
				}
			}




			/*calculating control point for the last tangent*/

			Cx=Double.parseDouble(arr[count-1][0])-x*Double.parseDouble(arr[1][0]);
			Cy=Double.parseDouble(arr[count-1][1])-x*Double.parseDouble(arr[1][1]);
			Cz=Double.parseDouble(arr[count-1][2])-x*Double.parseDouble(arr[1][2]);
			

			Clistx.add(Cx);
			Clisty.add(Cy);
			Clistz.add(Cz);


			//adding the last points to the list
			Clistx.add(Double.parseDouble(arr[count-1][0]));
			Clisty.add(Double.parseDouble(arr[count-1][1]));
			Clistz.add(Double.parseDouble(arr[count-1][2]));


			/*Parsing Clist into fours into another list*/
		
			for(int b=0;b<=Clistx.size()-4;b=b+4)
			{
				 List<Double> Blistx = new ArrayList<Double>();
				 List<Double> Blisty = new ArrayList<Double>();
				 List<Double> Blistz = new ArrayList<Double>();
				Blistx=Clistx.subList(b,b+4);
				Blisty=Clisty.subList(b,b+4);
				Blistz=Clistz.subList(b,b+4);
				
				Bezier(du, Blistx, Blisty, Blistz);
				countbezier++;
			}

		

			listx.add(Double.parseDouble(arr[count-1][0]));
			listy.add(Double.parseDouble(arr[count-1][1]));
			listz.add(Double.parseDouble(arr[count-1][2]));


			createscriptsys(du,radius);




		}
		catch (Exception e){
			e.printStackTrace();
		}




	}

	public static void createscriptsys(double du,double radius){



		System.out.println("Separator { \n"+"LightModel { \n"+" model BASE_COLOR\n"+" } \n"+
				"Material {\n"+"diffuseColor 1.0 1.0 0.2\n"+" }\n"+"Coordinate3 { \n"+"\tpoint [ \n");

		for(int i=0;i<listx.size();i++){

			System.out.print("\t\t"+new DecimalFormat("#0.000000").format(listx.get(i))+" "+new DecimalFormat("#0.000000").format(listy.get(i))+" "
					+new DecimalFormat("#0.000000").format(listz.get(i))+",\n");


		}
		System.out.println("\t\t]\n"+"}");
		System.out.println("IndexedLineSet {\n"+"coordIndex [");


		
		
		int j=0;
		int add=0;
		
		
		while(j<countbezier){
	
		for(int i=0;i<=Math.ceil(1/du);i++){
			add=(int)(Math.ceil(1/du))*j;
			System.out.print(" "+(i+add)+",");
		}

		System.out.print(" -1,\n");
		j++;
		}
	
		System.out.print("  ]\n"+"}\n");
		
		for(int i=2;i<count;i++){
			System.out.print("Separator {\n"+"LightModel {\n"+"model PHONG\n"+"}\n"
					+"Material {\n"+"\tdiffuseColor 1.0 1.0 1.0\n"+"}\n"+"Transform {\n"+"\t translation ");
			System.out.print(arr[i][0]+" "+arr[i][1] +" "+arr[i][2]+"\n"+"}\n"+"Sphere {\n"+"\tradius "+radius+"\n"+
					"}\n"+"}");
		}
		System.out.println("\n\n"+"}");	
	}

}
