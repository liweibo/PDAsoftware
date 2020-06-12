package com.crrc.pdasoftware.net;

public class Constant {
    static public String usualInterfaceAddr = "https://appagent2.csrzic.com/10000000/public/mro/common";
    static public String usualKey = "params";
    static public String usualRequParaJson = "{\"methodname\":\"toQueryMroData\",\n" +
            "\"parameter\":\n" +
            "{\n" +
            "\"userid\":\"20102230\",\n" +
            "\"tablename\":\"workorder\",\n" +
            "\"where\":\"status in ('草稿','已派工')\",\n" +
            "\"orderby\":\"ordernum desc\",\n" +
            "\"startrownum\":\"0\",\n" +
            "\"maxrownum\":\"10\"\n" +
            "}\n" +
            "}";

}
