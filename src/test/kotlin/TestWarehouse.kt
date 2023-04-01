import me.deotime.warehouse.list
import me.deotime.warehouse.provided.WindowsWarehouse

object TestWarehouse : WindowsWarehouse.Appdata {
    val SomeList by list<String>()
}