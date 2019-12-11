package utils.parse

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
    def attrPortFormat (attribute : String) : String = {
      if (attribute == null)
        return ""
      attribute.substring(14)
    }

    def attrTimestampFormat (attribute : String) : String = {
      if (attribute == null)
        return ""
      attribute.substring(0, 13)
    }
  }
}
