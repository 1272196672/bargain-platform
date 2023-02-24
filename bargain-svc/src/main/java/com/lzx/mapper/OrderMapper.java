package com.lzx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lzx.entity.Order;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单映射器
 *
 * @author Bobby.zx.lin
 * @date 2023/02/24
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {
}
