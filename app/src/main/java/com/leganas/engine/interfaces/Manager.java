package com.leganas.engine.interfaces;

import java.util.ArrayList;

/**Абстрактный менеджер событий (постановка в очередь и выполнение)
 * T - тип события*/
public abstract class Manager<T extends Event>{
	/**интерфейс обратной связи через назначенного слушателя*/
	public interface ManagerListener {
		/**Метод вызывается менеджером для информирования случателя о результатах обработки события*/
		public void ListenerMessage(Object msg);
	}
	
	
	protected ArrayList<T> eventQueue = new ArrayList<T>();
	public ProgController<?> progController;
	public ManagerListener listener;
	
	public Manager(ProgController<?> progController) {
		super();
		this.progController = progController;
	}
	
	
	public void process(){
		 synchronized (eventQueue) {
			 for (T event : eventQueue) {
				 Object msg = ((Event)event).Apply(progController,((Event) event).getId());
				 if (listener != null) listener.ListenerMessage(msg);
			 }
			 eventQueue.clear();
		 }
	}
	
	public void setListener(ManagerListener listener) {
		this.listener = listener;
	}

	public synchronized void addEventToQueue(T event) {
		synchronized (eventQueue) {
			eventQueue.add(event);
		}
	}
}
