package com.crrc.pdasoftware.utils.zhuangxiangdandata;


public class XianchangfachuDataInfo {

    /**
     * 单编号
     */
    private String Gdbh = "工单编号 - 102488";
    /**
     * 时间
     */
    private String Time;
    /**
     * 状态
     */
    private String Status;
    /**
     * 发出库房
     */
    private String fachukufang;

    /**
     * 制单人
     */
    private String xianchang_fachu_zhidan_people;


    public XianchangfachuDataInfo(String Gdbh, String Time,
                                  String Status,
                                  String fachukufang,
                                  String xianchang_fachu_zhidan_people) {
        this.Gdbh = Gdbh;
        this.Time = Time;
        this.Status = Status;
        this.fachukufang = fachukufang;
        this.xianchang_fachu_zhidan_people = xianchang_fachu_zhidan_people;

    }


    public String getGdbh() {
        return Gdbh;
    }

    public XianchangfachuDataInfo setGdbh(String Gdbh) {
        this.Gdbh = Gdbh;
        return this;
    }

    public String getTime() {
        return Time;
    }

    public XianchangfachuDataInfo setTime(String Time) {
        this.Time = Time;
        return this;
    }

    public String getStatus() {
        return Status;
    }

    public XianchangfachuDataInfo setStatus(String Status) {
        this.Status = Status;
        return this;
    }

    public String getfachukufang() {
        return fachukufang;
    }

    public XianchangfachuDataInfo setfachukufang(String Itemvalue) {
        this.fachukufang = Itemvalue;
        return this;
    }

    public String getxianchang_fachu_zhidan_people() {
        return xianchang_fachu_zhidan_people;
    }

    public XianchangfachuDataInfo setxianchang_fachu_zhidan_people(String Chexingvalue) {
        this.xianchang_fachu_zhidan_people = Chexingvalue;
        return this;
    }


    @Override
    public String toString() {
        return "-----test------";
    }
}
