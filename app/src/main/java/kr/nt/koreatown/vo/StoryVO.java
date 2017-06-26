package kr.nt.koreatown.vo;

import java.util.ArrayList;

/**
 * Created by user on 2017-05-16.
 */

public class StoryVO {

    public String result ;
    public Story data;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Story getData() {
        return data;
    }

    public void setData(Story data) {
        this.data = data;
    }

    public class Story{
        public String ST_SEQ ;
        public String MEMBER_ID;
        public String ST_STICKER;
        public String ST_DESC;
        public String ST_LON;
        public String ST_LAT;
        public String ST_ADDR1;
        public String ST_ADDR2;
        public String CREATE_DATE;
        public String END_TIME;
        public String TIME_HOUR;
        public String TIME_MINUTE;
        public ArrayList<Comment> COMMENT_LIST;

        public String getST_SEQ() {
            return ST_SEQ;
        }

        public void setST_SEQ(String ST_SEQ) {
            this.ST_SEQ = ST_SEQ;
        }

        public String getMEMBER_ID() {
            return MEMBER_ID;
        }

        public void setMEMBER_ID(String MEMBER_ID) {
            this.MEMBER_ID = MEMBER_ID;
        }

        public String getST_STICKER() {
            return ST_STICKER;
        }

        public void setST_STICKER(String ST_STICKER) {
            this.ST_STICKER = ST_STICKER;
        }

        public String getST_DESC() {
            return ST_DESC;
        }

        public void setST_DESC(String ST_DESC) {
            this.ST_DESC = ST_DESC;
        }

        public String getST_LON() {
            return ST_LON;
        }

        public void setST_LON(String ST_LON) {
            this.ST_LON = ST_LON;
        }

        public String getST_LAT() {
            return ST_LAT;
        }

        public void setST_LAT(String ST_LAT) {
            this.ST_LAT = ST_LAT;
        }

        public String getST_ADDR1() {
            return ST_ADDR1;
        }

        public void setST_ADDR1(String ST_ADDR1) {
            this.ST_ADDR1 = ST_ADDR1;
        }

        public String getST_ADDR2() {
            return ST_ADDR2;
        }

        public void setST_ADDR2(String ST_ADDR2) {
            this.ST_ADDR2 = ST_ADDR2;
        }

        public String getCREATE_DATE() {
            return CREATE_DATE;
        }

        public void setCREATE_DATE(String CREATE_DATE) {
            this.CREATE_DATE = CREATE_DATE;
        }

        public String getEND_TIME() {
            return END_TIME;
        }

        public void setEND_TIME(String END_TIME) {
            this.END_TIME = END_TIME;
        }

        public String getTIME_HOUR() {
            return TIME_HOUR;
        }

        public void setTIME_HOUR(String TIME_HOUR) {
            this.TIME_HOUR = TIME_HOUR;
        }

        public String getTIME_MINUTE() {
            return TIME_MINUTE;
        }

        public void setTIME_MINUTE(String TIME_MINUTE) {
            this.TIME_MINUTE = TIME_MINUTE;
        }

        public ArrayList<Comment> getCOMMENT_LIST() {
            return COMMENT_LIST;
        }

        public void setCOMMENT_LIST(ArrayList<Comment> COMMENT_LIST) {
            this.COMMENT_LIST = COMMENT_LIST;
        }

        public class Comment{
            String MEMBER_ID;
            String COMM_TEXT;
            String CREATE_DATE;
            String MEMBER_NICK;

            public String getMEMBER_NICK() {
                return MEMBER_NICK;
            }

            public void setMEMBER_NICK(String MEMBER_NICK) {
                this.MEMBER_NICK = MEMBER_NICK;
            }

            public String getMEMBER_ID() {
                return MEMBER_ID;
            }

            public void setMEMBER_ID(String MEMBER_ID) {
                this.MEMBER_ID = MEMBER_ID;
            }

            public String getCOMM_TEXT() {
                return COMM_TEXT;
            }

            public void setCOMM_TEXT(String COMM_TEXT) {
                this.COMM_TEXT = COMM_TEXT;
            }

            public String getCREATE_DATE() {
                return CREATE_DATE;
            }

            public void setCREATE_DATE(String CREATE_DATE) {
                this.CREATE_DATE = CREATE_DATE;
            }
        }

    }
}
