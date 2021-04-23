package ru.yandex.incoming34;

public class Car implements Runnable {
	private static int CARS_COUNT;
	static {
		CARS_COUNT = 0;
	}
	private Race race;
	private int speed;
	private String name;
	Thread thread = new Thread(this);

	public String getName() {
		return name;
	}

	public int getSpeed() {
		return speed;
	}

	public Car(Race race, int speed) {
		this.race = race;
		this.speed = speed;
		CARS_COUNT++;
		this.name = "Участник #" + CARS_COUNT;
	}

	@Override
	public void run() {
		try {
			System.out.println(this.name + " готовится");
			Thread.sleep(500 + (int) (Math.random() * 800));
			System.out.println(this.name + " готов");
			MainClass.startReady.countDown();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			MainClass.startReady.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while (MainClass.startAnnounced.get() == false) {

		}
		for (int i = 0; i < race.getStages().size(); i++) {
			race.getStages().get(i).go(this);
		}
		MainClass.checkResult(this.name);
		MainClass.allFinished.countDown();
	}
}