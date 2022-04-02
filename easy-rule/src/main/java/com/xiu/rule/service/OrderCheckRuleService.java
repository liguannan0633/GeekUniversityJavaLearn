package com.xiu.rule.service;

import com.xiu.rule.pojo.param.RuleParam;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * @author LiGuanNan
 * @date 2022/4/2 11:43 上午
 */
@Service
public class OrderCheckRuleService {

    @Resource
    private AuditService auditService;

    public boolean when(RuleParam param) {
        System.out.println("执行 OrderCheckRuleService.when, param: " + param);

        //模拟执行微服务RPC接口调用
        auditService.test();

        if(param.getAtomicMap().get() == null){
            param.getAtomicMap().compareAndSet(null,new HashMap<>());
        }

        param.getAtomicMap().get().put("OrderCheckRuleService","哈哈哈");
        return true;
    }

    public void then(RuleParam param) throws Exception {
        System.out.println("执行 OrderCheckRuleService.then, param: " + param);
    }

}
