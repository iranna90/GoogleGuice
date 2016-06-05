package titangraph.graphtest;

/**
 * Created by root on 1-6-16.
 */
import java.util.Collection;
import java.util.Set;

import com.hazelcast.core.HazelcastInstance;

/**
 * This class models a simple, undirected graph using an incidence list
 * representation. client.getMap("vertices") are identified uniquely by their
 * labels, and only unique client.getMap("vertices") are allowed. At most one
 * Edge per vertex pair is allowed in this Graph.
 *
 * Note that the Graph is designed to manage the Edges. You should not attempt
 * to manually add Edges yourself.
 */
public class Graph {

	private static final String VERTEX_MAP = "vertices";

	private static final String EDGE_MAP = "edges";

	public static HazelcastInstance client = null;

	public Graph() {
		client = DataStore.getDataBaseInstance();
	}

	/**
	 * This constructor accepts an ArrayList<Vertex> and populates
	 * this.client.getMap("vertices"). If multiple Vertex objects have the same
	 * label, then the last Vertex with the given label is used.
	 *
	 * @param client.getMap("vertices")
	 *            The initial client.getMap("vertices") to populate this Graph
	 */
	/*
	 * public Graph(ArrayList<Vertex> client.getMap("vertices")){
	 * this.client.getMap("vertices") = new HashMap<String, Vertex>();
	 * this.edges = new HashMap<Integer, Edge>();
	 * 
	 * for(Vertex v: client.getMap("vertices")){
	 * this.client.getMap("vertices").put(v.getLabel(), v); }
	 * 
	 * }
	 */

	/**
	 * This method adds am edge between client.getMap("vertices") one and two of
	 * weight 1, if no Edge between these client.getMap("vertices") already
	 * exists in the Graph.
	 *
	 * @param one
	 *            The first vertex to add
	 * @param two
	 *            The second vertex to add
	 * @return true iff no Edge relating one and two exists in the Graph
	 */
	public boolean addEdge(Vertex one, Vertex two) {
		return addEdge(one, two, Edge.RelationshipTypes.ENCRYPTEDBY.toString(), Edge.StatusTypes.ACTIVE.toString());
	}

	/**
	 * Accepts two client.getMap("vertices") and a relation, and adds the edge
	 * ({one, two}, relation) iff no Edge relating one and two exists in the
	 * Graph.
	 *
	 * @param from
	 *            The first Vertex of the Edge
	 * @param to
	 *            The second Vertex of the Edge
	 * @param relation
	 *            The relation of the Edge
	 * @return true iff no Edge already exists in the Graph
	 */
	public boolean addEdge(Vertex one, Vertex two, String relation, String status) {
		// ensures the Edge is not in the Graph
		Edge e = new Edge(one.getId(), two.getId(), relation, status);
		return this.addEdge(e);
	}

	/**
	 * Accepts two client.getMap("vertices") and a relation, and adds the edge
	 * ({one, two}, relation) iff no Edge relating one and two exists in the
	 * Graph.
	 *
	 * @param from
	 *            The first Vertex of the Edge
	 * @param to
	 *            The second Vertex of the Edge
	 * @param relation
	 *            The relation of the Edge
	 * @return true iff no Edge already exists in the Graph
	 */
	public boolean addEdge(String fromId, String toId, String relation, String status) {
		// ensures the Edge is not in the Graph
		Edge e = new Edge(fromId, toId, relation, status);
		return this.addEdge(e);
	}

	/**
	 * Accepts two client.getMap("vertices") and a relation, and adds the edge
	 * ({one, two}, relation) iff no Edge relating one and two exists in the
	 * Graph.
	 *
	 * @param from
	 *            The first Vertex of the Edge
	 * @param to
	 *            The second Vertex of the Edge
	 * @param edge
	 *            edge to be added
	 * @return true iff no Edge already exists in the Graph
	 */
	public boolean addEdge(Edge edge) {
		// Step 1: Add edge to map
		Graph.client.getMap(EDGE_MAP).put(edge.hashCode(), edge);
		// Step 2: Now update the from vertex outgoing edge list with this new
		// edge
		// get the vertex
		Vertex fromVertex = (Vertex) Graph.client.getMap(VERTEX_MAP).get(edge.getFrom());
		// add outgoing edge
		fromVertex.addOutgoingEdge(edge);
		// update from vertex to map
		Graph.client.getMap(VERTEX_MAP).put(fromVertex.getId(), fromVertex);
		// Step 3: Now update the to vertex incoming edges with this new edge
		// get the vertex
		Vertex toVertex = (Vertex) Graph.client.getMap(VERTEX_MAP).get(edge.getTo());
		// Add the edge to incoming list
		toVertex.addIncomingEdge(edge);
		// update back to data base
		Graph.client.getMap(VERTEX_MAP).put(toVertex.getId(), toVertex);
		return true;
	}

