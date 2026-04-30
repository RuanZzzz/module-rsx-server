package com.modulersx.service;

import com.modulersx.domain.dto.OrderExpressBindDTO;
import com.modulersx.domain.dto.OrderSaveDTO;
import com.modulersx.domain.dto.OrderStatusChangeDTO;
import com.modulersx.domain.vo.ExpressTraceVO;
import com.modulersx.domain.vo.OrderVO;
import java.util.List;

public interface OrderService {

    List<OrderVO> listOrders();

    OrderVO getOrder(Long id);

    OrderVO createOrder(OrderSaveDTO dto);

    OrderVO updateOrder(Long id, OrderSaveDTO dto);

    OrderVO changeStatus(Long id, OrderStatusChangeDTO dto);

    OrderVO bindExpress(Long id, OrderExpressBindDTO dto);

    ExpressTraceVO queryExpressTrace(Long id);
}
