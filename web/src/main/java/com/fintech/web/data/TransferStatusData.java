package com.fintech.web.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Get transfer status response body
 *
 * @author d.mikheev 13.05.19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferStatusData {
    private Integer transferStatus;
}
