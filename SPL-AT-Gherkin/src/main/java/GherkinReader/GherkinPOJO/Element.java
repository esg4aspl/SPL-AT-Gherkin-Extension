package GherkinReader.GherkinPOJO;


/**
 * Scenario Outline.
 * @author sercansensulun
 *
 */
public class Element {

	private Step[] steps;
	private Example[] examples;
	private String name;
	private String id;

	public Step[] getSteps() {
		return steps;
	}

	public void setSteps(Step[] steps) {
		this.steps = steps;
	}

	public String getName() {
		return name.replaceAll("\\s+", "")+"Test";
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Example[] getExamples() {
		return examples;
	}

	public void setExamples(Example[] examples) {
		this.examples = examples;
	}
	
}
