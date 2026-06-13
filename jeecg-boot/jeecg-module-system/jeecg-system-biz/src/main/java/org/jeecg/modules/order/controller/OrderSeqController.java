package org.jeecg.modules.order.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.order.entity.OrderSeq;
import org.jeecg.modules.order.service.IOrderSeqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description: 订单序号接口
 * @author: jeecg-boot
 */
@Slf4j
@RestController
@RequestMapping("/sys/orderSeq")
@Tag(name = "订单序号管理")
public class OrderSeqController {

    @Autowired
    private IOrderSeqService orderSeqService;

    /**
     * 获取下一个序号（供 JS 增强自动填充调用）
     * GET /sys/orderSeq/next?prefix=XS
     */
    @Operation(summary = "获取下一个序号")
    @GetMapping("/next")
    public Result<?> next(@Parameter(description = "序号前缀，如 XS/CK/CG/TEST") @RequestParam(name = "prefix", defaultValue = "CN") String prefix) {
        try {
            String orderNumber = orderSeqService.nextSeq(prefix);
            return Result.OK(orderNumber);
        } catch (Exception e) {
            log.error("生成序号失败, prefix={}", prefix, e);
            return Result.error("生成序号失败: " + e.getMessage());
        }
    }

    /**
     * 查询所有序号记录
     * GET /sys/orderSeq/list
     */
    @Operation(summary = "查询所有序号记录")
    @GetMapping("/list")
    public Result<?> list() {
        try {
            List<OrderSeq> list = orderSeqService.queryList();
            return Result.OK(list);
        } catch (Exception e) {
            log.error("查询序号列表失败", e);
            return Result.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 删除指定序号记录
     * DELETE /sys/orderSeq/delete/XS_20260613
     */
    @Operation(summary = "删除序号记录")
    @DeleteMapping("/delete/{seqKey}")
    public Result<?> delete(@Parameter(description = "序号键，如 XS_20260613") @PathVariable String seqKey) {
        try {
            boolean ok = orderSeqService.deleteByKey(seqKey);
            if (ok) {
                log.info("删除序号记录成功, seqKey={}", seqKey);
                return Result.OK("删除成功");
            } else {
                return Result.error("删除失败：记录不存在");
            }
        } catch (Exception e) {
            log.error("删除序号记录失败, seqKey={}", seqKey, e);
            return Result.error("删除失败: " + e.getMessage());
        }
    }

    /**
     * 重置指定序号计数器为 0
     * PUT /sys/orderSeq/reset/XS_20260613
     */
    @Operation(summary = "重置序号计数器")
    @PutMapping("/reset/{seqKey}")
    public Result<?> reset(@Parameter(description = "序号键，如 XS_20260613") @PathVariable String seqKey) {
        try {
            boolean ok = orderSeqService.resetSeq(seqKey);
            if (ok) {
                log.info("重置序号成功, seqKey={}", seqKey);
                return Result.OK("重置成功");
            } else {
                return Result.error("重置失败：记录不存在");
            }
        } catch (Exception e) {
            log.error("重置序号失败, seqKey={}", seqKey, e);
            return Result.error("重置失败: " + e.getMessage());
        }
    }

}
