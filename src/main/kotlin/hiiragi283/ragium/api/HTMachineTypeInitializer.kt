package hiiragi283.ragium.api

import hiiragi283.ragium.common.machine.HTMachineConvertible
import java.util.function.Consumer

fun interface HTMachineTypeInitializer {
    fun registerType(register: Consumer<HTMachineConvertible>)

    companion object {
        const val KEY = "ragium.machine_type"
    }
}
