package com.fintech.web.data;

import com.fintech.web.validation.AccountId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Create transfer request body
 *
 * @author d.mikheev 08.05.19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement
public class TransferData {
    @NotBlank
    private Long clientId;
    @AccountId
    private String fromAcc;
    @AccountId
    private String toAcc;
    @NotBlank
    private Long amount;
}
