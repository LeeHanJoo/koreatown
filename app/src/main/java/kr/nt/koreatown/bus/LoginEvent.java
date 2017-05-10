package kr.nt.koreatown.bus;

import kr.nt.koreatown.vo.MemberVO;

/**
 * Created by user on 2017-05-04.
 */

public class LoginEvent {

    private MemberVO memberVO;

    public MemberVO getMemberVO(){
        return memberVO;
    }
    public LoginEvent(MemberVO memberVO){
        this.memberVO = memberVO;
    }

}
