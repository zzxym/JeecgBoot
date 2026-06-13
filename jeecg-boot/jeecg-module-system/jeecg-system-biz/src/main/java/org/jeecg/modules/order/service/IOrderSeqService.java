package org.jeecg.modules.order.service;

import org.jeecg.modules.order.entity.OrderSeq;

import java.util.List;

/**
 * @Description: 订单序号服务接口
 * @author: jeecg-boot
 */
public interface IOrderSeqService {

    /**
     * 获取下一个序号
     * @param prefix 前缀，如 XS(销售订单)、CK(出库单)、CG(采购订单)
     * @return 完整序号，如 XS20260613000012
     */
    String nextSeq(String prefix);

    /**
     * 查询所有序号记录
     * @return 序号记录列表
     */
    List<OrderSeq> queryList();

    /**
     * 删除指定序号记录（按日期归零）
     * @param seqKey 序号键，如 XS_20260613
     * @return 删除是否成功
     */
    boolean deleteByKey(String seqKey);

    /**
     * 重置指定序号计数器为 0
     * @param seqKey 序号键，如 XS_20260613
     * @return 重置是否成功
     */
    boolean resetSeq(String seqKey);

}
