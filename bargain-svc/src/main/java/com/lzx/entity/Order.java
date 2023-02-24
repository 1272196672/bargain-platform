package com.lzx.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 共享停车订单表
 * </p>
 *
 * @author LiJunXiao
 * @since 2022-03-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("order")
@Accessors(chain = true)
@ApiModel(value = "payOrder对象", description = "")
public class Order extends Model<Order> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     * 订单状态:0.用户取消,1.已预约,2.进行中,3.已结束,4.系统关闭
     */
    @ApiModelProperty(value = "订单状态:0.用户取消,1.已预约,2.进行中,3.已结束,4.系统关闭")
    private Integer orderStatus;

    /**
     * 支付状态:0.未支付,1.已支付
     */
    @ApiModelProperty(value = "支付状态:0.未支付,1.已支付")
    private Integer orderPayStatus;

    /**
     * 订单超时时间
     */
    @ApiModelProperty(value = "订单超时时长(分钟)")
    private Integer orderOutTime;

    /**
     * 订单创建的时间
     */
    @ApiModelProperty(value = "订单创建时间")
    private LocalDateTime orderCreateTime;

    /**
     * 订单编号
     */
    @ApiModelProperty(value = "订单编号")
    private String orderNum;

    /**
     * 订单支付时间
     */
    @ApiModelProperty(value = "订单支付时间")
    private LocalDateTime orderPayTime;

    /**
     * 订单实收金额
     */
    @ApiModelProperty(value = "订单实收金额")
    private BigDecimal orderRealMoney;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
