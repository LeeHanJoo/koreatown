package kr.nt.koreatown.vo;

import java.util.ArrayList;

/**
 * Created by user on 2017-05-16.
 */

public class RoomVO {

    public String result ;
    public Room data;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Room getData() {
        return data;
    }

    public void setData(Room data) {
        this.data = data;
    }

    public class Room{
        public String RM_SEQ ;
        public String MEMBER_ID;
        public String RM_PRICE;
        public String RM_ROOM;
        public String RM_TOILET;
        public String RM_DESC;
        public String RM_COSTDAY;
        public String RM_LAT;
        public String RM_LON;
        public String RM_ADDR1;
        public String RM_ADDR2;
        public String RM_AREA;
        public String CREATE_DATE;
        public String END_TIME;
        public String TIME_HOUR;
        public String TIME_MINUTE;
        public ArrayList<FILE> FILE_LIST;
        public ArrayList<Comment> COMMENT_LIST;

        public ArrayList<Comment> getCOMMENT_LIST() {
            return COMMENT_LIST;
        }

        public void setCOMMENT_LIST(ArrayList<Comment> COMMENT_LIST) {
            this.COMMENT_LIST = COMMENT_LIST;
        }

        public String getRM_SEQ() {
            return RM_SEQ;
        }

        public void setRM_SEQ(String RM_SEQ) {
            this.RM_SEQ = RM_SEQ;
        }

        public String getMEMBER_ID() {
            return MEMBER_ID;
        }

        public void setMEMBER_ID(String MEMBER_ID) {
            this.MEMBER_ID = MEMBER_ID;
        }

        public String getRM_PRICE() {
            return RM_PRICE;
        }

        public void setRM_PRICE(String RM_PRICE) {
            this.RM_PRICE = RM_PRICE;
        }

        public String getRM_ROOM() {
            return RM_ROOM;
        }

        public void setRM_ROOM(String RM_ROOM) {
            this.RM_ROOM = RM_ROOM;
        }

        public String getRM_TOILET() {
            return RM_TOILET;
        }

        public void setRM_TOILET(String RM_TOILET) {
            this.RM_TOILET = RM_TOILET;
        }

        public String getRM_DESC() {
            return RM_DESC;
        }

        public void setRM_DESC(String RM_DESC) {
            this.RM_DESC = RM_DESC;
        }

        public String getRM_COSTDAY() {
            return RM_COSTDAY;
        }

        public void setRM_COSTDAY(String RM_COSTDAY) {
            this.RM_COSTDAY = RM_COSTDAY;
        }

        public String getRM_LAT() {
            return RM_LAT;
        }

        public void setRM_LAT(String RM_LAT) {
            this.RM_LAT = RM_LAT;
        }

        public String getRM_LON() {
            return RM_LON;
        }

        public void setRM_LON(String RM_LON) {
            this.RM_LON = RM_LON;
        }

        public String getRM_ADDR1() {
            return RM_ADDR1;
        }

        public void setRM_ADDR1(String RM_ADDR1) {
            this.RM_ADDR1 = RM_ADDR1;
        }

        public String getRM_ADDR2() {
            return RM_ADDR2;
        }

        public void setRM_ADDR2(String RM_ADDR2) {
            this.RM_ADDR2 = RM_ADDR2;
        }

        public String getRM_AREA() {
            return RM_AREA;
        }

        public void setRM_AREA(String RM_AREA) {
            this.RM_AREA = RM_AREA;
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

        public ArrayList<FILE> getFILE_LIST() {
            return FILE_LIST;
        }

        public void setFILE_LIST(ArrayList<FILE> FILE_LIST) {
            this.FILE_LIST = FILE_LIST;
        }

        public class FILE{
            public String URL;

            public String getURL() {
                return URL;
            }

            public void setURL(String URL) {
                this.URL = URL;
            }
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
