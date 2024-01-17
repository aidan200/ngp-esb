/*
 * Copyright (c) 2015, COMFORT TRANSPORTATION PTE. LTD./COMFORTDELGRO PTE. LTD.
 * All right reserved.
 *
 * This software is confidential and a proprietary property of
 * COMFORT TRANSPORTATION PTE. LTD./COMFORTDELGRO PTE. LTD.
 *
 * The contents of this software shall not be modified or disclosed and shall
 * only be used in accordance with the terms and conditions stated in
 * the contract or license agreement with COMFORT TRANSPORTATION PTE. LTD./COMFORTDELGRO PTE. LTD.
 *
 * Redistribution and use in source or binary forms, with or without
 * modification, in fraction or whole are permitted provided that the following
 * conditions are met:
 *
 *   - Upon written approval from COMFORT TRANSPORTATION PTE. LTD./COMFORTDELGRO PTE. LTD.
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of COMFORT TRANSPORTATION PTE. LTD./COMFORTDELGRO PTE. LTD.
 *     nor the names of its contributors may be used to endorse or promote products
 *     derived from this software without specific prior written permission.
 */

package com.cdg.ngp.esb.ms.component.common;

/**
 * This class is used to allow threads to wait for counters to reach a certain value. Typical use is to
 * wait for all threads to finish or all connections to close etc.
 */
public class Signal {
	int counter;
	
	public Signal() {
		counter = 0;
	}
	public Signal(int value) {
		counter = value;
	}
	
	public int read() {
		synchronized(this) {
			return counter;
		}
	}
	
	public void reset() {
		synchronized(this) {
			counter = 0;
		}
	}
	public void resetAndNotify() {
		synchronized(this) {
			counter = 0;
			notifyAll();
		}
	}
	
	public void set(int value) {
		synchronized(this) {
			counter = value;
		}
	}
	public void setIfNot(int value1,int value2) {
		synchronized(this) {
			if (counter!=value1)
				counter = value2;
		}
	}
	public void setAndNotify(int value) {
		synchronized(this) {
			counter = value;
			notifyAll();
		}
	}
	
	public void increment() {
		synchronized(this) {
			counter++;
		}
	}
	public void incrementAndNotify() {
		synchronized(this) {
			counter++;
			notifyAll();
		}
	}
	
	public void decrement() {
		synchronized(this) {
			counter--;
		}
	}
	public void decrementAndNotify() {
		synchronized(this) {
			counter--;
			notifyAll();
		}
	}
	
	private synchronized boolean doWait(long finalTime,long timeout) throws Exception {
		if (timeout == 0) {
			wait();
		}
		else {
			long leftTimeout = finalTime-System.currentTimeMillis();
			if (leftTimeout > 0) {
				wait(leftTimeout);
			}
			else {
				return false;
			}
		}
		
		return true;
	}	
	
	/**
	 * Waits for the counter's value to reach the given reference value or until 
	 * it times out.
	 * @param reference The referenced value.
	 * @param timeout The timeout in milliseconds.
	 * @return true if the referenced value was reach, false otherwise.
	 */
	public boolean waitUntilEQ(int reference,long timeout) throws Exception {
		long finalTime = System.currentTimeMillis()+timeout;
		
		synchronized(this) {
			while (counter != reference) {
				if (doWait(finalTime,timeout) == false)
					return false;
			}
		}
		
		return true;
	}
	
	public boolean waitUntilLE(int reference,long timeout) throws Exception {
		long finalTime = System.currentTimeMillis()+timeout;
		
		synchronized(this) {
			while (counter > reference) {
				if (doWait(finalTime,timeout) == false)
					return false;
			}
		}
		
		return true;
	}
	
	public boolean waitUntilGE(int reference,long timeout) throws Exception {
		long finalTime = System.currentTimeMillis()+timeout;
		
		synchronized(this) {
			while (counter < reference) {
				if (doWait(finalTime,timeout) == false)
					return false;
			}
		}
		
		return true;
	}
	
	public boolean waitAndIncrement(int reference,long timeout) throws Exception {
		long finalTime = System.currentTimeMillis()+timeout;
		
		synchronized(this) {
			while (counter >= reference) {
				if (doWait(finalTime,timeout) == false)
					return false;
			}
			counter++;
		}
		
		return true;
	}

	public boolean waitAndDecrement(int reference,long timeout) throws Exception {
		long finalTime = System.currentTimeMillis()+timeout;
		
		synchronized(this) {
			while (counter <= reference) {
				if (doWait(finalTime,timeout) == false)
					return false;
			}
			counter--;
		}
		
		return true;
	}	
}
