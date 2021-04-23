package ru.yandex.incoming34;

public class TunnelOverLoad extends Exception {
	public TunnelOverLoad(int qtyCars) {
		super();
		this.qtyCars = qtyCars;
	}

	@Override
	public String toString() {
		return "В тоннеле " + qtyCars + " машин";

	}

	int qtyCars = 0;
}
