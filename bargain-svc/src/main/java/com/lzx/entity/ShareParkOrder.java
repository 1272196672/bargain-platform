package com.lzx.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
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
@Accessors(chain = true)
public class ShareParkOrder extends Model<ShareParkOrder> {

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
     * 1.产权车位,2.公共车位
     */
    @ApiModelProperty(value = "车位来源:1.产权车位,2.公共车位")
    private Integer orderResource;

    /**
     * 订单预定时间(产权车位才有)
     */
    @ApiModelProperty(value = "订单预定时间(产权车位才有)")
    private LocalDateTime orderReserveTime;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    private LocalDateTime orderStartTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private LocalDateTime orderEndTime;

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
     * 订单流水号
     */
    @ApiModelProperty(value = "订单流水号")
    private String orderStreamCode;

    /**
     * 订单应收金额
     */
    @ApiModelProperty(value = "订单应收金额")
    private BigDecimal orderSystemMoney;

    /**
     * 订单实收金额
     */
    @ApiModelProperty(value = "订单实收金额")
    private BigDecimal orderRealMoney;

    /**
     * 订单减免金额
     */
    @ApiModelProperty(value = "订单减免金额")
    private BigDecimal orderReduceMoney;

    /**
     * 订单减免原因
     */
    @ApiModelProperty(value = "订单减免原因")
    private Integer orderReduceType;

    /**
     * 订单超时金额
     */
    @ApiModelProperty(value = "订单超时金额")
    private BigDecimal orderOutTimeMoney;
    /**
     * 分账状态：0未生成账单，1：已生成账单，2：已分账
     */
    @ApiModelProperty(value = "分账状态：0未生成账单，1：已生成账单，2：已分账")
    private Integer orderBillStatus;

    @ApiModelProperty(value = "是否超时")
    private Boolean orderOutStatus;

    @ApiModelProperty(value = "车辆离开时间")
    private LocalDateTime orderLeaveTime;

    @ApiModelProperty(value = "用户每笔订单对应的收益")
    private BigDecimal userProceedMoney;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
