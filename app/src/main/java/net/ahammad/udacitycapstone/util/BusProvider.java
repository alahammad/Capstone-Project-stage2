package net.ahammad.udacitycapstone.util;

import com.squareup.otto.Bus;

/**
 * Created by alahammad on 9/5/15.
 */
public class BusProvider {
    private static final Bus BUS = new Bus();

    public static Bus getInstance() {
        return BUS;
    }

    private BusProvider() {
        // No instances.
    }
}
