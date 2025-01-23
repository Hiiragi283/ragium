package hiiragi283.ragium.api.material

import hiiragi283.ragium.api.property.HTPropertyHolderBuilder
import net.neoforged.bus.api.Event
import net.neoforged.fml.event.IModBusEvent
import java.util.function.Function

class HTModifyPropertyEvent internal constructor(private val function: Function<HTMaterialKey, HTPropertyHolderBuilder>) :
    Event(),
    IModBusEvent {
        fun getBuilder(key: HTMaterialKey): HTPropertyHolderBuilder = function.apply(key)
    }
