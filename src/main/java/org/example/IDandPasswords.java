package org.example;

import java.util.HashMap;

public class IDandPasswords {
    HashMap<String, String> loginInfo = new HashMap<String, String>();
    IDandPasswords() {
        loginInfo.put("Odil", "nomer1");
        loginInfo.put("Odil1", "nomer2");
        loginInfo.put("Odil2", "nomer3");
    }
    public HashMap getLoginInfo() {
        return loginInfo;
    }

}
