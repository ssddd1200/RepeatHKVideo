package shiping.HKVRestful.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@ApiModel(value = "车辆GPS类", description = "传入车辆信息")
public class CarsGPSDTO {

    @NotNull
    @JsonProperty(value = "page")
    private int pageNo;
    @NotNull
    @JsonProperty(value = "rows")
    private int pageSize;
    @NotEmpty
    private String startTime;
    @NotEmpty
    private String endTime;
    @NotEmpty
    private String[] deviceIndexCodes;

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String[] getDeviceIndexCodes() {
        return deviceIndexCodes;
    }

    public void setDeviceIndexCodes(String[] deviceIndexCodes) {
        this.deviceIndexCodes = deviceIndexCodes;
    }
}
