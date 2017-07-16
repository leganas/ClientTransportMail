package com.leganas.engine.interfaces;

/**Абстрактное событие для чего либо 
 * (Это может быть команда на исполнение в последующем либо сообщение)*/
public abstract class Event {
	int id = -1;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	/**Метод реализующий реакцию на событие
	 * @param progController - Контроллер с помощью которого будет реализовано выполнение
	 * @param id - идентификатор создавшего событие (-1 если неизвестен, (по умолчанию))*/
	public abstract Event Apply(ProgController<?> progController, int id);
}
