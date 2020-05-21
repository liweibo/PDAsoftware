package com.crrc.pdasoftware.utils.zhuangxiangdandata;


public class JianpeiRecyItemDataInfo {


    private String wuliaoname;
    private String fachukufang;

    private String cangwei;

    private String wuliaozhuangtai;

    private String wuliaocode;


    private String picihao;
    private String xuliehao;

    private String diaobonumber;
    private String havescannumber;

    public JianpeiRecyItemDataInfo() {

    }

    public JianpeiRecyItemDataInfo(
            String wuliaoname,
            String fachukufang, String cangwei,
            String wuliaozhuangtai, String wuliaocode,
            String picihao,
            String xuliehao,
            String diaobonumber,
            String havescannumber

            ) {
        this.wuliaoname = wuliaoname;
        this.fachukufang = fachukufang;
        this.cangwei = cangwei;
        this.wuliaozhuangtai = wuliaozhuangtai;
        this.wuliaocode = wuliaocode;
        this.picihao = picihao;
        this.xuliehao = xuliehao;
        this.diaobonumber = diaobonumber;
        this.havescannumber = havescannumber;
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

    public JianpeiRecyItemDataInfo setfachukufang(String Gdbh) {
        this.fachukufang = fachukufang;
        return this;
    }

    public String getcangwei() {
        return cangwei;
    }

    public JianpeiRecyItemDataInfo setcangwei(String cangwei) {
        this.cangwei = cangwei;
        return this;
    }

    public String getwuliaozhuangtai() {
        return wuliaozhuangtai;
    }

    public JianpeiRecyItemDataInfo setwuliaozhuangtai(String wuliaozhuangtai) {
        this.wuliaozhuangtai = wuliaozhuangtai;
        return this;
    }

    public String getwuliaocode() {
        return wuliaocode;
    }

    public JianpeiRecyItemDataInfo setwuliaocode(String wuliaocode) {
        this.wuliaocode = wuliaocode;
        return this;
    }

    public String getpicihao() {
        return picihao;
    }

    public JianpeiRecyItemDataInfo setpicihao(String picihao) {
        this.picihao = picihao;
        return this;
    }

    public String getXuliehao() {
        return xuliehao;
    }

    public JianpeiRecyItemDataInfo setXuliehao(String xuliehao) {
        this.xuliehao = xuliehao;
        return this;
    }

    public String getDiaobonumber() {
        return diaobonumber;
    }

    public void setDiaobonumber(String diaobonumber) {
        this.diaobonumber = diaobonumber;
    }

    public String getHavescannumber() {
        return havescannumber;
    }

    public void setHavescannumber(String havescannumber) {
        this.havescannumber = havescannumber;
    }


    @Override
    public String toString() {
        return "-----test------";
    }
}
