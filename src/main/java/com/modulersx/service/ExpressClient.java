package com.modulersx.service;

import com.modulersx.domain.po.OrderExpressPO;
import com.modulersx.domain.vo.ExpressTraceVO;

public interface ExpressClient {

    ExpressTraceVO queryTrace(OrderExpressPO express);
}
