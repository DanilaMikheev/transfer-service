package com.fintech.web.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *  Uid response body
 *
 * @author d.mikheev 13.05.19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UidData {
    private String uid;
}
