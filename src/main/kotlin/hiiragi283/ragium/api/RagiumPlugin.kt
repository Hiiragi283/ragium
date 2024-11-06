package hiiragi283.ragium.api

import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.property.HTMutablePropertyHolder
import net.minecraft.item.Item
import java.util.function.BiConsumer
import java.util.function.Predicate

@JvmDefaultWithCompatibility
interface RagiumPlugin {
    companion object {
        const val KEY = "ragium.plugin"
    }

    val priority: Int

    fun shouldLoad(): Boolean = true

    fun registerMachineType(consumer: BiConsumer<HTMachineKey, HTMachineType>) {}

    fun registerMaterial(consumer: BiConsumer<HTMaterialKey, HTMaterialKey.Type>) {}

    fun setupCommonMachineProperties(helper: PropertyHelper<HTMachineKey>) {}

    fun setupClientMachineProperties(helper: PropertyHelper<HTMachineKey>) {}

    fun setupCommonMaterialProperties(helper: PropertyHelper<HTMaterialKey>) {}

    fun setupClientMaterialProperties(helper: PropertyHelper<HTMaterialKey>) {}

    fun bindMaterialToItem(consumer: (HTTagPrefix, HTMaterialKey, Item) -> Unit) {}

    fun afterRagiumInit(instance: RagiumAPI)

    //    PropertyHelper    //

    class PropertyHelper<T : Any>(private val key: T, private val properties: HTMutablePropertyHolder) {
        fun modify(vararg keys: T, builderAction: HTMutablePropertyHolder.() -> Unit) {
            modify(keys::contains, builderAction)
        }

        fun modify(key: T, builderAction: HTMutablePropertyHolder.() -> Unit) {
            modify({ it == key }, builderAction)
        }

        fun modify(filter: Predicate<T>, builderAction: HTMutablePropertyHolder.() -> Unit) {
            if (filter.test(this.key)) {
                properties.builderAction()
            }
        }
    }
}
