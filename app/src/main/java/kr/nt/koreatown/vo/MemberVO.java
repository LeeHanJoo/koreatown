package kr.nt.koreatown.vo;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import kr.nt.koreatown.BR;

/**
 * Created by user on 2017-05-04.
 */

public class MemberVO extends BaseObservable{


    public String MEMBER_SEQ;
    public String MEMBER_ID;
    public String MEMBER_BIRTH;
    public String MEMBER_EMAIL;
    public String MEMBER_NICK;
    public String MEMBER_TYPE;
    public String MEMBER_SEX;
    public String MEMBER_IMG;
    public String MEMBER_OS;
    public String MEMBER_VERSION;
    public String PASSWORD;

    public String getPASSWORD() {
        return PASSWORD;
    }

    public void setPASSWORD(String PASSWORD) {
        this.PASSWORD = PASSWORD;
    }

    @Bindable
    public String getMEMBER_SEQ() {
        return MEMBER_SEQ;
    }

    public void setMEMBER_SEQ(String MEMBER_SEQ) {
        this.MEMBER_SEQ = MEMBER_SEQ;
        notifyPropertyChanged(BR.mEMBER_SEQ);
    }

    @Bindable
    public String getMEMBER_ID() {
        return MEMBER_ID;
    }

    public void setMEMBER_ID(String MEMBER_ID) {
        this.MEMBER_ID = MEMBER_ID;
        notifyPropertyChanged(BR.mEMBER_ID);
    }

    @Bindable
    public String getMEMBER_BIRTH() {
        return MEMBER_BIRTH;
    }

    public void setMEMBER_BIRTH(String MEMBER_BIRTH) {
        this.MEMBER_BIRTH = MEMBER_BIRTH;
        notifyPropertyChanged(BR.mEMBER_BIRTH);
    }

    @Bindable
    public String getMEMBER_EMAIL() {
        return MEMBER_EMAIL;
    }

    public void setMEMBER_EMAIL(String MEMBER_EMAIL) {
        this.MEMBER_EMAIL = MEMBER_EMAIL;
        notifyPropertyChanged(BR.mEMBER_EMAIL);
    }
    @Bindable
    public String getMEMBER_NICK() {
        return MEMBER_NICK;
    }

    public void setMEMBER_NICK(String MEMBER_NICK) {
        this.MEMBER_NICK = MEMBER_NICK;
        notifyPropertyChanged(BR.mEMBER_NICK);
    }
    @Bindable
    public String getMEMBER_TYPE() {
        return MEMBER_TYPE;
    }

    public void setMEMBER_TYPE(String MEMBER_TYPE) {
        this.MEMBER_TYPE = MEMBER_TYPE;
        notifyPropertyChanged(BR.mEMBER_TYPE);
    }
    @Bindable
    public String getMEMBER_SEX() {
        return MEMBER_SEX;
    }

    public void setMEMBER_SEX(String MEMBER_SEX) {
        this.MEMBER_SEX = MEMBER_SEX;
        notifyPropertyChanged(BR.mEMBER_SEX);
    }

    public String getMEMBER_IMG() {
        return MEMBER_IMG;
    }

    public void setMEMBER_IMG(String MEMBER_IMG) {
        this.MEMBER_IMG = MEMBER_IMG;

    }

    @Bindable
    public String getMEMBER_OS() {
        return MEMBER_OS;
    }

    public void setMEMBER_OS(String MEMBER_OS) {
        this.MEMBER_OS = MEMBER_OS;
        notifyPropertyChanged(BR.mEMBER_OS);
    }
    @Bindable
    public String getMEMBER_VERSION() {
        return MEMBER_VERSION;
    }

    public void setMEMBER_VERSION(String MEMBER_VERSION) {
        this.MEMBER_VERSION = MEMBER_VERSION;
        notifyPropertyChanged(BR.mEMBER_VERSION);
    }
}
