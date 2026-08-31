package com.stepupbackend.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "member_tbl")
public class Member {

    @Id
    @Column(name = "id", length = 50)
    private String id;

    @Column(name = "pw", nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "part", length = 100)
    private String part;

    @OneToMany(mappedBy = "author")
    private List<Board> boards = new ArrayList<>();

    @OneToMany(mappedBy = "author")
    private List<Comment> comments = new ArrayList<>();

    protected Member() {
    }

    public Member(String id, String passwordHash, String name, String part) {
        this.id = id;
        this.passwordHash = passwordHash;
        this.name = name;
        this.part = part;
    }

    public String getId() {
        return id;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getName() {
        return name;
    }

    public String getPart() {
        return part;
    }
}
