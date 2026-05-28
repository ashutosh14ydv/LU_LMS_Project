package com.lms;

import com.lms.dao.UserDAO;
import com.lms.dao.MemberDAO;
import com.lms.model.Member;
import java.sql.SQLException;

public class TestRegister {
    public static void main(String[] args) {
        System.out.println("Testing registration logic...");
        UserDAO userDAO = new UserDAO();
        MemberDAO memberDAO = new MemberDAO();
        
        String user = "testuser_" + System.currentTimeMillis();
        String pass = "testpass";
        String role = "STUDENT";
        String fn = "Test Full Name";
        String rn = "RN" + System.currentTimeMillis();
        
        try {
            System.out.println("Step 1: Checking existing member by roll no: " + rn);
            Member existing = memberDAO.getMemberByRollNo(rn);
            Integer memberId = null;
            
            if (existing != null) {
                System.out.println("Found existing member.");
                memberId = existing.getId();
            } else {
                System.out.println("No existing member. Creating new profile.");
                Member m = new Member(0, fn, user + "@mail.com", "", rn, "");
                int generatedId = memberDAO.addMember(m);
                System.out.println("Generated Member ID: " + generatedId);
                memberId = generatedId;
            }
            
            System.out.println("Step 2: Registering User associated with Member ID: " + memberId);
            boolean success = userDAO.register(user, pass, role, memberId);
            System.out.println("Registration Success: " + success);
            
        } catch (SQLException e) {
            System.out.println("Caught SQL Exception during test:");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Caught generic exception:");
            e.printStackTrace();
        }
    }
}
