package com.hyecheon

import akka.actor.typed.Behavior
import akka.actor.typed.javadsl.AbstractBehavior
import akka.actor.typed.javadsl.ActorContext
import akka.actor.typed.javadsl.Behaviors
import akka.actor.typed.javadsl.Receive
import java.io.Serializable
import java.math.BigInteger

class ManagerBehavior(context: ActorContext<Command>) : AbstractBehavior<Command>(context) {

    private val primes = sortedSetOf<BigInteger>()
    override fun createReceive(): Receive<Command> {
        return newReceiveBuilder()
            .onAnyMessage { command ->
                when (command) {
                    is InstructionCommand -> {
                        if (command.message == "start") {
                            for (i in 0..20) {
                                val worker = context.spawn(WorkerBehavior.create(), "worker-$i")
                                worker.tell(WorkerBehavior.Command("start", context.self))
                            }
                        }
                    }
                    is ResultCommand -> {
                        primes.add(command.prime)
                        println("I have received ${primes.size} prime numbers")
                        if (primes.size == 20) {
                            primes.forEach { println(it) }
                        }
                    }
                }
                this
            }
            .build()
    }

    companion object {
        fun create(): Behavior<Command> = let {
            Behaviors.setup<Command> { ManagerBehavior(it) }
        }
    }

}

sealed class Command : Serializable {
    companion object {
        private const val serialVersionUID = 1L
    }
}

data class InstructionCommand(val message: String) : Command()
data class ResultCommand(val prime: BigInteger) : Command()