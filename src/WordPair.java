
public class WordPair {
	private int key;
	private String value;

	public WordPair(int key, String value) {
		// TODO Auto-generated constructor stub
		this.key = key;
		this.value = value;
	}

	public int getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

	public boolean equal(int key) {
		if (this.key == key) {
			return true;
		} else {
			return false;
		}
	}
}
