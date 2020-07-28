package shiping.HKVRestful.controller;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import shiping.HKVRestful.entity.*;
import shiping.HKVRestful.server.HKVService;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Api(value = "请求接口", tags = {"急救平台对接接口"})
@RestController
@RequestMapping(value = "/shiping")
public class HKVController {

    @Autowired
    private HKVService service;

    @ApiOperation(value = "所有监控设备分页", notes = "所有监控设备分页", httpMethod = "GET")
    @RequestMapping(value = "/cameraPage", method = RequestMethod.GET)
    @ResponseBody
    public ResMsg_grid cameraPage(
            @ApiParam(value = "页码", name = "page", required = true)
            @RequestParam(value="page") String pageNo,
            @ApiParam(value = "行数", name = "rows", required = true)
            @RequestParam(value = "rows") String pageSize){
        return service.getCamerarAll(pageNo, pageSize);
    }

    @ApiOperation(value = "视频流转化", notes="视频流转化", httpMethod = "GET")
    @RequestMapping(value = "/previewURLs",method = RequestMethod.GET)
    @ResponseBody
    public ResMsg previewURLs(
            @ApiParam(value = "监控点ID", name = "cameraID", required = true)
            @RequestParam String cameraID,
            @ApiParam(value = "码流类型", name = "streamType", required = false)
            @RequestParam(defaultValue = "0",required = false) int streamType,
            @ApiParam(value = "封装包名称", name = "transmode", required = false)
            @RequestParam(defaultValue = "1",required = false) int transmode,
            @ApiParam(value = "协议名称", name = "protocol", required = false)
            @RequestParam(defaultValue = "rtsp", required = false) String protocol){
        return service.getCameraPreviewURL(cameraID, streamType, transmode, protocol);
    }

    @ApiIgnore
    @ApiOperation(value = "视屏回放功能", notes = "视屏回放功能", httpMethod = "GET")
    @RequestMapping(value = "/getCameraPlayBack")
    @ResponseBody
    public ResMsg getCameraPlayBack(@RequestParam String cameraID, @RequestParam String startTime, @RequestParam String endTime,
                                    @RequestParam(defaultValue = "rtsp", required = false) String protocol){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        if(startTime.isEmpty() || endTime.isEmpty()){
            Date date = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_YEAR, -3);
            Date date2 = calendar.getTime();
            startTime = simpleDateFormat.format(date);
            endTime = simpleDateFormat.format(date2);
        }else{
            try {
                SimpleDateFormat s1 = new SimpleDateFormat("yyyyMMdd");
                Date date3 = s1.parse(startTime);
                Date date4 = s1.parse(endTime);
                Long defDay = (date4.getTime() - date3.getTime())/(24*60*60*1000);
                if (defDay>3){
                    return new ResMsg("-1", "时间间隔有误");
                }
                startTime = simpleDateFormat.format(date3);
                endTime = simpleDateFormat.format(date4);
            } catch (ParseException e) {
                e.printStackTrace();
                return new ResMsg("-1", "开始结束时间格式错误");
            }
        }
        return service.getCameraPlayBack(cameraID, startTime, endTime, protocol);
    }

    @ApiOperation(value = "车辆列表", notes = "车辆列表", httpMethod = "POST")
    @RequestMapping(value = "/carsList")
    @ResponseBody
    public ResMsg_grid carsList(@RequestBody @Valid CarsSearchDTO carsSearchDTO, BindingResult result){
        if (result.hasErrors()){
            Map<String, String> errors = result.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField,FieldError::getDefaultMessage,
                            (key1, key2) -> key2));
            return new ResMsg_grid("-1", "失败原因："+JSON.toJSONString(errors));
        }
        return service.encodeDeviceMss(carsSearchDTO);
    }

    @ApiOperation(value = "车辆GPS", notes = "车辆GPS", httpMethod = "POST")
    @RequestMapping(value = "/carsGPS")
    @ResponseBody
    public ResMsg_grid carsGPS(@RequestBody @Valid CarsGPSDTO carsGPSDTO, BindingResult result){
        if (result.hasErrors()){
            Map<String, String> errs = result.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage,
                            (key1, key2) -> key2));
            return new ResMsg_grid("-1","失败原因："+JSON.toJSONString(errs));
        }
        return service.carsGPS(carsGPSDTO);
    }

    @ApiOperation(value = "监控点在线状态", notes = "监控点在线状态", httpMethod = "POST")
    @RequestMapping(value = "/getCameraOnline")
    @ResponseBody
    public ResMsg_grid getCameraOnline(@RequestBody @Valid CameraOnlineDTO cameraOnlineDTO, BindingResult result){
        if (result.hasErrors()){
            Map<String, String> errors = result.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage,
                            (key1, key2) -> key2));
            return new ResMsg_grid("-1","失败原因："+JSON.toJSONString(errors));
        }
        return service.cameraOnline(cameraOnlineDTO);
    }

    @ApiOperation(value = "设备在线状态", notes = "设备在线状态", httpMethod = "POST")
    @RequestMapping(value = "/getDeviceOnline")
    @ResponseBody
    public ResMsg_grid getDeviceOnline(@RequestBody @Valid DeviceOnlineDTO deviceOnlineDTO, BindingResult result) {
        if (result.hasErrors()){
//            Map<String, String> errors = new HashMap<>();
//            for(FieldError error: result.getFieldErrors()) {
//                errors.put(error.getField(), error.getDefaultMessage());
//            }
            // LinkedHashMap::new 表示当key相同时取最新
            Map<String, String> errors = result.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage,
                    (key1,key2) -> key2, LinkedHashMap::new));
            return new ResMsg_grid("-1","失败原因："+ JSON.toJSONString(errors));
        }
        return service.deviceOnline(deviceOnlineDTO);
    }

    @ApiOperation(value = "清理所有进程", notes = "清例所有进程", httpMethod = "GET")
    @GetMapping(value = "/clear")
    public ResMsg Clear(){
        service.clear();
        return new ResMsg("0","success");
    }

    @GetMapping(value = "/test")
    public void test(){
        service.test();
    }
}
