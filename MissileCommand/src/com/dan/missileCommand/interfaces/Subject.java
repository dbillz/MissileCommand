package com.dan.missileCommand.interfaces;

public interface Subject<T> {

	void register(Observer<T> observer);
	
	void deregister(Observer<T> observer);
	
	void notifyObserver();
}
