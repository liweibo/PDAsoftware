package com.crrc.pdasoftware.utils;

public class FiledDataSave {
    public static int whichTab = -1;
    public static int whichPos = -1;
    public static boolean fwxyBtnEffect = false;
    public static boolean ddxcBtnEffect = false;
    public static boolean gzclBtnEffect = false;
    public static boolean hjBtnEffect = false;
    public static boolean guaqiBtnEffect = false;

    //FAILURELIB表中的字段
    public static String WHICHSTATION = "";//故障处理站点
    public static String FAULTCONSEQ = "";//故障后果
    public static String FAILUREDESC = "";//故障名称
    public static String CARSECTIONNUM = "";//车厢号  是failurelib中的

    public static String FAILURECODE = "";//故障代码
    public static String PRODUCTNICKNAME = "";//故障设备
    public static String FAULTTIME = "";//故障发生时间（）
    public static String FINDPROCESS = "";//发生阶段（）
    public static String RUNNINGMODE = "";//运行模式
    public static String QYFZDW = "";//牵引吨位（）
    public static String FAILWEATHER = "";//天气（）
    public static String ROADTYPE = "";//路况（）
    public static String FAULTDATAREC = "";//上传状态


    public static String FAULTQUALIT = "";//客户定责
    public static String NODATAREASON = "";// 无数据原因
    public static String FAULTDESC = "";//故障现象
    public static String PREREASONALYS = "";// 初步分析 即故障原因
    public static String DEALMEASURE = "";// 处理措施
    public static String FAULTCOMPONENTNAME = "";//  主故障件名称
    public static String ANALYSISREPNE = "";//客户需分析报告
    public static String DEALMETHOD = "";//处理方式



    public static String FAILURELIBID = "";
    public static String UNIQUEID = "";

    //workorder表中的字段
    public static String RUNKILOMETRE = "";//累计走行公里

}
