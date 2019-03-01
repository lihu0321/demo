package com.example.demo.framework.common.exception.handler;

import com.google.common.collect.Maps;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ClassUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.Map;

@RestControllerAdvice
public class SystemExceptionAdviceHalder {

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<?> excptionHalder(HttpServletRequest request, Throwable ex){
        Map<String, String> errorMsg = Maps.newHashMap();
        errorMsg.put("title", "系统异常");
        errorMsg.put("exception", ClassUtils.getShortName(ex.getClass()));
        errorMsg.put("message", ex.getMessage());
        return new ResponseEntity<>(errorMsg, getStatus(request));
    }

    //类似于需要long类型传过来的是string,仅限于方法中的参数例如method(long id)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Map<String, Object> handleBindException(HttpServletRequest request, MethodArgumentTypeMismatchException methodArgumentTypeMismatchException) throws IOException {
        return errorProcess(request, "请求参数类型错误",methodArgumentTypeMismatchException);
    }

    //数据校验异常，例如长度@Length、不为空的校验NotBlank 限于这种public void insert(@RequestBody @Valid UserPost userPost) 限制朱姐加在userpost中
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, Object> handleBindException(HttpServletRequest request, MethodArgumentNotValidException bindException) throws IOException {
        return errorProcess(request, "请求参数格式错误", buildObjectBindException(bindException.getBindingResult()));
    }

    //数据校验异常，仅限于对象的属性中使用注解 public void insert(@Valid UserPost userPost)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public Map<String, Object> handleBindException(HttpServletRequest request, BindException bindException) throws IOException {
        return errorProcess(request, "请求参数格式错误", buildObjectBindException(bindException.getBindingResult()));
    }

    //数据校验异常，仅限于在参数上使用 public void addEmaill(@Valid @Email String email)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public Map<String, Object> handleBindException(HttpServletRequest request, ConstraintViolationException bindException) throws IOException {
        return errorProcess(request, "请求参数格式错误", new InvalidParameterException(
                bindException.getConstraintViolations()
                        .stream()
                        .map(constraintViolation -> "输入的值:" + constraintViolation.getInvalidValue() + ",提示:" + constraintViolation.getMessage() + ";")
                        .reduce(String::concat)
                        .orElse("")
        ));
    }




    private Exception buildObjectBindException(BindingResult bindingResult) {
        return new InvalidParameterException(
                bindingResult.getFieldErrors()
                        .stream()
                        .map(fieldError -> fieldError.getObjectName() + "." + fieldError.getField() + ":" + fieldError.getDefaultMessage() + ";")
                        .reduce(String::concat)
                        .orElse("")
        );
    }


    private Map<String, Object> errorProcess(HttpServletRequest request, String exceptionSubject, Throwable ex) throws IOException {
        Map<String, Object> errorMsg = Maps.newHashMap();
        errorMsg.put("title", exceptionSubject);
        errorMsg.put("exceptionType", ClassUtils.getShortName(ex.getClass()));
        errorMsg.put("message", ex.getMessage());
        return errorMsg;
    }

    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(statusCode);
    }
}
