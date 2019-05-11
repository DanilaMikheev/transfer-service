package com.fintech.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author d.mikheev on 10.05.19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    private String id;
    private Long amount;
    private Long clientid;
}
