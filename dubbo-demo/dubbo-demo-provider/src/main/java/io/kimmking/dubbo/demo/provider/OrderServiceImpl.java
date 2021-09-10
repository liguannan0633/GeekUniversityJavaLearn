package io.kimmking.dubbo.demo.provider;

import io.kimmking.dubbo.demo.api.Order;
import io.kimmking.dubbo.demo.api.OrderService;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.RpcContext;


@DubboService(version = "1.0.0", tag = "red", weight = 100)
public class OrderServiceImpl implements OrderService {

    @Override
    public Order findOrderById(int id) {
        String traceId = RpcContext.getContext().getAttachment("traceId");
        System.out.println("=================>RpcContext traceId2 :" + traceId);
        return new Order(id, "Cuijing" + System.currentTimeMillis(), 9.9f);
    }
}
