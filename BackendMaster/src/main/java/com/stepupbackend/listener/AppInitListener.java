package com.stepupbackend.listener;

import com.stepupbackend.dao.MemberDAO;
import com.stepupbackend.dto.Member;
import com.stepupbackend.util.PasswordUtil;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppInitListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        MemberDAO dao = new MemberDAO();
        // ADMIN_INITIAL_PASSWORD가 설정된 개발 환경에서만 초기 관리자 계정을 생성한다.
        if (dao.getMemberById("admin") == null) {
            String initialPassword = System.getenv("ADMIN_INITIAL_PASSWORD");
            if (initialPassword == null || initialPassword.isBlank()) {
                System.out.println(">>> [시스템] ADMIN_INITIAL_PASSWORD가 없어 관리자 계정을 생성하지 않았습니다.");
                return;
            }

            String hashedPw = PasswordUtil.hashPassword(initialPassword);
            Member admin = new Member("admin", hashedPw, "마스터관리자", "시스템관리");
            dao.insertMember(admin);
            System.out.println(">>> [시스템] 관리자 계정이 자동으로 생성되었습니다.");
        }
    }
}
