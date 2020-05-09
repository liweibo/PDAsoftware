package com.crrc.pdasoftware.utils.guzhanggddata;


public class SeePaichaDataInfo {


    private String xitonggongnjian;

    private String guzhangxianx;

    private String guzhangyuanyin;

    private String chulicuoshi;


    private String chulijieguo;

    public SeePaichaDataInfo() {

    }

    public SeePaichaDataInfo(String xitonggongnjian, String guzhangxianx,
                             String guzhangyuanyin, String chulicuoshi,
                             String chulijieguo) {
        this.xitonggongnjian = xitonggongnjian;
        this.guzhangxianx = guzhangxianx;
        this.guzhangyuanyin = guzhangyuanyin;
        this.chulicuoshi = chulicuoshi;
        this.chulijieguo = chulijieguo;
    }


    public String getxitonggongnjian() {
        return xitonggongnjian;
    }

    public SeePaichaDataInfo setxitonggongnjian(String Gdbh) {
        this.xitonggongnjian = xitonggongnjian;
        return this;
    }

    public String getguzhangxianx() {
        return guzhangxianx;
    }

    public SeePaichaDataInfo setguzhangxianx(String guzhangxianx) {
        this.guzhangxianx = guzhangxianx;
        return this;
    }

    public String getguzhangyuanyin() {
        return guzhangyuanyin;
    }

    public SeePaichaDataInfo setguzhangyuanyin(String guzhangyuanyin) {
        this.guzhangyuanyin = guzhangyuanyin;
        return this;
    }

    public String getchulicuoshi() {
        return chulicuoshi;
    }

    public SeePaichaDataInfo setchulicuoshi(String chulicuoshi) {
        this.chulicuoshi = chulicuoshi;
        return this;
    }

    public String getchulijieguo() {
        return chulijieguo;
    }

    public SeePaichaDataInfo setchulijieguo(String chulijieguo) {
        this.chulijieguo = chulijieguo;
        return this;
    }



    @Override
    public String toString() {
        return "-----test------";
    }
}
