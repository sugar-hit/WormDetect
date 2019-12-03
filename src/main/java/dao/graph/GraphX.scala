package dao.graph

import util.Parser
import org.apache.spark.api.java.JavaRDD
import org.apache.spark.graphx.{Edge, Graph, VertexId}
import org.apache.spark.rdd.RDD

class GraphX {
  def graphGenerator (javaRDD : JavaRDD[Map[String, Object]]) : Graph[String, String] = {
    val scalaRDD: RDD[Map[String, Object]] = javaRDD.rdd
    val vertexSrc: RDD[String] = scalaRDD.map(
      record => Parser.toStr(record.get("i_client_ip"))
    )
    val vertexDst: RDD[String] = scalaRDD.map(
      record => Parser.toStr(record.get("i_server_ip"))
    )
    val vertexRDD: RDD[String] = vertexSrc.union(vertexDst).distinct()
    val vertex: RDD[(VertexId, String)] = vertexRDD.map(
      record => new Tuple2(Parser.ip2Long(record), record)
    )
    val edge: RDD[Edge[String]] = scalaRDD.map(
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
}
