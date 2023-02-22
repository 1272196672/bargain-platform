package com.lzx.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author LiJunXiao
 * @since 2022-03-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ShareParkOrderInfo extends Model<ShareParkOrderInfo> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     * 下单用户
     */
    @ApiModelProperty(value = "下单用户")
    private Long orderInfoUserId;

    /**
     * 订单车牌号
     */
    @ApiModelProperty(value = "订单车牌号")
    private String orderCarNum;

    /**
     * 订单车位号
     */
    @ApiModelProperty(value = "订单车位号")
    private String orderPortNum;

    /**
     * 订单所在场所
     */
    @ApiModelProperty(value = "订单所在场所")
    private Long orderPlaceId;

    /**
     * 订单id
     */
    @ApiModelProperty(value = "订单id")
    private Long orderId;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
