package shiping.HKVRestful.entity;

import javax.validation.constraints.NotEmpty;

public class CarsExpressions {

    @NotEmpty
    private String key;
    @NotEmpty
    private String operator;
    @NotEmpty
    private String[] values;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String[] getValues() {
        return values;
    }

    public void setValues(String[] values) {
        this.values = values;
    }
}
