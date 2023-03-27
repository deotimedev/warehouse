# warehouse

A fast, thread-safe, couroutine based local storage tool.

```kotlin
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

    // Use a ilst of items
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
```
