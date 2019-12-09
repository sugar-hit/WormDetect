package utils.compare

object ScalaCompare {
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
   * @param list TimeStamp list in edge parameters.
   * @return min timestamp (Long type-like String)
   */
  def timestampMin (list: List[String]) : String = timestampMin(list, "")

  def timestampMin (list: List[String], least : String): String = {
    if (list == null)
      return "-1"
    if (list.isEmpty)
      return "-1"
    if (least == null)
      return "-1"
    var result : StringBuffer = new StringBuffer(list.head)
    list.map(
      timestamp => {
        if (timestamp >= least && timestamp <= result.toString ) {
          result.setLength(0)
          result.append(timestamp)
        }
      }
    )
    result.toString
  }

  def string1BiggerThan2 (str1 : String, str2 : String) : Boolean = {
    if (str1 == null)
      return false
    if (str2 == null)
      return false
    if (str1 >= str2)
      return true
    false
  }

  def timestamp1BiggerThan2 (timestamp1 : String, timestamp2: String) : Boolean =
    string1BiggerThan2(timestamp1, timestamp2)

}
