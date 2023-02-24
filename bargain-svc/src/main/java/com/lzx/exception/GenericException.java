package com.lzx.exception;


import com.lzx.util.MessageUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 自定义异常
 *
 * @author Anonymous
 */
@Getter
@Setter
@NoArgsConstructor
public class GenericException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    /**
     * 错误码
     */
    private int code;
    /**
     * 错误附带数据
     */
    private Object[] data;

    /**
     * 构造方法 不允许直接使用
     *
     * @param code
     * @param msg
     * @param data
     */
    protected GenericException(int code, String msg, Object... data) {
        super(msg);
        this.code = code;
        this.data = data;
    }


    /**
     * 根据错误码获取提示
     *
     * @param code
     * @param data
     * @return
     */
    public static GenericException exception(int code, Object... data) {
        String msg = MessageUtils.getMessage(code);
        return new GenericException(code, msg, data);
    }

    /**
     * 根据错误码获取提示(错误提示之前拼接文本)
     *
     * @param code
     * @param msgParams
     * @return
     */
    private static GenericException splicingMsg(int code, String... msgParams) {
        String msg = MessageUtils.getMessage(code, msgParams);
        return new GenericException(code, msg);
    }

    /**
     * 业务错误
     *
     * @param bizCode 业务代码
     * @param msgCode 错误代码
     * @return
     */
    public static GenericException bizException(int bizCode, int msgCode) {
        String bizName = MessageUtils.getMessage(bizCode);
        return splicingMsg(msgCode, bizName);
    }

    /**
     * 查询错误
     *
     * @param bizCode
     * @return
     */
    public static GenericException queryFail(int bizCode) {
        return GenericException.bizException(bizCode, BaseErrCode.DATA_QUERY_FAIL);
    }

    /**
     * 新增错误
     *
     * @param bizCode
     * @return
     */
    public static GenericException insertFail(int bizCode) {
        return GenericException.bizException(bizCode, BaseErrCode.DATA_INSERT_FAIL);
    }

    /**
     * 修改错误
     *
     * @param bizCode
     * @return
     */
    public static GenericException updateFail(int bizCode) {
        return GenericException.bizException(bizCode, BaseErrCode.DATA_UPDATE_FAIL);
    }

    /**
     * 删除错误
     *
     * @param bizCode
     * @return
     */
    public static GenericException deleteFail(int bizCode) {
        return GenericException.bizException(bizCode, BaseErrCode.DATA_DELETE_FAIL);
    }


    @Override
    public String toString() {
        return String.format("{\"code\":%s,\"message\":\"%s\"}", code, getMessage());
    }
}