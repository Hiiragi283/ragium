package hiiragi283.ragium.api.event

import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.property.HTPropertyHolderBuilder
import net.neoforged.bus.api.Event
import net.neoforged.fml.event.IModBusEvent
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent
import java.util.function.Function

/**
 * [T]のキーに紐づいた[HTPropertyHolderBuilder]にアクセスするイベント
 *
 * [FMLConstructModEvent]中にフックされています。
 */
sealed class HTModifyPropertyEvent<T : Any>(private val function: Function<T, HTPropertyHolderBuilder>) :
    Event(),
    IModBusEvent {
    /**
     * 指定した[key]に紐づいた[HTPropertyHolderBuilder]を返します。
     */
    fun getBuilder(key: T): HTPropertyHolderBuilder = function.apply(key)

    /**
     * [HTMaterialKey]向け
     */
    class Material(function: Function<HTMaterialKey, HTPropertyHolderBuilder>) : HTModifyPropertyEvent<HTMaterialKey>(function)
}
