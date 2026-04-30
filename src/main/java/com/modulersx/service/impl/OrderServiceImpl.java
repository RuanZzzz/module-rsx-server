package com.modulersx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.modulersx.domain.dto.OrderExpressBindDTO;
import com.modulersx.domain.dto.OrderSaveDTO;
import com.modulersx.domain.dto.OrderStatusChangeDTO;
import com.modulersx.domain.po.OrderExpressPO;
import com.modulersx.domain.po.OrderPO;
import com.modulersx.domain.po.OrderStatusLogPO;
import com.modulersx.domain.vo.ExpressTraceVO;
import com.modulersx.domain.vo.OrderExpressVO;
import com.modulersx.domain.vo.OrderStatusLogVO;
import com.modulersx.domain.vo.OrderVO;
import com.modulersx.exception.BizException;
import com.modulersx.repository.OrderExpressMapper;
import com.modulersx.repository.OrderMapper;
import com.modulersx.repository.OrderStatusLogMapper;
import com.modulersx.service.ExpressClient;
import com.modulersx.service.OrderService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class OrderServiceImpl implements OrderService {

    private static final String DEFAULT_STATUS = "NOT_STARTED";

    private final OrderMapper orderMapper;
    private final OrderStatusLogMapper statusLogMapper;
    private final OrderExpressMapper expressMapper;
    private final ExpressClient expressClient;

    public OrderServiceImpl(
            OrderMapper orderMapper,
            OrderStatusLogMapper statusLogMapper,
            OrderExpressMapper expressMapper,
            ExpressClient expressClient) {
        this.orderMapper = orderMapper;
        this.statusLogMapper = statusLogMapper;
        this.expressMapper = expressMapper;
        this.expressClient = expressClient;
    }

    @Override
    public List<OrderVO> listOrders() {
        return orderMapper.selectList(new LambdaQueryWrapper<OrderPO>()
                        .orderByDesc(OrderPO::getId))
                .stream()
                .map(this::toVO)
                .toList();
    }

    @Override
    public OrderVO getOrder(Long id) {
        return toVO(requireOrder(id));
    }

    @Override
    @Transactional
    public OrderVO createOrder(OrderSaveDTO dto) {
        validateOrder(dto);
        if (existsByOrderNo(dto.getOrderNo())) {
            throw new BizException(400, "order no already exists");
        }
        OrderPO order = toPO(dto, null);
        order.setStatus(DEFAULT_STATUS);
        orderMapper.insert(order);
        insertStatusLog(order.getId(), null, DEFAULT_STATUS, "system", "创建订单");
        return toVO(order);
    }

    @Override
    @Transactional
    public OrderVO updateOrder(Long id, OrderSaveDTO dto) {
        validateOrder(dto);
        OrderPO order = requireOrder(id);
        order.setCustomerName(dto.getCustomerName());
        order.setProductName(dto.getProductName());
        order.setQuantity(dto.getQuantity());
        order.setRemark(dto.getRemark());
        orderMapper.updateById(order);
        return toVO(order);
    }

    @Override
    @Transactional
    public OrderVO changeStatus(Long id, OrderStatusChangeDTO dto) {
        if (!StringUtils.hasText(dto.getStatus())) {
            throw new BizException(400, "order status cannot be blank");
        }
        OrderPO order = requireOrder(id);
        String fromStatus = order.getStatus();
        validateStatusTransition(fromStatus, dto.getStatus());
        order.setStatus(dto.getStatus());
        orderMapper.updateById(order);
        // 每次状态变化都写入日志，后续排查订单流转问题时能看到完整时间线。
        insertStatusLog(order.getId(), fromStatus, dto.getStatus(), defaultText(dto.getOperator(), "admin"), dto.getRemark());
        return toVO(order);
    }

    @Override
    @Transactional
    public OrderVO bindExpress(Long id, OrderExpressBindDTO dto) {
        OrderPO order = requireOrder(id);
        validateBeforeShipping(order);
        validateExpress(dto);
        OrderExpressPO express = findExpress(id);
        if (express == null) {
            express = new OrderExpressPO();
            express.setOrderId(id);
        }
        express.setExpressCompanyCode(dto.getExpressCompanyCode());
        express.setExpressCompanyName(dto.getExpressCompanyName());
        express.setTrackingNo(dto.getTrackingNo());
        express.setReceiverPhoneSuffix(dto.getReceiverPhoneSuffix());
        express.setLatestStatus("待查询");
        express.setLatestLocation("暂无");
        if (express.getId() == null) {
            expressMapper.insert(express);
        } else {
            expressMapper.updateById(express);
        }
        if ("PACKED".equals(order.getStatus())) {
            order.setStatus("SHIPPING");
            orderMapper.updateById(order);
            // 打包完成后填写发货信息，订单才正式进入配送中。
            insertStatusLog(order.getId(), "PACKED", "SHIPPING", "admin", "填写发货信息，订单进入配送中");
        } else {
            insertStatusLog(order.getId(), "SHIPPING", "SHIPPING", "admin", "更新物流信息");
        }
        return getOrder(id);
    }

    @Override
    @Transactional
    public ExpressTraceVO queryExpressTrace(Long id) {
        requireOrder(id);
        OrderExpressPO express = findExpress(id);
        if (express == null) {
            throw new BizException(400, "express info is not bound");
        }
        ExpressTraceVO trace = expressClient.queryTrace(express);
        // 查询后把最新节点回写到订单物流表，列表页不用每次都调用第三方接口。
        express.setLatestStatus(trace.getLatestStatus());
        express.setLatestLocation(trace.getLatestLocation());
        express.setLatestTraceTime(LocalDateTime.now());
        express.setRawResponse(trace.getProvider());
        expressMapper.updateById(express);
        return trace;
    }

    private void validateOrder(OrderSaveDTO dto) {
        if (!StringUtils.hasText(dto.getOrderNo())) {
            throw new BizException(400, "order no cannot be blank");
        }
        if (!StringUtils.hasText(dto.getCustomerName())) {
            throw new BizException(400, "customer name cannot be blank");
        }
        if (!StringUtils.hasText(dto.getProductName())) {
            throw new BizException(400, "product name cannot be blank");
        }
        if (dto.getQuantity() == null || dto.getQuantity() <= 0) {
            throw new BizException(400, "quantity must be greater than 0");
        }
    }

    private void validateExpress(OrderExpressBindDTO dto) {
        if (!StringUtils.hasText(dto.getExpressCompanyCode())) {
            throw new BizException(400, "express company code cannot be blank");
        }
        if (!StringUtils.hasText(dto.getExpressCompanyName())) {
            throw new BizException(400, "express company name cannot be blank");
        }
        if (!StringUtils.hasText(dto.getTrackingNo())) {
            throw new BizException(400, "tracking no cannot be blank");
        }
    }

    private void validateBeforeShipping(OrderPO order) {
        if (!"PACKED".equals(order.getStatus()) && !"SHIPPING".equals(order.getStatus())) {
            throw new BizException(400, "express can only be edited after order is packed");
        }
    }

    private void validateStatusTransition(String fromStatus, String toStatus) {
        if (fromStatus.equals(toStatus)) {
            return;
        }
        if ("SHIPPING".equals(toStatus)) {
            throw new BizException(400, "please bind express info before shipping");
        }
        boolean allowed = ("NOT_STARTED".equals(fromStatus) && "PRODUCING".equals(toStatus))
                || ("PRODUCING".equals(fromStatus) && "PACKED".equals(toStatus))
                || ("SHIPPING".equals(fromStatus) && "COMPLETED".equals(toStatus));
        if (!allowed) {
            throw new BizException(400, "invalid order status transition");
        }
    }

    private OrderPO requireOrder(Long id) {
        OrderPO order = orderMapper.selectById(id);
        if (order == null) {
            throw new BizException(404, "order not found");
        }
        return order;
    }

    private boolean existsByOrderNo(String orderNo) {
        return orderMapper.selectCount(new LambdaQueryWrapper<OrderPO>()
                .eq(OrderPO::getOrderNo, orderNo)) > 0;
    }

    private OrderExpressPO findExpress(Long orderId) {
        return expressMapper.selectOne(new LambdaQueryWrapper<OrderExpressPO>()
                .eq(OrderExpressPO::getOrderId, orderId)
                .last("limit 1"));
    }

    private List<OrderStatusLogVO> listStatusLogs(Long orderId) {
        return statusLogMapper.selectList(new LambdaQueryWrapper<OrderStatusLogPO>()
                        .eq(OrderStatusLogPO::getOrderId, orderId)
                        .orderByDesc(OrderStatusLogPO::getId))
                .stream()
                .map(log -> new OrderStatusLogVO(log.getFromStatus(), log.getToStatus(), log.getOperator(), log.getRemark(), log.getCreatedAt()))
                .toList();
    }

    private void insertStatusLog(Long orderId, String fromStatus, String toStatus, String operator, String remark) {
        OrderStatusLogPO log = new OrderStatusLogPO();
        log.setOrderId(orderId);
        log.setFromStatus(fromStatus);
        log.setToStatus(toStatus);
        log.setOperator(operator);
        log.setRemark(remark);
        statusLogMapper.insert(log);
    }

    private OrderPO toPO(OrderSaveDTO dto, Long id) {
        OrderPO order = new OrderPO();
        order.setId(id);
        order.setOrderNo(dto.getOrderNo());
        order.setCustomerName(dto.getCustomerName());
        order.setProductName(dto.getProductName());
        order.setQuantity(dto.getQuantity());
        order.setRemark(dto.getRemark());
        return order;
    }

    private OrderVO toVO(OrderPO po) {
        OrderVO vo = new OrderVO();
        vo.setId(po.getId());
        vo.setOrderNo(po.getOrderNo());
        vo.setCustomerName(po.getCustomerName());
        vo.setProductName(po.getProductName());
        vo.setQuantity(po.getQuantity());
        vo.setStatus(po.getStatus());
        vo.setRemark(po.getRemark());
        vo.setCreatedAt(po.getCreatedAt());
        vo.setUpdatedAt(po.getUpdatedAt());
        vo.setExpress(toExpressVO(findExpress(po.getId())));
        vo.setStatusLogs(listStatusLogs(po.getId()));
        return vo;
    }

    private OrderExpressVO toExpressVO(OrderExpressPO po) {
        if (po == null) {
            return null;
        }
        return new OrderExpressVO(
                po.getExpressCompanyCode(),
                po.getExpressCompanyName(),
                po.getTrackingNo(),
                po.getReceiverPhoneSuffix(),
                po.getLatestStatus(),
                po.getLatestLocation(),
                po.getLatestTraceTime());
    }

    private String defaultText(String value, String fallback) {
        return StringUtils.hasText(value) ? value : fallback;
    }
}
