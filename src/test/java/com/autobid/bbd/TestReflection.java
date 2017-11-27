package com.autobid.bbd;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;

public class TestReflection {
	
	@Test
	public void testRefelect() throws ClassNotFoundException, InstantiationException, IllegalAccessException, 
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
}


class FunClass{
	
	static String staticStr = "I'm a static string!";
	String globalStr = "I'm a global string!";
	
	public void setStaticStr(String str) {
		staticStr = str;
		this.globalStr = str + ":global";
	}
	public String sayHello(String s){
		String localStr = s;
		System.out.println(localStr);
		return "hello!";
	}
}
