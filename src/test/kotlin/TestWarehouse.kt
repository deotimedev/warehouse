import co.q64.raindrop.annotation.Generate
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import me.deotime.warehouse.list
import me.deotime.warehouse.map
import me.deotime.warehouse.property
import me.deotime.warehouse.provided.WindowsWarehouse
import java.util.UUID
import kotlin.random.Random
import kotlin.test.Test

object TestWarehouse : WindowsWarehouse.Appdata /* Stores files in AppData directory */ {
    override val name = "TestWarehouse"

    val Username by property(default = "UnnamedUser")
    val Friends by list<String>()
    val Items by map<String, Int>()
}

suspend fun main() {
    // Get and set a property
    println("Old username: ${TestWarehouse.Username()}")
    TestWarehouse.Username set UUID.randomUUID().toString()
    println("New username: ${TestWarehouse.Username()}")

    // Use a list of friends
    println("${TestWarehouse.Username()} has ${TestWarehouse.Friends.size()} friends")
    println("They are:")
    TestWarehouse.Friends.collect { println(" - $it") }
    val newFriend = listOf("joe", "bob", "sally").random()
    println("Made a new friend: $newFriend!")
    TestWarehouse.Friends += newFriend

    // Dynamic map of items
    TestWarehouse.Items.collect { println("Old Item: $it") }
    TestWarehouse.Items.set("Burger", Random.nextInt(1, 20))
    TestWarehouse.Items.set("Waffle", Random.nextInt(1, 20))
    TestWarehouse.Items.set("Cake", Random.nextInt(1, 20))
    println("Amount of burger: ${TestWarehouse.Items.get("Burger")}")
    println("Amount of waffle: ${TestWarehouse.Items.get("Waffle")}")
    println("Amount of cake: ${TestWarehouse.Items.get("Cake")}")

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