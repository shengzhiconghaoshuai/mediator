/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：ChannelAttributeMappingDao.java
 * 描述： 
 */
package net.chinacloud.mediator.taobao.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import net.chinacloud.mediator.dao.DAO;
import net.chinacloud.mediator.domain.ChannelPictureMapping;
import net.chinacloud.mediator.domain.ProductPartnumberMapping;

import org.springframework.jdbc.core.RowMapper;

/**
 * @description 渠道属性映射DAO
 * @author yejunwu123@gmail.com
 * @since 2015年6月28日 上午11:02:43
 */
//@Repository("taobaoChannelAttributeMappingDao")
public class ChannelPictureMappingDao extends DAO {
	/**
	 * 根据itemid查询图片
	 * @param channelCategoryId 渠道类目id
	 * @param channelAttributeId 渠道属性ID
	 * @param applicationId 应用id
	 * @return
	 */
	public List<ChannelPictureMapping> getPictureList(String productId, Integer applicationId,Boolean uploadSucess) {
		String sql = "SELECT ID, CHANNEL_PRODUCT_ID, IMAGE_ID, SORT, APPLICATION_ID FROM PRODUCT_IMAGE WHERE  CHANNEL_PRODUCT_ID = ? AND APPLICATION_ID = ? ORDER BY ID DESC";
		return super.queryForList(sql, new RowMapper<ChannelPictureMapping>() {
			@Override
			public ChannelPictureMapping mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				if (null != rs) {
					return extractChannelAttributeMapping(rs);
				}
				return null;
			}
		}, productId, applicationId);
	}
	/**
	 * 将上传的图片ID插入本地库
	 */
	public int insertPicture(ChannelPictureMapping cMapping) throws SQLException{
		String sql="INSERT INTO PRODUCT_IMAGE (CHANNEL_PRODUCT_ID,IMAGE_ID,SORT,APPLICATION_ID) VALUES (?,?,?,?)";
		return super.update(sql, cMapping.getChannelProductId(),cMapping.getImageId(),cMapping.getSort(),cMapping.getApplicationId());
	}
	/**
	 *更新本地库信息
	 */
	public int updatePicture(ChannelPictureMapping cMapping) throws SQLException{
		String sql="UPDATE PRODUCT_IMAGE SET IMAGE_ID=? WHERE CHANNEL_PRODUCT_ID=? AND SORT=?";
		return super.update(sql, cMapping.getImageId(),cMapping.getChannelProductId(),cMapping.getSort());
	}
	/**
	 * @param rs
	 * @return
	 * @throws SQLException 
	 */
	private ChannelPictureMapping extractChannelAttributeMapping(
			ResultSet rs) throws SQLException {
		ChannelPictureMapping mapping = new ChannelPictureMapping();
		mapping.setId(rs.getInt("ID"));
		mapping.setChannelProductId(rs.getString("CHANNEL_PRODUCT_ID"));
		mapping.setImageId(rs.getString("IMAGE_ID"));
		mapping.setApplicationId(rs.getInt("APPLICATION_ID"));
		mapping.setSort(rs.getInt("SORT"));
		return mapping;
	}
	
	
	public List<ProductPartnumberMapping> getProductPartnumberMapping() {
		String sql = "SELECT MAPPING_ID, CHANNEL_PRODUCT_ID, OUTER_ID, TYPE, APPLICATION_ID FROM PRODUCT_PARTNUMBER_MAPPING";
		return super.queryForList(sql, new RowMapper<ProductPartnumberMapping>() {
			@Override
			public ProductPartnumberMapping mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				if (null != rs) {
					return extractProductPartnumberMapping(rs);
				}
				return null;
			}
		});
	}
	
	public List<ProductPartnumberMapping> getList(String outid) {
		String sql = "SELECT MAPPING_ID, CHANNEL_PRODUCT_ID, OUTER_ID, TYPE, APPLICATION_ID FROM PRODUCT_PARTNUMBER_MAPPING WHERE OUTER_ID = ?";
		return super.queryForList(sql, new RowMapper<ProductPartnumberMapping>() {
			@Override
			public ProductPartnumberMapping mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				if (null != rs) {
					return extractProductPartnumberMapping(rs);
				}
				return null;
			}
		},outid);
	}
	public int saveProductMapping(
			ProductPartnumberMapping product) {
		final String sql = "INSERT INTO product_partnumber_mapping (CHANNEL_PRODUCT_ID, OUTER_ID, TYPE,  APPLICATION_ID) "
						+ "VALUES (?, ?, ? ,?)";
		return super.update(sql, 
				product.getChannelProductId(),product.getOuterId(),0,3);
	}
	
	public int savePicture(
			ChannelPictureMapping picture) {
		String sql="INSERT INTO PRODUCT_IMAGE (CHANNEL_PRODUCT_ID,IMAGE_ID,SORT,APPLICATION_ID) VALUES (?,?,?,?)";
		return super.update(sql, picture.getChannelProductId(),picture.getImageId(),picture.getSort(),3);
	}
	
	private ProductPartnumberMapping extractProductPartnumberMapping(
			ResultSet rs) throws SQLException {
		ProductPartnumberMapping mapping = new ProductPartnumberMapping();
		mapping.setId(rs.getInt("MAPPING_ID"));
		mapping.setChannelProductId(rs.getString("CHANNEL_PRODUCT_ID"));
		mapping.setOuterId(rs.getString("OUTER_ID"));
		mapping.setType(rs.getInt("TYPE"));
		mapping.setApplicationId(rs.getInt("APPLICATION_ID"));
		return mapping;
	}
}
