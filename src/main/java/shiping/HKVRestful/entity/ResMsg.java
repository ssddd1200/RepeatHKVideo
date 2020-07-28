package shiping.HKVRestful.entity;

public class ResMsg<T> {

    private String appcode;
    private String databuffer;
    private T resultlist;

    public ResMsg(){}

    public ResMsg(String appcode, String df){
        this.appcode = appcode;
        this.databuffer = df;
    }

    public ResMsg(String appcode, String df, T jsonObject){
        this.appcode = appcode;
        this.databuffer = df;
        this.resultlist = jsonObject;
    }

    public String getAppcode() {
        return appcode;
    }

    public void setAppcode(String appcode) {
        this.appcode = appcode;
    }

    public String getDatabuffer() {
        return databuffer;
    }

    public void setDatabuffer(String databuffer) {
        this.databuffer = databuffer;
    }

    public T getResultlist() {
        return resultlist;
    }

    public void setResultlist(T resultlist) {
        this.resultlist = resultlist;
    }
}
