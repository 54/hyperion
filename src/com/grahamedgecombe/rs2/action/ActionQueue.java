package com.grahamedgecombe.rs2.action;

import java.util.LinkedList;
import java.util.Queue;

import com.grahamedgecombe.rs2.action.Action.QueuePolicy;
import com.grahamedgecombe.rs2.model.World;

/**
 * Stores a queue of pending actions.
 * @author blakeman8192
 * @author Graham
 *
 */
public class ActionQueue {
	
	/**
	 * The maximum number of actions allowed to be queued at once, deliberately
	 * set to the size of the player's inventory.
	 */
	public static final int MAXIMUM_SIZE = 28;
	
	/**
	 * A queue of <code>Action</code> objects.
	 */
	private final Queue<Action> queuedActions = new LinkedList<Action>();
	
	/**
	 * The current action.
	 */
	private Action currentAction = null;
	
	/**
	 * Cancels all queued action events.
	 */
	public void cancelQueuedActions() {
		for(Action actionEvent : queuedActions) {
			actionEvent.stop();
		}
		queuedActions.clear();
		currentAction.stop();
		currentAction = null;
	}
	
	/**
	 * Adds an <code>Action</code> to the queue.
	 * @param action The action.
	 */
	public void addAction(Action action) {
		if(queuedActions.size() >= MAXIMUM_SIZE) {
			return;
		}
		int queueSize = queuedActions.size() + (currentAction == null ? 0 : 1);
		switch(action.getQueuePolicy()) {
		case ALWAYS:
			break;
		case NEVER:
			if(queueSize > 0) {
				return;
			}
			break;
		}
		queuedActions.add(action);
		processNextAction();
	}

	/**
	 * Processes this next action.
	 */
	public void processNextAction() {
		if(currentAction != null) {
			if(currentAction.isRunning()) {
				return;
			}
		}
		if(queuedActions.size() > 0) {
			currentAction = queuedActions.poll();
			World.getWorld().submit(currentAction);
		}
	}

}
