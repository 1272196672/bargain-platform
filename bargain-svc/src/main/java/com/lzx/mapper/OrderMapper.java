package com.lzx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lzx.entity.Order;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 共享停车订单表 Mapper 接口
 * </p>
 *
 * @author LiJunXiao
 * @since 2022-03-08
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {
}
