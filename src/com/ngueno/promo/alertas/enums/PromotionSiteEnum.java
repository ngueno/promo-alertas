package com.ngueno.promo.alertas.enums;

public enum PromotionSiteEnum {

	HARDMOB(1), ADRENALINE(2), PELANDO(3);
	
	private Integer site;
	
	private PromotionSiteEnum(Integer site) {
		this.site = site; 
	}

	public Integer getSite() {
		return site;
	}

	public void setSite(Integer site) {
		this.site = site;
	}

}
