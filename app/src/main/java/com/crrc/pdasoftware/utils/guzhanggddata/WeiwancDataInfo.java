package com.crrc.pdasoftware.utils.guzhanggddata;


public class WeiwancDataInfo {

    /**
     * 工单编号
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



    public WeiwancDataInfo() {

    }

    public WeiwancDataInfo(String Gdbh, String Time, String Status, String Itemvalue, String Chexingvalue, String ChehaoValue) {
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

    public WeiwancDataInfo setGdbh(String Gdbh) {
        this.Gdbh = Gdbh;
        return this;
    }

    public String getTime() {
        return Time;
    }

    public WeiwancDataInfo setTime(String Time) {
        this.Time = Time;
        return this;
    }

    public String getStatus() {
        return Status;
    }

    public WeiwancDataInfo setStatus(String Status) {
        this.Status = Status;
        return this;
    }

    public String getItemvalue() {
        return Itemvalue;
    }

    public WeiwancDataInfo setItemvalue(String Itemvalue) {
        this.Itemvalue = Itemvalue;
        return this;
    }

    public String getChexingvalue() {
        return Chexingvalue;
    }

    public WeiwancDataInfo setChexingvalue(String Chexingvalue) {
        this.Chexingvalue = Chexingvalue;
        return this;
    }

    public String getChehaoValue() {
        return ChehaoValue;
    }

    public WeiwancDataInfo setChehaoValue(String ChehaoValue) {
        this.ChehaoValue = ChehaoValue;
        return this;
    }





    @Override
    public String toString() {
        return "-----test------";
    }
}
