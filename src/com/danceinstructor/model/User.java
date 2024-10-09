package com.danceinstructor.model;

public class User {
    private String name;
    private String skillLevel;

    public User() {
        this.name = "Default User";
        this.skillLevel = "Beginner";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSkillLevel() {
        return skillLevel;
    }

    public void setSkillLevel(String skillLevel) {
        this.skillLevel = skillLevel;
    }
}
