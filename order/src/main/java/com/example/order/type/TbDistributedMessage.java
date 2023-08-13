package com.example.order.type;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TbDistributedMessage {
    private String uniqueId;

    private String msgContent;

    private String msgStatus;

    private Date createTime;

}
