package sjsu.cohort9.chappal.subscriber.ml;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class ConvertFile {
	public static void main(String args[]) {
		try {
			
			File fout = new File("exercise_processed");
			FileOutputStream fos = new FileOutputStream(fout);
		 
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
		 
			BufferedReader br = new BufferedReader(new FileReader("exercise1.csv"));
		    String line;
		    while ((line = br.readLine()) != null) {
		      String [] arr = line.split(",");
		      StringBuffer sb = new StringBuffer();
		      sb.append(arr[0]);
		      for(int i = 1; i<=7; i++) {
		    	  sb.append(" " + i + ":"+arr[i]);
		      }
//		      System.out.println(sb.toString());
		      bw.write(sb.toString());
			  bw.newLine();
		    }
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
