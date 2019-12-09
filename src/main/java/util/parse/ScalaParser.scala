package util.parse

object ScalaParser {
  def toLong (any: Any) : Long = {
    if (any == Unit)
      return 0
    String.valueOf(any).toLong
  }
  def toInt (any: Any) : Int = {
    if (any == Unit)
      return 0
    String.valueOf(any).toInt
  }

  def removeSomeTag (string: String) : String = {
    if (string.isEmpty)
      return ""
    string.replace("Some(","").replace(")","")
  }

  def removeSomeTag (strLike : Any) : String = {
    removeSomeTag(String.valueOf(strLike))
  }

  object GraphParser {
    def timestampFormat (timestamp : Long) : String = {
      String.valueOf(timestamp).substring(0,14).concat(".").concat(String.valueOf(timestamp).substring(14))
    }

    def timestampFormat (timestamp : String) : String = {
      timestamp.substring(0,14).concat(".").concat(timestamp.substring(14))
    }
  }
}
