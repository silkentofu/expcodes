package exp.libs.algorithm.tsp.graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Graph {

	/** 是否为有向图 */
	private boolean arrow;
	
	private Map<Integer, String> nodeIdxs;
	
	private Map<String, Node> nodes;
	
	private Map<String, Edge> edges;
	
	private int[][] matrix;
	
	public Graph() {
		this(false);
	}
	
	public Graph(boolean arrow) {
		this.arrow = arrow;
		this.nodeIdxs = new HashMap<Integer, String>();
		this.nodes = new HashMap<String, Node>();
		this.edges = new HashMap<String, Edge>();
		this.matrix = null;
	}
	
	public void clear() {
		Iterator<Node> nodeIts = nodes.values().iterator();
		while(nodeIts.hasNext()) {
			nodeIts.next().clear();
		}
		nodeIdxs.clear();
		nodes.clear();
		edges.clear();
		matrix = null;
	}
	
	public boolean isArrow() {
		return arrow;
	}
	
	public boolean isEmpty() {
		return (nodeSize() <= 0);
	}
	
	public int nodeSize() {
		return nodes.size();
	}
	
	public int edgeSize() {
		return edges.size();
	}
	
	public boolean addEdge(String srcName, String snkName, int weight) {
		boolean isOk = false;
		if(matrix == null && srcName != null && snkName != null && weight >= 0) {
			Node src = addNode(srcName);
			Node snk = addNode(snkName);
			
			src.addNeighbor(snk);
			if(arrow == false) {
				snk.addNeighbor(src);
			}
			
			Edge edge = new Edge(arrow, src, snk, weight);
			edges.put(edge.getKey(), edge);
		}
		return isOk;
	}
	
	protected Node addNode(String name) {
		Node node = nodes.get(name);
		if(node == null) {
			node = new Node(nodes.size(), name);
			nodes.put(name, node);
			nodeIdxs.put(node.getId(), name);
		}
		return node;
	}
	
	public boolean delEdge(int srcId, int snkId) {
		boolean isOk = false;
		if(inRange(srcId) && inRange(snkId)) {
			Node src = getNode(srcId);
			Node snk = getNode(snkId);
			isOk = delEdge(src, snk);
		}
		return isOk;
	}
	
	public boolean delEdge(String srcName, String snkName) {
		boolean isOk = false;
		if(srcName != null && snkName != null) {
			Node src = getNode(srcName);
			Node snk = getNode(snkName);
			isOk = delEdge(src, snk);
		}
		return isOk;
	}
	
	public boolean delEdge(Node src, Node snk) {
		boolean isOk = false;
		if(matrix == null && src != null && snk != null && 
				src != Node.NULL && snk != Node.NULL) {
			src.delNeighbor(snk);
			if(arrow == false) {
				snk.delNeighbor(src);
			}
			
			Edge edge = getEdge(src, snk);
			edges.remove(edge.getKey());
		}
		return isOk;
	}
	
	private boolean inRange(int idx) {
		return (idx >= 0 && idx < nodeSize());
	}
	
	public Node getNode(String name) {
		Node node = nodes.get(name);
		return (node == null ? Node.NULL : node);
	}
	
	public Node getNode(int id) {
		return getNode(nodeIdxs.get(id));
	}
	
	public Edge getEdge(int srcId, int snkId) {
		return getEdge(getNode(srcId), getNode(snkId));
	}
	
	public Edge getEdge(String srcName, String snkName) {
		return getEdge(getNode(srcName), getNode(snkName));
	}
	
	public Edge getEdge(Node src, Node snk) {
		Edge edge = edges.get(Edge.toKey(arrow, src, snk));
		return (edge == null ? Edge.NULL : edge);
	}
	
	public int getWeight(String srcName, String snkName) {
		return getEdge(srcName, snkName).getWeight();
	}
	
	public int getWeight(int srcId, int snkId) {
		int weight = Edge.MAX_WEIGHT;
		if(matrix != null && inRange(srcId) && inRange(snkId)) {
			weight = matrix[srcId][snkId];
		}
		return weight;
	}
	
	public int getWeight(Node src, Node snk) {
		return (matrix == null ? getEdge(src, snk).getWeight() : 
			matrix[src.getId()][snk.getId()]);
	}
	
	public Set<Integer> getAllNodeIds() {
		return new HashSet<Integer>(nodeIdxs.keySet());
	}
	
	public Set<Node> getAllNodes() {
		return new HashSet<Node>(nodes.values());
	}
	
	public Set<Edge> getAllEdges() {
		return new HashSet<Edge>(edges.values());
	}
	
	public boolean setAdjacencyMatrix() {
		boolean isOk = false;
		if(matrix == null) {
			toAdjacencyMatrix();
			isOk = true;
		}
		return isOk;
	}
	
	public boolean resetAdjacencyMatrix() {
		toAdjacencyMatrix();
		return true;
	}
	
	private void toAdjacencyMatrix() {
		int size = nodeSize();
		matrix = new int[size][size];
		
		for(int r = 0; r < size; r++) {
			String rName = nodeIdxs.get(r);
			for(int c = (arrow ? 0 : r); c < size; c++) {
				String cName = nodeIdxs.get(c);
				matrix[r][c] = getWeight(rName, cName);
				if(arrow == false) {
					matrix[c][r] = matrix[r][c];
				}
			}
		}
	}
	
	public int[][] getAdjacencyMatrix() {
		return (matrix == null ? new int[0][0] : matrix);
	}
	
}