package com.test.db;

import java.util.HashMap;
import java.util.Map;

import com.test.dto.Board;
import com.test.dto.Member;

public class MockDB {
    
    // 1. 회원 테이블 (Key: 아이디, Value: 회원 객체)
    // 어디서든 접근할 수 있도록 public static 선언
    public static final Map<String, Member> memberTable = new HashMap<>();

    // 2. 게시판 테이블 (Key: 글 번호, Value: 게시글 객체)
    public static final Map<Integer, Board> boardTable = new HashMap<>();

    // 클래스가 메모리에 로드될 때 딱 한 번 실행되어 초기 데이터를 채워 넣는 static 블록
    static {
        System.out.println("====== MockDB 초기화 시작 ======");
        
        // --- 회원 가짜 데이터 셋업 ---
        memberTable.put("admin", new Member("admin", "1234", "관리자"));
        memberTable.put("user01", new Member("user01", "1111", "홍길동"));
        memberTable.put("user02", new Member("user02", "2222", "이순신"));

        // --- 게시글 가짜 데이터 셋업 ---
        boardTable.put(1, new Board(1, "첫 번째 게시글입니다", "공지사항입니다. 모두 필독해주세요.", "admin"));
        boardTable.put(2, new Board(2, "가입 인사드립니다!", "안녕하세요, 백엔드 마스터를 꿈꾸는 홍길동입니다.", "user01"));
        boardTable.put(3, new Board(3, "JSP 너무 재밌네요", "EL이랑 JSTL 쓰니까 자바 코드가 없어져서 깔끔해요.", "user02"));
        boardTable.put(4, new Board(4, "질문 있습니다!", "Session과 Cookie의 차이점이 정확히 뭔가요?", "user01"));
        
        System.out.println("====== MockDB 초기화 완료 ======");
    }
}