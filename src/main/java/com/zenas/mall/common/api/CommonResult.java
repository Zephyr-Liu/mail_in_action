package com.zenas.mall.common.api;

/**
 * 通用返回对象
 */
public class CommonResult<T> {
    private long code;
    private String Message;
    private T data;

    public CommonResult(long code, String message, T data) {
        this.code = code;
        Message = message;
        this.data = data;
    }
    public CommonResult(){

    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    /**
     *
     * @param data 获取的数据
     * @param <T> API信息
     * @return 操作成功后的结果
     */
   public static <T> CommonResult<T> success(T data){
       return new CommonResult<T>(ResultCode.SUCCESS.getCode(),ResultCode.SUCCESS.getMessage(),data);
   }


    /**
     *
     * @param data 获取的数据
     * @param message 提示信息
     * @param <T> API信息
     * @return 成功返回结果
     */
   public static  <T> CommonResult<T> success(T data,String message){
       return new CommonResult<T>(ResultCode.SUCCESS.getCode(), message, data);
   }

    /***
     *
     * @param errorCode 获取的数据
     * @param <T> API信息
     * @return 失败的结果
     */
   public static <T> CommonResult<T> failed(IErrorCode errorCode ){
       return new CommonResult<T>(ResultCode.FAILED.getCode(), ResultCode.FAILED.getMessage(), null);
   }

    /**
     *
     * @param message 获取的字符串信息
     * @param <T> API信息
     * @return 失败的结果
     */
   public static  <T> CommonResult<T> failed(String message){
       return new CommonResult<T>(ResultCode.FAILED.getCode(), message, null);
   }

    /**
     *
     * @param <T> 获取过来的数据
     * @return 失败返回的结果
     */
   public  static <T> CommonResult<T> failed(){
       return failed(ResultCode.FAILED);
   }

    /**
     *
     * 参数验证失败返回结果
     */
   public static <T> CommonResult<T> validatedFailed(){
       return failed(ResultCode.VALIDATION_FAILED);
   }

    /**
     *
     * @param message 提示信息
     * @return 参数验证失败返回结果
     */
   public static <T> CommonResult<T> validatedFailed(String message){
       return new CommonResult<T>(ResultCode.VALIDATION_FAILED.getCode(), message, null);
   }

    /**
     * @param data 获取的数据
     * @return 未登录的返回结果
     */
   public static <T> CommonResult<T> unauthorized(T data){
       return new CommonResult<T>(ResultCode.UNAUTHORIZED.getCode(), ResultCode.UNAUTHORIZED.getMessage(), data);
   }

    /**
     *
     * @param data 获取的数据
     * @return 未授权返回的结果
     */
   public static  <T> CommonResult<T> forbidden(T data){
       return new CommonResult<T>(ResultCode.FORBIDDEN.getCode(), ResultCode.FORBIDDEN.getMessage(), data);
   }
}
