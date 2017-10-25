package com.subway.utils;

import com.alibaba.fastjson.JSON;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;


/**
 * Created by huangbin on 2017/10/12.
 */

/**
 *
 * <p>
 *  Redis瀹㈡埛绔闂�
 * </p>
 *
 * @author 鍗撹僵
 * @鍒涘缓鏃堕棿锛�2014骞�7鏈�11鏃�
 * @version锛� V1.0
 */
public class RedisUtils {

    public  static  JedisPool jedisPool; // 姹犲寲绠＄悊jedis閾炬帴姹�

    static {
        JedisPoolConfig config = new JedisPoolConfig();
        //璁剧疆鏈�澶ц繛鎺ユ暟
        config.setMaxTotal(8);
        //璁剧疆鏈�澶х┖闂叉暟
        config.setMaxIdle(8);
        //璁剧疆瓒呮椂鏃堕棿
        config.setMaxWaitMillis(100000);
        //鍒濆鍖栬繛鎺ユ睜
        jedisPool = new JedisPool(config, "192.168.0.145", 6379);
    }

    /**
     * 鍚戠紦瀛樹腑璁剧疆瀛楃涓插唴瀹�
     * @param key key
     * @param value value
     * @return
     * @throws Exception
     */
    public static boolean  set(String key,String value) throws Exception{
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }finally{
            jedisPool.returnResource(jedis);
        }
    }

    /**
     * 鍚戠紦瀛樹腑璁剧疆瀵硅薄
     * @param key
     * @param value
     * @return
     */
    public static boolean  set(String key,Object value){
        Jedis jedis = null;
        try {
            String objectJson = JSON.toJSONString(value);
            jedis = jedisPool.getResource();
            jedis.set(key, objectJson);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }finally{
            jedisPool.returnResource(jedis);
        }
    }

    /**
     * 鍒犻櫎缂撳瓨涓緱瀵硅薄锛屾牴鎹甼ey
     * @param key
     * @return
     */
    public static boolean del(String key){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.del(key);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }finally{
            jedisPool.returnResource(jedis);
        }
    }

    /**
     * 鏍规嵁key 鑾峰彇鍐呭
     * @param key
     * @return
     */
    public static Object get(String key){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            Object value = jedis.get(key);
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }finally{
            jedisPool.returnResource(jedis);
        }
    }


    /**
     * 鏍规嵁key 鑾峰彇瀵硅薄
     * @param key
     * @return
     */
    public static <T> T get(String key,Class<T> clazz){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String value = jedis.get(key);
            return JSON.parseObject(value, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }finally{
            jedisPool.returnResource(jedis);
        }
    }


}
