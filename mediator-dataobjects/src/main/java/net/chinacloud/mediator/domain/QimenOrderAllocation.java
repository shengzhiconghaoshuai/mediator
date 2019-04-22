package net.chinacloud.mediator.domain;

import java.util.List;

/**
 * Created by Octopus8 on 2017/5/18.
 */
public class QimenOrderAllocation {

    /** 网店单号 **/
    String channelOrderId;

    /** 网店子订单号 **/
    List<String> channelOrderitemIds;

    /** 网店编号 **/
    String storeId;

    /** 门店编号 **/
    String shopId;

    /** 业务类型
     *  枚举值: FAHUO, ZITI
     */
    String type;

    /** 订单状态
     * 通知门店配货 X_ALLOCATION_NOTIFIED
     * 门店已接单 X_SHOP_ALLOCATION
     */
    String status;

    /** 操作人 **/
    String operator;

    /** 时间发生时间
     * format: yyyy-MM-dd HH:mm:ss
     */
    String actionTime;

    public String getChannelOrderId() {
        return channelOrderId;
    }

    public void setChannelOrderId(String channelOrderId) {
        this.channelOrderId = channelOrderId;
    }

    public List<String> getChannelOrderitemIds() {
        return channelOrderitemIds;
    }

    public void setChannelOrderitemIds(List<String> channelOrderitemIds) {
        this.channelOrderitemIds = channelOrderitemIds;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getActionTime() {
        return actionTime;
    }

    public void setActionTime(String actionTime) {
        this.actionTime = actionTime;
    }
}
