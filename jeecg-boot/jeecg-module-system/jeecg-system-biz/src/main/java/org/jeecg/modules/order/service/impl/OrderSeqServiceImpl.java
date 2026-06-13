package org.jeecg.modules.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.order.entity.OrderSeq;
import org.jeecg.modules.order.mapper.OrderSeqMapper;
import org.jeecg.modules.order.service.IOrderSeqService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @Description: 订单序号服务实现
 * @author: jeecg-boot
 */
@Slf4j
@Service("orderSeqService")
public class OrderSeqServiceImpl extends ServiceImpl<OrderSeqMapper, OrderSeq> implements IOrderSeqService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String nextSeq(String prefix) {
        // 生成序号键：prefix_yyyymmdd
        String dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String seqKey = prefix + "_" + dateStr;

        // 原子递增
        this.baseMapper.incrementAndGet(seqKey);

        // 查询当前序号
        Integer seqNo = this.baseMapper.getSeqNo(seqKey);

        // 格式：prefix + date + 5位序号
        return String.format("%s%s%05d", prefix, dateStr, seqNo != null ? seqNo : 1);
    }

    @Override
    public List<OrderSeq> queryList() {
        return this.baseMapper.selectList(null);
    }

    @Override
    public boolean deleteByKey(String seqKey) {
        return this.removeById(seqKey);
    }

    @Override
    public boolean resetSeq(String seqKey) {
        OrderSeq seq = new OrderSeq();
        seq.setSeqKey(seqKey);
        seq.setSeqNo(0);
        return this.updateById(seq);
    }

}
