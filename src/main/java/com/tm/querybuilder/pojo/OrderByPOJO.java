package com.tm.querybuilder.pojo;

import javax.validation.constraints.NotBlank;

import com.tm.querybuilder.enums.OrderType;

public class OrderByPOJO {

	@NotBlank(message = "Enter the Order Column Name")
	private String orderColumnName;

	private OrderType orderType;

	public OrderType getOrderType() {
		return orderType;
	}

	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}

	public String getOrderColumnName() {
		return orderColumnName;
	}

	public void setOrderColumnName(String orderColumnName) {
		this.orderColumnName = orderColumnName;
	}

}
