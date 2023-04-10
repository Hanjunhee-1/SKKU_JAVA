import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class Reverse {

	public static void main(String[] args) throws IOException {
		
		ArrayList<String> inputs = new ArrayList<String>();
		
		try {
			FileInputStream fis = new FileInputStream("[input 파일이 있는 경로]");
			
			InputStreamReader isr = new InputStreamReader (fis);
			BufferedReader br = new BufferedReader(isr);
			
			String data = new String("");
			while ((data = br.readLine()) != null) {
				inputs.add(data);
			}
			
			FileOutputStream fos = new FileOutputStream("[output 파일이 있는 경로]");
			
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			BufferedWriter bw = new BufferedWriter(osw);
			
			for (int i=inputs.size()-1; i>=0; i--) {
				bw.write(inputs.get(i));
				bw.newLine();
				bw.flush();
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

}
