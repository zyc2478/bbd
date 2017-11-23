package com.autobid.util;

import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;

public class OrderProperties extends Properties {

	private static final long serialVersionUID = -1458868163395916369L;
	
	 /** 
     * ��ΪLinkedHashSet�������ԣ�key�ڵ���put()��ʱ�򣬴�ŵ�����Ҳ������ 
     */ 
	private final LinkedHashSet<Object> keys = new LinkedHashSet<>();  
	
	@Override  
    public Enumeration<Object> keys() {  
        return Collections.enumeration(keys);  
    }  
	
    
    /** 
     * ��put��ʱ��ֻ�ǰ�key����Ĵ浽{@link OrderedProperties#keys} 
     * ȡֵ��ʱ�򣬸��������keys�����������ȡ������value 
     * ��Ȼ���ø����put����,Ҳ����key value ��ֵ�Ի��Ǵ���hashTable��. 
     * ֻ�����ڶ��˸���key������{@link OrderedProperties#keys} 
     */  
    @Override  
    public Object put(Object key, Object value) {  
        keys.add(key);  
        return super.put(key, value);  
    }  
    
    /** 
     * ��Ϊ��д������������ڣ���ʽһ����ʱ��,��������� 
     * {@link MainOrder#printProp} 
     */  
    @Override  
    public Set<String> stringPropertyNames() {  
        Set<String> set = new LinkedHashSet<>();  
        for (Object key : this.keys) {  
            set.add((String) key);  
        }  
        return set;  
    } 
    
    /** 
     * ��Ϊ��д������������ڣ���ʽ������ʱ��,��������� 
     * {@link MainOrder#printProp} 
     */  
    @Override  
    public Set<Object> keySet() {  
        return keys;  
    }  
    
    /** 
     * ��Ϊ��д������������ڣ���ʽ�ģ���ʱ��,��������� 
     * {@link MainOrder#printProp} 
     */  
    @Override  
    public Enumeration<?> propertyNames() {  
        return Collections.enumeration(keys);  
    }  
}
