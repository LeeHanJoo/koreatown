package kr.nt.koreatown.vo;

import java.util.ArrayList;

/**
 * Created by user on 2017-05-12.
 */

public class FeedVO {

    public  ArrayList<Feed> data;

    public ArrayList<Feed> getData() {
        return data;
    }

    public void setData(ArrayList<Feed> data) {
        this.data = data;
    }



    public class Feed /*implements ClusterItem*/{
        public String GUBUN;
        public String SEQ;
        public String MEMBER_ID;
        public String LAT;
        public String LON;
        public String DISTANCE;
        public String COMMENT_CNT;

        public String getGUBUN() {
            return GUBUN;
        }

        public void setGUBUN(String GUBUN) {
            this.GUBUN = GUBUN;
        }

        public String getSEQ() {
            return SEQ;
        }

        public void setSEQ(String SEQ) {
            this.SEQ = SEQ;
        }

        public String getMEMBER_ID() {
            return MEMBER_ID;
        }

        public void setMEMBER_ID(String MEMBER_ID) {
            this.MEMBER_ID = MEMBER_ID;
        }

        public String getLAT() {
            return LAT;
        }

        public void setLAT(String LAT) {
            this.LAT = LAT;
        }

        public String getLON() {
            return LON;
        }

        public void setLON(String LON) {
            this.LON = LON;
        }

        public String getDISTANCE() {
            return DISTANCE;
        }

        public void setDISTANCE(String DISTANCE) {
            this.DISTANCE = DISTANCE;
        }

        public String getCOMMENT_CNT() {
            return COMMENT_CNT;
        }

        public void setCOMMENT_CNT(String COMMENT_CNT) {
            this.COMMENT_CNT = COMMENT_CNT;
        }

      /*  @Override
        public LatLng getPosition() {
            return new LatLng(Double.valueOf(LAT),Double.valueOf(LON));
        }*/
    }


}
