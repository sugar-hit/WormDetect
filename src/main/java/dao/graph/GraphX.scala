package dao.graph
import util.parse.JavaParser
import util.parse.ScalaParser
import java.util


import org.apache.spark.graphx.{Edge, EdgeRDD, Graph, VertexId, VertexRDD}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, SparkSession}

class GraphX extends Serializable {
  def graphGenerator (rdd : RDD[java.util.Map[String, Object]]) : Graph[String, String] = {
    val vertexSrc: RDD[String] = rdd.map(
      record => JavaParser.toStr(record.get("i_client_ip"))
    )
    val vertexDst: RDD[String] = rdd.map(
      record => JavaParser.toStr(record.get("i_server_ip"))
    )
    val vertexRDD: RDD[String] = vertexSrc.union(vertexDst).distinct()
    val vertex: RDD[(VertexId, String)] = vertexRDD.map(
      record => new Tuple2(JavaParser.ip2Long(record), record)
    )
    val edge: RDD[Edge[String]] = rdd.map(
      record =>
        new Edge[String](
          JavaParser.ip2Long(JavaParser.toStr(record.get("i_client_ip"))),
          JavaParser.ip2Long(JavaParser.toStr(record.get("i_server_ip"))),
          JavaParser.toStr(record.get("@timestamp"), record.get("i_server_port")
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

  private def getOutDegree (graph: Graph[String, String]) : RDD[(VertexId, Int)] = {
    graph.outDegrees
  }

  def test (graph: Graph[String, String]) = {
    val rdd = graph.edges
    rdd.map(
      record => println(record.srcId + " -> "+ record.dstId + " : " + ScalaParser.GraphParser.timestampFormat(record.attr))
    ).collect()
  }

  def getOutDegreeOverList (graph: Graph[String, String], least : Int, sparkSession: SparkSession) : util.HashMap[Long, Long] = {
    val result : util.HashMap[Long, Long] = new util.HashMap[Long, Long]()
    if (graph == null)
      return result
    if (least < 1)
      return result
    if (sparkSession == null)
      return result
    val outDegreeRDD: RDD[(VertexId, Int)] = getOutDegree(graph)
    if (outDegreeRDD == null)
      return result
    import sparkSession.implicits._
    val outDegreeDataFrame : DataFrame = outDegreeRDD.map(
      record => (record._1 , record._2)
    ).toDF()
    outDegreeDataFrame.createOrReplaceTempView("outDegree")

    val outDegreeResultDataFrame : DataFrame = sparkSession.sql(
      "SELECT outdegree._1, outdegree._2 FROM outDegree WHERE outdegree._2 > " + least
    )
    val resultArray : Array[Map[String, Long]] = outDegreeResultDataFrame.map(
//      record => record.getJavaMap(1)
      record => {
        List("_1", "_2").map(
          name => name -> ScalaParser.toLong(record.getAs[Long](name))
        ).toMap
      }
    ).collect()

    resultArray.foreach(
      resultSingle => {
        result.put(
          ScalaParser.toLong(ScalaParser.removeSomeTag(resultSingle.get("_1"))),
          ScalaParser.toInt(ScalaParser.removeSomeTag(resultSingle.get("_2")))
        )
      }
    )
    result
  }
}
