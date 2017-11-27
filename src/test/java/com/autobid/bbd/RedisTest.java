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
        //连接redis服务器，localhost:6379
        //jedis = new Jedis("localhost", 6379);
        //权限认证
        //jedis.auth("admin");  
    }
    
   /* *//**
     * redis存储字符串
     * @throws InterruptedException 
     *//*

    public void testString() throws InterruptedException {
        //-----添加数据----------  
    	logger.debug("----testString----");
        jedis.set("name","xinxin");//向key-->name中放入了value-->xinxin  
        System.out.println(jedis.get("name"));//执行结果：xinxin  
        
        jedis.append("name", " is my lover"); //拼接
        System.out.println(jedis.get("name")); 
        
        jedis.del("name");  //删除某个键
        System.out.println(jedis.get("name"));
        //设置多个键值对
        jedis.mset("name","liuling","age","23","qq","476777XXX");
        jedis.incr("age"); //进行加1操作
        jedis.setex("richard", 1, "zeng");
        Thread.sleep(2000);
        System.out.println("my name is " +  jedis.get("richard"));
        System.out.println("76158902 value is:" + jedis.get("76158902"));
        System.out.println("76150916 value is:" + jedis.get("76150916"));
    	if(jedis.exists(String.valueOf(76164467))){
			System.out.println(76164467 + "在Redis中重复！");
    	}
        System.out.println("someone value is:" + jedis.get("someone"));
        System.out.println(jedis.get("name") + "-" + jedis.get("age") + "-" + jedis.get("qq"));
        
    }
    
    *//**
     * redis操作Map
     *//*

    public void testMap() {
        //-----添加数据----------  
    	logger.debug("----testMap----");
        Map<String, String> map = new HashMap<String, String>();
        jedis.del("user");
        map.put("name", "xinxin");
        map.put("age", "22");
        map.put("qq", "123456");
        jedis.hmset("user",map);
        //取出user中的name，执行结果:[minxr]-->注意结果是一个泛型的List  
        //第一个参数是存入redis中map对象的key，后面跟的是放入map中的对象的key，后面的key可以跟多个，是可变参数  
        List<String> rsmap = jedis.hmget("user", "name", "age", "qq");
        System.out.println(rsmap);  
  
        //删除map中的某个键值  
        jedis.hdel("user","age");
        System.out.println(jedis.hmget("user", "age")); //因为删除了，所以返回的是null  
        System.out.println(jedis.hlen("user")); //返回key为user的键中存放的值的个数2 
        System.out.println(jedis.exists("user"));//是否存在key为user的记录 返回true  
        System.out.println(jedis.hkeys("user"));//返回map对象中的所有key  
        System.out.println(jedis.hvals("user"));//返回map对象中的所有value 
  
        Iterator<String> iter=jedis.hkeys("user").iterator();  
        while (iter.hasNext()){  
            String key = iter.next();  
            System.out.println(key+":"+jedis.hmget("user",key));  
        }  
    }
    
    *//** 
     * jedis操作List 
     *//*  

    public void testList(){  
    	logger.debug("----testList----");
        //开始前，先移除所有的内容  
        jedis.del("java framework");  
        System.out.println(jedis.lrange("java framework",0,-1));  
        //先向key java framework中存放三条数据  
        jedis.lpush("java framework","spring");  
        jedis.lpush("java framework","struts");  
        jedis.lpush("java framework","hibernate");  
        //再取出所有数据jedis.lrange是按范围取出，  
        // 第一个是key，第二个是起始位置，第三个是结束位置，jedis.llen获取长度 -1表示取得所有  
        System.out.println(jedis.lrange("java framework",0,-1));  
        
        jedis.del("java framework");
        jedis.rpush("java framework","spring");  
        jedis.rpush("java framework","struts");  
        jedis.rpush("java framework","hibernate"); 
        System.out.println(jedis.lrange("java framework",0,-1));
    }  
    
    *//** 
     * jedis操作Set 
     *//*  

    public void testSet(){  
    	logger.debug("----testSet----");
    	jedis.del("user");
        //添加  
        jedis.sadd("user","liuling");  
        jedis.sadd("user","xinxin");  
        jedis.sadd("user","ling");  
        jedis.sadd("user","zhangxinxin");
        jedis.sadd("user","who");  
        //移除noname  
        jedis.srem("user","who");  
        System.out.println(jedis.smembers("user"));//获取所有加入的value  
        System.out.println(jedis.sismember("user", "who"));//判断 who 是否是user集合的元素  
        System.out.println(jedis.srandmember("user"));  
        System.out.println(jedis.scard("user"));//返回集合的元素个数  
    }  
  
  
    public void testSort() throws InterruptedException {  
    	logger.debug("----testSort----");
        //jedis 排序  
        //注意，此处的rpush和lpush是List的操作。是一个双向链表（但从表现来看的）  
        jedis.del("a");//先清除数据，再加入数据进行测试  
        jedis.rpush("a", "1");  
        jedis.lpush("a","6");  
        jedis.lpush("a","3");  
        jedis.lpush("a","9");  
        System.out.println(jedis.lrange("a",0,-1));// [9, 3, 6, 1]  
        System.out.println(jedis.sort("a")); //[1, 3, 6, 9]  //输入排序后结果  
        System.out.println(jedis.lrange("a",0,-1));  
    }  
    */

    public void testRedisPool() {
    	logger.debug("----testRedisPool----");
        RedisUtil.getJedis().set("newname", "中文测试");
        System.out.println(RedisUtil.getJedis().get("newname"));
    }
}