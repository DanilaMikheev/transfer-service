package com.fintech.web.data;

import com.fintech.web.validation.AccountId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author d.mikheev 08.05.19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement
public class TransferData {
    @NotBlank
    private Integer systemId;
    @NotBlank
    private Long clientId;
    @AccountId
    private String from;
    @AccountId
    private String to;
    @NotBlank
    private Long amount;
}
