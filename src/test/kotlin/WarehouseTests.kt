import kotlinx.coroutines.runBlocking
import me.deotime.warehouse.properties.AbstractCollectionProperty
import kotlin.test.Test
import kotlin.test.assertEquals

class WarehouseTests {

    @Test
    fun `test list reordering`(): Unit = runBlocking {
        TestWarehouse.SomeList.clear()
        TestWarehouse.SomeList.add("testing", "something", "really", "cool")
        assert(TestWarehouse.SomeList.remove(2))
        assertEquals(3, TestWarehouse.SomeList.size())
        assertEquals("cool", TestWarehouse.SomeList.get(2))
    }

}