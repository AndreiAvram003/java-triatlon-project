package org.example;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RefereeUpdateDTO {
    private long id;
    private String password;
    @JsonProperty("trial_id")
    private long trial_id;

    private String name;

    // Getters and setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getTrialId() {
        return trial_id;
    }

    public void setTrialId(long trial_id) {
        this.trial_id = trial_id;
    }
}
