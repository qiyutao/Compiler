import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Semantic {

	private int count;
	private int labelNum;
	private int typeOffset;
	private Map<String, String> idReg = null;
	private Stack<String> tmp = null;
	
	public Semantic() {
		// TODO Auto-generated constructor stub
		count = 0;
		labelNum = 0;
		typeOffset = 0;
		idReg = new HashMap<>();
		tmp = new Stack<>();
	}
	
	public String generate(String op,String op1,String op2,String ret) {
		System.out.println("("+op+","+op1+","+op2+","+ret+")");
		if(idReg.containsKey(op1))
			op1 = idReg.get(op1);
		if(idReg.containsKey(op2))
			op2 = idReg.get(op2);
		if(idReg.containsKey(ret))
			ret = idReg.get(ret);
		
		if(op2==null) {//¸³Öµ
			System.out.println("mov "+ret+","+op1);
		} else {
			if(op.equals("+")) {
				System.out.println("mov eax"+","+op1);
				System.out.println("add eax"+","+op2);
				System.out.println("mov "+ret+",eax");
			} else if(op.equals("-")) {
				System.out.println("mov eax"+","+op1);
				System.out.println("sub eax"+","+op2);
				System.out.println("mov "+ret+",eax");
			} else if(op.equals("*")) {
				System.out.println("mov eax"+","+op1);
				System.out.println("mov ebx"+","+op2);
				System.out.println("mul ebx");
				System.out.println("mov "+ret+",eax");
			} else if(op.equals("/")){
				System.out.println("mov eax"+","+op1);
				System.out.println("mov ebx"+","+op2);
				System.out.println("div ebx");
				System.out.println("mov "+ret+",eax");
			} else {
				compare(op, op1, op2, ret);
			}
		}
		return ret;
	}
	
	public String getT() {
		count++;
		return "T+"+count;
	}
	
	public void typeTable(String type,String val) {
		if(type.equals("int")) {
			typeOffset -= 4;
			
		} else {
			typeOffset -= 1;
		}
		String reg = "ebp"+typeOffset;
		idReg.put(val, reg);
		//System.out.println(val+"-----"+reg);
	}
	
	public String getLabel() {
		labelNum++;
		return "L"+labelNum;
	}
	
	public String outLabel() {
		System.out.println("L"+labelNum+":");
		return "L"+labelNum+":";
	}
	
	public String getLabelStr() {
		return "L"+labelNum;
	}
	
	public void write(String s) {
		System.out.println(s);
	}
	
	public void compare(String op,String op1,String op2,String ret) {
		tmp.clear();
		System.out.println("MOV eax,"+op1);
		System.out.println("MOV ebx,"+op2);
		System.out.println("CMP eax,ebx");
		if(op.equals(">")) {
			System.out.println("JNA "+ret);
			cmpWhileSave("<=", op1, op2);
		} else if(op.equals("<")) {
			System.out.println("JNB "+ret);
			cmpWhileSave(">=", op1, op2);
		} else if(op.equals(">=")) {
			System.out.println("JB "+ret);
			cmpWhileSave("<", op1, op2);
		} else if(op.equals("<=")) {
			System.out.println("JA "+ret);
			cmpWhileSave(">", op1, op2);
		} else if(op.equals("==")) {
			System.out.println("JNZ "+ret);
			cmpWhileSave("!=", op1, op2);
		} else {
			System.out.println("JZ "+ret);
			cmpWhileSave("==", op1, op2);
		}
	}
	
	public void cmpWhileSave(String op,String op1,String op2) {
		tmp.push(op2);
		tmp.push(op1);
		tmp.push(op);
	}
	
	public void cmpWhile(String ret) {
		String op = tmp.pop();
		String op1 = tmp.pop();
		String op2 = tmp.pop();
		compare(op,op1,op2,ret);
	}
}
