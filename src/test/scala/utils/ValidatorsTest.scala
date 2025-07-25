package utils

import models._
import org.scalatest.funsuite.AnyFunSuite

class ValidatorsTest extends AnyFunSuite {

  test("User validation should fail for invalid UserId") {
    val user = Member("John Doe", UserId(-1))

    val result = Validators.validateUser(user)

    assert(result.isLeft)
  }

  test("User validation should pass for valid UserId") {
    val user = Member("John Doe", UserId(42))

    val result = Validators.validateUser(user)

    assert(result.isRight)
  }
}
