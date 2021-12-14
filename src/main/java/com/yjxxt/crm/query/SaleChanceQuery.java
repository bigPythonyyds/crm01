package com.yjxxt.crm.query;

import com.yjxxt.crm.base.BaseQuery;

public class SaleChanceQuery extends BaseQuery {
//    客户名称
    private String customerName;
    private String createMan;
    private String state;
    private String AggsinMan;

    public SaleChanceQuery() {
    }

    public String getAggsinMan() {
        return AggsinMan;
    }

    public void setAggsinMan(String aggsinMan) {
        AggsinMan = aggsinMan;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCreateMan() {
        return createMan;
    }

    public void setCreateMan(String createMan) {
        this.createMan = createMan;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

}
