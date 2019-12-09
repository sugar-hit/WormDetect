package util.compare

class ScalaCompare {
  def max (array: Array[Long]) : Long = max(array, 0)
  def max (array: Array[Long] , least : Long) : Long = {
    if (array == null)
      return -1
    if (array.length == 0)
      return -1
    var tmp: Long = least
    var tag : Boolean = false
    array.map(
      number => {
        if (number >= least) {
          tmp = number
          tag = true
        }
        number
      }
    )
    if (!tag)
      return -1
    tmp
  }

  def min (array: Array[Long]) : Long = {
    if (array == null)
      return -1
    if (array.length == 0)
      return -1
    var tmp : Long = array(0)
    array.map(
      record => {
        if (record <= tmp)
          tmp = record
        record
      }
    )
    tmp
  }

  def min (array: Array[Long], most : Long) : Long = {
    var minux : Long = min(array)
    if (minux > most)
      minux = -1
    minux
  }

  /**
   * timestampMin()
   * @param array TimeStamp array in edge parameters.
   * @return min timestamp (Long type-like String)
   */
  def timestampMin (array: Array[String]) : String = {
    if (array == null)
      return "-1"
    if (array.length == 0)
      return "-1"
    var result : StringBuffer = new StringBuffer()
    result.toString
  }
}
