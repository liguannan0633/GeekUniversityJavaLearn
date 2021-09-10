package io.kimmking.dubbo.demo.provider;

import io.kimmking.dubbo.demo.api.User;
import io.kimmking.dubbo.demo.api.UserService;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.RpcContext;

@DubboService(version = "1.0.0")
public class UserServiceImpl implements UserService {

    @Override
    public User findById(int id) {
        String traceId = RpcContext.getContext().getAttachment("traceId");
        System.out.println("=================>RpcContext traceId1 :" + traceId);
        return new User(id, "KK" + System.currentTimeMillis());
    }
}
