package hazelcast;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import grap.Edge;
import grap.Vertex;

import java.util.concurrent.ConcurrentMap;
 
public class Server {
    public static void main(String[] args) {
        Config config = new Config();
        HazelcastInstance h = Hazelcast.newHazelcastInstance(config);
        ConcurrentMap<String, Vertex> vertexMap = h.getMap("vertices");
        ConcurrentMap<Integer, Edge> EdgeMap = h.getMap("edges");
        }
}   