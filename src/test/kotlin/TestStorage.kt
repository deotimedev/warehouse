import kotlinx.coroutines.runBlocking
import me.deotime.AppdataStorage
import me.deotime.property
import kotlin.test.Test

class TestStorageProperties {

    @Test
    fun testSingle() = runBlocking {

        println(TestStorage.Username.get())
    }

}

object TestStorage : AppdataStorage {
    override val name = "TestStorage"

    val Username by property("Unknown!")
}