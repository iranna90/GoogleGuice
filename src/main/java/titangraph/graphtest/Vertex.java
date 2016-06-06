package titangraph.graphtest;

/**
 * Created by root on 1-6-16.
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class models a vertex in a graph. For ease of the reader, a label for
 * this vertex is required. Note that the Graph object only accepts one Vertex
 * per label, so uniqueness of labels is important. This vertex's neighborhood
 * is described by the Edges incident to it.
 *
 */
@XmlRootElement
public class Vertex implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private String id;

	// incoming edges to this vertex
	private List<Edge> incomingEdges;

	private List<Edge> outgoingEdges;

	private String label;

	private String keyType;

	private String status;

	public Vertex(){}
	
	public enum StatusTypes {
		ACTIVE, OBSOLETE, DELETED
	}

	public enum KeyTypes {
		DEVICE_KEY, GROUP_KEY, PACKAGE_KEY, CHANNEL_KEY
	}

	/**
	 *
	 * @param label
	 *            The unique label associated with this Vertex
	 */
	public Vertex(String id, String label, String keyType, String status) {
		this.id = id;
		this.label = label;
		this.keyType = keyType;
		this.status = status;
		this.incomingEdges = new ArrayList<Edge>();
		this.outgoingEdges = new ArrayList<Edge>();
	}

	/**
	 * This method adds an Edge to the incidence incoming edges of this graph
	 * iff the edge is not already present.
	 *
	 * @param edge
	 *            The edge to add
	 */
	public void addIncomingEdge(Edge edge) {
		if (this.incomingEdges.contains(edge)) {
			return;
		}

		this.incomingEdges.add(edge);
	}

	/**
	 * This method adds an Edge to the incidence incoming edges of this graph
	 * iff the edge is not already present.
	 *
	 * @param edge
	 *            The edge to add
	 */
	public void addOutgoingEdge(Edge edge) {
		if (this.outgoingEdges.contains(edge)) {
			return;
		}

		this.outgoingEdges.add(edge);
	}

	/**
	 *
	 * @param other
	 *            The edge for which to search
	 * @return true iff other is contained in this.neighborhood
	 */
	public boolean containsIncomingEdge(Edge other) {
		return this.incomingEdges.contains(other);
	}

	/**
	 *
	 * @param other
	 *            The edge for which to search
	 * @return true iff other is contained in this.neighborhood
	 */
	public boolean containsOutgoingEdge(Edge other) {
		return this.outgoingEdges.contains(other);
	}

	/**
	 *
	 * @param index
	 *            The index of the Edge to retrieve
	 * @return Edge The Edge at the specified index in this.neighborhood
	 */
	public Edge getIncomingEdge(int index) {
		return this.incomingEdges.get(index);
	}

	/**
	 *
	 * @param index
	 *            The index of the Edge to retrieve
	 * @return Edge The Edge at the specified index in this.neighborhood
	 */
	public Edge getOutgoingEdge(int index) {
		return this.outgoingEdges.get(index);
	}

	/**
	 *
	 * @param index
	 *            The index of the edge to remove from this.neighborhood
	 * @return Edge The removed Edge
	 */
	Edge removeIncomingEdge(int index) {
		return this.incomingEdges.remove(index);
	}

	/**
	 *
	 * @param index
	 *            The index of the edge to remove from this.neighborhood
	 * @return Edge The removed Edge
	 */
	Edge removeOutgoingEdge(int index) {
		return this.outgoingEdges.remove(index);
	}

	/**
	 *
	 * @param e
	 *            The Edge to remove from this.neighborhood
	 */
	public void removeIncomingEdge(Edge e) {
		this.incomingEdges.remove(e);
	}

	/**
	 *
	 * @param e
	 *            The Edge to remove from this.neighborhood
	 */
	public void removeOutgoingEdge(Edge e) {
		boolean returnvalue = this.outgoingEdges.remove(e);
		System.out.println(returnvalue);
	}

	/**
	 *
	 * @return int The number of neighbors of this Vertex
	 */
	public int totalIncomingEdgeCount() {
		return this.incomingEdges.size();
	}

	/**
	 *
	 * @return int The number of neighbors of this Vertex
	 */
	public int totalOutgoingEdgeCount() {
		return this.outgoingEdges.size();
	}

	/**
	 *
	 * @return String The label of this Vertex
	 */
	public String getLabel() {
		return this.label;
	}

	/**
	 *
	 * @return The hash code of this Vertex's label
	 */
	public int hashCode() {
		return this.label.hashCode();
	}

	/**
	 *
	 * @param other
	 *            The object to compare
	 * @return true iff other instanceof Vertex and the two Vertex objects have
	 *         the same label
	 */
	public boolean equals(Object other) {
		if (!(other instanceof Vertex)) {
			return false;
		}
		Vertex v = (Vertex) other;
		return ((this.getId().equals(v.getId())) || (this.label.equals(v.label) && this.status.equals(v.status)));
	}

	/**
	 *
	 * @return ArrayList<Edge> A copy of this.neighborhood. Modifying the
	 *         returned ArrayList will not affect the neighborhood of this
	 *         Vertex
	 */
	public ArrayList<Edge> getIncomingEdges() {
		return new ArrayList<Edge>(this.incomingEdges);
	}

	/**
	 *
	 * @return ArrayList<Edge> A copy of this.neighborhood. Modifying the
	 *         returned ArrayList will not affect the neighborhood of this
	 *         Vertex
	 */
	public ArrayList<Edge> getOutgoingEdges() {
		return new ArrayList<Edge>(this.outgoingEdges);
	}

	/**
	 * @return the keyType
	 */
	public String getKeyType() {
		return keyType;
	}

	/**
	 * @param keyType
	 *            the keyType to set
	 */
	public void setKeyType(String keyType) {
		this.keyType = keyType;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param label
	 *            the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Vertex [id=" + id + ", incomingEdges=" + incomingEdges + ", outgoingEdges=" + outgoingEdges + ", label="
				+ label + ", keyType=" + keyType + ", status=" + status + "]";
	}

}