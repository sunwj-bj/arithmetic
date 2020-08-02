
import com.Application;
import com.jdbc.shardingShpere.bean.Order;
import com.jdbc.shardingShpere.mapper.OrderMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
//指定springboot的启动类
@SpringBootTest(classes = Application.class)
public class ShardingTest {

    @Autowired
    private OrderMapper orderMapper;

    //添加操作
    @Test
    public void addCourseDb() {
        Order order = new Order();
        order.setOrderNo(104L);
        order.setCount(1);
        order.setPrice(BigDecimal.TEN);
        orderMapper.insert(order);
    }

}
