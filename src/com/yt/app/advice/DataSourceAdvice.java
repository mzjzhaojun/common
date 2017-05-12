package com.yt.app.advice;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yt.app.annotation.DataSourceAnnotation;
import com.yt.app.enums.DataSourceEnum;
import com.yt.app.frame.datasource.DynamicDataSource;

@Aspect
@Component
public class DataSourceAdvice {
	@Autowired
	private DynamicDataSource dataSourceHolder;

	@Pointcut("(execution(* com.yt.app.api.v1.service..*(..))) || (execution(* com.yt.app.common.base.IBaseService.*(..))) ")
	public void aspect() {
	}

	@Before("aspect()")
	public void Before(JoinPoint joinPoint) throws Throwable {
		Annotation annotation = null;
		Method me = ((MethodSignature) joinPoint.getSignature()).getMethod();
		annotation = me.getAnnotations().length > 0 ? me.getAnnotations()[0] : null;
		if (annotation == null) {
			Method proxyMethod = ((MethodSignature) joinPoint.getSignature()).getMethod();
			Method mes = proxyMethod.getDeclaringClass().getMethod(proxyMethod.getName(), proxyMethod.getParameterTypes());
			annotation = mes.getAnnotations().length > 0 ? mes.getAnnotations()[0] : null;
			if (annotation == null) {
				dataSourceHolder.markMaster();
			}
		} else if (annotation.annotationType().equals(DataSourceAnnotation.class)) {
			DataSourceAnnotation rcea = (DataSourceAnnotation) annotation;
			if (rcea.datasource().equals(DataSourceEnum.SLAVE)) {
				dataSourceHolder.markSlave();
			} else {
				dataSourceHolder.markMaster();
			}
		}
	}

	@After("aspect()")
	public void After(JoinPoint joinPoint) throws Throwable {
		dataSourceHolder.markRemove();
	}

}
