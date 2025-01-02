package com.ruoyi.hotel.util;

import com.ruoyi.hotel.domain.HotelOrders;
import com.ruoyi.hotel.mapper.HotelOrdersMapper;
import org.redisson.api.RQueue;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author lan
 * @version 1.0
 * @data 1/2/2025 6:51 下午
 */
@Component
public class OrderDelayQueueConsumer {

    private static final String ORDER_DELAY_QUEUE = "ORDER_DELAY_QUEUE";

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private HotelOrdersMapper hotelOrdersMapper;

    // 可用 @PostConstruct 或其他方式，在项目启动后立即开始监听
    @PostConstruct
    public void initConsumer() {
        new Thread(() -> {
            RQueue<Long> queue = redissonClient.getQueue(ORDER_DELAY_QUEUE);
            while (true) {
                try {
                    // take() 会阻塞直到队列里有元素
                    Long orderId = queue.poll();
                    if (orderId != null) {
                        // 拿到订单ID，检查数据库看该订单是否仍为“待支付”状态
                        HotelOrders order = hotelOrdersMapper.selectHotelOrdersById(orderId);
                        if (order != null && order.getStatus() == 0L) {
                            // 0L => 待支付
                            order.setStatus(2L); // 2 => 已取消
                            hotelOrdersMapper.updateHotelOrders(order);
                            System.out.println("订单 " + orderId + " 超时未支付，已自动取消");
                        } else {
                            System.out.println("订单 " + orderId + " 已支付或不存在，跳过处理");
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }, "OrderDelayQueueConsumerThread").start();
    }
}
