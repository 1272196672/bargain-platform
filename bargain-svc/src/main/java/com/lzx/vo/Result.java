package com.lzx.vo;

import com.lzx.exception.BaseErrCode;
import com.lzx.exception.GenericException;
import com.lzx.util.MessageUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Objects;

/**
 * 响应数据
 *
 * @author Anonymous
 * @since 1.0.0
 */
@ApiModel(value = "响应")
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "编码：0表示成功，其他值表示失败")
    private int code = 0;
    @ApiModelProperty(value = "消息内容")
    private String msg = "success";
    @ApiModelProperty(value = "响应数据")
    private T data;

    public Result<T> ok(T data) {
        this.setData(data);
        return this;
    }

    public boolean success() {
        return code == 0;
    }

    public Result<T> error() {
        this.code = BaseErrCode.INTERNAL_SERVER_ERROR;
        this.msg = MessageUtils.getMessage(this.code);
        return this;
    }

    public Result<T> error(int code) {
        this.code = code;
        this.msg = MessageUtils.getMessage(this.code);
        return this;
    }

    public Result<T> error(int code, String msg) {
        this.code = code;
        this.msg = msg;
        return this;
    }

    public static Result error(int errorCode, Object data) {
        Result result = new Result();
        if (Objects.nonNull(data) && data instanceof Exception) {
            result.data = ((Exception) data).getMessage();
        } else {
            result.data = data;
        }
        result.code = errorCode;
        result.msg = MessageUtils.getMessage(errorCode);
        return result;
    }

    public Result<T> error(String msg) {
        this.code = BaseErrCode.INTERNAL_SERVER_ERROR;
        this.msg = msg;
        return this;
    }

    public static <T> Result success(T obj) {
        return success("success", obj);
    }

    public static <T> Result success(String message, T obj) {
        Result result = new Result();
        result.setData(obj);
        result.code = 0;
        result.msg = message;
        return result;
    }

    public static Result error(GenericException exception) {
        Result result = new Result();
        result.code = exception.getCode();
        result.msg = exception.getMessage();
        result.data = exception.getData();
        return result;
    }


    public static Result end(Tuple2 tuple2) {
        return tuple2.leftNotNull()
                ? Result.success(tuple2.left())
                : Result.error((GenericException) tuple2.right());
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
