package hiiragi283.ragium.api.material

import net.neoforged.bus.api.Event
import net.neoforged.fml.event.IModBusEvent
import java.util.function.BiConsumer

class HTRegisterMaterialEvent internal constructor(private val consumer: BiConsumer<HTMaterialKey, HTMaterialType>) :
    Event(),
    IModBusEvent {
        fun register(key: HTMaterialKey, type: HTMaterialType) {
            consumer.accept(key, type)
        }
    }
