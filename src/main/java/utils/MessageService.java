package utils;


import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.SendResult;
import model.SimpleMessageDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serial;
import java.io.Serializable;


/**
 * MQ消息发送服务
 *
 * @author zhangzheqi
 */
public class MessageService {
    private static final Logger log = LoggerFactory.getLogger(MessageService.class);
    
    private final transient Producer producer; // 将生产者对象提升为成员变量，避免重复创建和启动

    public MessageService(Producer existingProducer) {
        this.producer = existingProducer; // 使用传入的生产者对象
    }

    /**
     * 同步发送简单消息
     *
     * @param message 消息传输对象
     * @return 发送结果
     */

    public SendResult syncSimpleSend(SimpleMessageDto message) {
        log.info("消息初始内容为：{}", message.toString());
        Message event = getMessage(message);
        log.info("开始发送MQ");
        log.info("消息内容转化为：{}", event.toString());
        SendResult result = producer.send(event); // 直接使用成员变量的生产者对象
        log.info("syncSimpleSend message success. {}", result.toString());
        log.info("发送MQ已经完成");
        return result;
    }

    /**
     * 同步发送带延迟时间的消息
     *
     * @param message 消息传输对象
     * @return 发送结果
     */

    public SendResult syncDelaySend(SimpleMessageDto message) {
        Message event = getMessage(message);
        event.setStartDeliverTime(System.currentTimeMillis() + message.getDelayDate());
        return producer.send(event); // 直接使用成员变量的生产者对象
    }


    /**
     * 单向发送简单消息
     * 单向（Oneway）发送特点为发送方只负责发送消息，不等待服务器回应且没有回调函数触发，即只发送请求不等待应答。
     * 此方式发送消息的过程耗时非常短，一般在微秒级别。适用于某些耗时非常短，但对可靠性要求并不高的场景，例如日志收集。
     *
     * @param message 消息传输对象
     */

    public void asyncSendNotResult(SimpleMessageDto message) {
        Message event = getMessage(message);
        producer.sendOneway(event); // 直接使用成员变量的生产者对象
        log.info("asyncSendNotResult message success. {}", event.toString());
    }

    /**
     * 发送分区消息
     *
     * @param message 消息传输对象
     * @return 发送结果
     */

    public SendResult sendPartitionMessage(SimpleMessageDto message) {
        Message event = getMessage(message);
        event.setShardingKey(message.getShardingKey());
        return producer.send(event); // 直接使用成员变量的生产者对象
    }

    /**
     * 发送自定义分区消息
     *
     * @param message 消息传输对象
     */

    public void customMessagePartitionMessage(SimpleMessageDto message) {
        Message event = getMessage(message);
        event.setShardingKey(message.getShardingKey());
        producer.sendOneway(event); // 直接使用成员变量的生产者对象
    }

    /**
     * 根据 SimpleMessageDto 对象创建 Message 对象
     *
     * @param simpleMessageDto 消息传输对象
     * @return Message 对象
     */
    private Message getMessage(SimpleMessageDto simpleMessageDto) {
        return new Message(
                simpleMessageDto.getMessageTopic(),
                simpleMessageDto.getMessageTag(),
                simpleMessageDto.getMessageKey(),
                simpleMessageDto.getMessageBody().getBytes());
    }

}
