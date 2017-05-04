package kr.nt.koreatown.util;

import com.squareup.otto.Bus;

/**
 * Created by user on 2017-05-04.
 */

public class BusProvider {

    private static final Bus BUS = new Bus();

    public static Bus getInstance(){
        return BUS;
    }
}
