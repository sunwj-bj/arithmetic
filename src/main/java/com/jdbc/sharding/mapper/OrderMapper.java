package com.jdbc.sharding.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.jdbc.sharding.bean.Order;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderMapper extends BaseMapper<Order> {

}
