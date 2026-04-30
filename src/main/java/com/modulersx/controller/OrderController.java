package com.modulersx.controller;

import com.modulersx.common.response.ApiResponse;
import com.modulersx.domain.dto.OrderExpressBindDTO;
import com.modulersx.domain.dto.OrderSaveDTO;
import com.modulersx.domain.dto.OrderStatusChangeDTO;
import com.modulersx.domain.vo.ExpressTraceVO;
import com.modulersx.domain.vo.OrderVO;
import com.modulersx.service.OrderService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ApiResponse<List<OrderVO>> listOrders() {
        return ApiResponse.success(orderService.listOrders());
    }

    @GetMapping("/detail")
    public ApiResponse<OrderVO> getOrder(@RequestParam Long id) {
        return ApiResponse.success(orderService.getOrder(id));
    }

    @PostMapping("/create")
    public ApiResponse<OrderVO> createOrder(@RequestBody OrderSaveDTO dto) {
        return ApiResponse.success(orderService.createOrder(dto));
    }

    @PostMapping("/update")
    public ApiResponse<OrderVO> updateOrder(@RequestParam Long id, @RequestBody OrderSaveDTO dto) {
        return ApiResponse.success(orderService.updateOrder(id, dto));
    }

    @PostMapping("/status/change")
    public ApiResponse<OrderVO> changeStatus(@RequestParam Long id, @RequestBody OrderStatusChangeDTO dto) {
        return ApiResponse.success(orderService.changeStatus(id, dto));
    }

    @PostMapping("/express/bind")
    public ApiResponse<OrderVO> bindExpress(@RequestParam Long id, @RequestBody OrderExpressBindDTO dto) {
        return ApiResponse.success(orderService.bindExpress(id, dto));
    }

    @GetMapping("/express/trace")
    public ApiResponse<ExpressTraceVO> queryExpressTrace(@RequestParam Long id) {
        return ApiResponse.success(orderService.queryExpressTrace(id));
    }
}
