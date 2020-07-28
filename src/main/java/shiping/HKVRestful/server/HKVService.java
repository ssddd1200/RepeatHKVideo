package shiping.HKVRestful.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hikvision.artemis.sdk.ArtemisHttpUtil;
import com.hikvision.artemis.sdk.config.ArtemisConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shiping.HKVRestful.entity.*;
import shiping.ffmpeg.FFmpegServer;
import shiping.property.AtremisProperty;
import shiping.property.FFmpegProperty;

import java.util.HashMap;
import java.util.Map;

@Service
public class HKVService {

    @Autowired
    private AtremisProperty atremisProperty;

    @Autowired
    private FFmpegServer server;

    @Autowired
    private FFmpegProperty config;

    /**
     * 海康威视请求方法
     * @param reqURL API请求路径
     * @param jsonBody 请求实体
     * @return
     */
    private String HKVRequest(String reqURL, JSONObject jsonBody){
        /**
         * STEP1：设置平台参数，根据实际情况,设置host appkey appsecret 三个参数.
         */

        ArtemisConfig.host = atremisProperty.getHost();
        ArtemisConfig.appKey = atremisProperty.getAppKey();
        ArtemisConfig.appSecret = atremisProperty.getAppSecret();

        /**
         * STEP2：设置OpenAPI接口的上下文 封装在atremisProperty的path中
         * STEP3：设置接口的URI地址
         */
        final String apiURLs = atremisProperty.getPath() + reqURL;
        Map<String, String> path = new HashMap<String, String>(2){
            {
                put("https://", apiURLs);
            }
        };

        /**
         * STEP4：设置参数提交方式
         */
        String contentType = "application/json";

        /**
         * STEP5：组装请求参数
         */
        String body = jsonBody.toJSONString();

        /**
         * STEP6：调用接口
         */
        String result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, contentType , null);// post请求application/json类型参数
        return result;
    }

    private void videoReset(String procotol, String videoURL, String cameraID){
        String[] cmd = null;
        if("rtsp".equals(procotol)){
            // rtsp -> rtmp
            cmd = new String[]{config.getPath(), "-rtsp_transport", "tcp", "-i", videoURL, "-f", "flv", "-r", "25", "-s", "640*480", "-an", config.getUrl() + cameraID};
//            server.start(cameraID, cmd);
        }else if ("rtmp".equals(procotol)){
            cmd = new String[]{config.getPath(),"-i",videoURL,"-c:a","copy","-c:v","libx264","-vpre","slow","-f","flv",config.getUrl()+cameraID};

        }
        server.start(cameraID, cmd);
    }

    /**
     * 分页获取监控点资源
     * @return
     */
    public ResMsg_grid getCamerarAll(String pageNo, String pageSize){
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("pageNo", pageNo);
        jsonBody.put("pageSize", pageSize);
        String resStr = HKVRequest("/api/resource/v1/cameras", jsonBody);
        JSONObject resBody = JSONObject.parseObject(resStr);
        return new ResMsg_grid(resBody.getString("code"), resBody.getString("msg"), resBody.getJSONObject("data"));
    }

    /**
     * 视频输出格式转化
     * @param cameraIndexCode
     * @return
     */
    public ResMsg getCameraPreviewURL(String cameraIndexCode, int streamType, int transmode, String protocol){
//        server.stopAll();

        ResMsg returnBody = new ResMsg();
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("cameraIndexCode", cameraIndexCode);
        jsonBody.put("streamType", streamType);
        jsonBody.put("protocol", protocol);
        jsonBody.put("transmode", transmode);
        jsonBody.put("expand", "streamform=rtp");
        String resBody = HKVRequest("/api/video/v1/cameras/previewURLs",jsonBody);
        String code = JSONObject.parseObject(resBody).getString("code");
        returnBody.setAppcode(code);
        returnBody.setDatabuffer(JSONObject.parseObject(resBody).getString("msg"));
        if ("0".equals(code)){
            JSONObject resJson = JSONObject.parseObject(resBody).getJSONObject("data");
            String rtspURL = resJson.get("url").toString();
            videoReset(protocol, rtspURL, cameraIndexCode);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("url",config.getRealpath()+cameraIndexCode);
            jsonObject.put("rtspurl",rtspURL);
            returnBody.setResultlist(jsonObject);
        }
        return returnBody;
    }

