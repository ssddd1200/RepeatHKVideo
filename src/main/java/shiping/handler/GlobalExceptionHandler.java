package shiping.handler;

import com.alibaba.fastjson.JSON;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.NoHandlerFoundException;
import shiping.HKVRestful.entity.ResMsg;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 404异常
     */
    @ExceptionHandler(value = NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResMsg errorHandler(HttpServletRequest request, NoHandlerFoundException e){
        return commonHandler(request, e.getClass().getSimpleName(), HttpStatus.NOT_FOUND.value(),e.getMessage());
    }

    /**
     * 405 异常
     */
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ResMsg errorHandler(HttpServletRequest request, HttpRequestMethodNotSupportedException e){
        return commonHandler(request, e.getClass().getSimpleName(), HttpStatus.METHOD_NOT_ALLOWED.value(), e.getMessage());
    }

    /**
     * 415 异常
     */
    @ExceptionHandler(value = HttpMediaTypeNotSupportedException.class)
    public ResMsg errorHandler(HttpServletRequest request, HttpMediaTypeNotSupportedException e){
        return commonHandler(request, e.getClass().getSimpleName(), HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), e.getMessage());
    }

    /**
     * 500 异常
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    public ResMsg errorHandler(HttpServletRequest request, Exception e){
        return commonHandler(request, e.getClass().getSimpleName(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
    }

    /**
     * 校验异常捕获
     */
    @ExceptionHandler(value = BindException.class)
    @ResponseBody
    public ResMsg validExceptionHandler(HttpServletRequest request, BindException e){
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        Map<String, String> errors = new HashMap<>();
        for(FieldError error: fieldErrors){
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return commonHandler(request, e.getClass().getSimpleName(), HttpStatus.INTERNAL_SERVER_ERROR.value(),JSON.toJSONString(errors));
    }

    private ResMsg commonHandler(HttpServletRequest request,String error, int httpCode, String message){
        ExceptionEntity entity = new ExceptionEntity();
        entity.setPath(request.getRequestURI());
        entity.setCode(httpCode);
        entity.setError(error);
        entity.setMessage(message);
        return new ResMsg("-1","失败", entity);
    }

}
