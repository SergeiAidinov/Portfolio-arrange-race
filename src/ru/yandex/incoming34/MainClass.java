package ru.yandex.incoming34;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class MainClass {

	enum CarGoesThroughTunnel {
		ENTERED, LEFT
	};

	public static final int CARS_COUNT = 4;
	static final CountDownLatch startReady = new CountDownLatch(CARS_COUNT);
	static final CountDownLatch allFinished = new CountDownLatch(CARS_COUNT);
	static AtomicBoolean startAnnounced = new AtomicBoolean(false);
	private volatile static AtomicInteger carsInTunnel = new AtomicInteger(0);
	static AtomicBoolean firstFinished = new AtomicBoolean(false);
	static Object monitor = new Object();
	static String winner = null;

	public static void main(String[] args) throws InterruptedException {
		System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Подготовка!!!");
		Race race = new Race(new Road(60), new Tunnel(), new Road(40));
		Car[] cars = new Car[CARS_COUNT];
		for (int i = 0; i < cars.length; i++) {
			cars[i] = new Car(race, 20 + (int) (Math.random() * 10));
		}

		for (int i = 0; i < cars.length; i++) {
			new Thread(cars[i]).start();
		}
		startReady.await();
		System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!");
		startAnnounced.set(true);
		allFinished.await();
		System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!");
		System.out.println("Победителем является " + winner);
	}

	public static void checkCarsInTunnel(CarGoesThroughTunnel carTun) throws TunnelOverLoad {
		synchronized (monitor) {
			if (carTun == CarGoesThroughTunnel.ENTERED) {
				getCarsInTunnel().getAndIncrement();
			} else {
				getCarsInTunnel().getAndDecrement();
			}
		}

		if (getCarsInTunnel().intValue() > (MainClass.CARS_COUNT / 2) || (getCarsInTunnel().intValue() < 0)) {
			throw new TunnelOverLoad(getCarsInTunnel().intValue());
		}

	}

	public synchronized static void checkResult(String name) {
		if (firstFinished.get() == false) {
			firstFinished.set(true);
			winner = name;
			System.out.println(winner + " - WIN");

		} else {
			return;
		}

	}

	public static AtomicInteger getCarsInTunnel() {
		synchronized (monitor) {

			return carsInTunnel;
		}

	}
}
