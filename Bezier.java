import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;
/*import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;*/

public class Bezier {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
			 //  new Cli(args).parse();
			

		//Cli cli= new Cli(args);
		
		String filename="new.txt";
		//Scanner sc = new Scanner(System.in);
		 InputStream stream = ClassLoader.getSystemResourceAsStream(filename);
		BufferedReader buffer = new BufferedReader(new InputStreamReader(stream));
		System.out.println("Please enter file name:");
		//String filename = sc.nextLine();
		File file = new File(filename);
		try {
			 //sc = new Scanner(file);
			 String[] tokens=null;
			 String line;
			 int row = 0;
		        int size = 0;
		        int[][] matrix = null;
			 while((line = buffer.readLine()) != null) {
			        //System.out.println(sc.nextLine());
				 String[] vals = line.trim().split("\\s+");

		            // Lazy instantiation.
		            if (matrix == null) {
		                size = vals.length;
		                matrix = new int[size][size];
		            }

		            for (int col = 0; col < size; col++) {
		                matrix[row][col] = Integer.parseInt(vals[col]);
		            }

		            row++;
		        }

			 String str = "";
		        size = matrix.length;

		        if (matrix != null) {
		            for (int r = 0; r < size; r++) {
		                str += " ";
		                for (int col = 0; col < size; col++) {
		                    str += String.format("%2d",  matrix[r][col]);
		                    if (col < size - 1) {
		                        str += " | ";
		                    }
		                }
		                if (r < size - 1) {
		                    str += "\n";
		                    for (int col = 0; col < size; col++) {
		                        for (int i = 0; i < 4; i++) {
		                            str += "-";
		                        }
		                        if (col < size - 1) {
		                            str += "+";
		                        }
		                    }
		                    str += "\n";
		                } else {
		                    str += "\n";
		                }
		            }
		        }

		        System.out.println(str);
			    
			 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
