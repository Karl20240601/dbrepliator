package com.crazymaker.springcloud.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.crazymaker.springcloud.base.service.impl.UserServiceImpl;
import com.crazymaker.springcloud.common.constants.SessionConstants;
import com.crazymaker.springcloud.common.exception.BusinessException;
import com.crazymaker.springcloud.common.result.RestOut;
import com.crazymaker.springcloud.user.api.dto.LoginInfoDTO;
import com.crazymaker.springcloud.user.api.dto.LoginOutDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Api(value = "用户端登录与退出", tags = {"用户端登录与退出DEMO"})
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    //用户端会话服务，和管理控制台会话服务进行区分
    @Resource
    private UserServiceImpl userService;

 /*
  * 参考的值

  {
  "password": "123456",
  "username": "test"
  }

eyJhbGciOiJSUzI1NiJ9.eyJqdGkiOiIxIiwic2lkIjoiZmIxMzhlNmMtOGRjZS00MTRkLTk4OGEtYzllYTEyYTBmMTc5IiwiZXhwIjoxNjQ3ODYyNTM2LCJpYXQiOjE2NDc4MzAxMzZ9.IC7k4DOEbD3KDvtVcr_hmw19M0S8aD_k5HfOPMd0CO3XWZFzoK0VSnoENT1RbmcKFX2Iw6Y6006fVlf1b-n9hBypXbVoTG-sZ3yqdvrgXXU238p-0RhiD4iKXJjnNvjKxExJvDeVRggLz3zcIxg0TMT0asDZSAGN1JNobkw80rouuBGmwDZKMLv7f3irWm7vmEP3U09vkxFd63M9_SVYXlNNLExgKVltNyhXEDzVmkUeeGa6wtPq-DdlHTELfe8A_xmd83no-6bs6d_6zRImaOIoVIUOuhkfIsB_9FZ-5Ipdx0BTrb8Qu_h6UmcHmeESDfL2ftKAjhAod-IVCNToXw

 */

    @PostMapping("/app/token/v1")
    @ApiOperation(value = "应用获取令牌")
    public RestOut<LoginOutDTO> userToken(@RequestBody LoginInfoDTO loginInfoDTO,
                                          HttpServletRequest request,
                                          HttpServletResponse response) {

        LoginOutDTO dto = userService.produceUserToken(loginInfoDTO);
        response.setHeader("Content-Type", "text/html;charset=utf-8");
        response.setHeader(SessionConstants.AUTHORIZATION_HEAD, dto.getToken());
        return RestOut.success(dto);
    }


/*
  * 参考的值

  {
  "username": "18800000000"
  }
 */

    @Value("${websocket.register.gateway}")
    private String websocketRegisterGateway;

    @PostMapping("/account/token/v1")
    @ApiOperation(value = "应用为account获取WS令牌")
    public RestOut<JSONObject> accountToken(@RequestBody LoginInfoDTO loginInfoDTO,
                                            HttpServletRequest request,
                                            HttpServletResponse response) {
        String token = userService.produceAccountToken(loginInfoDTO);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put(SessionConstants.AUTHORIZATION_HEAD, token);
        jsonObject.put("gateWay", websocketRegisterGateway);
        response.setHeader("Content-Type", "text/html;charset=utf-8");
        response.setHeader(SessionConstants.AUTHORIZATION_HEAD, token);
        return RestOut.success(jsonObject);
    }

    @PostMapping("/token/refresh/v1")
    @ApiOperation(value = "前台刷新token")
    public RestOut<LoginOutDTO> tokenRefresh(HttpServletRequest request, HttpServletResponse response) {

        String token = request.getHeader(SessionConstants.AUTHORIZATION_HEAD);
        if (StringUtils.isEmpty(token)) {
            throw BusinessException.builder().errMsg("刷新失败").build();
        }
        LoginOutDTO outDTO = userService.tokenRefresh(token);
        response.setHeader("Content-Type", "text/html;charset=utf-8");
        response.setHeader(SessionConstants.AUTHORIZATION_HEAD, outDTO.getToken());
        return RestOut.success(outDTO);
    }

}
