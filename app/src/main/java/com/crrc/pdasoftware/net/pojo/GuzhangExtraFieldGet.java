package com.crrc.pdasoftware.net.pojo;

import java.util.List;

public class GuzhangExtraFieldGet {

    /**
     * code : S
     * data : {"data":[{"ORGID":"CRRC","SITEID":"ELEC","WORKORDERID":"36017","ROWSTAMP":"238239730","ORDERNUM":"DNY-GZ-2019-3141","CALLTIME":"2019-05-08 17:16:22","MODELS":"C28","TYPE":"故障","CARNUM":"01","SERVCOMPANY":"203121","WHICHOFFICE":"01062500","DISPATCHREASON":"9101车TCU的DIO、APU板和9701车TCU的APU板被烧损","MANAGEMENTREQ":"1、严格遵守公司及车辆段安全管理规章制度\n2、穿戴好劳保用品","REPORTER":"20137614","STATUS":"草稿","SERVENGINEER":"20137614","REPAIRPROCESS":"S-C-00A","ASSETNUM":"XC2327616","CREATEDATE":"2019-05-08 00:00:00","MODELPROJECT":"澳大利亚地铁墨尔本HCMT项目","PROJECTNUM":"SP000221","SALES":"Z010","ISERROR":"0","ISSUCCESS":"1","CREATER":"20137614","ISPLANCRAET":"0","GZDATE":"2019-05-08 00:00:00","ISMOBILECREATE":"0","ISMAINFAULTCOMP":"0","ISNOTICE":"0","ISTECHANALYZE":"0","IFACE309":"0","ISTWO":"1","DISPLAYNAME":"唐敏杰","PHONENUM":"18670879286","ADDRESS":"205Oakview Lane Nar Nar Goon VIC 3812"}]}
     * msg : 操作成功
     */

    public String code;
    public DataBeanX data;
    public String msg;

    public static class DataBeanX {
        public List<DataBean> data;

        public static class DataBean {
            /**
             * ORGID : CRRC
             * SITEID : ELEC
             * WORKORDERID : 36017
             * ROWSTAMP : 238239730
             * ORDERNUM : DNY-GZ-2019-3141
             * CALLTIME : 2019-05-08 17:16:22
             * MODELS : C28
             * TYPE : 故障
             * CARNUM : 01
             * SERVCOMPANY : 203121
             * WHICHOFFICE : 01062500
             * DISPATCHREASON : 9101车TCU的DIO、APU板和9701车TCU的APU板被烧损
             * MANAGEMENTREQ : 1、严格遵守公司及车辆段安全管理规章制度
             2、穿戴好劳保用品
             * REPORTER : 20137614
             * STATUS : 草稿
             * SERVENGINEER : 20137614
             * REPAIRPROCESS : S-C-00A
             * ASSETNUM : XC2327616
             * CREATEDATE : 2019-05-08 00:00:00
             * MODELPROJECT : 澳大利亚地铁墨尔本HCMT项目
             * PROJECTNUM : SP000221
             * SALES : Z010
             * ISERROR : 0
             * ISSUCCESS : 1
             * CREATER : 20137614
             * ISPLANCRAET : 0
             * GZDATE : 2019-05-08 00:00:00
             * ISMOBILECREATE : 0
             * ISMAINFAULTCOMP : 0
             * ISNOTICE : 0
             * ISTECHANALYZE : 0
             * IFACE309 : 0
             * ISTWO : 1
             * DISPLAYNAME : 唐敏杰
             * PHONENUM : 18670879286
             * ADDRESS : 205Oakview Lane Nar Nar Goon VIC 3812
             */

            public String ORGID;
            public String SITEID;
            public String WORKORDERID;
            public String ROWSTAMP;
            public String ORDERNUM;
            public String CALLTIME;
            public String MODELS;
            public String TYPE;
            public String CARNUM;
            public String SERVCOMPANY;
            public String WHICHOFFICE;
            public String DISPATCHREASON;
            public String MANAGEMENTREQ;
            public String REPORTER;
            public String STATUS;
            public String SERVENGINEER;
            public String REPAIRPROCESS;
            public String ASSETNUM;
            public String CREATEDATE;
            public String MODELPROJECT;
            public String PROJECTNUM;
            public String SALES;
            public String ISERROR;
            public String ISSUCCESS;
            public String CREATER;
            public String ISPLANCRAET;
            public String GZDATE;
            public String ISMOBILECREATE;
            public String ISMAINFAULTCOMP;
            public String ISNOTICE;
            public String ISTECHANALYZE;
            public String IFACE309;
            public String ISTWO;
            public String DISPLAYNAME;
            public String PHONENUM;
            public String ADDRESS;
            public String WHICHSTATION;//故障处理站点
            public  String FAULTCONSEQ ;//故障后果
            public  String FAILUREDESC;//故障名称
            public  String CARSECTIONNUM;//车厢号

            public  String FAILURECODE;//
            public  String PRODUCTNICKNAME;//
            public  String FAULTTIME;//
            public  String FINDPROCESS;//
            public  String RUNNINGMODE;//
            public  String FAULTQUALIT;//
            public  String QYFZDW;//
            public  String FAILWEATHER;//
            public  String ROADTYPE;//
            public  String FAILURELIBID;//
            public  String NODATAREASON  ;// 无数据原因
            public  String FAULTDESC  ;//故障现象
            public  String PREREASONALYS  ;// 初步分析 即故障原因
            public  String DEALMEASURE  ;// 处理措施
            public  String FAULTCOMPONENTNAME  ;//  主故障件名称
            public  String ANALYSISREPNE  ;//客户需分析报告
            public  String DEALMETHOD  ;//处理方式

        }
    }
}
