package com.fintech.web.data;

import lombok.Data;

import java.util.List;

/**
 * @author d.mikheev 08.05.19
 */
@Data
public class UserData {
    private Long clientId;
    private List<String> acc;
}
