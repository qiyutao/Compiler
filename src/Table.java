import java.awt.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Table {
	private HashMap<String, Integer> map = null;

	public Table() {
		map = new HashMap<String, Integer>();

		this.createTable();
	}

	private void createTable() {
		map.put("main", 1);
		map.put("int", 2);
		map.put("char", 3);
		map.put("if", 4);
		map.put("else", 5);
		map.put("for", 6);
		map.put("while", 7);
		map.put("ID", 10);
		map.put("NUM", 20);
		map.put("=", 21);
		map.put("+", 22);
		map.put("-", 23);
		map.put("*", 24);
		map.put("/", 25);
		map.put("(", 26);
		map.put(")", 27);
		map.put("[", 28);
		map.put("]", 29);
		map.put("{", 30);
		map.put("}", 31);
		map.put(",", 32);
		map.put(":", 33);
		map.put(";", 34);
		map.put(">", 35);
		map.put("<", 36);
		map.put(">=", 37);
		map.put("<=", 38);
		map.put("==", 39);
		map.put("!=", 40);
		map.put("'\0'", 1000);
		map.put("ERROR", -1);

	}

	public HashMap<String, Integer> getMap() {
		return map;
	}
}
