import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Semantic {

	private int count;
	private int labelNum;
	private int typeOffset;
	private Map<String, String> idReg = null;
	private Stack<String> tmp = null;
	private FileWriter file = null;

	public Semantic() {
		// TODO Auto-generated constructor stub
		count = 0;
		labelNum = 0;
		typeOffset = 0;
		idReg = new HashMap<>();
		tmp = new Stack<>();
		try {
			file = new FileWriter("d:\\1.asm");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void start() {
		try {
			file.write(".386" + "\n");
			file.write(".MODEL FLAT" + "\n");
			file.write("ExitProcess PROTO NEAR32 stdcall, dwExitCode:DWORD" + "\n");
			file.write("INCLUDE io.h                     ; header file for input/output" + "\n");
			file.write("cr      EQU         0dh          ; carriage return character" + "\n");
			file.write("Lf      EQU         0ah          ; line feed" + "\n");
			file.write(".STACK              4096         ; reserve 4096-byte stack" + "\n");
			file.write(".DATA                            ; reserve storage for data" + "\n");
			file.write("T       DWORD       40 DUP (?)" + "\n");
			file.write("label1  BYTE        cr, Lf, \"The result is \"" + "\n");
			file.write("result  BYTE        11 DUP (?)" + "\n");
			file.write("        BYTE        cr, Lf, 0" + "\n");
			file.write(".CODE                            ; start of main program code" + "\n");
			file.write("_start:" + "\n");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void end() {
		try {
			file.write("\tdtoa    result, eax     ; convert to ASCII characters" + "\n");
			file.write("\toutput  label1          ; output label and sum" + "\n");
			file.write("\tINVOKE  ExitProcess, 0  ; exit with return code 0" + "\n");
			file.write("PUBLIC _start                   ; make entry point public" + "\n");
			file.write("END                             ; end of source code" + "\n");

			file.flush();
			file.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String generate(String op, String op1, String op2, String ret) {
		System.out.println("(" + op + "," + op1 + "," + op2 + "," + ret + ")");
		if (idReg.containsKey(op1))
			op1 = idReg.get(op1);
		if (idReg.containsKey(op2))
			op2 = idReg.get(op2);
		if (idReg.containsKey(ret))
			ret = idReg.get(ret);
		try {
			if (op2 == null) {// ¸³Öµ
				file.write("\tMOV " + ret + "," + op1 + "\n");
			} else {
				if (op.equals("+")) {
					file.write("\tMOV EAX" + "," + op1 + "\n");
					file.write("\tADD EAX" + "," + op2 + "\n");
					file.write("\tMOV " + ret + ",EAX" + "\n");
				} else if (op.equals("-")) {
					file.write("\tMOV EAX" + "," + op1 + "\n");
					file.write("\tSUB EAX" + "," + op2 + "\n");
					file.write("\tMOV " + ret + ",EAX" + "\n");
				} else if (op.equals("*")) {
					file.write("\tMOV EAX" + "," + op1 + "\n");
					file.write("\tMOV EBX" + "," + op2 + "\n");
					file.write("\tMUL EBX" + "\n");
					file.write("\tMOV " + ret + ",EAX" + "\n");
				} else if (op.equals("/")) {
					file.write("\tMOV EAX" + "," + op1 + "\n");
					file.write("\tMOV EBX" + "," + op2 + "\n");
					file.write("\tDIV EBX");
					file.write("\tMOV " + ret + ",EAX" + "\n");
				} else {
					compare(op, op1, op2, ret);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ret;
	}

	public String getT() {
		count++;
		return "T+" + count;
	}

	public void typeTable(String type, String val) {
		if (type.equals("int")) {
			typeOffset -= 4;

		} else {
			typeOffset -= 1;
		}
		String reg = "ebp" + typeOffset;
		idReg.put(val, reg);
		// System.out.println(val+"-----"+reg);
	}

	public String getLabel() {
		labelNum++;
		return "_L" + labelNum;
	}

	public String outLabel() {
		try {
			file.write("_L" + labelNum + ":" + "\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "_L" + labelNum + ":";
	}

	public String getLabelStr() {
		return "_L" + labelNum;
	}

	public void write(String s) {
		try {
			file.write(s + "\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void compare(String op, String op1, String op2, String ret) {
		tmp.clear();
		try {
			file.write("\tMOV EAX," + op1 + "\n");
			file.write("\tMOV EBX," + op2 + "\n");
			file.write("\tCMP EAX,EBX" + "\n");
			if (op.equals(">")) {
				file.write("\tJNA " + ret + "\n");
				cmpWhileSave("<=", op1, op2);
			} else if (op.equals("<")) {
				file.write("\tJNB " + ret + "\n");
				cmpWhileSave(">=", op1, op2);
			} else if (op.equals(">=")) {
				file.write("\tJB " + ret + "\n");
				cmpWhileSave("<", op1, op2);
			} else if (op.equals("<=")) {
				file.write("\tJA " + ret + "\n");
				cmpWhileSave(">", op1, op2);
			} else if (op.equals("==")) {
				file.write("\tJNZ " + ret + "\n");
				cmpWhileSave("!=", op1, op2);
			} else {
				file.write("\tJZ " + ret + "\n");
				cmpWhileSave("==", op1, op2);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void cmpWhileSave(String op, String op1, String op2) {
		tmp.push(op2);
		tmp.push(op1);
		tmp.push(op);
	}

	public void cmpWhile(String ret) {
		String op = tmp.pop();
		String op1 = tmp.pop();
		String op2 = tmp.pop();
		compare(op, op1, op2, ret);
	}
}
