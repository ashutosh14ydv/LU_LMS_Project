package com.lms;

import com.lms.dao.MemberDAO;
import com.lms.dao.UserDAO;
import com.lms.model.Member;
import java.sql.SQLException;

public class ComprehensiveTest {
    public static void main(String[] args) {
        System.out.println("=== COMPREHENSIVE TEST STARTING ===");
        
        UserDAO userDAO = new UserDAO();
        MemberDAO memberDAO = new MemberDAO();
        
        // SCENARIO 1: Registration of a NEW Student (should create member and user)
        String rnd = String.valueOf(System.currentTimeMillis()).substring(8);
        String user1 = "stu_" + rnd;
        String rn1 = "RN_" + rnd;
        testRegistration(userDAO, memberDAO, user1, "pass123", "STUDENT", "Full Name " + rnd, rn1);
        
        // SCENARIO 2: Registration of a Student using EXISTING Roll No (should link user to existing member)
        // Let's reuse rn1 from above
        String user2 = "stu2_" + rnd;
        testRegistration(userDAO, memberDAO, user2, "pass123", "STUDENT", "Full Name 2", rn1);

        // SCENARIO 3: Registration of a LIBRARIAN
        String user3 = "lib_" + rnd;
        testRegistration(userDAO, memberDAO, user3, "pass123", "LIBRARIAN", "", "");
        
        System.out.println("=== COMPREHENSIVE TEST ENDING ===");
    }

    private static void testRegistration(UserDAO userDAO, MemberDAO memberDAO, String user, String pass, String role, String fn, String rn) {
        System.out.println("\n[TEST] Attempting register: " + user + " as " + role);
        try {
            Integer memberId = null;
            if ("STUDENT".equals(role)) {
                if (rn.isEmpty()) {
                    System.out.println("[ERROR] Roll No required.");
                    return;
                }
                Member existing = memberDAO.getMemberByRollNo(rn);
                if (existing != null) {
                    System.out.println("[LOG] Found existing member with ID: " + existing.getId());
                    memberId = existing.getId();
                } else {
                    System.out.println("[LOG] Member not found. Creating new...");
                    if (fn.isEmpty()) {
                        System.out.println("[ERROR] Full Name required.");
                        return;
                    }
                    Member m = new Member(0, fn, user + "@mail.com", "", rn, "");
                    int generatedId = memberDAO.addMember(m);
                    if (generatedId <= 0) {
                        System.out.println("[ERROR] Database issue creating member.");
                        return;
                    }
                    System.out.println("[LOG] Created member with generated ID: " + generatedId);
                    memberId = generatedId;
                }
            }

            System.out.println("[LOG] Registering User with memberId: " + memberId);
            boolean success = userDAO.register(user, pass, role, memberId);
            System.out.println("[RESULT] SUCCESS: " + success);
            
        } catch (SQLException e) {
            System.out.println("[EXCEPTION] Caught SQLException:");
            e.printStackTrace();
        }
    }
}
