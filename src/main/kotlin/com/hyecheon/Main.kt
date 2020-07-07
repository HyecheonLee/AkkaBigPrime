package com.hyecheon

import akka.actor.typed.ActorSystem


/*
fun main() {
    val actorSystem = ActorSystem.create(FirstSimpleBehavior.create(), "FirstActorSystem")
    actorSystem.tell("say hello")
    actorSystem.tell("who are you")
    actorSystem.tell("create a child")
    actorSystem.tell("This is the second message.")
}
*/
fun main() {
    val bigPrimes = ActorSystem.create(ManagerBehavior.create(), "BigPrime")
    bigPrimes.tell(InstructionCommand("start"))
}
