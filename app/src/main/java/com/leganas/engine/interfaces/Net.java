package com.leganas.engine.interfaces;

public abstract class Net<T> {
	// Храним экземпляр игрового контроллера 
	public T programController;

	public Net(T programController) {
		super();
		this.programController = programController;
	}
}
