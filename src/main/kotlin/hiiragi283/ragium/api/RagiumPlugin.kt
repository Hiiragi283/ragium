package hiiragi283.ragium.api

import hiiragi283.ragium.api.content.RagiumMaterials
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTypeNew
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.property.HTMutablePropertyHolder
import java.util.function.BiConsumer
import java.util.function.Predicate

@JvmDefaultWithCompatibility
interface RagiumPlugin {
    companion object {
        const val KEY = "ragium.plugin"
    }

    val priority: Int

    fun shouldLoad(): Boolean = true

    fun registerMachineType(register: BiConsumer<HTMachineKey, HTMachineTypeNew>) {}

    fun registerMaterial(register: BiConsumer<HTMaterialKey, RagiumMaterials.Type>) {}

    fun setupCommonMachineProperties(helper: PropertyHelper<HTMachineKey>) {}

    fun setupClientMachineProperties(helper: PropertyHelper<HTMachineKey>) {}

    fun setupCommonMaterialProperties(helper: PropertyHelper<HTMaterialKey>) {}

    fun setupClientMaterialProperties(helper: PropertyHelper<HTMaterialKey>) {}

    fun afterRagiumInit(instance: RagiumAPI)

    //    PropertyHelper    //

    class PropertyHelper<T : Any>(private val key: T, private val properties: HTMutablePropertyHolder) {
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
