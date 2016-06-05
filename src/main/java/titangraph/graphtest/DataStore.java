package titangraph.graphtest;

import java.util.Map;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

public class DataStore {

	 private static HazelcastInstance dataStoreInstance = null;

	    private DataStore() {

	    }

	    public static HazelcastInstance getDataBaseInstance() {
	        //initialize hazelcast
	        if (dataStoreInstance == null) {
	            dataStoreInstance = Hazelcast.newHazelcastInstance();
	            Map<String, Vertex> accountMap = dataStoreInstance.getMap("accounts");
	            Map<String, Edge> userMap = dataStoreInstance.getMap("users");
	        }
	        return dataStoreInstance;
	    }

	    public static void shutDownHazelcast() {
	        dataStoreInstance.shutdown();
	        dataStoreInstance = null;
	    }
}
