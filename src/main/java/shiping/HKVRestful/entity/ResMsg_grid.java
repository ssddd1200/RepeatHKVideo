package shiping.HKVRestful.entity;

import com.alibaba.fastjson.JSONObject;

public class ResMsg_grid<T> extends ResMsg {

    public ResMsg_grid(){
        super();
    }

    public ResMsg_grid(String code, String msg){
        super(code, msg);
    }

    public ResMsg_grid(String appcode, String df, JSONObject jsonObject) {
        super(appcode, df);
        initJson(jsonObject);
    }

    private void initJson(JSONObject jsonObject){
        JSONObject object = new JSONObject();
        object.put("page", jsonObject.getIntValue("pageNo"));
        object.put("total", Math.ceil(jsonObject.getFloatValue("total")/jsonObject.getFloat("pageSize")));
        object.put("records", jsonObject.getFloatValue("total"));
        object.put("data", jsonObject.getJSONArray("list"));
        this.setResultlist(object);
    }
}
