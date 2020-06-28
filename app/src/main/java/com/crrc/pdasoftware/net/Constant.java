package com.crrc.pdasoftware.net;

public class Constant {
    static public String usualInterfaceAddr = "https://appagent2.csrzic.com/10000000/public/mro/common";
    static public String usualKey = "params";

    static public String x = "";//最终都要用同一个x
    static public String y = "";//测试用
    static public String z = "";//测试用

    static public String userId = "";
    static public String extraBiaoGdbh = "";//不在故障表中的字段，


    static public String ActionId = "1";
    static public String appContrl = "";//如：FAILUREORD
    static public String whichTable = "";//如：workorder
    static public String failurelibTable = "FAILURELIB";

    static public String uniqueId = "";//中文是 唯一标识。英文在故障中为：WORKORDERID
    static public String yijian = "";
    static public String keyValuePosttomro = "";//格式：=""key1":"v1","k2":"v2""

    //危险源审批的值。
    static public String keyValueWeixianValuePosttomro = "\"" + "ISHAZARDTASK\":\"是\",\"ISHAZARDTASKAPPROVE\":\"是\"";//格式：=""key1":"v1","k2":"v2""
    static public String keyValueDaodaxcPosttomroWorkorder = "";//格式：=""key1":"v1","k2":"v2""
    static public String keyValueDaodaxcfailurelibPosttomro = "";//格式：=""key1":"v1","k2":"v2""

    //服务响应tab数据列表

    public static String getFwxyreq() {
        String ss = "{\"methodname\":\"toQueryMroData\",\n" +
                "\"parameter\":\n" +
                "{\n" +
                "\"userid\":\"20015017\",\n" +
                "\"tablename\":\"workorder\",\n" +
                "\"where\":\"workorderid in (select business_key_ from act_ru_execution t where proc_inst_id_ in (select proc_inst_id_ from ACT_RU_TASK where  assignee_='20015017')) and type='故障' and SERVENGINEER='20015017'\",\n" +
                "\"orderby\":\"ordernum desc\",\n" +
                "\"startrownum\":\"0\",\n" +
                "\"maxrownum\":\"100\"\n" +
                "}\n" +
                "}\n";
        String s = "{\"methodname\":\"toQueryMroData\",\n" +
                "\"parameter\":\n" +
                "{\n" +
                "\"userid\":\"" +
                userId
                +
                "\",\n" +
                "\"tablename\":\"workorder\",\n" +
                "\"where\":\"workorderid in (select business_key_ from act_ru_execution t where proc_inst_id_ in (select proc_inst_id_ from ACT_RU_TASK where  assignee_=" +
                x +
                "))" +
                " and type='故障' and SERVENGINEER=" +
                x +
                "\",\n" +
                "\"orderby\":\"ordernum desc\",\n" +
                "\"startrownum\":\"0\",\n" +
                "\"maxrownum\":\"100\"\n" +
                "}\n" +
                "}\n";

        return s;
    }

    public static String getFwxyreq2() {
        String s = "{\"methodname\":\"toQueryMroData\",\n" +
                "\"parameter\":\n" +
                "{\n" +
                "\"userid\":\"" +
                userId
                +
                "\",\n" +
                "\"tablename\":\"workorder\",\n" +
                "\"where\":\" type='故障' and status='草稿' and SERVENGINEER=" +
                y
                +
                "\",\n" +
                "\"orderby\":\"ordernum desc\",\n" +
                "\"startrownum\":\"0\",\n" +
                "\"maxrownum\":\"10\"\n" +
                "}\n" +
                "}\n";

        return s;
    }

    //未完成tab
    public static String getWeiwc() {
        String s = "{\"methodname\":\"toQueryMroData\",\n" +
                "\"parameter\":\n" +
                "{\n" +
                "\"userid\":\"" +
                userId
                +
                "\",\n" +
                "\"tablename\":\"workorder\",\n" +
                "\"where\":\"workorderid in (select business_key_ from act_ru_execution t where proc_inst_id_ in (select procinstid from act_hi_assign where  assignee=" +
                x
                +
                ")) and type='故障' and status <> '关闭'\",\n" +
                "\"orderby\":\"ordernum desc\",\n" +
                "\"startrownum\":\"0\",\n" +
                "\"maxrownum\":\"10\"\n" +
                "}\n" +
                "}\n";
        return s;
    }


    //已完成tab
    public static String getYiwc() {
        String s = "{\"methodname\":\"toQueryMroData\",\n" +
                "\"parameter\":\n" +
                "{\n" +
                "\"userid\":\"" +
                userId
                +
                "\",\n" +
                "\"tablename\":\"workorder\",\n" +
                "\"where\":\"workorderid in (select business_key_ from act_ru_execution t where proc_inst_id_ in (select procinstid from act_hi_assign where  assignee=" +
                z
                +
                ")) and type='故障' and status='关闭'\",\n" +
                "\"orderby\":\"ordernum desc\",\n" +
                "\"startrownum\":\"0\",\n" +
                "\"maxrownum\":\"10\"\n" +
                "}\n" +
                "}";
        return s;
    }

    //发起工作流
    public static String getStatMroWorkLiu() {
        String s = "{\"methodname\":\"startMroWF\",\n" +
                "\"parameter\":\n" +
                "{\n" +
                "\"userid\":\"" +
                userId +
                "\",\n" +
                "\"app\":\"" +
                appContrl +
                "\",\n" +
                "\"tablename\":\"" +
                whichTable +
                "\",\n" +
                "\"uniqueid\":\"" +
                uniqueId +
                "\"\n" +
                "}}";
        return s;
    }

