package k1.work.sampler;

import org.apache.jmeter.testbeans.BeanInfoSupport;

import java.beans.PropertyDescriptor;


/**
 * BeanInfo class for RocketmqSampler.
 * This class provides information about the configurable properties of the RocketmqSampler component in JMeter,
 * including default values and UI settings.
 *
 * @author zhangzheqi
 */
public class RocketMessageQueueSamplerBeanInfo extends BeanInfoSupport {
    /**
     * Constructs BeanInfo for the RocketmqSampler class.
     */
    public RocketMessageQueueSamplerBeanInfo() {
        super(RocketMessageQueueSampler.class);

        PropertyDescriptor p;
        createPropertyGroup("Authentication Information", new String[]{"accessKey", "secretKey", "address", "group"});

        // Configure the "AccessKey" property
        p = property("accessKey");
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, "");

        // Configure the "SecretKey" property
        p = property("secretKey");
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, "");

        // Configure the "Address" property
        p = property("address");
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, "");

        p = property("group");
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, "");

        // Group 2: Message Configuration Group
        createPropertyGroup("Message Configuration", new String[]{"messageTag", "messageKey", "messageBody", "messageTopic"});

        // Configure the "MessageTopic" property
        p = property("messageTopic");
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, "");


        // Configure the "MessageTag" property
        p = property("messageTag");
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, "");

        // Configure the "MessageKey" property
        p = property("messageKey");
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, "");

        // Configure the "MessageBody" property
        p = property("messageBody");
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, "");

        // Group 3: Additional Configuration Group
        createPropertyGroup("Additional Configuration", new String[]{"delayDate", "requestMethod"});

        // Configure the "DelayDate" property
        p = property("delayDate");
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, 0);

        // Configure the "RequestMethod" property
        p = property("requestMethod");
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, RocketMessageQueueSampler.SYNC_SIMPLE_SEND);
        p.setValue(NOT_OTHER, Boolean.TRUE);
        p.setValue(TAGS, new String[]{
                RocketMessageQueueSampler.SYNC_SIMPLE_SEND,
                RocketMessageQueueSampler.SYNC_DELAY_SEND,
                RocketMessageQueueSampler.SYNC_SPEED_SEND
        });
    }
}
