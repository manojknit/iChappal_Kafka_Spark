package sjsu.cohort9.chappal.subscriber.entity;

public class UserProfile {
	String Name;
	String age;
	String height;
	String weight;
	String userid;
	String gender;
	
	String heartRate = "105";
	String bodyTemp = "40";
	
	public UserProfile(String name, String age, String height, String weight, String userid) {
		super();
		Name = name;
		this.age = age;
		this.height = height;
		this.weight = weight;
		this.userid = userid;
	}


	@Override
	public String toString() {
		return "UserProfile [Name=" + Name + ", age=" + age + ", height=" + height + ", weight=" + weight + ", userid="
				+ userid + ", gender=" + gender + ", heartRate=" + heartRate + ", bodyTemp=" + bodyTemp + "]";
	}





	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	

	public String getHeartRate() {
		return heartRate;
	}

	public String getBodyTemp() {
		return bodyTemp;
	}
	
	public String getGender() {
		return gender;
	}


	public void setGender(String gender) {
		this.gender = gender;
	}

	public UserProfile() {
		
	}
	
}
