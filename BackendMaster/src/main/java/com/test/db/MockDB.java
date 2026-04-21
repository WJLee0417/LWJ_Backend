package com.test.db;

import java.util.HashMap;
import java.util.Map;
import com.test.dto.Board;
import com.test.dto.Member;

public class MockDB {
    public static Map<String, Member> memberTable = new HashMap<>();
    public static Map<Integer, Board> boardTable = new HashMap<>();
    public static int boardSeq = 1; // 게시글 번호 자동 증가용 변수

    static {
        // 1. 회원 더미 데이터 세팅 (유저 5명 + 관리자 1명)
        memberTable.put("admin", new Member("admin", "1234", "관리자"));
        memberTable.put("user01", new Member("user01", "1111", "백엔드초보"));
        memberTable.put("user02", new Member("user02", "2222", "JSP마스터"));
        memberTable.put("user03", new Member("user03", "3333", "쿠키도둑"));
        memberTable.put("user04", new Member("user04", "4444", "EL매니아"));
        memberTable.put("user05", new Member("user05", "5555", "필터장인"));

        // =================================================================
        // 2. 게시글 더미 데이터 세팅 (공지사항 + 5인 5색 스터디 기록)
        // =================================================================

        // 📢 관리자(admin)의 공지사항
        boardTable.put(boardSeq, new Board(boardSeq++, "📢 [공지사항] Backend Master 통합 게시판 오픈 및 실습 안내", 
            "안녕하세요! Backend Master 스터디 그룹에 오신 것을 환영합니다.\n\n" +
            "이곳은 그동안 배운 Servlet, JSP, MVC 패턴 등의 백엔드 기술을 직접 테스트하고 확인해 볼 수 있는 통합 실습 게시판입니다. " +
            "각 유저분들은 본인이 맡은 학습 주제를 하나씩 게시글로 잘 정리해 주시기 바랍니다.\n\n" +
            "다른 사람의 글은 함부로 삭제할 수 없도록 권한 로직이 적용되어 있으니 안심하고 작성해 주세요!\n\n" +
            "- 관리자 드림 -", 
            "admin"));

        // 📝 user01 ~ user05의 학습 기록 릴레이
        boardTable.put(boardSeq, new Board(boardSeq++, "📚 [학습 기록] 1. Servlet (서블릿) 기초", 
            "자바(Java)를 사용하여 웹 페이지를 동적으로 생성하는 서버 측 프로그램입니다.\n\n" +
            "웹 서버가 처리하기 힘든 비즈니스 로직이나 DB 연동 등을 수행하기 위해 WAS(Tomcat) 위에서 동작합니다. 클라이언트의 요청(Request)을 받아 처리하고 응답(Response)을 돌려주는 백엔드의 가장 기본적인 뼈대입니다!", 
            "user01"));

        boardTable.put(boardSeq, new Board(boardSeq++, "📚 [학습 기록] 2. JSP 개념 및 동작 원리", 
            "서블릿에서 자바 코드 안에 HTML을 직접 써야 했던 out.println()의 끔찍한 불편함을 해결하기 위해 등장했습니다.\n\n" +
            "\"화면을 그리는 작업(HTML)은 자바 파일에서 완전히 분리해서 따로 관리하자!\"라는 웹 개발의 핵심 철학을 실현하며, 실행 시에는 최종적으로 서블릿으로 변환되어 구동됩니다.", 
            "user02"));

        boardTable.put(boardSeq, new Board(boardSeq++, "📚 [학습 기록] 3. Session & Cookie (상태 관리)", 
            "HTTP 프로토콜의 '기억상실증(Stateless)'을 극복하고, \"서버가 사용자를 어떻게 기억할 것인가?\"에 대한 해답입니다.\n\n" +
            "- Cookie: 브라우저(클라이언트)에 저장되는 가벼운 정보 (예: 아이디 저장 기능에 활용)\n" +
            "- Session: 서버 내부의 안전한 메모리 공간에 저장되는 정보 (예: 로그인 유저 상태 유지에 활용)", 
            "user03"));

        boardTable.put(boardSeq, new Board(boardSeq++, "📚 [학습 기록] 4. EL과 JSTL (화면 로직 분리)", 
            "JSP 화면에서 꺾쇠와 퍼센트 기호(<% %>)가 난무하는 자바 코드를 걷어내기 위한 구원 투수입니다.\n\n" +
            "- EL (${ }): 데이터를 직관적이고 간결하게 출력\n" +
            "- JSTL (<c:forEach> 등): 반복문이나 조건문을 HTML 태그처럼 우아하게 작성할 수 있게 도와줍니다. MVC 패턴 뷰(View)의 핵심!", 
            "user04"));

        boardTable.put(boardSeq, new Board(boardSeq++, "📚 [학습 기록] 5. Filter & Listener (보안과 감시)", 
            "서버의 백그라운드에서 조용히 활약하는 고급 제어 기법입니다.\n\n" +
            "- Filter: 요청이 서블릿에 닿기 전/후를 가로채어 공통 작업(UTF-8 인코딩, 로그인 권한 체크, 성능 측정)을 일괄 처리합니다.\n" +
            "- Listener: 웹 애플리케이션이나 세션의 생성/소멸 이벤트를 감지하는 감시자 역할을 합니다.", 
            "user05"));
    }
}