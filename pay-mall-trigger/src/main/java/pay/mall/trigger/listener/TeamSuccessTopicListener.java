package pay.mall.trigger.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 拼团完成消费消息监听器
 */
@Slf4j
@Component
public class TeamSuccessTopicListener {

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = "${spring.rabbitmq.config.consumer.topic_team_success.queue}"),
                    exchange = @Exchange(value = "${spring.rabbitmq.config.consumer.topic_team_success.exchange}", type = ExchangeTypes.TOPIC),
                    key = "${spring.rabbitmq.config.consumer.topic_team_success.routing_key}"
            )
    )
    public void listener(String message) {
        log.info("接收消息:{}", message);
    }
}
