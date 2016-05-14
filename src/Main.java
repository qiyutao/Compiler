import java.io.File;
import java.util.Vector;

public class Main {
	public static void main(String[] args) {
			LexicalAnsy lAnsy = new LexicalAnsy();
			File file = new File("D:\\1.c");
			try {
				lAnsy.read(file);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Parser parser = new Parser(lAnsy.getWords());
			parser.launch();
		
			/*Vector<WordPair> vector = lAnsy.getWords();
			System.out.println(vector.size());
			for(int i=0;i<vector.size();i++)
				System.err.println(vector.get(i).getKey()+"  "+vector.get(i).getValue());
	*/
	}
}