//    /**
//     * 视频输出格式转化 rtmp->rtmp
//     * @param cameraIndexCode
//     * @return
//     */
//    public String getCameraPreviewURL2(String cameraIndexCode){
//        server.stopAll();
//        JSONObject jsonBody = new JSONObject();
//        jsonBody.put("cameraIndexCode", cameraIndexCode);
//        jsonBody.put("streamType", 0);
//        jsonBody.put("protocol", "rtmp");
//        jsonBody.put("transmode", 1);
//        jsonBody.put("expand", "streamform=rtp");
//        String resBody = HKVRequest("/api/video/v1/cameras/previewURLs",jsonBody);
//        String code = JSONObject.parseObject(resBody).getString("code");
//        SuccessDTO successDTO = new SuccessDTO();
//        if("0".equals(code)){
//            JSONObject resJson = JSONObject.parseObject(resBody).getJSONObject("data");
//            String rtmpURL = resJson.get("url").toString();
//            videoReset("rtmp", rtmpURL, cameraIndexCode);
//            successDTO.setCode("0");
//            successDTO.setMsg("success");
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("url",config.getRealpath()+cameraIndexCode);
//            jsonObject.put("url2",rtmpURL);
//            successDTO.setData(jsonObject);
//        }else{
//            successDTO.setCode(code);
//            successDTO.setMsg(JSONObject.parseObject(resBody).getString("msg"));
//        }
//        return JSONObject.toJSONString(successDTO);
//    }

    /**
     * 监控点回放取URL
     */
    public ResMsg getCameraPlayBack(String cameraIndexCode, String startTime, String endTime, String protocol){
//        server.stopAll();
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("cameraIndexCode", cameraIndexCode);
        jsonBody.put("protocol", protocol);
        jsonBody.put("beginTime", startTime);
        jsonBody.put("endTime", endTime);
        jsonBody.put("expand", "streamform=rtp");
        String resBody = HKVRequest("/api/video/v1/cameras/playbackURLs",jsonBody);
        String code = JSONObject.parseObject(resBody).getString("code");
        ResMsg returnBody = new ResMsg();
        returnBody.setAppcode(code);
        returnBody.setDatabuffer(JSONObject.parseObject(resBody).getString("msg"));
        if("0".equals(code)){
            JSONObject resJson = JSONObject.parseObject(resBody).getJSONObject("data");
            String rtspURL = resJson.get("url").toString();
            videoReset(protocol, rtspURL, cameraIndexCode);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("url",config.getRealpath()+cameraIndexCode);
            jsonObject.put("url2",rtspURL);
            returnBody.setResultlist(jsonObject);
        }
        return returnBody;
    }

//    /**
//     * 监控点回放取URL rtmp->rtmp
//     */
//    public String getCameraPlayBack2(String cameraIndexCode, String startTime, String endTime){
//        server.stopAll();
//        JSONObject jsonBody = new JSONObject();
//        jsonBody.put("cameraIndexCode", cameraIndexCode);
//        jsonBody.put("protocol", "rtmp");
//        jsonBody.put("beginTime", startTime);
//        jsonBody.put("endTime", endTime);
//        jsonBody.put("expand", "streamform=rtp");
//        String resBody = HKVRequest("/api/video/v1/cameras/playbackURLs",jsonBody);
//        String code = JSONObject.parseObject(resBody).getString("code");
//        SuccessDTO successDTO = new SuccessDTO();
//        if("0".equals(code)){
//            JSONObject resJson = JSONObject.parseObject(resBody).getJSONObject("data");
//            String rtmpURL = resJson.get("url").toString();
//            videoReset("rtmp", rtmpURL, cameraIndexCode);
//            successDTO.setCode("0");
//            successDTO.setMsg("success");
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("url",config.getRealpath()+cameraIndexCode);
//            jsonObject.put("url2",rtmpURL);
//            successDTO.setData(jsonObject);
//        }else{
//            successDTO.setCode(code);
//            successDTO.setMsg(JSONObject.parseObject(resBody).getString("msg"));
//        }
//        return JSONObject.toJSONString(successDTO);
//    }

    /**
     * 查询车载设备列表
     */
    public ResMsg_grid encodeDeviceMss(CarsSearchDTO carsSearchDTO){
        JSONObject jsonBody = (JSONObject) JSON.toJSON(carsSearchDTO);
        String resStr = HKVRequest("/api/resource/v1/encodeDeviceMss/search",jsonBody);
        JSONObject resBody = JSONObject.parseObject(resStr);
        return new ResMsg_grid(resBody.getString("code"), resBody.getString("msg"), resBody.getJSONObject("data"));
    }

    /**
     * 车辆GPS轨迹线
     */
    public ResMsg_grid carsGPS(CarsGPSDTO carsGPSDTO){
        JSONObject jsonBody = (JSONObject) JSON.toJSON(carsGPSDTO);
        String resStr = HKVRequest("/api/mss/v1/gps/track/search",jsonBody);
        JSONObject resBody = JSONObject.parseObject(resStr);
        return new ResMsg_grid(resBody.getString("code"), resBody.getString("msg"), resBody.getJSONObject("data"));
    }

    /**
     * 监控点在线状态
     */
    public ResMsg_grid cameraOnline(CameraOnlineDTO cameraOnlineDTO){

        JSONObject jsonBody = (JSONObject) JSON.toJSON(cameraOnlineDTO);
        String resStr = HKVRequest("/api/nms/v1/online/camera/get", jsonBody);
        JSONObject resBody = JSONObject.parseObject(resStr);
        return new ResMsg_grid(resBody.getString("code"), resBody.getString("msg"), resBody.getJSONObject("data"));
    }

    /**
     * 编码设备在线状态
     */
    public ResMsg_grid deviceOnline(DeviceOnlineDTO deviceOnlineDTO){

        JSONObject jsonBody = (JSONObject) JSON.toJSON(deviceOnlineDTO);
        String resStr = HKVRequest("/api/nms/v1/online/encode_device/get", jsonBody);
        JSONObject resBody = JSONObject.parseObject(resStr);
        return new ResMsg_grid(resBody.getString("code"), resBody.getString("msg"),resBody.getJSONObject("data"));
    }

    public void clear(){
        server.stopAll();
    }

    public void test(){
        String in = "rtmp://58.200.131.2:1935/livetv/hunantv";
        String out = "rtmp://localhost:1936/live/hunantv";
//        String[] cmd1 = new String[] {"ffmpeg", "-re", "-i", in, "-acodec", "aac", "-ar", "44100", "-vcodec", "copy", "-report" , "-y", "-f", "flv", out};
        String[] cmd1 = new String[] {"ffplay",  in};
        server.start("1", cmd1);
    }
}
