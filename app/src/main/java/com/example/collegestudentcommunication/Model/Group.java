package com.example.collegestudentcommunication.Model;

import java.util.List;

public class Group {
    private String code;
    private String imageLogo;
    private String name;
    private String id;
    private List<Participants> participants;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Participants> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Participants> participants) {
        this.participants = participants;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getImageLogo() {
        return imageLogo;
    }

    public void setImageLogo(String imageLogo) {
        this.imageLogo = imageLogo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Group() {
    }

    public Group(String code, String imageLogo, String name, String id, List<Participants> participants) {
        this.code = code;
        this.imageLogo = imageLogo;
        this.name = name;
        this.id = id;
        this.participants = participants;
    }
}
