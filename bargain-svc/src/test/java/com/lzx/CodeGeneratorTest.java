package com.lzx;

import com.lzx.util.mybatisplus.MybatisPlusGeneratorUtil;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

@SpringBootTest
public class CodeGeneratorTest {
    // Mysql
    @Test
    public void test() {
        String[] tableNames = {"product"};
        MybatisPlusGeneratorUtil generator = new MybatisPlusGeneratorUtil(
                "Bobby.zx.lin",
                "com.lzx",
                "jdbc:mysql://192.168.17.130:3306/bargain",
                "root",
                "123456"
        );
        generator.startGenerator(tableNames);
    }

    // redis
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    public void redis() {
        redisTemplate.opsForValue().set("name", "令在下");
        Object name = redisTemplate.opsForValue().get("name");
        System.out.println(name);
    }

    //rabbitmq
    @Resource
    private RabbitTemplate rabbitTemplate;

    @Test
    public void producer(){
        String message = "This is a message";
        rabbitTemplate.convertAndSend("notice_queue", message);
    }

    @Test
    @RabbitHandler
    @RabbitListener(queuesToDeclare = @Queue("notice_queue"))
    public void consumer(String message){
        System.out.println("receive message: " + message);
    }
}
