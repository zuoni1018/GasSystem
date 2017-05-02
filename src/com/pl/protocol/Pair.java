package com.pl.protocol;

public class Pair<T1, T2> {
	public T1 key;
	public T2 value;

	public Pair(T1 a, T2 b) {
		key = a;
		value = b;
	}

	public void SetPair(T1 a, T2 b) {
		key = a;
		value = b;
	}

	public void SetKey(T1 a) {
		key = a;
	}

	public void SetValue(T2 b) {
		value = b;
	}
}
