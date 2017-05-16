package com.yt.app.frame.a;

import com.yt.app.frame.b.DataSourceAnnotation;
import com.yt.app.frame.e.Aj;
import com.yt.app.frame.f.DataSourceEnum;

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

@Aspect
@Component
public class Aa {

	@Autowired
	private Aj dataaj;

	@Pointcut("(execution(* com.yt.app.api.v1.service..*(..))) || (execution(* com.yt.app.common.base.IBaseService.*(..))) ")
	public void aspect() {
	}

	@Before("aspect()")
	public void a(JoinPoint paramJoinPoint) throws Throwable {
		Annotation annotation = null;
		Method me = ((MethodSignature) paramJoinPoint.getSignature()).getMethod();
		annotation = me.getAnnotations().length > 0 ? me.getAnnotations()[0] : null;
		if (annotation == null) {
			Method proxyMethod = ((MethodSignature) paramJoinPoint.getSignature()).getMethod();
			Method mes = proxyMethod.getDeclaringClass().getMethod(proxyMethod.getName(), proxyMethod.getParameterTypes());
			annotation = mes.getAnnotations().length > 0 ? mes.getAnnotations()[0] : null;
			if (annotation == null) {
				this.dataaj.o();
			}
		} else if (annotation.annotationType().equals(DataSourceAnnotation.class)) {
			DataSourceAnnotation rcea = (DataSourceAnnotation) annotation;
			if (rcea.datasource().equals(DataSourceEnum.SLAVE)) {
				this.dataaj.n();
			} else {
				this.dataaj.o();
			}
		}
	}

	@After("aspect()")
	public void b(JoinPoint paramJoinPoint) {
		this.dataaj.p();
	}
}