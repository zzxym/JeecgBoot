package org.jeecg.modules.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.jeecg.modules.order.entity.OrderSeq;

/**
 * @Description: 订单序号Mapper
 * @author: jeecg-boot
 */
public interface OrderSeqMapper extends BaseMapper<OrderSeq> {

    /**
     * 原子递增序号（INSERT ON DUPLICATE KEY UPDATE）
     */
    @Update("INSERT INTO sys_order_seq(seq_key, seq_no) VALUES(#{seqKey}, 1) ON DUPLICATE KEY UPDATE seq_no = seq_no + 1")
    int incrementAndGet(@Param("seqKey") String seqKey);

    /**
     * 查询当前序号值
     */
    @Select("SELECT seq_no FROM sys_order_seq WHERE seq_key = #{seqKey}")
    Integer getSeqNo(@Param("seqKey") String seqKey);

}
