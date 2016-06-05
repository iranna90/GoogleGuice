package titangraph.graphtest;

/**
 * Created by root on 1-6-16.
 */
import java.io.Serializable;

/**
 * This class models an undirected Edge in the Graph implementation. An Edge
 * contains two vertices and a weight. If no weight is specified, the default is
 * a weight of 1. This is so traversing edges is assumed to be of greater
 * distance or cost than staying at the given vertex.
 *
 * This class also deviates from the expectations of the Comparable interface in
 * that a return value of 0 does not indicate that this.equals(other). The
 * equals() method only compares the vertices, while the compareTo() method
 * compares the edge weights. This provides more efficient implementation for
 * checking uniqueness of edges, as well as the fact that two edges of equal
 * weight should be considered equitably in a pathfinding or spanning tree
 * algorithm.
 */
public class Edge implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private String from, to, relation, status;

	public enum StatusTypes {
		ACTIVE, OBSOLETE, DELETED
	}
	
	public enum RelationshipTypes {
		ENCRYPTEDBY,BELONGSTO
	}

	/**
	 *
	 * @param one
	 *            The first vertex in the Edge
	 * @param two
	 *            The second vertex of the Edge
	 * @param relation
	 *            relation from start to end vertex
	 */
	public Edge(String fromId, String toId, String relation, String status) {
		this.from = fromId;
		this.to = toId;
		this.relation = relation;
		this.status = status;
	}

	/**
	 *
	 * @param current
	 * @return The neighbor of current along this Edge
	 */
	public String getNeighbor(Vertex current) {
		if (!(current.getId().equals(from) || current.getId().equals(to))) {
			return null;
		}

		return (current.getId().equals(from)) ? to : from;
	}

	/**
	 * @return the from
	 */
	public String getFrom() {
		return from;
	}

	/**
	 * @param from
	 *            the from to set
	 */
	public void setFrom(String from) {
		this.from = from;
	}

	/**
	 * @return the to
	 */
	public String getTo() {
		return to;
	}

	/**
	 * @param to
	 *            the to to set
	 */
	public void setTo(String to) {
		this.to = to;
	}

	/**
	 * @return the relation
	 */
	public String getRelation() {
		return relation;
	}

	/**
	 * @param relation
	 *            the relation to set
	 */
	public void setRelation(String relation) {
		this.relation = relation;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Edge [from=" + from + ", to=" + to + ", relation=" + relation + ", status=" + status + "]";
	}

}