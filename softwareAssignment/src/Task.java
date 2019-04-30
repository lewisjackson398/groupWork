import java.security.Timestamp;

public class Task {
	
	private int id;
	private String title;
	private String priority;
	private boolean status;
	private int assignedTo;
	private String startDate;
	private String endDate;
	private int expectedTimeTaken;
	private String type;
	
	public Task(int id, String title, String type, String priority, boolean status,  int assignedTo, String startDate, String endDate, int expectedTimeTaken)
	{
		this.id = id;
		this.title = title;
		this.type = type;
		this.priority = priority;
		this.status = status;
		this.assignedTo = assignedTo;
		this.startDate = startDate;
		this.endDate = endDate;
		this.expectedTimeTaken = expectedTimeTaken;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(int assignedTo) {
		this.assignedTo = assignedTo;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public boolean getStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public int getExpectedTimeTaken() {
		return expectedTimeTaken;
	}

	public void setExpectedTimeTaken(int expectedTimeTaken) {
		this.expectedTimeTaken = expectedTimeTaken;
	}
	
	
}

