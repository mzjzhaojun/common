package com.yt.app.frame.p;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

@Component
public class RedisCacheUtil {

	@Autowired
	public RedisTemplate<String, Object> redisTemplate;

	public void setCacheObject(String key, Object value) {
		ValueOperations<String, Object> operation = redisTemplate.opsForValue();
		operation.set(key, value);
	}

	public Object getCacheObject(String key) {
		ValueOperations<String, Object> operation = redisTemplate.opsForValue();
		return operation.get(key);
	}

	public void setCacheList(String key, Object value) {
		ListOperations<String, Object> listOperation = redisTemplate.opsForList();
		listOperation.rightPush(key, value);
	}

	public void setCacheListExpireTime(String key, Object value, Long expireTime) {
		ListOperations<String, Object> listOperation = redisTemplate.opsForList();
		listOperation.rightPush(key, value);
		if (expireTime != null) {
			redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
		}
	}

	public Object getCacheList(String key) {
		ListOperations<String, Object> listOperation = redisTemplate.opsForList();
		return listOperation.leftPop(key);
	}

	public void setCacheSet(String key, Object value) {
		BoundSetOperations<String, Object> setOperation = redisTemplate.boundSetOps(key);
		setOperation.add(value);
	}

	public Object getCacheSet(String key) {
		BoundSetOperations<String, Object> operation = redisTemplate.boundSetOps(key);
		return operation.pop();
	}

	public Object getCacheMap(String key, String keys) {
		HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
		return hashOperations.get(key, keys);
	}

	public void setCacheMap(String key, String keys, Object value) {
		HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
		hashOperations.put(key, keys, value);
	}

	public void setCacheMapExpireTime(String key, String keys, Object data, Long expireTime) {
		HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
		hashOperations.put(key, keys, data);
		redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
	}

	public void removeCacheMapExpire(String key, String keys) {
		redisTemplate.opsForHash().delete(key, keys);
	}

	public Object getCacheMapExpireTime(String key, String keys, Long expireTime) {
		redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
		return redisTemplate.opsForHash().get(key, keys);
	}

	public void delete(final String key) {
		redisTemplate.delete(key);
	}

	// //////////////////////////////////////////////////////////////////////////////分布式加锁实现原子性/////////////////////////////////////////

	// public boolean checkSoldCountByRedisDate(String seckillid, int
	// limitCount, int buyCount, Date endDate, String lock, String userId) {
	// boolean check = false;
	// int expire = 1000;// 默认锁过期时间
	// if (endDate.before(new Date()))
	// return check;
	// if (this.lock(lock, expire)) {
	// Integer flag = (Integer) this.getCacheObject(seckillid + userId);
	// if (flag != null)
	// return check;
	// Integer soldCount = (Integer) this.getCacheObject(seckillid);
	// Integer totalSoldCount = (soldCount == null ? 0 : soldCount) + buyCount;
	// if (totalSoldCount <= limitCount) {
	// this.setCacheObjectExpireTime(seckillid, totalSoldCount,
	// DateTimeUtil.diffDateTime(endDate, new Date()));
	// this.setCacheObjectExpireTime(seckillid + userId, 0,
	// DateTimeUtil.diffDateTime(endDate, new Date()));
	// check = true;
	// }
	// this.remove(lock);
	// } else {
	// this.unDieLock(lock);
	// }
	// return check;
	// }

	// @SuppressWarnings({ "rawtypes", "unchecked" })
	// public Boolean lock(final String lock, final int expire) {
	// return (Boolean) redisTemplate.execute(new RedisCallback<Boolean>() {
	// @Override
	// public Boolean doInRedis(RedisConnection connection) throws
	// DataAccessException {
	// boolean locked = false;
	// Date date = DateTimeUtil.getDateAddMillSecond(null, expire);
	// RedisSerializer serializer = redisTemplate.getValueSerializer();
	// byte[] lockValue = serializer.serialize(date);
	// byte[] lockName = redisTemplate.getStringSerializer().serialize(lock);
	// locked = connection.setNX(lockName, lockValue);
	// // 设定死锁自动过期
	// if (locked)
	// connection.expire(lockName, TimeoutUtils.toSeconds(expire,
	// TimeUnit.MILLISECONDS));
	// connection.close();
	// return locked;
	// }
	// });
	// }

	@SuppressWarnings("rawtypes")
	public Boolean unDieLock(final String lock) {
		return (Boolean) redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				boolean unLock = false;
				byte[] lockName = redisTemplate.getStringSerializer().serialize(lock);
				RedisSerializer serializer = redisTemplate.getValueSerializer();
				Date lockValue = (Date) serializer.deserialize(connection.get(lockName));
				if (lockValue != null && lockValue.getTime() <= (new Date().getTime())) {
					connection.del(lockName);
					unLock = true;
				}
				connection.close();
				return unLock;
			}
		});
	}

	public boolean exists(final String key) {
		return redisTemplate.hasKey(key);
	}

	public Boolean remove(final String key) {
		return redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				byte[] lockName = redisTemplate.getStringSerializer().serialize(key);
				if (connection.exists(lockName)) {
					connection.del(lockName);
				}
				connection.close();
				return true;
			}
		});
	}

	public boolean setCacheObjectExpireTime(final String key, Object value, Long expireTime) {
		boolean result = false;
		try {
			ValueOperations<String, Object> operations = redisTemplate.opsForValue();
			operations.set(key, value);
			if (expireTime != null) {
				redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
			}
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}