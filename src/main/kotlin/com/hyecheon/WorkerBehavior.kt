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

    class Command(
        val message: String,
        val sender: ActorRef<com.hyecheon.Command>
    ) : Serializable {
        companion object {
            private const val serialVersionUID = 1L
        }
    }

    override fun createReceive(): Receive<Command> {
        return newReceiveBuilder()
            .onAnyMessage { command ->
                when (command.message) {
                    "start" -> {
                        val bigInteger = BigInteger(2000, Random())
                        command.sender.tell(ResultCommand(bigInteger.nextProbablePrime()))
                    }
                }
                this
            }
            .build()
    }

    companion object {
        fun create(): Behavior<Command> {
            return Behaviors.setup { WorkerBehavior(it) }
        }
    }

}