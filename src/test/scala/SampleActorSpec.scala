import SampleActor.TestCase
import akka.actor.testkit.typed.scaladsl.ActorTestKit
import akka.actor.testkit.typed.scaladsl.ScalaTestWithActorTestKit
import akka.persistence.testkit.scaladsl.EventSourcedBehaviorTestKit
import com.typesafe.config.ConfigFactory
import org.scalatest.matchers.must.Matchers.convertToAnyMustWrapper
import org.scalatest.wordspec.AnyWordSpecLike
import org.scalatest.{BeforeAndAfterEach, GivenWhenThen}

class SampleActorSpec extends ScalaTestWithActorTestKit(ConfigFactory.parseString("akka.actor.allow-java-serialization = true").withFallback(EventSourcedBehaviorTestKit.config)) with AnyWordSpecLike with BeforeAndAfterEach with GivenWhenThen {

  private val eventSourcedTestKit = EventSourcedBehaviorTestKit[SampleActor.Command, SampleActor.Event, SampleActor.State](
    system, SampleActor("testId")
  )

  override protected def beforeEach(): Unit = {
    super.beforeEach()
    eventSourcedTestKit.clear()
  }

  "SampleActor#TestMe" should {
    "return TestSuccess for test case TEST_CASE_1" in {
      Given("empty SampleActor")
      // do nothing - declared above

      When("TestMe message with testCase=TEST_CASE_1 is send")
      val result = eventSourcedTestKit.runCommand(ref => SampleActor.Commands.TestMe(TestCase.TEST_CASE_1, ref))

      Then("actor replies with TestSuccess")
      result.reply mustBe theSameInstanceAs(SampleActor.Commands.TestMe.Results.TestSuccess)
    }

    /*
    This test fails with stacktrace:
    - should return TestSuccess for test case TEST_CASE_2 *** FAILED ***
        java.lang.AssertionError: Timeout (3 seconds) during receiveMessage while waiting for message.
    */
//    "return TestSuccess for test case TEST_CASE_2 (this test fails)" in {
//      Given("empty SampleActor")
//      // do nothing - declared above
//
//      When("TestMe message with testCase=TEST_CASE_2 is send")
//      val result = eventSourcedTestKit.runCommand(ref => SampleActor.Commands.TestMe(TestCase.TEST_CASE_2, ref))
//
//      Then("actor replies with TestSuccess")
//      result.reply mustBe theSameInstanceAs(SampleActor.Commands.TestMe.Results.TestSuccess)
//    }

    "return TestSuccess for test case TEST_CASE_2" in {
      Given("empty SampleActor")
      val actorTestKit = ActorTestKit(system)
      val ref = actorTestKit.spawn(SampleActor("testId2"))

      When("TestMe message with testCase=TEST_CASE_2 is send")
      val probeRef = actorTestKit.createTestProbe[SampleActor.Commands.TestMe.Result]()
      ref ! SampleActor.Commands.TestMe(TestCase.TEST_CASE_2, probeRef.ref)

      Then("actor replies with TestSuccess")
      probeRef.expectMessage(SampleActor.Commands.TestMe.Results.TestSuccess)
    }
  }
}
