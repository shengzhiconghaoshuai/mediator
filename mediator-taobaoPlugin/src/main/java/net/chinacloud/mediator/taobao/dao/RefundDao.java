/**
 * 
 */
package net.chinacloud.mediator.taobao.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import net.chinacloud.mediator.dao.DAO;

/**
 * @author 黄锡涛
 * @description
 * @since 2016年3月14日 下午2:41:28
 */
public class RefundDao extends DAO {

	 /**
     * 查询refund
     * @param startModified
     * @param endModified
     * @param offset
     * @param rows
     * @return
     */
    public List<Long> getRefundIdList(Date startModified, Date endModified , String name , String status) {
        final String sql = "SELECT REFUND_ID  FROM JDP_TB_REFUND WHERE JDP_MODIFIED >= ? AND JDP_MODIFIED < ? AND SELLER_NICK = ? AND STATUS = ?";
        return super.queryForList(sql, new RowMapper<Long>() {
            @Override
            public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getLong("REFUND_ID");
            }
        }, startModified, endModified, name, status);
    }
}
