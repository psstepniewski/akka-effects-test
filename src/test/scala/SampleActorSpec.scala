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
    "return TestSuccess if TestMe flag is set" in {
      Given("empty SampleActor")
      // do nothing - declared above

      When("TestMe message with flag 'isSuccess = true' is send")
      val result = eventSourcedTestKit.runCommand(ref => SampleActor.Commands.TestMe(isSuccess = true, ref))

      Then("actor replies with TestSuccess")
      result.reply mustBe theSameInstanceAs(SampleActor.Commands.TestMe.Results.TestSuccess)
    }

    "return TestSuccess if TestMe flag is not set" in {
      Given("empty SampleActor")
      // do nothing - declared above

      When("TestMe message with flag 'isSuccess = false' is send")
      val result = eventSourcedTestKit.runCommand(ref => SampleActor.Commands.TestMe(isSuccess = false, ref))

      Then("actor replies with TestFail")
      result.reply mustBe theSameInstanceAs(SampleActor.Commands.TestMe.Results.TestFail)
    }
  }
}
