package com.modulersx.service.impl;

import com.modulersx.domain.po.OrderExpressPO;
import com.modulersx.domain.vo.ExpressTraceItemVO;
import com.modulersx.domain.vo.ExpressTraceVO;
import com.modulersx.service.ExpressClient;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class MockKuaidi100ExpressClient implements ExpressClient {

    @Override
    public ExpressTraceVO queryTrace(OrderExpressPO express) {
        ExpressTraceVO vo = new ExpressTraceVO();
        vo.setProvider("kuaidi100-mock");
        vo.setExpressCompanyCode(express.getExpressCompanyCode());
        vo.setExpressCompanyName(express.getExpressCompanyName());
        vo.setTrackingNo(express.getTrackingNo());
        vo.setLatestStatus("配送中");
        vo.setLatestLocation("深圳南山派送网点");
        // 这里先返回模拟轨迹，后续拿到快递100 customer/key 后替换为真实 HTTP 调用。
        vo.setTraces(List.of(
                new ExpressTraceItemVO("2026-04-30 09:12:00", "广州分拨中心", "快件已从广州分拨中心发出"),
                new ExpressTraceItemVO("2026-04-30 13:35:00", "深圳转运中心", "快件到达深圳转运中心"),
                new ExpressTraceItemVO("2026-04-30 16:20:00", "深圳南山派送网点", "快件正在派送中，请保持电话畅通")
        ));
        return vo;
    }
}
