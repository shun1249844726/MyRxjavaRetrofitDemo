package com.lexinsmart.cms.myrxjavaretrofitdemo;

/**
 * Created by xushun on 2017/7/13.
 */

public class LoginResult {
    /**
     * success : 0
     * msg : User authorize success!
     * data : {"token":"eyJ1c2VybmFtZSI6ICJ4dXNodW4iLCAiZXhwIjogIjIwMTctMDctMjMgMDY6NTc6MTIifQ=="}
     */

    private int success;
    private String msg;
    private DataBean data;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * token : eyJ1c2VybmFtZSI6ICJ4dXNodW4iLCAiZXhwIjogIjIwMTctMDctMjMgMDY6NTc6MTIifQ==
         */

        private String token;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
