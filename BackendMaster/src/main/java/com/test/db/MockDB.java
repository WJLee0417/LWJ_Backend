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
        // 2. 게시글 더미 데이터 세팅 (공지 / 학습 / 자유 카테고리 적용)
        // =================================================================

        boardTable.put(boardSeq, new Board(boardSeq++, "공지", "📢 [공지사항] Backend Master 통합 게시판 오픈 및 실습 안내", 
            "안녕하세요! Backend Master 스터디 그룹에 오신 것을 환영합니다.\n\n" +
            "이곳은 그동안 배운 Servlet, JSP, MVC 패턴 등의 백엔드 기술을 직접 테스트하고 확인해 볼 수 있는 통합 실습 게시판입니다. " +
            "각 유저분들은 본인이 맡은 학습 주제를 하나씩 게시글로 잘 정리해 주시기 바랍니다.\n\n" +
            "다른 사람의 글은 함부로 삭제할 수 없도록 권한 로직이 적용되어 있으니 안심하고 작성해 주세요!\n\n" +
            "- 관리자 드림 -", 
            "admin"));

        boardTable.put(boardSeq, new Board(boardSeq++, "학습", "📚 [학습 기록] 1. Servlet (서블릿) 핵심 정리", 
            "자바(Java)를 사용하여 웹 페이지를 동적으로 생성하는 서버 측 프로그램입니다.\n\n" +
            "[📌 핵심 특징]\n" +
            "1. 동작 환경: 웹 서버가 처리하기 힘든 비즈니스 로직이나 DB 연동 등을 수행하기 위해 WAS(Tomcat) 위에서 동작합니다.\n" +
            "2. 생명주기(Lifecycle): \n" +
            "   - init(): 최초 1회 생성 시 초기화\n" +
            "   - service(): 클라이언트 요청 시 doGet() 또는 doPost()로 분기하여 처리\n" +
            "   - destroy(): 서버 종료 등 서블릿 소멸 시 자원 반납\n" +
            "3. 역할: MVC 패턴에서 Controller 역할을 수행하며 클라이언트의 요청을 가장 먼저 받습니다.", 
            "user01"));

        boardTable.put(boardSeq, new Board(boardSeq++, "학습", "📚 [학습 기록] 2. JSP 개념 및 동작 원리 완벽 해부", 
            "서블릿에서 자바 코드 안에 HTML을 직접 써야 했던 out.println()의 끔찍한 불편함을 해결하기 위해 등장했습니다. \"화면(HTML)은 자바 파일에서 분리하자!\"는 철학을 담고 있습니다.\n\n" +
            "[📌 동작 원리]\n" +
            "사용자가 JSP를 요청하면, 톰캣 내부에서 JSP 파일을 통째로 Servlet(.java)으로 변환하고 컴파일(.class)한 뒤에 실행됩니다. 즉, JSP의 본질은 결국 서블릿입니다!\n\n" +
            "[📌 스크립팅 요소]\n" +
            "- 선언부 (<%! %>): 전역 변수나 메서드 선언\n" +
            "- 스크립트릿 (<% %>): 일반적인 자바 로직 작성 (최근엔 최대한 지양함)\n" +
            "- 표현식 (<%= %>): 화면에 변수 값을 출력", 
            "user02"));

        boardTable.put(boardSeq, new Board(boardSeq++, "학습", "📚 [학습 기록] 3. Session & Cookie (HTTP의 기억상실증 극복)", 
            "HTTP 프로토콜은 상태를 저장하지 않는 무상태성(Stateless) 특징을 가집니다. 이를 극복하고 '서버가 사용자를 기억하게 만드는' 두 가지 방법입니다.\n\n" +
            "[🍪 Cookie (쿠키)]\n" +
            "- 저장 위치: 클라이언트(브라우저)\n" +
            "- 형태: 문자열(String)만 저장 가능\n" +
            "- 특징: 보안에 취약하므로 '아이디 저장'이나 '오늘 하루 창 열지 않음' 등에 사용됩니다.\n\n" +
            "[🗄️ Session (세션)]\n" +
            "- 저장 위치: 서버의 안전한 메모리 공간\n" +
            "- 형태: 자바의 모든 객체(Object) 저장 가능\n" +
            "- 특징: 브라우저에는 'JSESSIONID'라는 식별표(쿠키)만 쥐어주고, 실제 데이터는 서버가 가집니다. '로그인 상태 유지'의 핵심 기술입니다.", 
            "user03"));

        boardTable.put(boardSeq, new Board(boardSeq++, "학습", "📚 [학습 기록] 4. EL과 JSTL (JSP 화면 로직 분리)", 
            "JSP 화면에서 꺾쇠와 퍼센트 기호(<% %>)가 난무하는 자바 코드를 걷어내기 위한 구원 투수들입니다. View 레이어를 훨씬 우아하게 만들어줍니다.\n\n" +
            "[✨ EL (Expression Language)]\n" +
            "- 문법: ${ 데이터이름 }\n" +
            "- 특징: 4가지 내장 객체 영역(page, request, session, application)을 좁은 곳부터 넓은 곳 순서대로 탐색하여 데이터를 직관적으로 화면에 출력합니다.\n\n" +
            "[🛠️ JSTL (JSP Standard Tag Library)]\n" +
            "- 문법: <c:if>, <c:forEach> 등\n" +
            "- 특징: 자바의 조건문이나 반복문을 HTML 태그처럼 사용할 수 있게 해줍니다. 외부 라이브러리이므로 반드시 pom.xml이나 lib 폴더에 jar 파일을 추가해야 동작합니다.", 
            "user04"));

        boardTable.put(boardSeq, new Board(boardSeq++, "학습", "📚 [학습 기록] 5. Filter & Listener (보안과 전역 제어)", 
            "서버의 백그라운드에서 조용히 활약하는 고급 제어 기법입니다.\n\n" +
            "[🛡️ Filter (필터)]\n" +
            "- 역할: 클라이언트의 요청이 목적지(Servlet)에 닿기 전, 또는 응답이 나가기 전에 가로채어 공통 작업을 수행합니다.\n" +
            "- 활용: 전역 UTF-8 인코딩 적용, 로그인하지 않은 사용자의 게시판 접근 차단, 응답 시간 성능 측정 등\n\n" +
            "[🎧 Listener (리스너)]\n" +
            "- 역할: 웹 애플리케이션의 시작/종료, 세션의 생성/소멸 등 특정 '이벤트'를 감지하고 반응합니다.\n" +
            "- 활용: 서버 기동 시 DB 설정 불러오기, 현재 접속 중인 동시 사용자 수 카운트 등", 
            "user05"));

        // =================================================================
        // 📌 추가된 '자유' 게시판 더미 데이터
        // =================================================================

        boardTable.put(boardSeq, new Board(boardSeq++, "자유", "💬 다들 백엔드 공부 잘 되가시나요?", 
            "JSP 동작 원리랑 Filter 부분 넘어오니까 슬슬 헷갈리기 시작하네요 ㅠㅠ\n" +
            "특히 Session이랑 Cookie 넘나드는 부분이 제일 어려운 것 같아요.\n\n" +
            "다들 복습 어떻게 하고 계신가요? 팁 좀 공유해주세요! ☕️", 
            "user03"));
            
        boardTable.put(boardSeq, new Board(boardSeq++, "자유", "💬 이번 주 스터디 모임 시간 변경 안내!", 
            "이번 주 금요일 디스코드 스터디 모임 시간이 저녁 7시에서 8시로 1시간 미뤄졌습니다.\n\n" +
            "참고하셔서 늦지 않게 접속해 주세요! 다들 화이팅입니다 💪", 
            "user01"));
    }
}