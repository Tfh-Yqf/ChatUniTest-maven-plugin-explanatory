package zju.cst.aces.utils;

import lombok.Data;

@Data
public class MethodTokenCostInfo {

    public int FirstAskCost;

    public int FirstResponseCost;

    public int FixedAskCost;
    public int FixedResponseCost;

    public String methodName;
    public String className;

}
