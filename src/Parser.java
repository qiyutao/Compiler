import java.awt.Window.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

public class Parser {
	private WordPair word = null;
	private Vector<WordPair> words = null;
	private Semantic sem = null;
	private int cur;
	private int line;
	private int oldLine;
	
	public Parser(Vector<WordPair> w) {
		// TODO Auto-generated constructor stub
		words = w;
		cur = 1;
		line = words.get(0).getKey();
		oldLine = line;
		sem = new Semantic();
	}
	
	public void launch() {
		prog();
	}
	
	public void prog() {
		funcName();
		block();
	}
	
	/* int main()*/
	public void funcName() {
		getWord();
		if(word.equal(2)) {
			getWord();
			if(word.equal(1)) {
				getWord();
				if(word.equal(26)) {
					getWord();
					if(word.equal(27)) {
						//cur++;
					} else {
						System.out.println("error:in line "+line+"  need \")\"   ->"+word.getValue());
						return;
					}
				} else {
					System.out.println("error:in line "+line+"  need \"(\"   ->"+word.getValue());
					return;
				}
			} else {
				System.out.println("error:in line "+line+"  need \"main\"   ->"+word.getValue());
				return;
			}
		} else {
			System.out.println("error:in line "+line+"  need \"int\"   ->"+word.getValue());
			return;
		}
	}
	
	/* { DECLS STMTS } */
 	public void block() {
		getWord();
		if(word.equal(30)) {
			decls();
			stmts();
			
			//getWord();
			if(word.equal(31)) {
				//go
			} else {
				System.out.println("error:in line "+line+"  need \"}\"   ->"+word.getValue());
				return;
			}
		} else {
			System.out.println("error:in line "+line+"  need \"{\"   ->"+word.getValue());
			return;
		}
	}
 	
 	/*DESCls -> null descls1*/
 	public void decls() {
 		decls1();
 	}
 	
 	/*descls1 -> decl decls1 |null*/
 	public void decls1() {
 		getWord();
 		if(word.equal(2)||word.equal(3)) {
 			decl(word.getKey());
 			decls1();
 		}
 		//follow(decls1)=[ if while id  { }
 		else if(word.equal(28)||word.equal(4)||word.equal(31)||
 				word.equal(7)||word.equal(10)||word.equal(30)) {
 			curBack();
 		} else {
 			System.out.println("error:in line "+line+"     ->"+word.getValue());
			return;
 		}
 		
 	}
 	
 	public void decl(int i) {
 		type();
 		String type = null;
 		if(i==2) {
 			type = "int";
 		} else {
 			type = "char";
 		}
 		getWord();
 		if(word.equal(10)) {
 			//go
 			sem.typeTable(type, word.getValue());
 			/*is array ?*/
 			getWord();
 			if(word.equal(28)) {
 				getWord();
 				if(word.equal(20)) {
 					getWord();
 					if(word.equal(29)) {
 						//go
 					} else {
 						System.out.println("error:in line "+line+"    need \"]\"   ->"+word.getValue());
 						return;
 					}
 				} else {
 					System.out.println("error:in line "+line+"  need \"num\"   ->"+word.getValue());
 					return;
 				}
 			}
 			if(word.equal(34)) {
 				//go
 			} else {
 				System.out.println("error:in line "+line+"  there is no \";\"   ->"+word.getValue());;
 			}
 		} else {
 			System.out.println("error:in line "+line+"  need \"标识符\"   ->"+word.getValue());
			return;
 		}
 	}
 	
 	public void type() {
 		//getWord();
 		if(word.equal(2)||word.equal(3)) {
 			//go
 		} else {
 			System.out.println("error:in line "+line+"  need \"类型\"   ->"+word.getValue());
			return;
 		}
 	}
	
 	/*STMTS -> NULL STMTS1
	  STMTS1 -> STMT STMTS1|NULL
	  */
 	public void stmts() {
 		
 		stmts1();
 	}
 	
 	public void stmts1() {
 		if(cur!=words.size())
 		getWord();
 		//first(stmt)=[ if while id  {
 		if(word.equal(28)||word.equal(4)||
 			word.equal(7)||word.equal(10)||word.equal(30)) {
 			stmt();
 			stmts1();
 		} else if(word.equal(31)) {//follow(stmts) = }
 			//go
 		} else {
 			System.out.println("error:in line "+line+"     ->"+word.getValue());
			return;
 		}
 	}
 	
