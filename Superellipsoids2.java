import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Superellipsoids2 {
	static List<Double> listx = new ArrayList<Double>();
	static List<Double> listy = new ArrayList<Double>();
	static List<Double> listz = new ArrayList<Double>();
	
	static List<Double> Slista = new ArrayList<Double>();
	static List<Double> Slistb = new ArrayList<Double>();
	static List<Double> Slistc = new ArrayList<Double>();
	static int count=0;
	
	public static double signfunction(double x) {
		if (x > 0) {
			return 1;
		} 
		else if(x==0){
			return 0;
		}
		else{   //if(x > 1)
			return -1;
		}
	}
	
	public static double auxfuncC(double w,double m) {
		
		double c=signfunction(Math.cos(w))*Math.pow(Math.abs(Math.cos(w)), m);
		
		return c;
	}
	
	
	public static double auxfuncS(double w,double m) {
		
		double s=signfunction(Math.sin(w))*Math.pow(Math.abs(Math.sin(w)), m);
		
		return s;
	}
	
	public static void smooth(double A,double B,double C,double u, double v, double s1, double s2) {
	double nx, ny, nz;
	nx=(1/A)*auxfuncC(v, 2-s1)*auxfuncC(u, 2-s2);
	ny=(1/B)*auxfuncC(v, 2-s1)*auxfuncS(u, 2-s2);
	nz=(1/C)*auxfuncS(v, 2-s1);
	
	Slista.add(nx);
	Slistb.add(ny);
	Slistc.add(nz);
		
	}
	
	
public static void main(String[] args) throws FileNotFoundException{
		
		//using for now
		double radius=(double)1;
		double du=(double)19;
		double dv=(double)9;
		double t=(double)1;
		double A=(double)1;
		double B=(double)1;
		double C=(double)1;
		
		//File filename = new File("patchPoints.txt");
		int flat=1;
		int smooth=0;

		//command line parsing logic
		if(args.length>0)
		{        
			
			for(int i = 0; i < args.length; i++)
			{       
				
				/*if((args[i]).equals("-f")){
					filename = new File(args[i+1]);
				}*/
				if((args[i]).equalsIgnoreCase("-u")){
					du = Double.parseDouble(args[i+1]);
					}
				else if((args[i]).equalsIgnoreCase("-v")){
					dv = Double.parseDouble(args[i+1]);
				}
				else if((args[i]).equalsIgnoreCase("-r")){
					radius = Double.parseDouble(args[i+1]);
				}
				else if((args[i]).equalsIgnoreCase("-t")){
					t = Double.parseDouble(args[i+1]);
				}
				else if((args[i]).equals("-A")){
					A = Double.parseDouble(args[i+1]);
				}
				else if((args[i]).equals("-B")){
					B = Double.parseDouble(args[i+1]);
				}
				else if((args[i]).equals("-C")){
					C = Double.parseDouble(args[i+1]);
				}
				else if((args[i]).equals("-F")){
					flat=1;
				}
				else if((args[i]).equals("-S")){
					smooth=1;
					flat=0;
				}
			}
		}
		
		
		
		
		//superellipsoids Logic
		
		
		double u=-(Math.PI);
		double v=-(Math.PI)/2;
		
		double x=0;
		double y=0;
		double z=0;
		
		double diffu;
		double diffv;
		diffu= 2*(Math.PI)/(du-1);
		diffv= (Math.PI)/(dv-1);
		
		
		
		for(int i=1;i<=du;i++){
			v=(-(Math.PI)/2)+diffv;
			for(int j=1;j<=dv-2;j++){
				
				
				
				x=A*auxfuncC(v, radius)*auxfuncC(u, t);
				y=B*auxfuncC(v, radius)*auxfuncS(u, t);
				z=C*auxfuncS(v, radius);

				
				listx.add(x);
				listy.add(y);
				listz.add(z);

				smooth(A, B, C, u, v,radius, t);
				
				count++;
				v=v+diffv;
			}
			u=u+diffu;
		}
		
	/*	while(u<(Math.PI)){
			v=(-(Math.PI)/2)+diffv;
			while(v<((Math.PI)/2)){
				
				
				x=A*auxfuncC(v, radius)*auxfuncC(u, t);
				y=B*auxfuncC(v, radius)*auxfuncS(u, t);
				z=C*auxfuncS(v, radius);

				
				listx.add(x);
				listy.add(y);
				listz.add(z);

				smooth(A, B, C, u, v,radius, t);
				
				count++;


				v=v+diffv;
				
			}
			u=u+diffu;
			
		}*/
		
		//if u>Math.PI reset u=-(Math.PI)
		
		/*if(u>=Math.PI){
			u=-(Math.PI);
			
			
			v=(-(Math.PI)/2)+diffv;
				while(v<((Math.PI)/2)){
					
					
					x=A*auxfuncC(v, radius)*auxfuncC(u, t);
					y=B*auxfuncC(v, radius)*auxfuncS(u, t);
					z=C*auxfuncS(v, radius);

					
					listx.add(x);
					listy.add(y);
					listz.add(z);

					smooth(A, B, C, u, v,radius, t);
					
					count++;


					v=v+diffv;
					
				}
				
			
		}*/
		
		
		
		
		
		//for values at poles
		//double temp=u;
		//u=-(Math.PI);
		v=-(Math.PI)/2;
		x=A*auxfuncC(v, radius)*auxfuncC(u, t);
		y=B*auxfuncC(v, radius)*auxfuncS(u, t);
		z=C*auxfuncS(v, radius);
		listx.add(x);
		listy.add(y);
		listz.add(z);
		smooth(A, B, C, u, v,radius, t);
		
		v=(Math.PI)/2;
		x=A*auxfuncC(v, radius)*auxfuncC(u, t);
		y=B*auxfuncC(v, radius)*auxfuncS(u, t);
		z=C*auxfuncS(v, radius);
		listx.add(x);
		listy.add(y);
		listz.add(z);
		smooth(A, B, C, u, v,radius, t);
		
		if(flat==1){
			createscriptsys(du,dv, radius);
		}
		else if(smooth==1){
			createscriptsyssmooth(du, dv, radius);
		}
		else{
			createscriptsys(du,dv, radius);
		}
		
		
}

public static void createscriptsys(double du,double dv, double radius){

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
	System.out.println("NormalBinding {");
	System.out.println(" value        PER_VERTEX_INDEXED");
	System.out.println("}");
	
	System.out.println("IndexedFaceSet {\n"+"coordIndex [");

	int temp=0;
	int change=0;
	int it;

	while(change<((((du*(dv-2))-1)-(dv-2))-1)){
		for(it=change;it<(((dv-2+change)-1));it++){
			
			
			System.out.print("\t"+it+", "+(int)(it+(dv-2))+", "+(it+1)+", -1,\n");
			System.out.print("\t"+(it+1)+", "+(int)(it+(dv-2))+", "+(int)(it+(dv-2)+1)+", -1,\n");
		
		}
		change=it+1;
		}
	
	
	
	
	double itp=0;
	
		for(it=0;it<(du-1);it++){
			
			
			System.out.print("\t"+(int)itp+", "+(int)((du*(dv-2)))+", "+(int)(itp+(dv-2))+", -1,\n");
		itp=itp+(dv-2);	
		
		}
		itp=(dv-2)-1;
		for(it=0;it<(du-1);it++){
			
			
			System.out.print("\t"+(int)itp+", "+(int)((du*(dv-2))+1)+", "+(int)(itp+(dv-2))+", -1,\n");
		itp=itp+(dv-2);	
		
		}
	
	System.out.print("  ]\n"+"}\n");
	
	System.out.println("\n\n"+"}");	

}

public static void createscriptsyssmooth(double du,double dv,double radius){

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
	
	System.out.println("NormalBinding {");
	System.out.println(" value        PER_VERTEX_INDEXED");
	System.out.println("}");
	

	System.out.println("Normal { \n vector [");
	 for(int i=0;i<(Slista.size());i++){
		  System.out.println("\t"+new DecimalFormat("#0.000000").format(Slista.get(i))+" "+new DecimalFormat("#0.000000").format(Slistb.get(i))+" "+new DecimalFormat("#0.000000").format(Slistc.get(i))+",");
	  }
	System.out.println( " \t]\n}");
	
	
	System.out.println("IndexedFaceSet {\n"+"coordIndex [");
	
	int temp=0;
	int change=0;
	int it;

	while(change<((((du*(dv-2))-1)-(dv-2))-1)){
	for(it=change;it<(((dv-2+change)-1));it++){
		
		
		System.out.print("\t"+it+", "+(int)(it+(dv-2))+", "+(it+1)+", -1,\n");
		System.out.print("\t"+(it+1)+", "+(int)(it+(dv-2))+", "+(int)(it+(dv-2)+1)+", -1,\n");
	
	}
	change=it+1;
	}
	
	double itp=0;
	
	for(it=0;it<(du-1);it++){
		
		
		System.out.print("\t"+(int)itp+", "+(int)((du*(dv-2)))+", "+(int)(itp+(dv-2))+", -1,\n");
	itp=itp+(dv-2);	
	
	}
	itp=(dv-2)-1;
	for(it=0;it<(du-1);it++){
		
		
		System.out.print("\t"+(int)itp+", "+(int)(itp+(dv-2))+", "+(int)((du*(dv-2))+1)+", -1,\n");
	itp=itp+(dv-2);	
	
	}
	
	System.out.print("  ]\n"+"}\n");
	System.out.println("\n\n"+"}");	
}


}
