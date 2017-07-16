package com.leganas.engine.interfaces;

/**Абстрактный класс описывающий Event типа Команда для чего либо*/
public abstract class Command<T> extends Event{
	public abstract Event ApplyCommand(T controller);
	
	@Override
	public Event Apply(ProgController<?> progController, int id) {
		return  ApplyCommand((T) progController);
	}

}
