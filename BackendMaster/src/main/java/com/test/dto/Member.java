package com.test.dto;

public class Member {
    private String id;
    private String password;
    private String name;

    // 기본 생성자 (필수)
    public Member() {}

    // 데이터 초기화를 편하게 하기 위한 생성자
    public Member(String id, String password, String name) {
        this.id = id;
        this.password = password;
        this.name = name;
    }

    // --- Getter & Setter ---
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}