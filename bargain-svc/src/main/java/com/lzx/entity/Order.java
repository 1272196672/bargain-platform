package com.lzx.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
@ApiModel(value = "Order对象", description = "")
public class Order extends Model<Order> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 订单编号
     */
    @ApiModelProperty(value = "订单编号")
    private String orderNum;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "产品id")
    private String productId;

    @ApiModelProperty(value = "订单金额")
    private BigDecimal realMoney;

    @ApiModelProperty(value = "二维码地址")
    private String codeUrl;

    /**
     * 订单状态:0.未支付,1.支付成功,2.超时已关闭,3.用户已取消,4.退款中,5.退款成功,6.退款异常
     */
    @ApiModelProperty(value = "订单状态:0.未支付,1.支付成功,2.超时已关闭,3.用户已取消,4.退款中,5.退款成功,6.退款异常")
    private Integer orderStatus;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
