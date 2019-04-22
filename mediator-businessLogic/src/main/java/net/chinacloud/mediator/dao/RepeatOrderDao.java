package net.chinacloud.mediator.dao;

import net.chinacloud.mediator.domain.RepeatOrder;




//@Repository
public class RepeatOrderDao extends DAO {
	public void saveRepeatOrder(RepeatOrder repeatOrder) {
		final String sql = "INSERT INTO REPEATORDER (TID, APPLICATION_ID, STARTTIME, ORDERTYPE) VALUES (?, ?, ?, ?)";
		super.update(sql, 
				repeatOrder.getTid(),repeatOrder.getApplicationId(),repeatOrder.getStarttime(),repeatOrder.getType());
	}
}
