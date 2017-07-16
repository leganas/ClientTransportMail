package com.leganas.engine.interfaces;

import com.leganas.engine.Status;

/**Абстрактный контроллер*/
public abstract class ProgController<T> implements Disposable {
	long sleepMillis=0;
	int sleepNano=500;

	public long getSleepMillis() {
		return sleepMillis;
	}

	public void setSleepMillis(long sleepMillis) {
		this.sleepMillis = sleepMillis;
	}

	public int getSleepNano() {
		return sleepNano;
	}

	public void setSleepNano(int sleepNano) {
		this.sleepNano = sleepNano;
	}

	boolean updateRun = false; // Флаг определяющий работу фонового потока обновления

	public class Update implements Runnable {
		@Override
		public void run() {
			while (updateRun) {
				update();
				try {
					Thread.sleep(sleepMillis, sleepNano);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			Logs.out("Thread " + threadUpdate.getName() + " stop");
			if (Status.statusServer == Status.StatusServer.dispose) System.exit(0);
		}
	}

	void startUpdateThread(){
		Logs.out("Thread " + threadUpdate.getName() + " run");
		updateRun = true;
		threadUpdate.start();
	}

	void stopUpdateThread(){
		updateRun = false;
	}

	public T items;
	Thread threadUpdate;

	public ProgController(String name){
		init();
		threadUpdate = new Thread(new Update());
		threadUpdate.setName("Update" + name);
		startUpdateThread();
	}

	public T getItems() {
		return (T) items;
	}

	public void setItems(T items) {
		this.items = items;
	}

	/***
	 * Вызывается перед запуском основного потока обновления контроллера
	 */
	public abstract void init();

	/**
	 * Вызывается в фоновом потоке с интервалом 	long sleepMillis=0;	 int sleepNano=500;
	 * */
	public abstract void update();

	public void dispose() {
		stopUpdateThread();
	}
	
}
