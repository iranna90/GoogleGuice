package grap;
import java.util.*;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;

/**
 * This class models a simple, undirected graph using an 
 * incidence list representation. client.getMap("vertices") are identified 
 * uniquely by their labels, and only unique client.getMap("vertices") are allowed.
 * At most one Edge per vertex pair is allowed in this Graph.
 * 
 * Note that the Graph is designed to manage the Edges. You
 * should not attempt to manually add Edges yourself.
 */
public class Graph {
        
	HazelcastInstance client = null;
    public Graph(){
    	 ClientConfig clientConfig = new ClientConfig();
         clientConfig.addAddress("127.0.0.1:5701");
         client = HazelcastClient.newHazelcastClient(clientConfig);
    }
    
    /**
     * This constructor accepts an ArrayList<Vertex> and populates
     * this.client.getMap("vertices"). If multiple Vertex objects have the same label,
     * then the last Vertex with the given label is used. 
     * 
     * @param client.getMap("vertices") The initial client.getMap("vertices") to populate this Graph
     */
   /* public Graph(ArrayList<Vertex> client.getMap("vertices")){
        this.client.getMap("vertices") = new HashMap<String, Vertex>();
        this.edges = new HashMap<Integer, Edge>();
        
        for(Vertex v: client.getMap("vertices")){
            this.client.getMap("vertices").put(v.getLabel(), v);
        }
        
    }*/
    
    /**
     * This method adds am edge between client.getMap("vertices") one and two
     * of weight 1, if no Edge between these client.getMap("vertices") already
     * exists in the Graph.
     * 
     * @param one The first vertex to add
     * @param two The second vertex to add
     * @return true iff no Edge relating one and two exists in the Graph
     */
    public boolean addEdge(Vertex one, Vertex two){
        return addEdge(one, two, 1);
    }
    
    
    /**
     * Accepts two client.getMap("vertices") and a weight, and adds the edge 
     * ({one, two}, weight) iff no Edge relating one and two 
     * exists in the Graph.
     * 
     * @param one The first Vertex of the Edge
     * @param two The second Vertex of the Edge
     * @param weight The weight of the Edge
     * @return true iff no Edge already exists in the Graph
     */
    public boolean addEdge(Vertex one, Vertex two, int weight){
        if(one.equals(two)){
            return false;   
        }
       
        //ensures the Edge is not in the Graph
        Edge e = new Edge(one, two, weight);
        if(client.getMap("edges").containsKey(e.hashCode())){
            return false;
        }
       
        //and that the Edge isn't already incident to one of the client.getMap("vertices")
        else if(one.containsNeighbor(e) || two.containsNeighbor(e)){
            return false;
        }
            
        client.getMap("edges").put(e.hashCode(), e);
        one.addNeighbor(e);
        two.addNeighbor(e);
        return true;
    }
    
    /**
     * 
     * @param e The Edge to look up
     * @return true iff this Graph contains the Edge e
     */
    public boolean containsEdge(Edge e){
        if(e.getOne() == null || e.getTwo() == null){
            return false;
        }
        
        return this.client.getMap("edges").containsKey(e.hashCode());
    }
    
    
    /**
     * This method removes the specified Edge from the Graph,
     * including as each vertex's incidence neighborhood.
     * 
     * @param e The Edge to remove from the Graph
     * @return Edge The Edge removed from the Graph
     */
    public Edge removeEdge(Edge e){
       e.getOne().removeNeighbor(e);
       e.getTwo().removeNeighbor(e);
       return (Edge) this.client.getMap("edges").remove(e.hashCode());
    }
    
    /**
     * 
     * @param vertex The Vertex to look up
     * @return true iff this Graph contains vertex
     */
    public boolean containsVertex(Vertex vertex){
        return this.client.getMap("vertices").get(vertex.getLabel()) != null;
    }
    
    /**
     * 
     * @param label The specified Vertex label
     * @return Vertex The Vertex with the specified label
     */
    public Vertex getVertex(String label){
        return (Vertex) client.getMap("vertices").get(label);
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
    public boolean addVertex(Vertex vertex, boolean overwriteExisting){
        Vertex current = (Vertex) this.client.getMap("vertices").get(vertex.getLabel());
        if(current != null){
            if(!overwriteExisting){
                return false;
            }
            
            while(current.getNeighborCount() > 0){
                this.removeEdge(current.getNeighbor(0));
            }
        }
        
        
        client.getMap("vertices").put(vertex.getLabel(), vertex);
        return true;
    }
    
    /**
     * 
     * @param label The label of the Vertex to remove
     * @return Vertex The removed Vertex object
     */
    public Vertex removeVertex(String label){
        Vertex v = (Vertex) client.getMap("vertices").remove(label);
        
        while(v.getNeighborCount() > 0){
            this.removeEdge(v.getNeighbor((0)));
        }
        
        return v;
    }
    
    /**
     * 
     * @return Set<String> The unique labels of the Graph's Vertex objects
     */
    public Set<Object> vertexKeys(){
        return this.client.getMap("vertices").keySet();
    }
    
    /**
     * 
     * @return Set<Edge> The Edges of this graph
     */
    public Collection<Object> getEdges(){
        return this.client.getMap("edges").values();
    }
    
}
