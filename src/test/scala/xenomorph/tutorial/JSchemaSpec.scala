package xenomorph.tutorial

//import argonaut.Json
//import argonaut.Json._
//import argonaut.DecodeJson
//import argonaut.DecodeJson._

import scalaz.FreeAp

import org.specs2._
//import org.scalacheck._
//import org.scalacheck.Gen._

import xenomorph.tutorial.schema5._

case class Person(
  name: String, 
  birthDate: Long,
  roles: Vector[Role]
)

sealed trait Role

case object User extends Role
case class Administrator(department: String) extends Role

class JSchemaSpec extends Specification with org.specs2.ScalaCheck {
  def is = s2"""
  Serialization of primitive values to JSON should
    serialize boolean values $serBool
    serialize double values $serLong
    serialize string values $serString
  """

  def serBool = {
    ok
  }

  def serLong = {
    ok
  }

  def serString = {
    ok
  }

  val roleSchema = JSumT(
    Alt[Role, Unit](
      "user", 
      JObjT(FreeAp.pure(())), 
      (_: Unit) => User, 
      { 
        case User => Some(())
        case _ => None
      }
    ) ::
    Alt[Role, String](
      "user", 
      JObjT(FreeAp.lift(PropSchema("department", JStrT, identity))), 
      Administrator(_),
      { 
        case Administrator(dept) => Some(dept)
        case _ => None
      }
    ) :: Nil
  )
}
