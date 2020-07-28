package shiping.HKVRestful.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

@ApiModel(value = "设备在线类", description = "设备在线类")
public class DeviceOnlineDTO {

    private String regionId;
    private String ip;
    private String[] indexCodes;
    private String status;
    @NotNull
    @JsonProperty(value = "page")
    private Number pageNo;
    @NotNull
    @JsonProperty(value = "rows")
    private Number pageSize;
    private String includeSubNode;

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String[] getIndexCodes() {
        return indexCodes;
    }

    public void setIndexCodes(String[] indexCodes) {
        this.indexCodes = indexCodes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Number getPageNo() {
        return pageNo;
    }

    public void setPageNo(Number pageNo) {
        this.pageNo = pageNo;
    }

    public Number getPageSize() {
        return pageSize;
    }

    public void setPageSize(Number pageSize) {
        this.pageSize = pageSize;
    }

    public String getIncludeSubNode() {
        return includeSubNode;
    }

    public void setIncludeSubNode(String includeSubNode) {
        this.includeSubNode = includeSubNode;
    }
}
