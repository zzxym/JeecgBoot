package org.jeecg.modules.order.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Description: 订单序号表
 * @author: jeecg-boot
 */
@Data
@TableName("sys_order_seq")
public class OrderSeq {

    @TableId
    private String seqKey;

    private Integer seqNo;

}
