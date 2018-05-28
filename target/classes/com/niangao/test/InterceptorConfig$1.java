// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   InterceptorConfig.java

package com.niangao.test;

import java.io.PrintStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

// Referenced classes of package com.niangao.test:
//			InterceptorConfig

class InterceptorConfig$1 extends HandlerInterceptorAdapter
{

	final InterceptorConfig this$0;

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
		throws Exception
	{
		System.out.println("r==========================我的interceptor==========================");
		System.out.println((new StringBuilder()).append("request ").append(request).toString());
		System.out.println((new StringBuilder()).append("response ").append(response).toString());
		System.out.println("r==========================我的interceptor==========================");
		return true;
	}

	InterceptorConfig$1()
	{
		this.this$0 = InterceptorConfig.this;
		super();
	}
}
