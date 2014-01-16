package com.simulation.neteasy.inter;

public interface OnNewsListener {

	 void onComplete(String result);
	 void onError(String error);
	 void onException(Exception exception);
}
