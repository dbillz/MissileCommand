package com.dan.missileCommand.runtime;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.List;

import com.dan.missileCommand.types.Location;
import com.dan.missileCommand.types.Target;
import com.dan.missileCommand.utils.StubUtils;
import com.dan.missileCommand.worldObjects.Listener;
import com.dan.missileCommand.worldObjects.RadarSite;
import com.dan.missileCommand.worldObjects.World;

public class Runnable {

	/**
	 * TODO: 
	 * debug target movement (updateLocation method)
	 * add unit tests
	 * debug triangulation
	 * add integration tests for multi-point target movement, spotting and triangulation
	 * 
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		final int size = 100;
		World world = new World(size, size);
		Listener listener = new Listener();
		RadarSite leftSite = new RadarSite(new Location(0, 50));
		leftSite.register(listener);
		RadarSite rightSite = new RadarSite(new Location(50, 0));
		rightSite.register(listener);
		List<RadarSite> radarSites = Arrays.asList(leftSite, rightSite);
		System.out.println("Starting!");
		for (int i = 0; i < 50; i++) {
			try  {
				world.addTarget(new Target(StubUtils.getRandomId(), StubUtils.getRandomHeading()), StubUtils.getRandomLocation(size));
			} catch (InvalidParameterException e) {};
			radarSites.forEach(site -> site.scan(world));
			world.updateTargetLocations();
		}

		listener.printSpottings();
		listener.getTriangulatedLocations().forEach(l -> System.out.println(l));
	}
}
