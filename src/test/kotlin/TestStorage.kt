import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import me.deotime.AppdataStorage
import me.deotime.Storage
import me.deotime.collection
import me.deotime.property
import java.util.UUID
import kotlin.test.Test

class TestStorageProperties {

    @Test
    fun testSingle() = runBlocking {
        TestStorage.Items.toList().also { println("Current items: $it") }
        TestStorage.Items add "Carrot"
        TestStorage.Items add "Chocolate"
        TestStorage.Items add "Goat"
        TestStorage.Items add "Cheeseburger"
        TestStorage.Items.filter { it.startsWith("C") }.collect {
            println("Item that starts with C: $it")
        }
    }

}

object TestStorage : AppdataStorage {
    override val name = "TestStorage"

//    val Username by property("Anonymous")
//    val Level by property(0)
//
    val Items by collection<String>()
}