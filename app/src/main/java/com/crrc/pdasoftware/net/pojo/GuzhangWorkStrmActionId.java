package com.crrc.pdasoftware.net.pojo;

import java.util.List;

public class GuzhangWorkStrmActionId {

    /**
     * code : S
     * data : [{"actionid":"1","actiondesc":"提交审核"}]
     * msg : 操作成功
     */

    public String code;
    public String msg;
    public List<DataBean> data;

    public static class DataBean {
        /**
         * actionid : 1
         * actiondesc : 提交审核
         */

        public String actionid;
        public String actiondesc;
    }
}
