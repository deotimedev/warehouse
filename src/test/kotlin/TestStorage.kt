import kotlinx.coroutines.runBlocking
import me.deotime.AppdataStorage
import kotlin.test.Test

class TestStorageProperties {

    @Test
    fun testSingle() = runBlocking {

    }

}

object TestStorage : AppdataStorage {
    override val name = "TestStorage"
}