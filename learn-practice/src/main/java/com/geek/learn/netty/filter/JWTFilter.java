package com.geek.learn.netty.filter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import org.springframework.util.StringUtils;

public class JWTFilter implements HttpRequestFilter {

    @Override
    public Integer filter(FullHttpRequest fullRequest, ChannelHandlerContext ctx) {

        //统一jwt验证
        Integer userId = parseToken(fullRequest);

        if(userId != null){
            fullRequest.headers().set("Cookie", "KUID=277gh71626856109557; log.session=e1cbda48f93a73531d41ebe1da60725e; gr_user_id=5977b55d-3640-4460-8f05-dca44774bdef; _ga=GA1.2.312123648.1626856112; _gid=GA1.2.1941342424.1626856112; Hm_lvt_5023f5fc98cfb5712c364bb50b12e50e=1624848374,1624933816,1626944623; Hm_lpvt_5023f5fc98cfb5712c364bb50b12e50e=1626944643; pt_s_6dc02627=vt=1626944643986&cad=; pt_6dc02627=uid=yMbrAItzVBdBFu0p6eRtNA&nid=0&vid=DVQeiCBGUgO/DCEEVPGmuw&vn=2&pvn=1&sact=1626945644379&to_flag=0&pl=pfZn4y5MAETsLoFxhMWS4w*pt*1626945237181; koo.line=room; JSESSIONID=3233BF57DDFF4A35A8C88D06782BED0B; 9dee9d3e36a527e1_gr_session_id=9d7c5d81-47fc-4d54-bbca-8ae3670c28a6; 9dee9d3e36a527e1_gr_session_id_9d7c5d81-47fc-4d54-bbca-8ae3670c28a6=true; Qs_lvt_143225=1626856111%2C1626924692%2C1626955722; _gat_UA-16054642-5=1; Qs_pv_143225=2305273991545490200%2C4589578640649164300%2C1751884139514043600%2C899467692980364800%2C420013740115074940; sso.ssoId=f9b5d797701d71d8b695ad92be8578a3; ssoSessionID=1D1646ADC0B8262E4B5F015010637843; login_token=login_token_v2_1D1646ADC0B8262E4B5F015010637843; prelogid=0655bbbbdfe9ba79e4603389ef6a5390");
        }

        return userId;
    }

    private Integer parseToken(FullHttpRequest fullRequest) {
        HttpHeaders headers = fullRequest.headers();
        String token = headers.get("token");
        System.out.println("token: " + token);
        if(StringUtils.hasText(token) && token.equalsIgnoreCase("test")){
            return 999;
        }
        return null;
    }
}
