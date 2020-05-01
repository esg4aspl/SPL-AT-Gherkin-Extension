package GherkinReader.GherkinPOJO;

/**
 * @author sercansensulun
 */
public class Row {

	public String id;
	
	public String[] cells;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String[] getCells() {
		return cells;
	}

	public void setCells(String[] cells) {
		this.cells = cells;
	}
}
