package com.example.goods.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class AspectConf {

	// 定义一个全局的Pointcut
	@Pointcut("execution(* com.example.goods.controller.TestController.test*(..))")
	public void myPointcut1() {
	}


	// 定义一个Before Advice
	@Before("myPointcut1() && args(tk,..)")
	public void before3(String tk) {
		System.out.println("----------- AspectAdviceBeanUseAnnotation before3  增强  参数tk= " + tk);
	}

	@Around("myPointcut1()")
	public Object around2(ProceedingJoinPoint pjp) throws Throwable {
		System.out.println("----------- AspectAdviceBeanUseAnnotation arround2 环绕-前增强 for " + pjp);
		Object ret = pjp.proceed();
		System.out.println("----------- AspectAdviceBeanUseAnnotation arround2 环绕-后增强 for " + pjp);
		return ret;
	}

	@AfterReturning(pointcut = "myPointcut1()", returning = "retValue")
	public void afterReturning(Object retValue) {
		System.out.println("----------- AspectAdviceBeanUseAnnotation afterReturning 增强 , 返回值为： " + retValue);
	}

	@AfterThrowing(pointcut = "myPointcut1()", throwing = "e")
	public void afterThrowing(JoinPoint jp, Exception e) {
		System.out.println("----------- AspectAdviceBeanUseAnnotation afterThrowing 增强  for " + jp);
		System.out.println("----------- AspectAdviceBeanUseAnnotation afterThrowing 增强  异常 ：" + e);
	}

	@After("myPointcut1()")
	public void after(JoinPoint jp) {
		System.out.println("----------- AspectAdviceBeanUseAnnotation after 增强  for " + jp);
	}

}
