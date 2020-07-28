package shiping.HKVRestful.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;

import javax.validation.constraints.NotNull;

@ApiModel(value = "车辆查询类", description = "车辆查询类")
public class CarsSearchDTO {

    private String name;
    private String[] regionIndexCodes;
    private boolean isSubregion;
    @NotNull
    @JsonProperty(value = "page")
    private int pageNo;
    @NotNull
    @JsonProperty(value = "rows")
    private int pageSize;
    private CarsExpressions[] expressions;
    private String orderBY;
    private String orderType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getRegionIndexCodes() {
        return regionIndexCodes;
    }

    public void setRegionIndexCodes(String[] regionIndexCodes) {
        this.regionIndexCodes = regionIndexCodes;
    }

    public boolean isSubregion() {
        return isSubregion;
    }

    public void setSubregion(boolean subregion) {
        isSubregion = subregion;
    }

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

    public CarsExpressions[] getExpressions() {
        return expressions;
    }

    public void setExpressions(CarsExpressions[] expressions) {
        this.expressions = expressions;
    }

    public String getOrderBY() {
        return orderBY;
    }

    public void setOrderBY(String orderBY) {
        this.orderBY = orderBY;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }
}
