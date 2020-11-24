package com.zz.web.aop;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.zz.web.commonutil.ResponseVO;
import lombok.extern.slf4j.Slf4j;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.NamedThreadLocal;
import org.springframework.stereotype.Component;



/**
 * token aop
 * @author Administrator
 *
 */
@Aspect
@Component
@Slf4j
public class TokenAspect {

	public static NamedThreadLocal<String> tokenThreadLocal = new NamedThreadLocal<String>("token");
	@Pointcut("execution(* com.zz.web.controller.*.*.*(..))")
	private void aspectJMethod() {
	};

	@Around("aspectJMethod()")
	public Object doAround(ProceedingJoinPoint pjp) throws Throwable {

		System.out.println("----doAround()开始----");
		//核心逻辑
		Object retval = pjp.proceed();
		if (retval instanceof ResponseVO) {
			ResponseVO responseVO = (ResponseVO)retval;
			log.info(responseVO.toString());
			if (responseVO.getCode().equals("0000")) {

				String userID = tokenThreadLocal.get();
				if (!"".equals(userID)) {
					Map<String , Object> payload=new HashMap<String, Object>();
					Date date=new Date();
					payload.put("uid", userID);//用户ID
					payload.put("iat", date.getTime());//生成时间
					//payload.put("ext",date.getTime()+1000*60*Integer.valueOf(Constants.LOGGING_TIME));//过期时间30分钟
					payload.put("ext",date.getTime()+1000*10);//过期时间10S
					for (String key : payload.keySet()) {
						System.out.println("generating playload:"+key + "[" +payload.get(key)+"]");
					}
					String token= "";
				//	String token=JWT.createToken(payload);
					responseVO.setToken(token);
				}

			}
			System.out.println("----doAround()结束----");
			return responseVO;
		}else {
			System.out.println("----doAround()结束----");
			return retval;
		}


	}
}