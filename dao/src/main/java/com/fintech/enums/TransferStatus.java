package com.fintech.enums;

import java.util.Arrays;

/**
 * @author d.mikheev on 11.05.19
 */
public enum TransferStatus {

    INIT(0),
    SUCCESSFUL(1),
    FAILED(-1);

    private final int val;

    TransferStatus(int val) {
        this.val = val;
    }

    public int getVal() {
        return val;
    }

    public static TransferStatus valueOf(int value) {
        return Arrays.stream(values())
                .filter(status -> status.val == value)
                .findFirst()
                .get();
    }

}
