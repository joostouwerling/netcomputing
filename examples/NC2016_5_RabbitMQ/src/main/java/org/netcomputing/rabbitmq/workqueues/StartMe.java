package org.netcomputing.rabbitmq.workqueues;

public class StartMe {
	public static void main(String[] argv) throws Exception {
		Worker worker1 = new Worker("number 1",0,100);
		Worker worker2 = new Worker("number 2",200,100);
		Worker worker3 = new Worker("number 3",400,100);
		new Commander("Commander",200,320);
	}
}
