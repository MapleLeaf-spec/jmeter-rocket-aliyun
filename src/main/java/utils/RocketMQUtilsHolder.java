package utils;

import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.shade.org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * @author zhangzheqi
 */
public class RocketMQUtilsHolder {

    private static final Logger log = LoggerFactory.getLogger(RocketMQUtilsHolder.class);

    private static volatile Producer producerInstance;

    private RocketMQUtilsHolder() {
        // 私有构造函数，防止外部实例化
    }


    public static Producer getProducerInstance(String accessKey, String secretKey, String address, String group) {
        if (producerInstance == null) {
            synchronized (RocketMQUtilsHolder.class) {
                if (producerInstance == null) {
                    log.info("创建生产者实例");
                    producerInstance = createAndStartProducer(accessKey, secretKey, address, group);
                }
            }
        }
        return producerInstance;
    }

    private static Producer createAndStartProducer(String accessKey, String secretKey, String address, String group) {


        // producer实例配置初始化
        Properties properties = new Properties();

        if (StringUtils.isBlank(accessKey) || StringUtils.isBlank(secretKey) || StringUtils.isBlank(address)) {
            log.error("accessKey, secretKey, address 不能为空");
        }
        if (!StringUtils.isBlank(group)) {
            properties.put(PropertyKeyConst.GROUP_ID, group);
        }
        // 设置AccessKey ID和SecretKey
        properties.put(PropertyKeyConst.AccessKey, accessKey);
        properties.put(PropertyKeyConst.SecretKey, secretKey);


        // 设置发送超时时间，单位：毫秒
        properties.setProperty(PropertyKeyConst.SendMsgTimeoutMillis, "3000");

        // 设置TCP接入域名
        properties.put(PropertyKeyConst.NAMESRV_ADDR, address);

        log.info("配置信息为：{}", properties);

        Producer producer = ONSFactory.createProducer(properties);

        // 在发送消息前，必须调用start方法来启动Producer，只需调用一次即可
        log.info("启动生产者实例");
        producer.start();

        return producer;
    }

    /**
     * 手动重置 producerInstance，下一次请求将重新创建新的 Producer 实例。
     */
    public static void resetProducerInstance() {
        log.info("手动重置 producerInstance");
        if (producerInstance != null) {
            producerInstance.shutdown();
        }
        producerInstance = null;
    }
}
