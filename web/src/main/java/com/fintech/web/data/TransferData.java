package com.fintech.web.data;

import com.fintech.web.validation.AccountId;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author d.mikheev 08.05.19
 */
@Data
public class TransferData {
    @NotEmpty
    private Integer systemId;
    @NotEmpty
    private Long clientId;
    @AccountId
    private Long from;
    @AccountId
    private Long to;
    @NotEmpty
    private Long amount;
}