 	/*STMT	->	LOC  =  BOOL ;				--SL
		->	if  (  BOOL )  STMT				--SI
		->	if  (  BOOL )  STMT  else  STMT		--SF
		->	while  (  BOOL )  STMT			--SW
		->	BLOCK
		*/
 	public void stmt() {
 		if(word.equal(28)||word.equal(10)) {//first(LOC)=[||id
 			String a = loc();
 			getWord();
 			if(word.equal(21)) {
 				String op = word.getValue();
 				getWord();
 				String b = equal();
 				sem.generate(op, b, null, a);
 				getWord();
 				if(word.equal(34)||word.equal(31)) { //follow ; }
 					//go
 				} else {
 					System.out.println("error:in line "+line+"  need \";\"   ->"+word.getValue());
 					return;
 				}
 			} else {
 				System.out.println("error:in line "+line+"  need \"=\"   ->"+word.getValue());
				return;
 			}
 		} else if(word.equal(4)) {//first(IF)
 			ifPross();
 		} else if(word.equal(7)) {//first(WHILE)
 			whilePross();
 		} else if(word.equal(30)) {//first(block)
 			curBack();
 			block();
 		} else if(word.equal(31)) {//follow(stmts) = }
 			curBack();
 		} else {
 			System.out.println("error:in line "+line+"     ->"+word.getValue());
			return;
 		}
 	}
 	
 	/*LOC -> id LOC1
	  LOC1 -> [BOOL] LOC1 | NULL
	  */
 	public String loc() {
 		String id = null;
 		
 		if(word.equal(10)) {
 			id = word.getValue();
 			getWord();
 			loc1();
 		} else {
 			System.out.println("error:in line "+line+"  need id   ->"+word.getValue());
			
 		}
 		
 		return id;
 	}
 	
 	public void loc1() {
 		if(word.equal(28)) {
 			getWord();
 			equal();
 			getWord();
 			if(word.equal(29)) {
 				getWord();
 				loc1();
 			} else {
 				System.out.println("error:in line "+line+"  need \"]\"   ->"+word.getValue());
 				return;
 			}
 		} else if(word.equal(21)||word.equal(22)||word.equal(23)||
 				word.equal(24)||word.equal(25)||word.equal(35)||
 				word.equal(36)||word.equal(37)||word.equal(38)||
 				word.equal(39)||word.equal(40)||word.equal(34)){//follow(loc) = "= + - * / > < >= <= == != ;"
 			//go
 			curBack();
 		} else {
 			System.out.println("error:in line "+line+"  need \"[\"   ->"+word.getValue());
			return;
 		}
 	}
 	
 	/* EQUAL -> REL EQUAL1*/
 	public String equal() {
 		if(word.equal(23)||word.equal(26)||
 		   word.equal(28)||word.equal(10)||word.equal(20)) { //first(equal) = - ( [  id num
 			String a = rel();
 			//getWord();
 			return equal1(a);
 		} else {
 			System.out.println("error:in line "+line+"     ->"+word.getValue());
			return null;
 		}
 	}
 	
 	/* EQUAL1 -> == REL EQUAL1|NULL
 	 * 		  -> != REL EQUAL1 | NULL
 	 */
 	public String equal1(String i) {
 		if(word.equal(39)||word.equal(40)) {
 			getWord();
 			String a = rel();
 			
 			//getWord();
 			return equal1(a);
 		} else if(word.equal(34)||word.equal(31)||word.equal(27)) {//follow(equal) = ; } ）
 			curBack();
 			return i;
 		} else {
 			System.out.println("error:in line "+line+"     ->"+word.getValue());
			return i;
 		}
 	}
 	
 	/*REL		->	EXPR  ROP  EXPR
			    ->	EXPR
	*/
 	public String rel() {
 		String id = expr();
 		//getWord();
 		if(word.equal(35)||word.equal(37)||
 		   word.equal(36)||word.equal(38)) {//first(rop) = > < >= <=
 			rop(id);
 			
 		} else if(word.equal(34)||word.equal(31)||word.equal(27)||word.equal(39)||word.equal(40)) {//follow(expr) = ; } ）== !=
 			//go
 		} else {
 			System.out.println("error:in line "+line+"     ->"+word.getValue());
			
 		}
 		return id;
 	}
 	
 	/* ROR	->	> | >= | < | <= */
 	public void rop(String i) {
 		String op = word.getValue();
 		getWord();
		String op2 = expr();
		sem.generate(op, i, op2, sem.getLabel());
 	}
 	
 	/* EXPR -> TERM EXPR1 */
 	public String expr() {
 		String i;
 		i = term();
 		//getWord();
 		return expr1(i);
 	}
 	
