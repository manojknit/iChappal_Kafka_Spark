package com.sjsu.ichappal.Model;

public class UserProfile {


    private String Name;
    private String userid;
    private String age;
    private String gender;
    private String height;
    private String weight;



    public UserProfile(String name, String userid, String age, String gender, String height, String weight) {
        this.Name = name;
        this.userid = userid;
        this.age = age;
        this.gender = gender;
        this.height = height;
        this.weight = weight;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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


    @Override
    public String toString() {
        return "UserProfile{" +
                "Name='" + Name + '\'' +
                ", userid='" + userid + '\'' +
                ", age='" + age + '\'' +
                ", gender='" + gender + '\'' +
                ", height='" + height + '\'' +
                ", weight='" + weight + '\'' +
                '}';
    }






}
