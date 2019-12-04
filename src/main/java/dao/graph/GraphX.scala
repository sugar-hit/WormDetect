package dao.graph

import util.Parser
import org.apache.spark.graphx.{Edge, Graph, VertexId}
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

  def graphOutDegreeAVG (graph: Graph[String, String]) : Long = {
    if (graph == null)
      return 0
    val nodes : Long = graph.vertices.count()
    val outDegree : Long = graph.outDegrees.count()
    if (nodes == 0L)
      return outDegree
    outDegree / nodes
  }

}
