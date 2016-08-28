package com.ngueno.promo.alertas.action;

import java.awt.AWTException;
import java.awt.Desktop;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;

import javax.imageio.ImageIO;

import com.ngueno.promo.alertas.bean.Promotion;
import com.ngueno.promo.alertas.constants.Constants;
import com.ngueno.promo.alertas.thread.PromoAlertasThread;

public class CustomSystemTray implements ActionListener {

	private PromoAlertasThread thread;

	private SystemTray systemTray;
	private TrayIcon trayIcon;
	
	private PopupMenu popup;
	
	private Menu hardmobMenuItem;
	private Menu adrenalineMenuItem;
	private Menu pelandoMenuItem;

	public CustomSystemTray() {
		prepareSystemTray();
	}

	private void prepareSystemTray() {
		try {
			prepareTrayIcon();

			systemTray = SystemTray.getSystemTray();
			systemTray.add(getTrayIcon());

			notify(Constants.PROMO_ALERTAS_ATIVO, MessageType.INFO);

		} catch (AWTException e) {
			notify(e.getMessage(), MessageType.ERROR);
		}
	}

	public void addPopupMenuItem(Promotion promo, Menu menu) {
		MenuItem menuItem = new MenuItem();
		menuItem.addActionListener(this);
		menuItem.setLabel(promo.getTitle());
		menuItem.setActionCommand(promo.getLink());
		menu.insert(menuItem, Constants.DEFAULT_INDEX);
	}

	private void prepareTrayIcon() {
		try {
			trayIcon = new TrayIcon(getToolkitImage(), Constants.HARDMOB_ALERTAS);
			trayIcon.addActionListener(this);
			trayIcon.setImageAutoSize(true);
			prepareTrayPopupMenu();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void prepareTrayPopupMenu() {
		popup = new PopupMenu();
		addDefaultOptions();
		trayIcon.setPopupMenu(popup);
	}

	private void addDefaultOptions() {
		popup.insert(getExitOption(), Constants.DEFAULT_INDEX);
		popup.insert(getAboutOption(), Constants.DEFAULT_INDEX);
		popup.insert(Constants.SEPARATOR, Constants.DEFAULT_INDEX);
		popup.insert(getHarmobMenu(), Constants.DEFAULT_INDEX);
		popup.insert(getAdrenalineMenu(), Constants.DEFAULT_INDEX);
		popup.insert(getPelandoMenu(), Constants.DEFAULT_INDEX);
	}
	
	private Menu getHarmobMenu() {
		hardmobMenuItem = new Menu();
		hardmobMenuItem.addActionListener(this);
		hardmobMenuItem.setLabel(Constants.HARDMOB);
		return hardmobMenuItem;
	}
	
	private Menu getAdrenalineMenu() {
		adrenalineMenuItem = new Menu();
		adrenalineMenuItem.addActionListener(this);
		adrenalineMenuItem.setLabel(Constants.ADRENALINE);
		return adrenalineMenuItem;
	}

	private Menu getPelandoMenu() {
		pelandoMenuItem = new Menu();
		pelandoMenuItem.addActionListener(this);
		pelandoMenuItem.setLabel(Constants.PELANDO);
		return pelandoMenuItem;
	}

	private MenuItem getAboutOption() {
		MenuItem menuItem = new MenuItem();
		menuItem.addActionListener(this);
		menuItem.setLabel(Constants.ABOUT);
		menuItem.setActionCommand(Constants.ABOUT);
		return menuItem;
	}

	private MenuItem getExitOption() {
		MenuItem menuItem = new MenuItem();
		menuItem.addActionListener(this);
		menuItem.setLabel(Constants.EXIT);
		menuItem.setActionCommand(Constants.EXIT);
		return menuItem;
	}

	private Image getToolkitImage() throws IOException {
		return ImageIO.read(getClass().getResource(Constants.TRAY_ICON_IMAGE));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {

			if( e.getActionCommand().equals(Constants.ABOUT) ) {
				showAboutMessage();
			} else if( e.getActionCommand().equals(Constants.EXIT) ) {
				System.exit(0);
			} else {
				Desktop.getDesktop().browse(new URI(e.getActionCommand()));
			}

		} catch (Exception e1) {
			notify(e1.getMessage(), MessageType.ERROR);
		}
	}

	private void showAboutMessage() {
		notify(Constants.ABOUT_TEXT, MessageType.INFO);
	}

	public void notify(Promotion promotion, String promotionSite, MessageType messageType) {
		getTrayIcon().displayMessage(promotionSite, promotion.getTitle(), messageType);
	}

	public void notify(String message, MessageType messageType) {
		getTrayIcon().displayMessage(Constants.PROMOTIONS_ALERT, message, messageType);
	}

	public SystemTray getSystemTray() {
		return systemTray;
	}

	public void setSystemTray(SystemTray systemTray) {
		this.systemTray = systemTray;
	}

	public TrayIcon getTrayIcon() {
		return trayIcon;
	}

	public void setTrayIcon(TrayIcon trayIcon) {
		this.trayIcon = trayIcon;
	}

	public PopupMenu getPopup() {
		return popup;
	}

	public void setPopup(PopupMenu popup) {
		this.popup = popup;
	}

	public PromoAlertasThread getThread() {
		return thread;
	}

	public void setThread(PromoAlertasThread thread) {
		this.thread = thread;
	}

	public Menu getHardmobMenuItem() {
		return hardmobMenuItem;
	}

	public void setHardmobMenuItem(Menu hardmobMenuItem) {
		this.hardmobMenuItem = hardmobMenuItem;
	}

	public Menu getAdrenalineMenuItem() {
		return adrenalineMenuItem;
	}

	public void setAdrenalineMenuItem(Menu adrenalineMenuItem) {
		this.adrenalineMenuItem = adrenalineMenuItem;
	}

	public Menu getPelandoMenuItem() {
		return pelandoMenuItem;
	}

	public void setPelandoMenuItem(Menu pelandoMenuItem) {
		this.pelandoMenuItem = pelandoMenuItem;
	}

}
