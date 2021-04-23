package ru.yandex.incoming34;

import ru.yandex.incoming34.MainClass.CarGoesThroughTunnel;

public class Tunnel extends Stage {
	public Tunnel() {
		this.length = 80;
		this.description = "Тоннель " + length + " метров";
	}

	@Override
	public void go(Car c) {

		try {
			System.out.print(c.getName() + " подъехал к участку: " + description + ". В тоннеле "
					+ MainClass.getCarsInTunnel().get() + " машин(ы). ");
			if (MainClass.getCarsInTunnel().intValue() < (MainClass.CARS_COUNT / 2)) {
				MainClass.checkCarsInTunnel(CarGoesThroughTunnel.ENTERED);
			} else {
				System.out.print(c.getName() + " ожидает своей очереди.");

				while (true) {
					c.thread.yield();
					if (MainClass.getCarsInTunnel().intValue() < (MainClass.CARS_COUNT / 2)) {
						MainClass.checkCarsInTunnel(CarGoesThroughTunnel.ENTERED);
						break;
					}
				}
			}
			System.out.println();
			System.out.println(c.getName() + " начал этап: " + description);
			Thread.sleep(length / c.getSpeed() * 1000);

		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (TunnelOverLoad e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				MainClass.checkCarsInTunnel(CarGoesThroughTunnel.LEFT);
			} catch (TunnelOverLoad e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(c.getName() + " закончил этап: " + description + " В тоннеле осталось "
					+ MainClass.getCarsInTunnel().get() + " машины.");
		}
	}
}
