import co.q64.raindrop.annotation.Generate
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import me.deotime.warehouse.AppdataStorage
import me.deotime.warehouse.map
import me.deotime.warehouse.property
import me.deotime.warehouse.provided.WindowsStorage
import java.util.UUID
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.random.Random
import kotlin.test.Test

class TestStorageProperties {

    @Test
    fun testSingle() = runBlocking {
        println("Old username: ${TestStorage.Username.get()}")
        TestStorage.Username set UUID.randomUUID().toString()
        println("New username: ${TestStorage.Username.get()}")
    }

    @Test
    fun testMap() = runBlocking {
        TestStorage.Items.collect { println("Old Item: $it") }
        TestStorage.Items.set("Burger", Random.nextInt(1, 20))
        TestStorage.Items.set("Waffle", Random.nextInt(1, 20))
        TestStorage.Items.set("Cake", Random.nextInt(1, 20))
        println("Amount of burger: ${TestStorage.Items.get("Burger")}")
        println("Amount of waffle: ${TestStorage.Items.get("Waffle")}")
        println("Amount of cake: ${TestStorage.Items.get("Cake")}")
    }


    @Test
    fun testOptics() = runBlocking {

    }
}

object TestStorage : WindowsStorage.Appdata {
    override val name = "TestStorage"

    val Username by property("Unknown!")
    val Items by map<String, Int>()
    val Complex by property(Complex())


}

@Serializable
@Generate.Optics
@Generate.Empty
data class Complex(
    val name: String = "Name",
    val data: SomeData = SomeData()
) {
    @Serializable
    @Generate.Optics
    @Generate.Empty
    data class SomeData(
        val amount: Int = 0,
        val data: String = ""
    ) {
        companion object
    }

    companion object
}