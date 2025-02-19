package hiiragi283.ragium.api.event

import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialType
import net.neoforged.bus.api.Event
import net.neoforged.fml.event.IModBusEvent
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent
import java.util.function.BiConsumer

/**
 * [HTMaterialKey]を登録するイベント
 *
 * [FMLConstructModEvent]中にフックされています。
 */
class HTRegisterMaterialEvent(private val consumer: BiConsumer<HTMaterialKey, HTMaterialType>) :
    Event(),
    IModBusEvent {
    /**
     * 指定した[key]に[type]を登録します。
     */
    fun register(key: HTMaterialKey, type: HTMaterialType) {
        consumer.accept(key, type)
    }
}
