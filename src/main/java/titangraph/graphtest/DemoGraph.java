package titangraph.graphtest;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by root on 1-6-16.
 */

public class DemoGraph {
	private static final int NUMBER_OF_DEVICES = 1600;
	private static final int NUMBER_OF_GROUPS = 16;
	private static final int NUMBER_OF_PACKAGES = 500;
	private static final int NUMBER_OF_CHANNELS = 50;

	private static final String DEVICE = "DEVICE";
	private static final String GROUP = "GROUP";
	private static final String PACKAGE = "PACKAGE";
	private static final String CHANNEL = "CHANNEL";

	Graph graph;

	public static void main(String[] args) {
		System.out.println("starr");
		DemoGraph startDemo = new DemoGraph();
		startDemo.start();
		startDemo.createVertices();
		startDemo.createEdges();

		startDemo.startQuerying();
	}

	private void start() {
		graph = new Graph();
	}

	/**
	 * creates domain entities as graph vertices
	 */
	private void createVertices() {

		// Create Device vertices : example create
		System.out.println("started vertex creation yes");
		createDeviceKeyVertices();
		System.out.println("Device keys are created");
		// Create GroupKey vertices
		createGroupKeyVertices();
		System.out.println("Group keys are created");
		// Create PackageKey vertices
		createPackageKeyVertices();
		System.out.println("Package keys are created");
		// Create Content vertices
		createContentKeyVertices();
		System.out.println("Content keys are added");

	}

	private void createDeviceKeyVertices() {

		for (int i = 0; i < NUMBER_OF_DEVICES; i++) {
			graph.addVertex(new Vertex(DEVICE + i, DEVICE + i, Vertex.KeyTypes.DEVICE_KEY.toString(),
					Vertex.StatusTypes.ACTIVE.toString()), true);
		}
	}

	private void createGroupKeyVertices() {
		for (int i = 0; i < NUMBER_OF_GROUPS; i++) {
			graph.addVertex(new Vertex(GROUP + i, GROUP + i, Vertex.KeyTypes.GROUP_KEY.toString(),
					Vertex.StatusTypes.ACTIVE.toString()), true);
		}

	}

	private void createPackageKeyVertices() {
		for (int i = 0; i < NUMBER_OF_PACKAGES; i++) {
			graph.addVertex(new Vertex(PACKAGE + i, PACKAGE + i, Vertex.KeyTypes.PACKAGE_KEY.toString(),
					Vertex.StatusTypes.ACTIVE.toString()), true);
		}
	}

	private void createContentKeyVertices() {
		for (int i = 0; i < NUMBER_OF_CHANNELS; i++) {
			graph.addVertex(new Vertex(CHANNEL + i, CHANNEL + i, Vertex.KeyTypes.CHANNEL_KEY.toString(),
					Vertex.StatusTypes.ACTIVE.toString()), true);
		}
	}

	/**
	 * creates the edges between vertices, these are the relationship between
	 * entities
	 */
	private void createEdges() {
		// user has account
		createChannelBelongsToPackages();
		System.out.println("Channel belongs to packages relationship is created");
		createPackagesBelongsToGroup();
		System.out.println("Packages belongs to group relationship is created");
		createGroupBelongsToDevices();
		System.out.println("Groups belongs to group Devices is created");
	}

	private void createChannelBelongsToPackages() {
		// for each package in
		for (int i = 0; i < NUMBER_OF_PACKAGES; i++) {
			graph.addEdge(CHANNEL + generateRandomNumber(0, NUMBER_OF_CHANNELS), PACKAGE + i,
					Edge.RelationshipTypes.ENCRYPTEDBY.toString(), Edge.StatusTypes.ACTIVE.toString());
		}
	}

	private void createPackagesBelongsToGroup() {
		// for each package in
		for (int i = 0; i < NUMBER_OF_PACKAGES; i++) {
			graph.addEdge(PACKAGE + i, GROUP + (i % NUMBER_OF_GROUPS), Edge.RelationshipTypes.ENCRYPTEDBY.toString(),
					Edge.StatusTypes.ACTIVE.toString());
		}
	}

	private void createGroupBelongsToDevices() {
		// for each package in
		// for each package in
		for (int i = 0; i < NUMBER_OF_DEVICES; i++) {
			graph.addEdge(GROUP + (i % NUMBER_OF_GROUPS), DEVICE + i, Edge.RelationshipTypes.ENCRYPTEDBY.toString(),
					Edge.StatusTypes.ACTIVE.toString());
		}
	}

	private int generateRandomNumber(int low, int high) {
		Random r = new Random();
		return r.nextInt(high - low) + low;
	}

	private void startQuerying() {
		while (true) {
			System.out.println("Enter 1 for device retrieval");
			System.out.println("Enter 2 for channel retrieval");
			@SuppressWarnings("resource")
			Scanner in = new Scanner(System.in);
			String input = null;
			input = in.next();
			if (input.equalsIgnoreCase("exit"))
				System.exit(0);
			switch (Integer.valueOf(input).intValue()) {
			case 1:
				System.out.println("Enter device id ");
				String id = in.next();
				System.out.println("Retrieving keys for devide " + id);
				accessAllKeysForDevice(DEVICE + id);
				break;
			case 2:
				System.out.println("Enter channel id ");
				String channelId = in.next();
				System.out.println("Retrieving keys for channel " + channelId);
				accessAllKeysForChannel(CHANNEL + channelId);
				break;
			}

		}

	}

	private void accessAllKeysForDevice(String deviceId) {
		Vertex vertex = graph.getVertex(deviceId);
		ArrayList<Vertex> traversedVertex = new ArrayList<Vertex>();
		// device added
		traversedVertex.add(vertex);
		System.out.println("started at " + LocalTime.now());
		vertex.getIncomingEdges().stream().forEach(groupToDeviceEdge -> {
			Vertex groupVertex = graph.getVertex(groupToDeviceEdge.getFrom());
			// group added
			traversedVertex.add(groupVertex);
			groupVertex.getIncomingEdges().stream().forEach(packageToGroupEdge -> {
				Vertex packgaeVertex = graph.getVertex(packageToGroupEdge.getFrom());
				// package added
				traversedVertex.add(packgaeVertex);
				packgaeVertex.getIncomingEdges().stream().forEach(
						channelToPackageEdge -> traversedVertex.add(graph.getVertex(channelToPackageEdge.getFrom())));
			});
		});
		traversedVertex.stream().forEach(ver -> System.out.println(ver.getLabel()));
		System.out.println("ended at " + LocalTime.now());
	}
	
	private void accessAllKeysForChannel(String channelId) {
		Vertex vertex = graph.getVertex(channelId);
		ArrayList<Vertex> traversedVertex = new ArrayList<Vertex>();
		// channel added
		traversedVertex.add(vertex);
		System.out.println("started at " + LocalTime.now());
		vertex.getOutgoingEdges().stream().forEach(channelToPackageEdge -> {
			Vertex packageVertex = graph.getVertex(channelToPackageEdge.getTo());
			// package added
			traversedVertex.add(packageVertex);
			packageVertex.getOutgoingEdges().stream().forEach(packageToGroupEdge -> {
				Vertex groupVertex = graph.getVertex(packageToGroupEdge.getTo());
				// group added
				traversedVertex.add(groupVertex);
				groupVertex.getOutgoingEdges().stream().forEach(
						groupToDeviceEdge -> traversedVertex.add(graph.getVertex(groupToDeviceEdge.getTo())));
			});
		});
		traversedVertex.stream().forEach(ver -> System.out.println(ver.getLabel()));
		System.out.println("ended at " + LocalTime.now());
	}

}
