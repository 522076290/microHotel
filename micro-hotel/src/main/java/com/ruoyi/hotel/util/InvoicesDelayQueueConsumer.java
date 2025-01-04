package com.ruoyi.hotel.util;

import com.ruoyi.hotel.domain.HotelInvoices;
import com.ruoyi.hotel.domain.HotelOrders;
import com.ruoyi.hotel.mapper.HotelInvoicesMapper;
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
public class InvoicesDelayQueueConsumer {

    private static final String INVOICES_DELAY_QUEUE = "INVOICES_DELAY_QUEUE";

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private HotelOrdersMapper hotelOrdersMapper;

    @Autowired
    private HotelInvoicesMapper hotelInvoicesMapper;

    // 可用 @PostConstruct 或其他方式，在项目启动后立即开始监听
    @PostConstruct
    public void initConsumer() {
        new Thread(() -> {
            RQueue<Long> queue = redissonClient.getQueue(INVOICES_DELAY_QUEUE);
            while (true) {
                try {
                    // take() 会阻塞直到队列里有元素
                    Long invoicesId = queue.poll();
                    if (invoicesId != null) {
                        // 拿到订单ID，检查数据库看该订单是否仍为“待支付”状态
                        HotelInvoices invoices = hotelInvoicesMapper.selectHotelInvoicesById(invoicesId);
                        HotelOrders order = hotelOrdersMapper.selectHotelOrdersById(invoices.getOrderId());
                        if (order != null && order.getMerchantAcceptStatus() == 0L) {
                            // 0L => 未接单
                            order.setStatus(3L); // 3 => 已退款
                            order.setMerchantAcceptStatus(2L); // 2 => 已拒绝
                            hotelOrdersMapper.updateHotelOrders(order);
                            invoices.setId(null);
                            invoices.setTotalRoomCharge(invoices.getTotalRoomCharge().negate());
                            invoices.setPaymentStatus(3L); // 3 => 已退款
                            hotelInvoicesMapper.insertHotelInvoices(invoices);
                            System.out.println("账单 " + invoicesId + " 超时未接单，已自动取消,退还支付款");
                        } else {
                            System.out.println("账单 " + invoicesId + " 已支付或不存在，跳过处理");
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }, "OrderDelayQueueConsumerThread").start();
    }
}
