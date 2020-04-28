package com.crrc.pdasoftware.utils.guzhanggddata;


public class FuwuDataInfo {

    /**
     * 工单编号
     */
    private String Gdbh = "102488";
    /**
     * 时间
     */
    private String Time;
    /**
     * 状态
     */
    private String Status;
    /**
     * 项目值
     */
    private String Itemvalue;

    /**
     * 车型值
     */
    private String Chexingvalue;
    /**
     * 点车号值
     */
    private String ChehaoValue;



    public FuwuDataInfo() {

    }

    public FuwuDataInfo(String Gdbh, String Time, String Status, String Itemvalue, String Chexingvalue, String ChehaoValue) {
        this.Gdbh = Gdbh;
        this.Time = Time;
        this.Status = Status;
        this.Itemvalue = Itemvalue;
        this.Chexingvalue = Chexingvalue;
        this.ChehaoValue = ChehaoValue;

    }





    public String getGdbh() {
        return Gdbh;
    }

    public FuwuDataInfo setGdbh(String Gdbh) {
        this.Gdbh = Gdbh;
        return this;
    }

    public String getTime() {
        return Time;
    }

    public FuwuDataInfo setTime(String Time) {
        this.Time = Time;
        return this;
    }

    public String getStatus() {
        return Status;
    }

    public FuwuDataInfo setStatus(String Status) {
        this.Status = Status;
        return this;
    }

    public String getItemvalue() {
        return Itemvalue;
    }

    public FuwuDataInfo setItemvalue(String Itemvalue) {
        this.Itemvalue = Itemvalue;
        return this;
    }

    public String getChexingvalue() {
        return Chexingvalue;
    }

    public FuwuDataInfo setChexingvalue(String Chexingvalue) {
        this.Chexingvalue = Chexingvalue;
        return this;
    }

    public String getChehaoValue() {
        return ChehaoValue;
    }

    public FuwuDataInfo setChehaoValue(String ChehaoValue) {
        this.ChehaoValue = ChehaoValue;
        return this;
    }





    @Override
    public String toString() {
        return "-----test------";
    }
}