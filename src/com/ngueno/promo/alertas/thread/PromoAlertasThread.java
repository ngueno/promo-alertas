package com.ngueno.promo.alertas.thread;

import java.awt.TrayIcon.MessageType;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ngueno.promo.alertas.action.CustomSystemTray;
import com.ngueno.promo.alertas.bean.Promotion;
import com.ngueno.promo.alertas.constants.Constants;
import com.ngueno.promo.alertas.enums.PromotionSiteEnum;

public class PromoAlertasThread extends Thread {

	private CustomSystemTray systemTray = new CustomSystemTray();
	private Document hardmobPage;
	private Document adrenalinePage;
	private Document pelandoPage;

	private List<Promotion> promotions = new ArrayList<>();

	private boolean firstRun = true;

	@Override
	public void run() {
		init();
	}

	private void init() {

		try {

			while (true) {

				URLConnection hardmobConnection = getConnection(Constants.HARDMOB_URL);
				hardmobPage = parsePage(hardmobConnection, Constants.ENCODING_ISO);

				URLConnection adrenalineConnection = getConnection(Constants.ADRENALINE_URL);
				adrenalinePage = parsePage(adrenalineConnection, Constants.ENCODING_UTF8);

				URLConnection pelandoConnection = getConnection(Constants.PELANDO_URL);
				pelandoPage = parsePage(pelandoConnection, Constants.ENCODING_UTF8);

				proccessPage();

				Thread.sleep(5000);
			}

		} catch (Exception e) {
			e.printStackTrace();
			getSystemTray().notify(e.getMessage(), MessageType.ERROR);
		}
	}

	private List<Promotion> proccessPromotions(PromotionSiteEnum site) {

		List<Promotion> promotions = new ArrayList<Promotion>();

		if( PromotionSiteEnum.HARDMOB.equals(site) ) {
			Elements elements = getHardmobPage().select(Constants.HARDMOB_REGEX_TITLE_CSS);

			for (Element element : elements) {
				Promotion promotion = new Promotion();
				promotion.setTitle(element.text());
				promotion.setLink(element.absUrl(Constants.ABSOLUTE_HREF));
				promotions.add(promotion);
			}
		} else if( PromotionSiteEnum.ADRENALINE.equals(site) ) {
			Elements elements = getAdrenalinePage().select(Constants.ADRENALINE_REGEX_TOOLTIP_CSS);

			for (Element element : elements) {
				Promotion promotion = new Promotion();
				promotion.setTitle(element.text());
				promotion.setLink(element.absUrl(Constants.ABSOLUTE_HREF));
				promotions.add(promotion);
			}
		} else if (PromotionSiteEnum.PELANDO.equals(site)) {
			Elements elements = getPelandoPage().select(Constants.PELANDO_REGEX_TOOLTIP_CSS);

			for (Element element : elements) {
				Promotion promotion = new Promotion();
				promotion.setTitle(element.text());
				promotion.setLink(element.absUrl(Constants.ABSOLUTE_HREF));
				promotions.add(promotion);
			}
		}

		return promotions;
	}

	private void proccessPage() throws InterruptedException {

		List<Promotion> allPromotions = new ArrayList<>();

		List<Promotion> hardmobPromotions = proccessPromotions(PromotionSiteEnum.HARDMOB);
		List<Promotion> adrenalinePromotions = proccessPromotions(PromotionSiteEnum.ADRENALINE);
		List<Promotion> pelandoPromotions = proccessPromotions(PromotionSiteEnum.PELANDO);

		for (Promotion promo : hardmobPromotions) {
			if (!getPromotions().contains(promo)) {
				getSystemTray().addPopupMenuItem(promo, getSystemTray().getHardmobMenuItem());
			}
		}

		for (Promotion promo : adrenalinePromotions) {
			if (!getPromotions().contains(promo)) {
				getSystemTray().addPopupMenuItem(promo, getSystemTray().getAdrenalineMenuItem());
			}
		}

		for (Promotion promo : pelandoPromotions) {
			if (!getPromotions().contains(promo)) {
				getSystemTray().addPopupMenuItem(promo, getSystemTray().getPelandoMenuItem());
			}
		}

		if( !isFirstRun() ) {
			for (Promotion promo : hardmobPromotions) {
				if (!getPromotions().contains(promo)) {
					getSystemTray().notify(promo, Constants.HARDMOB_ALERTAS, MessageType.INFO);
				}
				Thread.sleep(3000);
			}
			for (Promotion promo : adrenalinePromotions) {
				if (!getPromotions().contains(promo)) {
					getSystemTray().notify(promo, Constants.ADRENALINE_ALERTAS, MessageType.INFO);
				}
				Thread.sleep(3000);
			}
			for (Promotion promo : pelandoPromotions) {
				if (!getPromotions().contains(promo)) {
					getSystemTray().notify(promo, Constants.PELANDO_ALERTAS, MessageType.INFO);
				}
				Thread.sleep(3000);
			}
		}

		allPromotions.addAll(hardmobPromotions);
		allPromotions.addAll(adrenalinePromotions);
		allPromotions.addAll(pelandoPromotions);

		setPromotions(allPromotions);
		setFirstRun(false);
	}

	private Document parsePage(URLConnection connection, String encoding) throws UnsupportedEncodingException, IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), encoding));

		String line = "";
		String buffer = "";

		while ((buffer = reader.readLine()) != null) {
			line += buffer;
		}

		return Jsoup.parse(line);
	}

	private URLConnection getConnection(String url) throws Exception {

		URLConnection connection = new URL(url).openConnection();
		connection.setRequestProperty(Constants.USER_AGENT, Constants.USER_AGENT_DESC);
		connection.connect();

		return connection;
	}

	public CustomSystemTray getSystemTray() {
		return systemTray;
	}

	public void setSystemTray(CustomSystemTray systemTray) {
		this.systemTray = systemTray;
	}

	public Document getHardmobPage() {
		return hardmobPage;
	}

	public void setHardmobPage(Document hardmobPage) {
		this.hardmobPage = hardmobPage;
	}

	public List<Promotion> getPromotions() {
		return promotions;
	}

	public void setPromotions(List<Promotion> promotions) {
		this.promotions = promotions;
	}

	public boolean isFirstRun() {
		return firstRun;
	}

	public void setFirstRun(boolean firstRun) {
		this.firstRun = firstRun;
	}

	public Document getAdrenalinePage() {
		return adrenalinePage;
	}

	public void setAdrenalinePage(Document adrenalinePage) {
		this.adrenalinePage = adrenalinePage;
	}

	public Document getPelandoPage() {
		return pelandoPage;
	}

	public void setPelandoPage(Document pelandoPage) {
		this.pelandoPage = pelandoPage;
	}

}
