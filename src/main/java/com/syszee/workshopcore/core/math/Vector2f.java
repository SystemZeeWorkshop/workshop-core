package com.syszee.workshopcore.core.math;

//TODO: Remove in 1.19.3
public class Vector2f {
	private float x;
	private float y;

	public Vector2f() {}

	public Vector2f set(float x, float y) {
		this.x = x;
		this.y = y;
		return this;
	}

	public void mul(float f) {
		this.x *= f;
		this.y *= f;
	}

	public float x() {
		return this.x;
	}

	public float y() {
		return this.y;
	}
}
