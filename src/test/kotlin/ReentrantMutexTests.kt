import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull
import me.deotime.warehouse.util.ReentrantMutex
import org.junit.Test
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class ReentrantMutexTests {

    private val mutex = ReentrantMutex()

    @Test
    fun `reentrant mutex doesn't deadlock`() = runBlocking {
        withTimeoutOrNull((10).seconds) {
            mutex.withLock {
                println("about to call other")
                delay(250)
                other()
                println("did not deadlock")
            }
        } ?: error("Reentrant mutex deadlocked.")
    }

    @Test
    fun `reentrant mutex waits`(): Unit = runBlocking {
        launch {
            mutex.withLock {
                println("using mutex")
                delay((3).seconds)
                println("done using mutex")
            }
        }
        launch {
            delay((100).milliseconds)
            println("trying to access locked mutex now")
            withTimeoutOrNull((1).seconds) {
                mutex.withLock {
                    error("Reentrant mutex did not wait.")
                }
            }
            println("waited correctly")
        }
    }

    private suspend fun other() {
        mutex.withLock {
            println("doing something")
            delay(500)
            println("done")
        }
    }
}