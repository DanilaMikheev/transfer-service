package com.fintech.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * @author d.mikheev 08.05.19
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transfer {

    private String id;
    private String accFrom;
    private String accTo;
    private Long amount;
    private int status;
    private Timestamp updated;

}
