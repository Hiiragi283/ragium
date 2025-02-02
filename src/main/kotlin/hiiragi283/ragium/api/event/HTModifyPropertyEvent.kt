package hiiragi283.ragium.api.event

import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.property.HTPropertyHolderBuilder
import net.neoforged.bus.api.Event
import net.neoforged.fml.event.IModBusEvent
import java.util.function.Function

sealed class HTModifyPropertyEvent<T : Any>(private val function: Function<T, HTPropertyHolderBuilder>) :
    Event(),
    IModBusEvent {
    fun getBuilder(key: T): HTPropertyHolderBuilder = function.apply(key)

    class Machine(function: Function<HTMachineKey, HTPropertyHolderBuilder>) : HTModifyPropertyEvent<HTMachineKey>(function)

    class Material(function: Function<HTMaterialKey, HTPropertyHolderBuilder>) : HTModifyPropertyEvent<HTMaterialKey>(function)
}