	/**
	 *
	 * @param e
	 *            The Edge to look up
	 * @return true iff this Graph contains the Edge e
	 */
	public boolean containsEdge(Edge e) {
		if (e.getFrom() == null || e.getTo() == null) {
			return false;
		}

		return Graph.client.getMap(EDGE_MAP).containsKey(e.hashCode());
	}

	/**
	 * This method removes the specified Edge from the Graph, including as each
	 * vertex's incidence neighborhood.
	 *
	 * @param e
	 *            The Edge to remove from the Graph
	 * @return Edge The Edge removed from the Graph
	 */
	public Edge removeEdge(Edge e) {
		Vertex fromVertex = (Vertex) Graph.client.getMap(VERTEX_MAP).get(e.getFrom());
		if (fromVertex != null) {
			fromVertex.removeOutgoingEdge(e);
			Graph.client.getMap(VERTEX_MAP).put(fromVertex.getId(), fromVertex);
		}
		Vertex toVertex = (Vertex) Graph.client.getMap(VERTEX_MAP).get(e.getTo());
		if (toVertex != null) {
			toVertex.removeIncomingEdge(e);
			Graph.client.getMap(VERTEX_MAP).put(toVertex.getId(), toVertex);
		}
		return (Edge) Graph.client.getMap(EDGE_MAP).remove(e.hashCode());
	}

	/**
	 *
	 * @param vertex
	 *            The Vertex to look up
	 * @return true iff this Graph contains vertex
	 */
	public boolean containsVertex(Vertex vertex) {
		return Graph.client.getMap("vertices").get(vertex.getLabel()) != null;
	}

	/**
	 *
	 * @param label
	 *            The specified Vertex label
	 * @return Vertex The Vertex with the specified label
	 */
	public Vertex getVertex(String id) {
		return (Vertex) client.getMap(VERTEX_MAP).get(id);
	}

	/**
	 * This method adds a Vertex to the graph. If a Vertex with the same label
	 * as the parameter exists in the Graph, the existing Vertex is overwritten
	 * only if overwriteExisting is true. If the existing Vertex is overwritten,
	 * the Edges incident to it are all removed from the Graph.
	 *
	 * @param vertex
	 * @param overwriteExisting
	 * @return true iff vertex was added to the Graph
	 */
	public boolean addVertex(Vertex vertex, boolean overwriteExisting) {
		Vertex current = (Vertex) Graph.client.getMap(VERTEX_MAP).get(vertex.getId());
		if (current != null) {
			if (!overwriteExisting) {
				return false;
			}
			// to over write remove all incoming and outgoing edges
			while (current.getIncomingEdgeCount() > 0) {
				this.removeEdge(current.getIncomingEdge(0));
			}

			while (current.getOutgoingEdgeCount() > 0) {
				this.removeEdge(current.getOutgoingEdge(0));
			}
		}
		client.getMap(VERTEX_MAP).put(vertex.getId(), vertex);
		return true;
	}

	/**
	 *
	 * @param label
	 *            The label of the Vertex to remove
	 * @return Vertex The removed Vertex object
	 */
	public Vertex removeVertex(String id) {
		Vertex vertex = (Vertex) client.getMap(VERTEX_MAP).remove(id);
		// to over write remove all incoming and outgoing edges
		while (vertex.getIncomingEdgeCount() > 0) {
			this.removeEdge(vertex.getIncomingEdge(0));
		}
		while (vertex.getOutgoingEdgeCount() > 0) {
			this.removeEdge(vertex.getOutgoingEdge(0));
		}
		return vertex;
	}

	/**
	 *
	 * @return Set<String> The unique labels of the Graph's Vertex objects
	 */
	public Set<Object> vertexKeys() {
		return Graph.client.getMap(VERTEX_MAP).keySet();
	}

	/**
	 *
	 * @return Set<Edge> The Edges of this graph
	 */
	public Collection<Object> getEdges() {
		return Graph.client.getMap(EDGE_MAP).values();
	}

}
