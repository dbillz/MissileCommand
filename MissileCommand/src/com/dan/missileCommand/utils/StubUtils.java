package com.dan.missileCommand.utils;

import java.util.Random;
import java.util.UUID;

import com.dan.missileCommand.types.Location;

public class StubUtils {
	

	public static int getRandomHeading() {
		return new Random().nextInt(Constants.MAX_DIRECTIONS+1-Constants.MIN_DIRECTION) + Constants.MIN_DIRECTION;
	}
	
	public static int getRandomDistance() {
		return new Random().nextInt(Constants.MAX_DISTANCE);
	}
	
	public static Location getRandomLocation(int size) {
		Random random = new Random();
		return new Location(random.nextInt(size), random.nextInt(size));
	}
	
	public static String getRandomId() {
		return UUID.randomUUID().toString();
	}
}
