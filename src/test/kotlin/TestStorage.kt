import co.q64.raindrop.annotation.Generate
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import me.deotime.AppdataStorage
import me.deotime.property
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.test.Test

class TestStorageProperties {

    @Test
    fun testSingle() = runBlocking {
        println(TestStorage.Username.get())
    }


    @Test
    fun testOptics() = runBlocking {

    }
}

object TestStorage : AppdataStorage {
    override val name = "TestStorage"

    val Username by property("Unknown!")
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