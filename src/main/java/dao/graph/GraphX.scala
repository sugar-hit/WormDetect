package dao.graph

import util.Parser
import org.apache.spark.graphx.{Edge, EdgeRDD, Graph, VertexId, VertexRDD}
import org.apache.spark.rdd.RDD

class GraphX {
  def graphGenerator (rdd : RDD[java.util.Map[String, Object]]) : Graph[String, String] = {
    val vertexSrc: RDD[String] = rdd.map(
      record => Parser.toStr(record.get("i_client_ip"))
    )
    val vertexDst: RDD[String] = rdd.map(
      record => Parser.toStr(record.get("i_server_ip"))
    )
    val vertexRDD: RDD[String] = vertexSrc.union(vertexDst).distinct()
    val vertex: RDD[(VertexId, String)] = vertexRDD.map(
      record => new Tuple2(Parser.ip2Long(record), record)
    )
    val edge: RDD[Edge[String]] = rdd.map(
      record =>
        new Edge[String](
          Parser.ip2Long(Parser.toStr(record.get("i_client_ip"))),
          Parser.ip2Long(Parser.toStr(record.get("i_server_ip"))),
          Parser.toStr(record.get("@timestamp"), record.get("i_server_port")
      ))
    )
    val graph: Graph[String, String] = Graph(vertex, edge)
    return graph
  }

  def graphOutDegreeAvg (graph: Graph[String, String]) : Long = {
    if (graph == null)
      return 0
    val nodes : Long = graph.vertices.count()
    val outDegree : Long = graph.outDegrees.map(line => line._2).reduce((r1, r2) => r1 + r2)
    if (nodes == 0)
      return 0
    outDegree / nodes
  }

  def graphInDegreeAvg (graph : Graph[String, String]) : Long = {
    if (graph == null)
      return 0
    val nodes : Long = graph.vertices.count()
    val inDegree : Long  = graph.inDegrees.map(line => line._2).reduce((r1, r2) =>  r1 + r2)
    if (nodes == 0)
      return 0
    inDegree / nodes
  }
}