 	/* EXPR1 ->ADD TERM EXPR1|NULL */ 
 	public String expr1(String i) {
 		String ret = null;
 		if(word.equal(22)||word.equal(23)) {
 			String op = word.getValue();
 			getWord();
 	 		String r = term();
 	 		ret = sem.getT();
 	 		sem.generate(op, i, r, ret);
 	 		if(!word.equal(34)) //patch
 	 			getWord();
 	 		ret = expr1(ret);
 		} else if(word.equal(34)||word.equal(31)||word.equal(38)||
 				  word.equal(35)||word.equal(37)||word.equal(36)||
 	 		      word.equal(27)||word.equal(39)||word.equal(40)) {//follow(expr) = ; } > | >= | < | <= ）!= ==
 			//go
 			//curBack();
 			return i;
 		} else {
 			System.out.println("error:in line "+line+"  need \"+\" or \"-\"   ->"+word.getValue());
			
 		}
 		return ret;
 	}
 	
 	/* TERM -> UNARY TERM1 */
 	public String term() {
 		String id = null;
 		if(word.equal(23)||word.equal(26)||
 	 	   word.equal(28)||word.equal(10)||word.equal(20)) { //first(equal) = - ( [  id num
			String r =unary();
			getWord();
			id = term1(r);
		} else {
			System.out.println("error:in line "+line+"     ->" + word.getValue());
			
		}
 		return id;
 	}
 	
 	/* TERM1 - >MUL UNARY TERM1 |NULL */
 	public String term1(String i) {
 		String id = null;
 		if(word.equal(24)||word.equal(25)) {
 			String op = word.getValue();
 			getWord();
 			String op2 = unary();
 			id = sem.getT();
 			sem.generate(op, i, op2, id);
			getWord();
			id = term1(id);
 		} else if(word.equal(34)||word.equal(22)||word.equal(23)||word.equal(31)||
 				  word.equal(35)||word.equal(37)||word.equal(27)||word.equal(36)||
 	 		      word.equal(38)||word.equal(39)||word.equal(40)) { //follow ； + - } = > < >= <= ）!= ==
 			//go
 			return i;
 		} else {
 			System.out.println("error:in line "+line+"  need \"*\" or \"/\"   ->"+word.getValue());
			
 		}
 		return id;
 	}
 	
 	/*UNARY	 ->	-FACTOR
			 ->	FACTOR
	*/
 	public String unary() {
 		return factor();
 	}
 	
 	/* FACTOR	->	(  equal )
			->	LOC
			->	num
	*/
 	public String factor() {
 		String id = null;
 		if(word.equal(26)) {
 			getWord();
 			equal();
 			getWord();
 			if(word.equal(27)) {
 				//go
 			} else {
 				System.out.println("error:in line "+line+"  need \")\"   ->"+word.getValue());
 				
 			}
 		} else if(word.equal(28)||word.equal(10)) {
 			//getWord();
 			id = loc();
 			
 		} else if(word.equal(20)) {
 			//go
 			id = word.getValue();
 		} else {
 			System.out.println("error:in line "+line+"     ->"+word.getValue());
			
 		}
		return id;
 	}
 	
 	public void ifPross() {
 		getWord();
		if (word.equal(26)) {// match (
			getWord();
			equal();
			getWord();
			if(word.equal(27)) { //match )
				getWord();
				stmt();
				getWord();
				if(word.equal(5)) {// match else
					String oldLabel = sem.getLabelStr();
					sem.getLabel();
					sem.write("JMP "+sem.getLabelStr());
					sem.write(oldLabel+":");
					getWord();
					stmt();
					sem.write(sem.getLabelStr()+":");
				} else {
					//go
					cur--;
					sem.outLabel();
				}
			} else {
				System.out.println("error:in line "+line+"  need \")\"   ->" + word.getValue());
				return;
			}
		} else {
			System.out.println("error:in line "+line+"  need \"(\"   ->" + word.getValue());
			return;
		}
 	}
 	
 	public void whilePross() {
 		getWord();
 		sem.getLabel();
 		String oldLabel = sem.outLabel();
 		if (word.equal(26)) {// match (
			getWord();
			equal();
			getWord();
			if(word.equal(27)) { //match )
				getWord();
				stmt();
				getWord();
				sem.cmpWhile(oldLabel);
				sem.outLabel();
			} else {
				System.out.println("error:in line "+line+"  need \")\"   ->" + word.getValue());
				return;
			}
		} else {
			System.out.println("error:in line "+line+"  need \"(\"   ->" + word.getValue());
			return;
		}
 	}
 	
	public void getWord() {
			word = words.get(cur);
			//if(cur != words.size()-1)
			curFront();
	
	}
	
