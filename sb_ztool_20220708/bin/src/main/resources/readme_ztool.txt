!!!!!!!! 在新项目中配置好 redis db 等必要的配置项，然后使用 @注解启用相应的功能

@EnableLoginVerification  
	启用登录校验 
	配置项在 LoginVerificationConfiguration
	
@EnableZQPSLimit 
	启用接口限流，具体配置项看注解的属性值
		
	
@EnableRefererAspect
	表示启用RefererAspect，在登录之前的接口上加入@@RefererAnnotation,
	然后在登录接口ok后RefererAspect.get()取得之前的referer在cr.setRedirectURL
	中返回到前端 ，前端判断RedirectURL不为null则跳转到此RedirectURL
	
@EnableZMemoryCache 
	表示启用内存Map实现的缓存

@EnableZRedisCache
	表示启用Redis实现的缓存
	
@EnableZRedisMQ
	表示启用redis实现的MQ
	@MQConsumer 
		标记一个方法，表示此方法是一个消费者
	@Autowired MQClient client; 
		生产者使用client.send发送消息
	
# 线程池 
	ZE ze = ZES.newZE()来获取一个新的线程池
	ze.executeXXX 来执行一个任务		
	