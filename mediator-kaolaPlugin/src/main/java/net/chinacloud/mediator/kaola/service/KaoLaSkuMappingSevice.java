package net.chinacloud.mediator.kaola.service;

import net.chinacloud.mediator.domain.Sku;

public interface KaoLaSkuMappingSevice {
	public Boolean saveOrupdateSkuPartnumberMapping(Sku sku,int applicationId,String modifiedtime);

}
