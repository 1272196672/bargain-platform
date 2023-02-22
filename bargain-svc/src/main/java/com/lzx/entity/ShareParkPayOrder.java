package com.lzx.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author 林子翔
 * @since 2022/10/09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("share_park_pay_order")
@ApiModel(value = "payOrder对象", description = "")
public class ShareParkPayOrder extends Model<ShareParkPayOrder> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "款单id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty(value = "订单编号")
    private String orderOriginNum;

    @ApiModelProperty(value = "映射订单编号")
    private String orderNum;

    @ApiModelProperty(value = "0.未支付,1.已支付,2.超时已关闭,3.用户已取消,4.退款中,5.退款成功,6.退款异常")
    private Integer orderPayStatus;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
