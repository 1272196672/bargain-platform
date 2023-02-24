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
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author 林子翔
 * @since 2022-09-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("refund_info")
@ApiModel(value = "RefundInfo对象", description = "")
public class RefundInfo extends Model<RefundInfo> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "款单id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty(value = "商户订单编号")
    private String orderNo;

    @ApiModelProperty(value = "商户退款单编号")
    private String refundNo;

    @ApiModelProperty(value = "支付系统退款单号")
    private String refundId;

    @ApiModelProperty(value = "原订单金额(分)")
    private BigDecimal totalFee;

    @ApiModelProperty(value = "退款金额(分)")
    private BigDecimal refund;

    @ApiModelProperty(value = "退款原因")
    private String reason;

    @ApiModelProperty(value = "退款状态")
    private String refundStatus;

    @ApiModelProperty(value = "申请退款返回参数")
    private String contentReturn;

    @ApiModelProperty(value = "退款结果通知参数")
    private String contentNotify;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
