package com.test.listener;

import com.test.dao.MemberDAO;
import com.test.dto.Member;
import com.test.util.PasswordUtil;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppInitListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        MemberDAO dao = new MemberDAO();
        // 1. admin 계정이 이미 있는지 확인
        if (dao.getMemberById("admin") == null) {
            // 2. 없다면 비밀번호 1234인 관리자 계정 생성
            String hashedPw = PasswordUtil.hashPassword("1234");
            Member admin = new Member("admin", hashedPw, "마스터관리자", "시스템관리");
            dao.insertMember(admin);
            System.out.println(">>> [시스템] 관리자 계정이 자동으로 생성되었습니다.");
        }
    }
}
