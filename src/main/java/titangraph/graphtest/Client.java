package titangraph.graphtest;

import java.time.LocalTime;
import java.util.ArrayList;

public class Client {

	private static final String DEVICE = "DEVICE";
	private static final String GROUP = "GROUP";
	private static final String PACKAGE = "PACKAGE";
	private static final String CHANNEL = "CHANNEL";

	Graph graph = null;

	public static void main(String[] args) {
		Client test = new Client();
		test.accessAllKeysForDevice(DEVICE + 8);
		//test.accessAllKeysForChannel(CHANNEL + 1);
		// System.out.println("Out going edges are "+outGoing);
		test.graph.client.shutdown();
	}

	public Client() {
		graph = new Graph();
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

}
