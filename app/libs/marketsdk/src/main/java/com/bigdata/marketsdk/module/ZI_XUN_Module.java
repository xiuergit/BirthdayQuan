package com.bigdata.marketsdk.module;

import java.util.List;

/**
 * Created by windows on 2016/2/17.
 */
public class ZI_XUN_Module {

    /**
     * TABLENAME : BD_JXH_NEWS_1
     * STRUCTURE : {"ID":"Int64","PUB_DT":"String","TIT":"String","MED_NAME":"String","AUT":"String","ABST":"String","CONT_ID":"Int64"}
     * DATA : [{"ID":1428809867,"PUB_DT":"2016-02-02 14:44","TIT":"鎷涘晢閾惰(600036)棰戦鎶兼敞涓鑲＄鏈夊寲","MED_NAME":"涓浗璇佸埜缃�","AUT":null,"ABST":"宸ㄤ汉缃戠粶鍥炲綊A鑲\u2033垱涓嬬殑20涓定鍋滄澘甯︾粰浜嗕腑姒傝偂鏃犳暟鐨勯亹鎯炽\u20ac備腑姒傝偂鍥炲綊涓氬姟涔熸垚涓哄悇璺祫","CONT_ID":1428809941},{"ID":1414879232,"PUB_DT":"2016-01-27 13:54","TIT":"鎷涘晢閾惰(600036)鎴樼暐鍏ヨ偂婊存淮","MED_NAME":"涓浗璇佸埜缃�","AUT":null,"ABST":"26鏃ワ紝婊存淮鍑鸿鍜屾嫑鍟嗛摱琛岃仈鍚堝甯冿紝鎷涘晢閾惰浣滀负鎴樼暐鎶曡祫鑰呮姇璧勬淮婊达紝鍚屾椂鍙屾柟灏嗗湪璧勬湰","CONT_ID":1414879361},{"ID":1414551188,"PUB_DT":"2016-01-27 09:05","TIT":"甯冨眬浜掕仈缃戦噾铻� 鎷涘晢閾惰(600036)鐗垫墜婊存淮鍑鸿","MED_NAME":"涓浗璇佸埜鎶�","AUT":"鍛ㄦ枃闈�","ABST":"1鏈�26鏃ワ紝鎷涘晢閾惰銆佹淮婊村嚭琛岃仈鍚堝甯冨弻鏂硅揪鎴愭垬鐣ュ悎浣滐紝鏈潵鍙屾柟灏嗗湪璧勬湰銆佹敮浠樼粨绠椼\u20ac�","CONT_ID":1414551548},{"ID":1414277600,"PUB_DT":"2016-01-27 08:03","TIT":"婊存淮鍑鸿鑾峰緱鎷涘晢閾惰(600036)鎴樼暐鎶曡祫 涓撹溅鍙告満鍙埌鎷涜缃戠偣鍔炵悊娉ㄥ唽","MED_NAME":"璇佸埜鏃ユ姤","AUT":"璐洪獜","ABST":"灏界涓撹溅蹇溅绛変笟鍔\u2033皻鏈緱鍒扮浉鍏崇墝鐓э紝浣嗛殢鐫\u20ac璇稿鍥借祫鑳屾櫙鎶曡祫鑰呯殑杩涘叆锛屾淮婊村嚭琛岀殑鏀跨瓥椋�","CONT_ID":1414277760},{"ID":1368059442,"PUB_DT":"2016-01-07 20:39","TIT":"鎷涘晢閾惰(600036)鐗靛ご濂囪檸360绉佹湁鍖�34浜跨編鍏冮摱鍥㈣捶娆�","MED_NAME":"涓浗璇佸埜缃�","AUT":"楂樻敼鑺�","ABST":"杩戞棩锛屽铏�360瀹ｅ竷涓庡叕鍙歌懀浜嬮暱鍏糃EO鍛ㄩ缚绁庨琛旂殑涔版柟鍥㈣揪鎴愮鏈夊寲鍗忚銆傛嵁浜嗚В锛屾嫑","CONT_ID":1368059616}]
     */

