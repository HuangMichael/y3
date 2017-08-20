package com.subway.aop;


import com.subway.domain.log.UserLog;
import com.subway.domain.userLog.UserLogService;
import com.subway.object.ReturnObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 用户日志aop
 */
@Component
@Aspect
public class UserLoginAop {
    @Autowired
    UserLogService userLogService;
    protected Log log = LogFactory.getLog(this.getClass());

    /**
     * @param joinPoint   结合点
     * @param loginResult 登录结果
     */
    @AfterReturning(value = "execution(* com.subway.controller.app.LoginController.login(..))", returning = "loginResult")
    public void doAfterReturningAdvice(JoinPoint joinPoint, Object loginResult) {
        String userName = (String) joinPoint.getArgs()[1];
        log.info("userName------------" + userName);
        ReturnObject returnObject = (ReturnObject) loginResult;
        boolean result = returnObject.getResult();
        Object[] args = joinPoint.getArgs();
        HttpServletRequest request = (HttpServletRequest) args[0];
        String ip = request.getRemoteHost();
        UserLog userLog = new UserLog();
        userLog.setUserName(userName);
        userLog.setLoginIp(ip);
        userLog.setOperation("登录");
        userLog.setOperationTime(new Date());
        userLog.setStatus(result ? "登录成功" : "登录失败");
        userLogService.createUserLog(userLog);
    }


    /**
     * 前置通知，方法调用前被调用
     *
     * @param joinPoint
     */
    @Before(value = "execution(* com.subway.controller.app.LoginController.logout(..))")
    public void doBeforeAdvice(JoinPoint joinPoint) {
//        //获取目标方法的参数信息
//        Object[] obj = joinPoint.getArgs();
//        HttpServletRequest request = (HttpServletRequest) obj[0];
//        String ip = request.getRemoteHost();
//        HttpSession session = request.getSession();
//        User user = (User) session.getAttribute("user");
//        UserLog userLog = new UserLog();
//        userLog.setUserName(user.getUserName());
//        userLog.setLoginIp(ip);
//        userLog.setOperation("退出");
//        userLog.setOperationTime(new Date());
//        userLog.setStatus("退出系统");
//        userLogService.createUserLog(userLog);
    }
}
