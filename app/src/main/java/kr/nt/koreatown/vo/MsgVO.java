package kr.nt.koreatown.vo;

/**
 * Created by user on 2017-05-04.
 */

public class MsgVO {

    public String result;
    public MSG data;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public MSG getData() {
        return data;
    }

    public void setData(MSG data) {
        this.data = data;
    }

    public class MSG{
        public String msg;

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }

}