    //不在故障表中的字段
    public static String getExtraBiao() {
        String s = "{\"methodname\":\"toQuerySqlData\",\n" +
                "\"parameter\":\n" +
                "{\n" +
                "\"userid\":\"" +
                userId +
                "\",\n" +
                "\"sql\":\"select  *  from pda_failureorder where type='故障' and ordernum=" +
                extraBiaoGdbh +
                " \"\n" +
                "}\n" +
                "}";
        return s;
    }

    //拿到工作流actionid
    public static String getGZWorkStreamActionId() {
        String s = "{\"methodname\":\"nextMroWFAction\",\n" +
                "\"parameter\":\n" +
                "{\n" +
                "\"userid\":\"" +
                userId
                +
                "\",\n" +
                "\"app\":\"" +
                appContrl
                +
                "\",\n" +
                "\"tablename\":\"" +
                whichTable
                +
                "\",\n" +
                "\"uniqueid\":\"" +
                uniqueId
                +
                "\"\n" +
                "}}\n";
        return s;
    }


    //服务响应中改派工作流
    public static String getGZGaipaiWorkStream() {
        String s = "{\"methodname\":\"exNextMroWFAction\",\n" +
                "\"parameter\":\n" +
                "{\n" +
                "\"userid\":\"" +
                userId
                +
                "\",\n" +
                "\"app\":\"" +
                appContrl
                +

                "\",\n" +
                "\"tablename\":\"" +

                whichTable
                +
                "\",\n" +
                "\"uniqueid\":\"" +

                uniqueId +

                "\",\n" +
                "\"actionid\":\"" +

                "2" +//actionid = 1表示请求处理  为2表示改派。

                "\",\n" +
                "\"memo\":\"" +

                yijian
                +
                "\"\n" +
                "}}";
        return s;
    }


    //服务响应申请处理工作流
    public static String getFaqiFwxyWorkStream() {
        String s = "{\"methodname\":\"exNextMroWFAction\",\n" +
                "\"parameter\":\n" +
                "{\n" +
                "\"userid\":\"" +
                userId
                +
                "\",\n" +
                "\"app\":\"" +
                appContrl
                +

                "\",\n" +
                "\"tablename\":\"" +

                whichTable
                +
                "\",\n" +
                "\"uniqueid\":\"" +

                uniqueId +

                "\",\n" +
                "\"actionid\":\"" +

                "1" +//actionid = 1表示请求处理  为2表示改派。
                "\"\n" +
                "}}";
        return s;
    }


    //发送危险源的值
    public static String getPostWeixianvalueInfo() {
        String s = "{\"methodname\":\"toUpdateMroData\",\n" +
                "\"parameter\":\n" +
                "{\n" +
                "\"userid\":\"" +

                userId
                +
                "\",\n" +
                "\"tablename\":\"" +

                whichTable +
                "\",\n" +
                "\"uniqueid\":\"" +
                "" +

                uniqueId
                +
                "\",\n" +
                "\"data\":{\n" +

                keyValueWeixianValuePosttomro
                +
                "\n" +
                "}\n" +
                "}\n" +
                "}";
        return s;
    }


    public static String getFuxyPostInfo() {
        String s = "{\"methodname\":\"toUpdateMroData\",\n" +
                "\"parameter\":\n" +
                "{\n" +
                "\"userid\":\"" +

                userId
                +
                "\",\n" +
                "\"tablename\":\"" +

                whichTable +
                "\",\n" +
                "\"uniqueid\":\"" +
                "" +

                uniqueId
                +
                "\",\n" +
                "\"data\":{\n" +

                keyValuePosttomro
                +
                "\n" +
                "}\n" +
                "}\n" +
                "}";
        return s;
    }


    //到达现场提交数据至workorder表中
    public static String getdaodaxcPostInfoToWorkorder() {
        String s = "{\"methodname\":\"toUpdateMroData\",\n" +
                "\"parameter\":\n" +
                "{\n" +
                "\"userid\":\"" +

                userId
                +
                "\",\n" +
                "\"tablename\":\"" +

                whichTable +
                "\",\n" +
                "\"uniqueid\":\"" +
                "" +

                uniqueId
                +
                "\",\n" +
                "\"data\":{\n" +

                keyValueDaodaxcPosttomroWorkorder
                +
                "\n" +
                "}\n" +
                "}\n" +
                "}";
        return s;
    }

    //到达现场提交数据到Failurelib表
    public static String getdaodaxcPostInfoToFailurelib() {
        String s = "{\"methodname\":\"toUpdateMroData\",\n" +
                "\"parameter\":\n" +
                "{\n" +
                "\"userid\":\"" +

                userId
                +
                "\",\n" +
                "\"tablename\":\"" +

                failurelibTable +
                "\",\n" +
                "\"uniqueid\":\"" +
                "" +

                uniqueId
                +
                "\",\n" +
                "\"data\":{\n" +

                keyValueDaodaxcfailurelibPosttomro
                +
                "\n" +
                "}\n" +
                "}\n" +
                "}";
        return s;
    }


    public static String getPaigongPostInfo() {
        String s = "{\"methodname\":\"toUpdateMroData\",\n" +
                "\"parameter\":\n" +
                "{\n" +
                "\"userid\":\"" +

                userId
                +
                "\",\n" +
                "\"tablename\":\"" +

                whichTable +
                "\",\n" +
                "\"uniqueid\":\"" +
                "" +

                uniqueId
                +
                "\",\n" +
                "\"data\":{\n" +

                keyValuePosttomro
                +
                "\n" +
                "}\n" +
                "}\n" +
                "}";
        return s;
    }

}
