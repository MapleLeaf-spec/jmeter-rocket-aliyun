package k1.work.sampler;

import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.SendResult;
import com.aliyun.openservices.shade.com.alibaba.fastjson.JSON;
import com.aliyun.openservices.shade.org.apache.commons.lang3.StringUtils;
import model.SimpleMessageDto;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.testbeans.TestBean;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.MessageService;
import utils.RocketMQUtilsHolder;

/**
 * @author zhangzheqi
 */
public class RocketMessageQueueSampler extends AbstractTestElement implements TestBean, Sampler {

    private static final Logger log = LoggerFactory.getLogger(RocketMessageQueueSampler.class);

    public static final String SYNC_SIMPLE_SEND = "同步普通消息";

    public static final String SYNC_DELAY_SEND = "同步延迟消息";

    public static final String SYNC_SPEED_SEND = "异步极速消息";




    private transient String accessKey;

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    private transient String secretKey;
    private transient String address;
    private transient String group;
    private transient String messageTopic;
    private transient String messageTag;
    private transient String messageKey;
    private transient String messageBody;
    private transient int delayDate;
    private transient String requestMethod;


    public String getMessageTopic() {
        return messageTopic;
    }

    public void setMessageTopic(String messageTopic) {
        this.messageTopic = messageTopic;
    }

    public String getMessageTag() {
        return messageTag;
    }

    public void setMessageTag(String messageTag) {
        this.messageTag = messageTag;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public int getDelayDate() {
        return delayDate;
    }

    public void setDelayDate(int delayDate) {
        this.delayDate = delayDate;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    //sample 方法则会在每个线程的每个采样时执行
    @Override
    public SampleResult sample(Entry entry) {
        log.info("开始处理请求");
        SampleResult sampleResult = new SampleResult();
        sampleResult.sampleStart();
        //构建请求体
        try {
            if (StringUtils.isBlank(getAccessKey()) || StringUtils.isBlank(getSecretKey()) || StringUtils.isBlank(getAddress())) {
                log.info("开始进行参数基础连接环境检查");
                sampleResult.setSuccessful(false);
                sampleResult.setResponseData("Access密钥，Secret密钥，连接地址(address) 不能为空", "UTF-8");
                return sampleResult;
            }
            Producer producer = RocketMQUtilsHolder.getProducerInstance(getAccessKey(), getSecretKey(), getAddress(), getGroup());

            if (StringUtils.isBlank(getMessageTopic()) || StringUtils.isBlank(getMessageTag()) || StringUtils.isBlank(getMessageBody())) {
                log.info("开始进行基础消息队列配置检查");
                sampleResult.setSuccessful(false);
                sampleResult.setResponseData("消息主题，消息标签，消息体不能为空", "UTF-8");
                return sampleResult;
            }
            log.info("开始构建请求体");
            SimpleMessageDto messageDto = new SimpleMessageDto();
            messageDto.setMessageTopic(getMessageTopic());
            messageDto.setMessageTag(getMessageTag());
            messageDto.setMessageKey(getMessageKey());
            messageDto.setMessageBody(getMessageBody());
            messageDto.setDelayDate((long) getDelayDate());

            log.info("开始请求 MQ");
            log.info("请求参数为：{}", JSON.toJSONString(messageDto));

            log.info("请求方式为：{}", getRequestMethod());

            MessageService messageService = new MessageService(producer);
            SendResult sendResult = null;
            String logMessage = null;

            switch (getRequestMethod()) {
                case "同步普通消息":
                    sendResult = messageService.syncSimpleSend(messageDto);
                    logMessage = "阿里云发送同步消息响应结果";
                    break;

                case "同步延迟消息":
                    sendResult = messageService.syncDelaySend(messageDto);
                    logMessage = "阿里云发送同步延迟消息响应结果";
                    break;

                case "异步极速消息":
                    messageService.asyncSendNotResult(messageDto);
                    logMessage = "异步极速消息成功";
                    break;

                default:
                    log.warn("未知的请求方法: {}", getRequestMethod());
                    break;
            }

            if (sendResult != null) {
                log.info("{} {}", logMessage, sampleResult);
                sampleResult.setResponseData(JSON.toJSONString(sendResult), "UTF-8");
            }else {
                sampleResult.setResponseData("异步极速消息发送成功", "UTF-8");
            }

            sampleResult.setSamplerData(JSON.toJSONString(messageDto));
            sampleResult.setSampleLabel(getName());
            sampleResult.setSuccessful(true);

        } catch (Exception ex) {
            log.info(ex.getMessage());
            log.info("关闭阿里云链接");
            RocketMQUtilsHolder.resetProducerInstance();
            String errorMessageBuilder = "请求失败，可能是由于配置项未正确设置，或者配置项缺失。请确保配置信息准确无误。同时，也需要检查是否填写了正确的 Topic，确保 Topic 存在且拼写正确。另外，请确保请求的用户拥有足够的权限执行该操作。\n\n" +
                    "如果经过检查以上因素后问题仍未解决，建议联系中台测试组的 4F_B268 童鞋，请求协助进行进一步的排查和解决。";

            String errorMessage = errorMessageBuilder + "\n实际报错原因：" + ex.getMessage();


            sampleResult.setResponseData(errorMessage, "UTF-8");
        } finally {
            log.info("请求完成");
        }

        sampleResult.setDataType(SampleResult.TEXT);
        sampleResult.sampleEnd();
        return sampleResult;
    }

}
