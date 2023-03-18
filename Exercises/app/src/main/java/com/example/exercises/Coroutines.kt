package com.example.exercises

import kotlinx.coroutines.*
import java.lang.System.currentTimeMillis

class Coroutines {
    companion object Functions {
        fun main() = runBlocking {
            launch { // launch a new coroutine and continue
                delay(1000L) // non-blocking delay for 1 second (default time unit is ms)
                println("World!") // print after delay
            }
            println("Hello") // main coroutine continues while a previous one is delayed
        }

        fun main2() = runBlocking {
            launch {
                doWorld()
            }
            println("Hello")
        }

        // this is your first suspending function
        private suspend fun doWorld() {
            delay(1000L)
            println("World!")
        }

        fun main3() = runBlocking {
            doWorld2()
        }

        private suspend fun doWorld2() = coroutineScope {
            launch {
                delay(1000)
                println("World!")
            }
            println("Hello")
        }

        // Executa sequencialmente doWorld seguido de "Done"
        fun main4() = runBlocking {
            doWorld3()
            println("Done")
        }

        // Executa simultaneamente ambas as seções
        private suspend fun doWorld3() = coroutineScope {
            launch {
                delay(2000)
                println("World 2")
            }
            launch {
                delay(1000)
                println("World 1")
            }
            println("Hello")
        }

        fun main5() = runBlocking {
            // Inicia uma nova co-rotina e mantém uma referência ao main thread
            val job = launch {
                delay(1000)
                println("World!")
            }
            println("Hello")
            // Espera até que a co-rotina filha seja concluída
            job.join()
            println("Done")
        }

        fun main6() = runBlocking {
            val deferred: Deferred<Int> = async {
                loadData()
            }
            println("waiting...")
            // Espera a função terminar para mostrar o retorno
            println(deferred.await())
        }

        private suspend fun loadData(): Int {
            println("loading...")
            delay(1000L)
            println("loaded!")
            return 42
        }

        fun main7() = runBlocking {
            val deferreds: List<Deferred<Int>> = (1..3).map {
                async {
                    delay(1000L * it)
                    println("Loading $it")
                    it
                }
            }
            val sum = deferreds.awaitAll().sum()
            println("$sum")
        }

        fun main8() = runBlocking {
            val job = launch {
                repeat(1000) { i ->
                    println("job: I'm sleeping $i ...")
                    delay(500L)
                }
            }
            delay(1300L) // delay a bit
            println("main: I'm tired of waiting!")
            job.cancel() // cancels the job
            job.join() // waits for job's completion
            println("main: Now I can quit.")
        }

        fun main9() = runBlocking {
            val startTime = currentTimeMillis()
            val job = launch(Dispatchers.Default) {
                var nextPrintTime = startTime
                var i = 0
                while (i < 5) { // computation loop, just wastes CPU
                    // print a message twice a second
                    if (currentTimeMillis() >= nextPrintTime) {
                        println("job: I'm sleeping ${i++} ...")
                        nextPrintTime += 500L
                    }
                }
            }
            delay(1300L) // delay a bit
            println("main: I'm tired of waiting!")
            job.cancelAndJoin() // cancels the job and waits for its completion
            println("main: Now I can quit.")
        }

        fun main10() = runBlocking {
            val startTime = currentTimeMillis()
            val job = launch(Dispatchers.Default) {
                var nextPrintTime = startTime
                var i = 0
                while (isActive) { // cancellable computation loop
                    // print a message twice a second
                    if (currentTimeMillis() >= nextPrintTime) {
                        println("job: I'm sleeping ${i++} ...")
                        nextPrintTime += 500L
                    }
                }
            }
            delay(1300L) // delay a bit
            println("main: I'm tired of waiting!")
            job.cancelAndJoin() // cancels the job and waits for its completion
            println("main: Now I can quit.")
        }

        fun main11() = runBlocking {
            val job = launch {
                try {
                    repeat(1000) { i ->
                        println("job: I'm sleeping $i ...")
                        delay(500L)
                    }
                } finally {
                    println("job: I'm running finally")
                }
            }
            delay(1300L) // delay a bit
            println("main: I'm tired of waiting!")
            job.cancelAndJoin() // cancels the job and waits for its completion
            println("main: Now I can quit.")
        }

        fun main12() = runBlocking {
            val job = launch {
                try {
                    repeat(1000) { i ->
                        println("job: I'm sleeping $i ...")
                        delay(500L)
                    }
                } finally {
                    withContext(NonCancellable) {
                        println("job: I'm running finally")
                        delay(1000L)
                        println("job: And I've just delayed for 1 sec because I'm non-cancellable")
                    }
                }
            }
            delay(1300L) // delay a bit
            println("main: I'm tired of waiting!")
            job.cancelAndJoin() // cancels the job and waits for its completion
            println("main: Now I can quit.")
        }

        fun main13() = runBlocking {
            withTimeout(1300L) {
                repeat(1000) { i ->
                    println("I'm sleeping $i ...")
                    delay(500L)
                }
            }
        }

        fun main14() = runBlocking {
            val result = withTimeoutOrNull(1300L) {
                repeat(1000) { i ->
                    println("I'm sleeping $i ...")
                    delay(500L)
                }
                "Done" // will get cancelled before it produces this result
            }
            println("Result is $result")
        }

        var acquired1 = 0

        class Resource1 {
            init { acquired1++ } // Acquire the resource
            fun close() { acquired1-- } // Release the resource
        }

        fun main15() {
            runBlocking {
                repeat(10_000) { // Launch 10K coroutines
                    launch {
                        val resource = withTimeout(60) { // Timeout of 60 ms
                            delay(50) // Delay for 50 ms
                            Resource1() // Acquire a resource and return it from withTimeout block
                        }
                        resource.close() // Release the resource
                    }
                }
            }
            // Outside of runBlocking all coroutines have completed
            println(acquired1) // Print the number of resources still acquired
        }

        var acquired2 = 0

        class Resource2 {
            init { acquired2++ } // Acquire the resource
            fun close() { acquired2-- } // Release the resource
        }

        fun main16() {
            runBlocking {
                repeat(10_000) { // Launch 10K coroutines
                    launch {
                        var resource: Resource2? = null // Not acquired yet
                        try {
                            withTimeout(60) { // Timeout of 60 ms
                                delay(50) // Delay for 50 ms
                                resource = Resource2() // Store a resource to the variable if acquired
                            }
                            // We can do something else with the resource here
                        } finally {
                            resource?.close() // Release the resource if it was acquired
                        }
                    }
                }
            }
            // Outside of runBlocking all coroutines have completed
            println(acquired2) // Print the number of resources still acquired
        }
    }
}