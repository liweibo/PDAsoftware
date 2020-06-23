package com.crrc.pdasoftware.utils.guzhanggddata;


public class WeiwancDataInfo {


    /**
     * 工单编号
     */
    private String Gdbh = "102488";
    /**
     * 时间
     */
//    private String Time;
    /**
     * 状态
     */
    private String Status;
    /**
     * 项目值
     */
//    private String Itemvalue;

    /**
     * 车型值
     */
    private String Chexingvalue;
    /**
     * 点车号值
     */
    private String ChehaoValue;


    //配属用户
    String peishuyh;
    //客户来电时间
    String kehuCALLTIME;
    //派工理由即故障描述
    String paigongliyou;
    //服务单位联系人
    String fuwudanweiContact;
    //安全管理要求
    String secureGuanli;

    //-------------到达现场------------------
    //累计走行公里
    String leijizouxing;


    //服务单位联系人备用电话
    String fwdanweicontactphone;


    //------------不在故障表中的字段---------------------------
    String fwdanweiaddress;//服务单位地址

    //现场处理人
    String xianchangchulirenname;
    String xianchangchulirenphone;


    String workorderid;


    public WeiwancDataInfo() {

    }

    public WeiwancDataInfo(String Gdbh,
                           String Status,
                           String Chexingvalue,
                           String ChehaoValue,
                           String peishuyh,
                           String kehuCALLTIME,
                           String paigongliyou,
                           String fuwudanweiContact,
                           String secureGuanli,
                           String leijizouxing,
                           String fwdanweicontactphone,
                           String workorderid) {
        this.Gdbh = Gdbh;
        this.Status = Status;
        this.Chexingvalue = Chexingvalue;
        this.ChehaoValue = ChehaoValue;

        this.peishuyh = peishuyh;
        this.kehuCALLTIME = kehuCALLTIME;
        this.paigongliyou = paigongliyou;
        this.fuwudanweiContact = fuwudanweiContact;
        this.secureGuanli = secureGuanli;
        this.leijizouxing = leijizouxing;
        this.fwdanweicontactphone = fwdanweicontactphone;
        this.workorderid = workorderid;

    }



    public String getGdbh() {
        return Gdbh;
    }

    public void setGdbh(String gdbh) {
        Gdbh = gdbh;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getChexingvalue() {
        return Chexingvalue;
    }

    public void setChexingvalue(String chexingvalue) {
        Chexingvalue = chexingvalue;
    }

    public String getChehaoValue() {
        return ChehaoValue;
    }

    public void setChehaoValue(String chehaoValue) {
        ChehaoValue = chehaoValue;
    }

    public String getPeishuyh() {
        return peishuyh;
    }

    public void setPeishuyh(String peishuyh) {
        this.peishuyh = peishuyh;
    }

    public String getKehuCALLTIME() {
        return kehuCALLTIME;
    }

    public void setKehuCALLTIME(String kehuCALLTIME) {
        this.kehuCALLTIME = kehuCALLTIME;
    }

    public String getPaigongliyou() {
        return paigongliyou;
    }

    public void setPaigongliyou(String paigongliyou) {
        this.paigongliyou = paigongliyou;
    }

    public String getFuwudanweiContact() {
        return fuwudanweiContact;
    }

    public void setFuwudanweiContact(String fuwudanweiContact) {
        this.fuwudanweiContact = fuwudanweiContact;
    }

    public String getSecureGuanli() {
        return secureGuanli;
    }

    public void setSecureGuanli(String secureGuanli) {
        this.secureGuanli = secureGuanli;
    }

    public String getLeijizouxing() {
        return leijizouxing;
    }

    public void setLeijizouxing(String leijizouxing) {
        this.leijizouxing = leijizouxing;
    }

    public String getFwdanweicontactphone() {
        return fwdanweicontactphone;
    }

    public void setFwdanweicontactphone(String fwdanweicontactphone) {
        this.fwdanweicontactphone = fwdanweicontactphone;
    }

    public String getFwdanweiaddress() {
        return fwdanweiaddress;
    }

    public void setFwdanweiaddress(String fwdanweiaddress) {
        this.fwdanweiaddress = fwdanweiaddress;
    }

    public String getXianchangchulirenname() {
        return xianchangchulirenname;
    }

    public void setXianchangchulirenname(String xianchangchulirenname) {
        this.xianchangchulirenname = xianchangchulirenname;
    }

    public String getXianchangchulirenphone() {
        return xianchangchulirenphone;
    }

    public void setXianchangchulirenphone(String xianchangchulirenphone) {
        this.xianchangchulirenphone = xianchangchulirenphone;
    }

    public String getWorkorderid() {
        return workorderid;
    }

    public void setWorkorderid(String workorderid) {
        this.workorderid = workorderid;
    }





}
