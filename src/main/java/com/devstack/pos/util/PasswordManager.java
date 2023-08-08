package com.devstack.pos.util;


import org.mindrot.jbcrypt.BCrypt;

public class PasswordManager {
    public static String encriptPassword(String plainText){
        return BCrypt.hashpw(plainText,BCrypt.gensalt(10));
    }
    public static boolean checkPassword(String plainText,String hash){
        return BCrypt.checkpw(plainText, hash);
    }
}
