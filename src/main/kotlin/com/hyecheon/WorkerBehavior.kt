package com.hyecheon

import akka.actor.typed.ActorRef
import akka.actor.typed.Behavior
import akka.actor.typed.javadsl.AbstractBehavior
import akka.actor.typed.javadsl.ActorContext
import akka.actor.typed.javadsl.Behaviors
import akka.actor.typed.javadsl.Receive
import java.io.Serializable
import java.math.BigInteger
import java.util.*

class WorkerBehavior(context: ActorContext<Command>) : AbstractBehavior<WorkerBehavior.Command>(context) {

    private lateinit var prime: BigInteger

    class Command(val message: String, val sender: ActorRef<com.hyecheon.Command>) : Serializable {
        companion object {
            private const val serialVersionUID = 1L
        }
    }

    override fun createReceive(): Receive<Command> {
        return handleMessagesWhenWeDontYetHaveAPrimeNumber()
    }

    private fun handleMessagesWhenWeDontYetHaveAPrimeNumber(): Receive<Command> {
        return newReceiveBuilder()
            .onAnyMessage { command ->
                val bigInteger = BigInteger(2000, Random())
                prime = bigInteger.nextProbablePrime()
                command.sender.tell(ResultCommand(prime))
                handleMessagesWhenWeAlreadyHaveAPrimeNumber(prime)
            }
            .build()
    }

    private fun handleMessagesWhenWeAlreadyHaveAPrimeNumber(prime: BigInteger): Receive<Command> {
        return newReceiveBuilder()
            .onAnyMessage {
                if (it.message == "start") {
                    it.sender.tell(ResultCommand(prime = prime));
                }
                Behaviors.same()
            }
            .build()
    }

    companion object {
        fun create(): Behavior<Command> {
            return Behaviors.setup { WorkerBehavior(it) }
        }
    }

}