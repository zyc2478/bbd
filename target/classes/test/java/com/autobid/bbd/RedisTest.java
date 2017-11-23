package com.autobid.bbd;

//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.Before;
//import org.junit.Test;

import com.autobid.util.RedisUtil;

//import redis.clients.jedis.Jedis;

public class RedisTest {
    //private Jedis jedis; 
	private static Logger logger = Logger.getLogger(RedisTest.class);  
	
    @Before
    public void setup() {
        //����redis��������localhost:6379
        //jedis = new Jedis("localhost", 6379);
        //Ȩ����֤
        //jedis.auth("admin");  
    }
    
   /* *//**
     * redis�洢�ַ���
     * @throws InterruptedException 
     *//*

    public void testString() throws InterruptedException {
        //-----�������----------  
    	logger.debug("----testString----");
        jedis.set("name","xinxin");//��key-->name�з�����value-->xinxin  
        System.out.println(jedis.get("name"));//ִ�н����xinxin  
        
        jedis.append("name", " is my lover"); //ƴ��
        System.out.println(jedis.get("name")); 
        
        jedis.del("name");  //ɾ��ĳ����
        System.out.println(jedis.get("name"));
        //���ö����ֵ��
        jedis.mset("name","liuling","age","23","qq","476777XXX");
        jedis.incr("age"); //���м�1����
        jedis.setex("richard", 1, "zeng");
        Thread.sleep(2000);
        System.out.println("my name is " +  jedis.get("richard"));
        System.out.println("76158902 value is:" + jedis.get("76158902"));
        System.out.println("76150916 value is:" + jedis.get("76150916"));
    	if(jedis.exists(String.valueOf(76164467))){
			System.out.println(76164467 + "��Redis���ظ���");
    	}
        System.out.println("someone value is:" + jedis.get("someone"));
        System.out.println(jedis.get("name") + "-" + jedis.get("age") + "-" + jedis.get("qq"));
        
    }
    
    *//**
     * redis����Map
     *//*

    public void testMap() {
        //-----�������----------  
    	logger.debug("----testMap----");
        Map<String, String> map = new HashMap<String, String>();
        jedis.del("user");
        map.put("name", "xinxin");
        map.put("age", "22");
        map.put("qq", "123456");
        jedis.hmset("user",map);
        //ȡ��user�е�name��ִ�н��:[minxr]-->ע������һ�����͵�List  
        //��һ�������Ǵ���redis��map�����key����������Ƿ���map�еĶ����key�������key���Ը�������ǿɱ����  
        List<String> rsmap = jedis.hmget("user", "name", "age", "qq");
        System.out.println(rsmap);  
  
        //ɾ��map�е�ĳ����ֵ  
        jedis.hdel("user","age");
        System.out.println(jedis.hmget("user", "age")); //��Ϊɾ���ˣ����Է��ص���null  
        System.out.println(jedis.hlen("user")); //����keyΪuser�ļ��д�ŵ�ֵ�ĸ���2 
        System.out.println(jedis.exists("user"));//�Ƿ����keyΪuser�ļ�¼ ����true  
        System.out.println(jedis.hkeys("user"));//����map�����е�����key  
        System.out.println(jedis.hvals("user"));//����map�����е�����value 
  
        Iterator<String> iter=jedis.hkeys("user").iterator();  
        while (iter.hasNext()){  
            String key = iter.next();  
            System.out.println(key+":"+jedis.hmget("user",key));  
        }  
    }
    
    *//** 
     * jedis����List 
     *//*  

    public void testList(){  
    	logger.debug("----testList----");
        //��ʼǰ�����Ƴ����е�����  
        jedis.del("java framework");  
        System.out.println(jedis.lrange("java framework",0,-1));  
        //����key java framework�д����������  
        jedis.lpush("java framework","spring");  
        jedis.lpush("java framework","struts");  
        jedis.lpush("java framework","hibernate");  
        //��ȡ����������jedis.lrange�ǰ���Χȡ����  
        // ��һ����key���ڶ�������ʼλ�ã��������ǽ���λ�ã�jedis.llen��ȡ���� -1��ʾȡ������  
        System.out.println(jedis.lrange("java framework",0,-1));  
        
        jedis.del("java framework");
        jedis.rpush("java framework","spring");  
        jedis.rpush("java framework","struts");  
        jedis.rpush("java framework","hibernate"); 
        System.out.println(jedis.lrange("java framework",0,-1));
    }  
    
    *//** 
     * jedis����Set 
     *//*  

    public void testSet(){  
    	logger.debug("----testSet----");
    	jedis.del("user");
        //���  
        jedis.sadd("user","liuling");  
        jedis.sadd("user","xinxin");  
        jedis.sadd("user","ling");  
        jedis.sadd("user","zhangxinxin");
        jedis.sadd("user","who");  
        //�Ƴ�noname  
        jedis.srem("user","who");  
        System.out.println(jedis.smembers("user"));//��ȡ���м����value  
        System.out.println(jedis.sismember("user", "who"));//�ж� who �Ƿ���user���ϵ�Ԫ��  
        System.out.println(jedis.srandmember("user"));  
        System.out.println(jedis.scard("user"));//���ؼ��ϵ�Ԫ�ظ���  
    }  
  
  
    public void testSort() throws InterruptedException {  
    	logger.debug("----testSort----");
        //jedis ����  
        //ע�⣬�˴���rpush��lpush��List�Ĳ�������һ��˫���������ӱ��������ģ�  
        jedis.del("a");//��������ݣ��ټ������ݽ��в���  
        jedis.rpush("a", "1");  
        jedis.lpush("a","6");  
        jedis.lpush("a","3");  
        jedis.lpush("a","9");  
        System.out.println(jedis.lrange("a",0,-1));// [9, 3, 6, 1]  
        System.out.println(jedis.sort("a")); //[1, 3, 6, 9]  //�����������  
        System.out.println(jedis.lrange("a",0,-1));  
    }  
    */

    public void testRedisPool() {
    	logger.debug("----testRedisPool----");
        RedisUtil.getJedis().set("newname", "���Ĳ���");
        System.out.println(RedisUtil.getJedis().get("newname"));
    }
}