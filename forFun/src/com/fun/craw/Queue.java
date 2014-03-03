package com.fun.craw;

import java.util.LinkedList;

public class Queue {

	private LinkedList<Object> queue = new LinkedList<Object>();
	
	public void enQueue(Object obj)
	{
		queue.addLast(obj);
	}
	
	public Object deQueue()
	{
		return queue.removeFirst();
	}
	
	
	public boolean empty()
	{
		return queue.isEmpty();
	}
	
	public boolean contains(Object obj)
	{
		return queue.contains(obj);
	}
}
