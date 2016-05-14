import java.awt.Window.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class Parser {
	private WordPair word = null;
	private Vector<WordPair> words = null;
	private int cur;
	private int line;
	private int oldLine;
	
	public Parser(Vector<WordPair> w) {
		// TODO Auto-generated constructor stub
		words = w;
		cur = 1;
		line = words.get(0).getKey();
		oldLine = line;
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
 			decl();
 			decls1();
 		}
 		//fellow(decls1)=[ if while id  { }
 		else if(word.equal(28)||word.equal(4)||word.equal(31)||
 				word.equal(7)||word.equal(10)||word.equal(30)) {
 			curBack();
 		} else {
 			System.out.println("error:in line "+line+"     ->"+word.getValue());
			return;
 		}
 		
 	}
 	
 	public void decl() {
 		type();
 		getWord();
 		if(word.equal(10)) {
 			//go
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
 		} else if(word.equal(31)) {//fellow(stmts) = }
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
 			loc();
 			getWord();
 			if(word.equal(21)) {
 				getWord();
 				equal();
 				getWord();
 				if(word.equal(34)||word.equal(31)) { //fellow ; }
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
 		} else if(word.equal(31)) {//fellow(stmts) = }
 			curBack();
 		} else {
 			System.out.println("error:in line "+line+"     ->"+word.getValue());
			return;
 		}
 	}
 	
 	/*LOC -> id LOC1
	  LOC1 -> [BOOL] LOC1 | NULL
	  */
 	public void loc() {
 		if(word.equal(10)) {
 			getWord();
 			loc1();
 		} else {
 			System.out.println("error:in line "+line+"  need id   ->"+word.getValue());
			return;
 		}
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
 				word.equal(39)||word.equal(40)||word.equal(34)){//fellow(loc) = "= + - * / > < >= <= == != ;"
 			//go
 			curBack();
 		} else {
 			System.out.println("error:in line "+line+"  need \"[\"   ->"+word.getValue());
			return;
 		}
 	}
 	
 	/* EQUAL -> REL EQUAL1*/
 	public void equal() {
 		if(word.equal(23)||word.equal(26)||
 		   word.equal(28)||word.equal(10)||word.equal(20)) { //first(equal) = - ( [  id num
 			rel();
 			//getWord();
 			equal1();
 		} else {
 			System.out.println("error:in line "+line+"     ->"+word.getValue());
			return;
 		}
 	}
 	
 	/* EQUAL1 -> == REL EQUAL1|NULL
 	 * 		  -> != REL EQUAL1 | NULL
 	 */
 	public void equal1() {
 		if(word.equal(39)||word.equal(40)) {
 			getWord();
 			rel();
 			
 			//getWord();
 			equal1();
 		} else if(word.equal(34)||word.equal(31)||word.equal(27)) {//fellow(equal) = ; } ）
 			curBack();
 		} else {
 			System.out.println("error:in line "+line+"     ->"+word.getValue());
			return;
 		}
 	}
 	
 	/*REL		->	EXPR  ROP  EXPR
			    ->	EXPR
	*/
 	public void rel() {
 		expr();
 		//getWord();
 		if(word.equal(35)||word.equal(37)||
 		   word.equal(36)||word.equal(38)) {//first(rop) = > < >= <=
 			rop();
 			
 		} else if(word.equal(34)||word.equal(31)||word.equal(27)||word.equal(39)||word.equal(40)) {//fellow(expr) = ; } ）== !=
 			//go
 		} else {
 			System.out.println("error:in line "+line+"     ->"+word.getValue());
			return;
 		}
 	}
 	
 	/* ROR	->	> | >= | < | <= */
 	public void rop() {
 		getWord();
		expr();
 	}
 	
 	/* EXPR -> TERM EXPR1 */
 	public void expr() {
 		term();
 		//getWord();
 		expr1();
 	}
 	
 	/* EXPR1 ->ADD TERM EXPR1|NULL */ 
 	public void expr1() {
 		if(word.equal(22)||word.equal(23)) {
 			add();
 		} else if(word.equal(34)||word.equal(31)||word.equal(38)||
 				  word.equal(35)||word.equal(37)||word.equal(36)||
 	 		      word.equal(27)||word.equal(39)||word.equal(40)) {//fellow(expr) = ; } > | >= | < | <= ）!= ==
 			//go
 			//curBack();
 		} else {
 			System.out.println("error:in line "+line+"  need \"+\" or \"-\"   ->"+word.getValue());
			return;
 		}
 		
 	}
 	
 	public void add() {
 		getWord();
 		term();
 		if(!word.equal(34)) //patch
 			getWord();
 		expr1();
 	}
 	
 	/* TERM -> UNARY TERM1 */
 	public void term() {
 		if(word.equal(23)||word.equal(26)||
 	 	   word.equal(28)||word.equal(10)||word.equal(20)) { //first(equal) = - ( [  id num
			unary();
			getWord();
			term1();
		} else {
			System.out.println("error:in line "+line+"     ->" + word.getValue());
			return;
		}
 	}
 	
 	/* TERM1 - >MUL UNARY TERM1 |NULL */
 	public void term1() {
 		if(word.equal(24)||word.equal(25)) {
 			getWord();
 			unary();
			getWord();
			term1();
 		} else if(word.equal(34)||word.equal(22)||word.equal(23)||word.equal(31)||
 				  word.equal(35)||word.equal(37)||word.equal(27)||word.equal(36)||
 	 		      word.equal(38)||word.equal(39)||word.equal(40)) { //fellow ； + - } = > < >= <= ）!= ==
 			//go
 		} else {
 			System.out.println("error:in line "+line+"  need \"*\" or \"/\"   ->"+word.getValue());
			return;
 		}
 	}
 	
 	/*UNARY	 ->	-FACTOR
			 ->	FACTOR
	*/
 	public void unary() {
 		factor();
 	}
 	
 	/* FACTOR	->	(  equal )
			->	LOC
			->	num
	*/
 	public void factor() {
 		if(word.equal(26)) {
 			getWord();
 			equal();
 			getWord();
 			if(word.equal(27)) {
 				//go
 			} else {
 				System.out.println("error:in line "+line+"  need \")\"   ->"+word.getValue());
 				return;
 			}
 		} else if(word.equal(28)||word.equal(10)) {
 			//getWord();
 			loc();
 		} else if(word.equal(20)) {
 			//go
 		} else {
 			System.out.println("error:in line "+line+"     ->"+word.getValue());
			return;
 		}
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
					getWord();
					stmt();
				} else {
					//go
					cur--;
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
 		if (word.equal(26)) {// match (
			getWord();
			equal();
			getWord();
			if(word.equal(27)) { //match )
				getWord();
				stmt();
				getWord();
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
