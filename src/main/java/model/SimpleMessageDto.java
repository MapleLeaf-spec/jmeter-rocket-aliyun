package model;


/**
 * 简单消息数据传输对象（DTO），用于封装需要发送的消息的各种属性。
 * 包括消息的主题、标签、键、主体内容、延迟时间、定时日期、分片键、环境、数据源和组ID等。
 *
 * @author 张哲祺
 */
public class SimpleMessageDto {
    /**
     * 消息的主题，不能为空。
     */

    private String messageTopic;
    /**
     * 消息的标签，默认为空字符串。
     */

    private String messageTag = "";
    /**
     * 消息的键，用于构建消息的唯一标识，不检测重复，可以为空，不影响消息收发，默认为空字符串。
     */

    private String messageKey = "";
    /**
     * 消息的主体内容，不能为空。
     */

    private String messageBody;
    /**
     * 消息的延迟时间，单位为毫秒，默认为0。
     */


    private Long delayDate = 0L;
    /**
     * 消息的分片键，在分区事务中需要添加分片键。
     */
    private String shardingKey;

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

    public Long getDelayDate() {
        return delayDate;
    }

    public void setDelayDate(Long delayDate) {
        this.delayDate = delayDate;
    }

    public String getShardingKey() {
        return shardingKey;
    }

    public void setShardingKey(String shardingKey) {
        this.shardingKey = shardingKey;
    }

}
