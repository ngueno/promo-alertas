package com.ngueno.promo.alertas.main;
import com.ngueno.promo.alertas.thread.PromoAlertasThread;

public class Main {

	public static void main(String[] args) throws Exception {
		PromoAlertasThread thread = new PromoAlertasThread();
		thread.start();
	}
}
