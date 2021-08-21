import SampleActor.TestCase
import akka.actor.testkit.typed.scaladsl.ActorTestKit
import akka.actor.testkit.typed.scaladsl.ScalaTestWithActorTestKit
import akka.actor.typed.scaladsl.AskPattern.{Askable, schedulerFromActorSystem}
import akka.persistence.testkit.scaladsl.EventSourcedBehaviorTestKit
import com.typesafe.config.ConfigFactory
import org.scalatest.matchers.must.Matchers.convertToAnyMustWrapper
import org.scalatest.wordspec.AnyWordSpecLike
import org.scalatest.{BeforeAndAfterEach, GivenWhenThen}

import scala.concurrent.Await

class SampleActorSpec extends ScalaTestWithActorTestKit(ConfigFactory.parseString("akka.actor.allow-java-serialization = true").withFallback(EventSourcedBehaviorTestKit.config)) with AnyWordSpecLike with BeforeAndAfterEach with GivenWhenThen {

  private val eventSourcedTestKit = EventSourcedBehaviorTestKit[SampleActor.Command, SampleActor.Event, SampleActor.State](
    system, SampleActor("testId")
  )

  override protected def beforeEach(): Unit = {
    super.beforeEach()
    eventSourcedTestKit.clear()
  }

  "SampleActor#TestMe" should {
    "return TestSuccess for test case WORKS" in {
      Given("empty SampleActor")
      // do nothing - declared above

      When("TestMe message with testCase=WORKS is send")
      val result = eventSourcedTestKit.runCommand(ref => SampleActor.Commands.TestMe(TestCase.WORKS, ref))

      Then("actor replies with TestSuccess")
      result.reply mustBe theSameInstanceAs(SampleActor.Commands.TestMe.Results.TestSuccess)
    }

    /*
    This test fails with stacktrace:
    - should return TestSuccess for test case FAIL *** FAILED ***
        java.lang.AssertionError: Timeout (3 seconds) during receiveMessage while waiting for message.
    */
    "return TestSuccess for test case FAIL" in {
      Given("empty SampleActor")
      // do nothing - declared above

      When("TestMe message with testCase=FAIL is send")
      val result = eventSourcedTestKit.runCommand(ref => SampleActor.Commands.TestMe(TestCase.FAIL, ref))

      Then("actor replies with TestSuccess")
      result.reply mustBe theSameInstanceAs(SampleActor.Commands.TestMe.Results.TestSuccess)
    }
  }
}
