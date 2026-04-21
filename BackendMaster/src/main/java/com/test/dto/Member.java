package com.test.dto;

public class Member {
    private String id;
    private String pw;    // 암호화된 비밀번호가 저장될 필드
    private String name;
    private String part;  // 담당 학습 주제

    // 1. 객체 생성 시 사용하는 생성자
    public Member(String id, String pw, String name, String part) {
        this.id = id;
        this.pw = pw;
        this.name = name;
        this.part = part;
    }

    // 2. 값을 꺼내올 때 사용하는 Getter
    public String getId() { return id; }
    public String getPw() { return pw; }
    public String getName() { return name; }
    public String getPart() { return part; }

    // 🚀 3. 추후 '비밀번호 변경' 및 '정보 수정' 기능을 위한 Setter 추가
    public void setPw(String pw) { 
        this.pw = pw; 
    }
    
    public void setName(String name) { 
        this.name = name; 
    }
    
    public void setPart(String part) { 
        this.part = part; 
    }
    
    // (참고: id는 보통 가입 후 변경하지 않으므로 setId는 만들지 않는 것이 안전합니다.)
}