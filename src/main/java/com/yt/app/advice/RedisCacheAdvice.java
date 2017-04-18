package com.yt.app.advice;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.yt.app.annotation.RedisCacheAnnotation;
import com.yt.app.annotation.RedisCacheEvictAnnotation;
import com.yt.app.frame.config.AppConfig;
import com.yt.app.frame.config.RedisConfig;
import com.yt.app.frame.id.IdWorker;
import com.yt.app.util.RedisCacheUtil;

@Aspect
@Component
public class RedisCacheAdvice {
	@Autowired
	RedisCacheUtil redisUtil;

	@Autowired
	RedisConfig redisconfig;

	@Autowired
	AppConfig appconfig;
	
	@Autowired
	IdWorker idworker;

	@Autowired
	Gson gson;

	@Pointcut("(execution(* com.yt.app.api.v1.mapper..*(..))) || (execution(* com.yt.app.common.base.IBaseMapper.*(..))) ")
	public void aspect() {
	}

	@Around("aspect()")
	public Object Around(ProceedingJoinPoint joinPoint) throws Throwable {
		Object result = null;
		Annotation annotation = null;
		String methodName = joinPoint.getSignature().getName();
		Object[] args = joinPoint.getArgs();
		String key = getCacheKey(joinPoint.getSignature().getDeclaringType().getCanonicalName(), methodName, joinPoint.getArgs());
		Method me = ((MethodSignature) joinPoint.getSignature()).getMethod();
		annotation = me.getAnnotations().length > 0 ? me.getAnnotations()[0] : null;
		if (annotation == null) {
			Method proxyMethod = ((MethodSignature) joinPoint.getSignature()).getMethod();
			Method mes = proxyMethod.getDeclaringClass().getMethod(proxyMethod.getName(), proxyMethod.getParameterTypes());
			annotation = mes.getAnnotations().length > 0 ? mes.getAnnotations()[0] : null;
			if (annotation == null) {
				result = joinPoint.proceed(args);
				return result;
			}
		}
		if (annotation.annotationType().equals(RedisCacheEvictAnnotation.class)) {
			RedisCacheEvictAnnotation rcea = (RedisCacheEvictAnnotation) annotation;
			if (getMethodMatchesPost(methodName)) {
				Method mg = args[0].getClass().getDeclaredMethod("getId");
				Long id = (Long) mg.invoke(args[0]);
				if (id == null) {
					Method ms = args[0].getClass().getDeclaredMethod("setId", Long.class);
					ms.invoke(args[0], idworker.nextId());
				}
			}
			Class<?>[] classs = rcea.classs();
			for (Class<?> cl : classs) {
				redisUtil.delete(cl.getName());
			}
			result = joinPoint.proceed(args);
		} else if (annotation.annotationType().equals(RedisCacheAnnotation.class)) {
			RedisCacheAnnotation rcea = (RedisCacheAnnotation) annotation;
			result = redisUtil.getCacheMapExpireTime(rcea.classs().getName(), key, (long) redisconfig.getExpire());
			if (result != null) {
				return result;
			} else {
				result = joinPoint.proceed(args);
				if (result != null) {
					redisUtil.setCacheMapExpireTime(rcea.classs().getName(), key, result, (long) redisconfig.getExpire());
				}
			}
		}
		return result;
	}

	private boolean getMethodMatchesPost(String method) {
		if (method.matches("add(.*)") || method.matches("post(.*)")) {
			return true;
		}
		return false;
	}

	private String getCacheKey(String targetName, String methodName, Object[] arguments) {
		StringBuffer sbu = new StringBuffer();
		sbu.append(targetName).append("_" + appconfig.getWorkerId() + "_").append(methodName);
		sbu.append("_").append(gson.toJson(arguments));
		return sbu.toString();
	}

}