	public void curBack() {
		cur--;
		if(words.get(cur).getValue().equals("lineNumber")) {
			cur--;
			line = oldLine;
		}
	}
	
	public void curFront() {
		cur++;
		if(cur!=words.size())
		if(words.get(cur).getValue().equals("lineNumber")) {
			oldLine = line;
			line = words.get(cur).getKey();
			cur++;
		}
	}
	
	
	
	/*public static void main(String[] args) {
		WordPair wordPair  = new WordPair(2, "int");
		Vector<WordPair> words = new Vector<>();
		words.add(wordPair);
		wordPair = new WordPair(1, "main");
		words.add(wordPair);
		wordPair = new WordPair(26, "(");
		words.add(wordPair);
		wordPair = new WordPair(27, ")");
		words.add(wordPair);
		wordPair = new WordPair(30, "{");
		words.add(wordPair);
		wordPair = new WordPair(2, "int");
		words.add(wordPair);
		wordPair = new WordPair(10, "a");
		words.add(wordPair);
		wordPair = new WordPair(34, ";");
		words.add(wordPair);
		wordPair = new WordPair(2, "int");
		words.add(wordPair);
		wordPair = new WordPair(10, "b");
		words.add(wordPair);
		wordPair = new WordPair(34, ";");
		words.add(wordPair);
		wordPair = new WordPair(2, "int");
		words.add(wordPair);
		wordPair = new WordPair(10, "c");
		words.add(wordPair);
		wordPair = new WordPair(34, ";");
		words.add(wordPair);
		wordPair = new WordPair(10, "b");
		words.add(wordPair);
		wordPair = new WordPair(21, "=");
		words.add(wordPair);
		wordPair = new WordPair(20, "2");
		words.add(wordPair);
		wordPair = new WordPair(34, ";");
		words.add(wordPair);
		wordPair = new WordPair(10, "c");
		words.add(wordPair);
		wordPair = new WordPair(21, "=");
		words.add(wordPair);
		wordPair = new WordPair(20, "3");
		words.add(wordPair);
		wordPair = new WordPair(34, ";");
		words.add(wordPair);
		wordPair = new WordPair(10, "a");
		words.add(wordPair);
		wordPair = new WordPair(21, "=");
		words.add(wordPair);
		wordPair = new WordPair(10, "b");
		words.add(wordPair);
		wordPair = new WordPair(22, "+");
		words.add(wordPair);
		wordPair = new WordPair(10, "c");
		words.add(wordPair);
		wordPair = new WordPair(24, "*");
		words.add(wordPair);
		wordPair = new WordPair(20, "3");
		words.add(wordPair);
		wordPair = new WordPair(34, ";");
		words.add(wordPair);
		
		wordPair = new WordPair(4, "if");
		words.add(wordPair);
		wordPair = new WordPair(26, "(");
		words.add(wordPair);
		wordPair = new WordPair(10, "a");
		words.add(wordPair);
		wordPair = new WordPair(35, ">");
		words.add(wordPair);
		wordPair = new WordPair(20, "4");
		words.add(wordPair);
		wordPair = new WordPair(27, ")");
		words.add(wordPair);
		wordPair = new WordPair(10, "c");
		words.add(wordPair);
		wordPair = new WordPair(21, "=");
		words.add(wordPair);
		wordPair = new WordPair(20, "3");
		words.add(wordPair);
		wordPair = new WordPair(34, ";");
		words.add(wordPair);
		
		//while
		wordPair = new WordPair(7, "while");
		words.add(wordPair);
		wordPair = new WordPair(26, "(");
		words.add(wordPair);
		wordPair = new WordPair(10, "c");
		words.add(wordPair);
		wordPair = new WordPair(40, "!=");
		words.add(wordPair);
		wordPair = new WordPair(20, "0");
		words.add(wordPair);
		wordPair = new WordPair(27, ")");
		words.add(wordPair);
		wordPair = new WordPair(30, "{");
		words.add(wordPair);
		wordPair = new WordPair(10, "c");
		words.add(wordPair);
		wordPair = new WordPair(21, "=");
		words.add(wordPair);
		wordPair = new WordPair(10, "c");
		words.add(wordPair);
		wordPair = new WordPair(23, "-");
		words.add(wordPair);
		wordPair = new WordPair(20, "1");
		words.add(wordPair);
		wordPair = new WordPair(34, ";");
		words.add(wordPair);
		wordPair = new WordPair(31, "}");
		words.add(wordPair);
		
		
		wordPair = new WordPair(31, "}");
		words.add(wordPair);
		Parser p = new Parser(words);
		p.launch();
	}*/
}
