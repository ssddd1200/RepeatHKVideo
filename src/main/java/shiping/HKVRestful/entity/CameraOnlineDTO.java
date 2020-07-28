package shiping.HKVRestful.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;

import javax.validation.constraints.NotNull;

@ApiModel(value = "监控点在线类", description = "监控点在线类")
public class CameraOnlineDTO {

    private String regionId;
    private String includeSubNode;
    private String[] indexCodes;
    private String status;
    @NotNull
    @JsonProperty(value = "page")
    private String pageNo;
    @NotNull
    @JsonProperty(value = "rows")
    private String pageSize;

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getIncludeSubNode() {
        return includeSubNode;
    }

    public void setIncludeSubNode(String includeSubNode) {
        this.includeSubNode = includeSubNode;
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

    public String getPageNo() {
        return pageNo;
    }

    public void setPageNo(String pageNo) {
        this.pageNo = pageNo;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }
}
