package dao.graph.aggregation

import java.util

import dao.graph.GraphX
import org.apache.spark.graphx.{Graph, VertexRDD}
import org.apache.spark.sql.{DataFrame, SparkSession}
import utils.parse.ScalaParser

import scala.collection.mutable.ArrayBuffer

object AggregationList extends Serializable {
  val srcArray : ArrayBuffer[Long] = new ArrayBuffer[Long]()
  val outDegreeArray : ArrayBuffer[Long] = new ArrayBuffer[Long]()
  val usableSrcArray : ArrayBuffer[Long] = new ArrayBuffer[Long]()
  val usableOutDegreeArray :ArrayBuffer[Long] = new ArrayBuffer[Long]()
  var outDegreeAVG : Long = 3
  var outDegreeSub : Long = 0

  def srcArrayAppend (src: Long) : Unit = {
    srcArray += src
  }

  def getSrcArray: Array[Long] =
    srcArray.toArray

  def test : Array[Long] =
    usableSrcArray.toArray

  def resetSrcArray(): Unit =
    srcArray.clear()

  def outDegreeArrayAppend (src: Long) : Unit = {
    outDegreeArray += src
  }

  def getOutDegreeArray: Array[Long] =
    outDegreeArray.toArray

  def resetOutDegreeArray(): Unit =
    outDegreeArray.clear()


  def usableSrcArrayAppend (src: Long) : Unit =  {
    usableSrcArray += src
  }

  private def resetUsableSrcArray() : Unit =
    usableSrcArray.clear()


  def usableOutDegreeArrayAppend (src: Long) : Unit =  {
    usableOutDegreeArray += src
  }

  private def resetUsableOutDegreeArray() : Unit =
    usableOutDegreeArray.clear()

  def outDegreeSubAdd (num : Long) : Unit = {
    outDegreeSub = outDegreeSub + num
  }

  def outDegreeSubAdd (num : Int) : Unit = {
    outDegreeSub = outDegreeSub + num
  }

  private def resetOutDegreeSub() : Unit = {
    outDegreeSub = 0
  }

  def reset() : Unit = {
    resetSrcArray()
    resetOutDegreeArray()
    resetUsableSrcArray()
    resetUsableOutDegreeArray()
    resetOutDegreeSub()
  }

  def generate (): Unit = {
//    usableOutDegreeArray.map(
//      re => {
//        print(re + "/")
//        re
//      }
//    )
    if (outDegreeSub == 0) {
      reset()
      return
    }
    if (usableSrcArray.isEmpty){
      reset()
      return
    }
//    outDegreeAVG = outDegreeSub / usableSrcArray.size
    outDegreeAVG = 10

    for (i <- usableSrcArray.indices) {
      if (usableOutDegreeArray(i) > outDegreeAVG) {
        srcArrayAppend(usableSrcArray(i))
        outDegreeArrayAppend(usableOutDegreeArray(i))
      }
    }
  }

//
//  def getAggregationAvg (outDegreeHashMap: util.HashMap[java.lang.Long, java.lang.Long], sparkSession: SparkSession) : java.lang.Long = {
//    val outDegreeArray = outDegreeHashMap.keySet().toArray()
//    val outDegreeArraySize = outDegreeArray.size
//    var outDegreeCombine : java.lang.Long = 0L
//    for ( i <- 0 until (outDegreeArray.length - 1) )
//      outDegreeCombine += ScalaParser.toLong(outDegreeArray(i))
//    println (outDegreeCombine)
//    println(outDegreeArraySize)
//    outDegreeCombine / outDegreeArraySize
//  }
//
//  def aggregationAvgAboveFilterVertexList (outDegreeHashMap: util.HashMap[java.lang.Long, java.lang.Long], moreThan : Long) : Array[Long] = {
//    AggregationListHelper.aggregationAvgAboveFilterVertexList(
//      outDegreeHashMap, moreThan
//    ).toArray()
//     .map(
//       record => {
//         ScalaParser.toLong(record)
//       }
//     )
//  }

}
