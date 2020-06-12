package com.crrc.pdasoftware.utils.zhuangxiangdandata;


public class BaozhuangRecyItemDataInfo {


    private String wuliaoname;
    private String fachukufang;

    private String receiveKufang;

    private String wuliaozhuangtai;

    private String wuliaocode;


    private String picihao;
    private String xuliehao;

    private String diaobonumber;
    private String baozhuangxianghao;


    public BaozhuangRecyItemDataInfo(
            String wuliaoname,
            String fachukufang, String receiveKufang,
            String wuliaozhuangtai, String wuliaocode,
            String picihao,
            String xuliehao,
            String diaobonumber,
            String baozhuangxianghao

    ) {
        this.wuliaoname = wuliaoname;
        this.fachukufang = fachukufang;
        this.receiveKufang = receiveKufang;
        this.wuliaozhuangtai = wuliaozhuangtai;
        this.wuliaocode = wuliaocode;
        this.picihao = picihao;
        this.xuliehao = xuliehao;
        this.diaobonumber = diaobonumber;
        this.baozhuangxianghao = baozhuangxianghao;
    }

    public String getWuliaoname() {
        return wuliaoname;
    }

    public void setWuliaoname(String wuliaoname) {
        this.wuliaoname = wuliaoname;
    }

    public String getfachukufang() {
        return fachukufang;
    }

    public BaozhuangRecyItemDataInfo setfachukufang(String Gdbh) {
        this.fachukufang = fachukufang;
        return this;
    }

    public String getreceiveKufang() {
        return receiveKufang;
    }

    public BaozhuangRecyItemDataInfo setreceiveKufang(String receiveKufang) {
        this.receiveKufang = receiveKufang;
        return this;
    }

    public String getwuliaozhuangtai() {
        return wuliaozhuangtai;
    }

    public BaozhuangRecyItemDataInfo setwuliaozhuangtai(String wuliaozhuangtai) {
        this.wuliaozhuangtai = wuliaozhuangtai;
        return this;
    }

    public String getwuliaocode() {
        return wuliaocode;
    }

    public BaozhuangRecyItemDataInfo setwuliaocode(String wuliaocode) {
        this.wuliaocode = wuliaocode;
        return this;
    }

    public String getpicihao() {
        return picihao;
    }

    public BaozhuangRecyItemDataInfo setpicihao(String picihao) {
        this.picihao = picihao;
        return this;
    }

    public String getXuliehao() {
        return xuliehao;
    }

    public BaozhuangRecyItemDataInfo setXuliehao(String xuliehao) {
        this.xuliehao = xuliehao;
        return this;
    }

    public String getDiaobonumber() {
        return diaobonumber;
    }

    public void setDiaobonumber(String diaobonumber) {
        this.diaobonumber = diaobonumber;
    }

    public String getbaozhuangxianghao() {
        return baozhuangxianghao;
    }

    public void setbaozhuangxianghao(String baozhuangxianghao) {
        this.baozhuangxianghao = baozhuangxianghao;
    }
}
