import java.awt.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;


public class LexicalAnsy {
	private ArrayList<String> list = null;
	private String line = null;
	private  HashMap<String, Integer> map = null;
	private WordPair wordPair = null;
	private Vector<WordPair> vector = null;
	private int lineNumber;
	
	public LexicalAnsy() {
		list = new ArrayList<String>();
		Table table = new Table();
		map = table.getMap();
		vector = new Vector<>();
		lineNumber = 0;
	}
	
	public void read(File file) throws Exception {
		BufferedReader in = new BufferedReader(new FileReader(file));
		/*char[] chuf = new char[(int)file.length()];
		in.read(chuf, 0, (int)file.length());*/
		StringBuffer lineBuf = new StringBuffer();
		String tmp = null;
		int lineNum = 1;
		while((tmp = in.readLine())!=null) {
			lineBuf.append(""+lineNum+"#"+tmp+"\n");
			lineNum++;
		}
		//System.out.println(lineBuf.toString());
		String text = new String(lineBuf);
		text=text.replaceAll("\\/\\/.*", "");
		text=text.replaceAll("\\/\\*([^\\*^\\/]*|[\\*^\\/*]*|[^\\**\\/]*)*\\*\\/", "");
		
		String[] lines = text.split("\\n");
		
		for(int i=0;i<=lines.length-1;i++) {
			//line.replaceAll(" ", "");
			String[] line_num =lines[i].split("#");
			lineNumber = Integer.parseInt(line_num[0]);
			if(line_num.length==2) {
				//System.out.println(lineNumber);
				line = line_num[1].replaceAll("\\s*", "");
				vector.add(new WordPair(lineNumber,"lineNumber"));
				split();
			}
		}
	}

	private void split() {
		// TODO Auto-generated method stub
		int i = 0;
		for(;i<line.length();i++) {
			char first = line.charAt(i);
			if(('a'<=first&&first<='z')||('A'<=first&&first<='Z')) {
				i = isChar(i);
			} else if(first>='0'&&first<='9') {
				i = isNum(i);
			} else if((first>=33&&first<=47)||(first>=58&&first<=63)||
					  (first>=91&&first<=96)||(first>=123&&first<=125)) {
				i = isSymbol(i);
			} else {
				System.out.println("("+map.get("ERROR")+",ERROR)");
			}
		}
	}
	
	private int isSymbol(int i) {
		StringBuffer stringBuffer = new StringBuffer();
		int flag = i;
		char ch = '\0';
		for(;i<=line.length();i++) {
			if(i!=line.length())
			 ch = line.charAt(i);
			if(flag==i)
				stringBuffer.append(ch);
			else {
				if(((ch>=60&&ch<=62)||ch==33)&&((line.charAt(i-1)>=60&&line.charAt(i-1)<=62)||line.charAt(i-1)==33)) {
					stringBuffer.append(ch);
				} else /*(ch>33&&ch<=47)||(ch>=58&&ch<=59)||
						  (ch>=91&&ch<=96))*/{
					System.out.println("("+map.get(stringBuffer.toString())+","+stringBuffer.toString()+")");
					wordPair = new WordPair(map.get(stringBuffer.toString()), stringBuffer.toString());
					vector.add(wordPair);
					return --i;
				}
			}
		}
		
		return i;
	}
	
	private int isNum(int i) {
		StringBuffer stringBuffer = new StringBuffer();
		for(;i<line.length();i++) {
			char ch = line.charAt(i);
			if(ch<'0'||ch>'9') {
				int tmp = Integer.parseInt(stringBuffer.toString());
				System.out.println("("+20+","+tmp+")");
				wordPair = new WordPair(20, ""+tmp);
				vector.add(wordPair);
				return --i;
			}
			
			stringBuffer.append(ch);
		}
		
		return i;
	}
	
	private int isChar(int i) {
		StringBuffer strBuffer = new StringBuffer();
		for(;i<line.length();i++) {
			char ch = line.charAt(i);
			
			if((ch>=33&&ch<=47)||(ch>=59&&ch<=63)||(ch>=91&&ch<=96)) {
				System.out.println("("+10+","+strBuffer.toString()+")");
				wordPair = new WordPair(10, strBuffer.toString());
				vector.add(wordPair);
				return --i;
			}
			
			strBuffer.append(ch);
			if(map.get(strBuffer.toString())!=null)
			{
				System.out.println("("+map.get(strBuffer.toString())+","+strBuffer.toString()+")");
				wordPair = new WordPair(map.get(strBuffer.toString()), strBuffer.toString());
				vector.add(wordPair);
				return i;
			}
		}
		
		return i;
	}
	
	public Vector<WordPair> getWords() {
		return vector;
	}
}
