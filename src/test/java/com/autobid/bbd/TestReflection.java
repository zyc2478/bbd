package com.autobid.bbd;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;

public class TestReflection {
	
	@Test
	public void testReflect() throws ClassNotFoundException, InstantiationException, IllegalAccessException, 
			NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
		String className = "com.autobid.bbd.FunClass";
		String methodName = "sayHello";
		Class<?> clz = Class.forName(className);  
		Object obj = clz.newInstance();
		//获取方法  
		Method m = obj.getClass().getDeclaredMethod(methodName, String.class);
		//调用方法  
		String  result = (String) m.invoke(obj, "aaaaa");
		System.out.println(result);
		
		
  
		//获取字段
		Field[] fields = obj.getClass().getDeclaredFields();
		for(int i=0;i<fields.length;i++) {
			Field f = fields[i];
			f.setAccessible(true);
			Object value = f.get(obj); 
			//System.out.println(value);
			StringBuffer strBuffer = new StringBuffer();
			strBuffer.append(value);
			strBuffer.append(" My name is:");
			strBuffer.append(fields[i].getName());
			System.out.println(strBuffer);
		}
	}
	
	@Test
	public void testFuncReflect() {
		FunProc.proc();
		FunClass.callProc();
	}
}



class FunClass{
	
	String staticStr = "I'm a static string!";
	String globalStr = "I'm a global string!";
	
	public void setStaticStr(String str) {
		staticStr = str;
		this.globalStr = str + ":global";
	}
	
	public static void callProc() {
		System.out.println("~~~~~ in callProc() ~~~~~~ ");
		FunProc.proc();
	}
	public String sayHello(String s){
		String localStr = s;
		System.out.println(localStr);
		return "hello!";
	}
}

class FunProc{
	public static void proc() {
		System.out.println("~~~~~ this is the proc in fun! ~~~~~");
		StackTraceElement stack[] = Thread.currentThread().getStackTrace();
		for(int i=0;i<stack.length;i++) {
			System.out.println(stack[i].getClassName()+"."+stack[i].getMethodName());
		}
		System.out.println("第2个元素是："+stack[1].getClassName()+"."+stack[1].getMethodName());
/*		String callName=stack[2].getClassName();
		if (!callName.endsWith("SelectDefBindingContainer")){	}
		System.out.println("called by "+ste.getClassName()+"."+ste.getMethodName()+"/"+ste.getFileName());*/
	}
}