    private String TABLENAME;
    /**
     * ID : Int64
     * PUB_DT : String
     * TIT : String
     * MED_NAME : String
     * AUT : String
     * ABST : String
     * CONT_ID : Int64
     */

    private STRUCTUREEntity STRUCTURE;
    /**
     * ID : 1428809867
     * PUB_DT : 2016-02-02 14:44
     * TIT : 鎷涘晢閾惰(600036)棰戦鎶兼敞涓鑲＄鏈夊寲
     * MED_NAME : 涓浗璇佸埜缃�
     * AUT : null
     * ABST : 宸ㄤ汉缃戠粶鍥炲綊A鑲″垱涓嬬殑20涓定鍋滄澘甯︾粰浜嗕腑姒傝偂鏃犳暟鐨勯亹鎯炽€備腑姒傝偂鍥炲綊涓氬姟涔熸垚涓哄悇璺祫
     * CONT_ID : 1428809941
     */

    private List<DATAEntity> DATA;

    public void setTABLENAME(String TABLENAME) {
        this.TABLENAME = TABLENAME;
    }

    public void setSTRUCTURE(STRUCTUREEntity STRUCTURE) {
        this.STRUCTURE = STRUCTURE;
    }

    public void setDATA(List<DATAEntity> DATA) {
        this.DATA = DATA;
    }

    public String getTABLENAME() {
        return TABLENAME;
    }

    public STRUCTUREEntity getSTRUCTURE() {
        return STRUCTURE;
    }

    public List<DATAEntity> getDATA() {
        return DATA;
    }

    public static class STRUCTUREEntity {
        private String ID;
        private String PUB_DT;
        private String TIT;
        private String MED_NAME;
        private String AUT;
        private String ABST;
        private String CONT_ID;

        public void setID(String ID) {
            this.ID = ID;
        }

        public void setPUB_DT(String PUB_DT) {
            this.PUB_DT = PUB_DT;
        }

        public void setTIT(String TIT) {
            this.TIT = TIT;
        }

        public void setMED_NAME(String MED_NAME) {
            this.MED_NAME = MED_NAME;
        }

        public void setAUT(String AUT) {
            this.AUT = AUT;
        }

        public void setABST(String ABST) {
            this.ABST = ABST;
        }

        public void setCONT_ID(String CONT_ID) {
            this.CONT_ID = CONT_ID;
        }

        public String getID() {
            return ID;
        }

        public String getPUB_DT() {
            return PUB_DT;
        }

        public String getTIT() {
            return TIT;
        }

        public String getMED_NAME() {
            return MED_NAME;
        }

        public String getAUT() {
            return AUT;
        }

        public String getABST() {
            return ABST;
        }

        public String getCONT_ID() {
            return CONT_ID;
        }
    }

    public static class DATAEntity {
        private int ID;
        private String PUB_DT;
        private String TIT;
        private String MED_NAME;
        private String AUT;
        private String ABST;
        private int CONT_ID;

        public void setID(int ID) {
            this.ID = ID;
        }

        public void setPUB_DT(String PUB_DT) {
            this.PUB_DT = PUB_DT;
        }

        public void setTIT(String TIT) {
            this.TIT = TIT;
        }

        public void setMED_NAME(String MED_NAME) {
            this.MED_NAME = MED_NAME;
        }

        public void setAUT(String AUT) {
            this.AUT = AUT;
        }

        public void setABST(String ABST) {
            this.ABST = ABST;
        }

        public void setCONT_ID(int CONT_ID) {
            this.CONT_ID = CONT_ID;
        }

        public int getID() {
            return ID;
        }

        public String getPUB_DT() {
            return PUB_DT;
        }

        public String getTIT() {
            return TIT;
        }

        public String getMED_NAME() {
            return MED_NAME;
        }

        public String getAUT() {
            return AUT;
        }

        public String getABST() {
            return ABST;
        }

        public int getCONT_ID() {
            return CONT_ID;
        }
    }
}
