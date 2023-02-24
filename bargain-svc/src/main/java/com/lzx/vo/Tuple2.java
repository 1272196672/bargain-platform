package com.lzx.vo;

import com.lzx.exception.GenericException;


/**
 * tuple2
 *
 * @author Bobby.zx.lin
 * @date 2023/02/24
 */
public class Tuple2<A, B> {

    private final A success;
    private final B fail;

    public Tuple2(A _1, B _2) {
        this.success = _1;
        this.fail = _2;
    }

    public Boolean leftIsNull() {
        return success == null;
    }

    public Boolean rightIsNull() {
        return fail == null;
    }

    public Boolean leftNotNull() {
        return success != null;
    }

    public Boolean rightNotNull() {
        return fail != null;
    }

    public A left() {
        return success;
    }

    public B right() {
        return fail;
    }


    public static <A, B> Tuple2<A, B> left(A a) {
        return new Tuple2(a, null);
    }

    public static <A, B> Tuple2<A, B> right(B b) {
        return new Tuple2(null, b);
    }

    /**
     * 根据状态码返回具体错误
     *
     * @param b
     * @param <A>
     * @param <B>
     * @return
     */
    public static <A, B> Tuple2<A, B> errorCode(Integer b) {
        return new Tuple2(null, GenericException.exception(b));
    }

    /**
     * java8 以上版本中缺少get会导致返回结果报错
     *
     * @return
     */
    public A getSuccess() {
        return success;
    }

    public B getFail() {
        return fail;
    }

    @Override
    public String toString() {
        return "Tuple2(_1: " + this.success + ", _2: " + this.fail + ")";
    }

}
