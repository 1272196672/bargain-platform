package com.lzx.exception;

/**
 * 错误编码，由5位数字组成，前2位为模块编码，后3位为业务编码
 * 如：10001（10代表系统模块，001代表业务代码）
 *
 * @author Anonymous
 * @since 1.0.0
 */
public interface BaseErrCode {
    int INTERNAL_SERVER_ERROR = 500;

    //系统级错误

    int JSON_ERROR = 1000;
    int BROWSER_CONNECTION_CLOSED = 1001;
    int BAD_SQL_GRAMMAR = 1002;
    int CHECK_PARAMS_FAIL = 1003;
    int PARAMS_TYPE_MUST_BE_JSON = 1004;
    int REQUEST_TYPE_NO_SUPPORT = 1005;
    int NULL_EXCEPTION = 1006;
    int DYNAMIC_PROXY_ERROR = 1007;
    int PARAMS_MISSING_ERROR = 1009;
    int DB_RECORD_EXISTS = 1010;

    //业务错误

    int BUSINESS_EXECUTION_FAILURE = 2000;
    int DATA_QUERY_FAIL = 2001;
    int DATA_INSERT_FAIL = 2002;
    int DATA_UPDATE_FAIL = 2003;
    int DATA_DELETE_FAIL = 2004;
    int DATA_NOT_EXISTS = 2005;
    int DATA_IS_EXISTS = 2006;
    int DATA_SAVE_BATCH_FAIL = 2007;

    //用户身份

    int ACCOUNT_NOT_EXIST = 2100;
    int TOKEN_NOT_EMPTY = 2101;
    int TOKEN_INVALID = 2102;

    //文件

    int DOWNLOAD_FILE_NOT_EXIST = 2200;
    int FILE_DOWNLOAD_FAIL = 2201;
    int ATTACHMENT_IS_EMPTY = 2202;
    int BUSINESS_SOURCE_NOT_EXIST = 2203;
    int ATTACHMENT_UPLOAD_FAIL = 2204;
    int ATTACHMENT_DELETE_PARAMS_MISSING = 2205;
    int DOCUMENT_IS_EMPTY = 2206;
    int DOCUMENT_FORMAT_IS_WRONG = 2207;
    int DOCUMENT_READ_FAIL = 2208;
    int DOCUMENT_EXPORT_FAIL = 2209;
    int DOCUMENT_OPERATION_FAIL = 2210;


    //业务名称

    int ATTACHMENT_CODE = 9000;
    int USER_CODE = 9001;
    int NAME_EXISTS = 9003;
    int DATA_EXISTS = 9004;
}
