package com.hyecheon

import akka.actor.typed.Behavior
import akka.actor.typed.javadsl.AbstractBehavior
import akka.actor.typed.javadsl.ActorContext
import akka.actor.typed.javadsl.Behaviors
import akka.actor.typed.javadsl.Receive

class FirstSimpleBehavior(context: ActorContext<String>) :
    AbstractBehavior<String>(context) {

    override fun createReceive(): Receive<String> {
        return newReceiveBuilder()
            .onMessageEquals("say hello") {
                println("Hello")
                this
            }
            .onMessageEquals("who are you") {
                println("My path is ${context.self.path()}")
                this
            }
            .onMessageEquals("create a child") {
                val secondActor = context.spawn(create(), "secondActor")
                secondActor.tell("who are you")
                this
            }
            .onAnyMessage { message ->
                println("I received the message : $message")
                this
            }
            .build()
    }

    companion object {
        fun create(): Behavior<String> {
            return Behaviors.setup<String> { context ->
                FirstSimpleBehavior(context)
            }
        }
    }
}