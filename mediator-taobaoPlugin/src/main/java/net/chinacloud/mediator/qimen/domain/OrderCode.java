package net.chinacloud.mediator.qimen.domain;

import java.io.Serializable;

/**
 * Created by Octopus8 on 2017/5/16.
 */
public class OrderCode implements Serializable{
    private static final long serialVersionUID = 1L;

    private String orderCode;

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }
}
