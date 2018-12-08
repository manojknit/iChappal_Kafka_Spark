package sjsu.cohort9.chappal.demo.entiry;

import java.util.Date;

public class StepsEO {
	String userId;
	Date dateTime;
	int step;
		
	StepsEO() {
		
	}
	
	public StepsEO(String userId, Date dateTime, int step) {
		super();
		this.userId = userId;
		this.dateTime = dateTime;
		this.step = step;
	}

	@Override
	public String toString() {
		return "StepsEO [userId=" + userId + ", dateTime=" + dateTime + ", step=" + step 
				+ "]";
	}

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public int getStep() {
		return step;
	}
	public void setStep(int step) {
		this.step = step;
	}
	
	
}
