package hiiragi283.ragium.api

import hiiragi283.ragium.api.machine.HTMachineConvertible
import hiiragi283.ragium.api.machine.HTMachineTypeKey
import hiiragi283.ragium.api.property.HTMutablePropertyHolder
import net.fabricmc.fabric.api.util.TriState
import java.util.function.Predicate

@JvmDefaultWithCompatibility
interface RagiumPlugin {
    companion object {
        const val KEY = "ragium.plugin"
    }

    val priority: Int

    fun shouldLoad(): Boolean = true

    fun registerMachineType(register: MachineRegister) {}

    fun setupCommonMachineProperties(helper: PropertyHelper) {}

    fun setupClientMachineProperties(helper: PropertyHelper) {}

    fun afterRagiumInit()

    //    MachineRegister    //

    class MachineRegister(private val register: (HTMachineTypeKey, TriState) -> Unit) {
        fun registerConsumer(key: HTMachineTypeKey) {
            register(key, TriState.DEFAULT)
        }

        fun registerGenerator(key: HTMachineTypeKey) {
            register(key, TriState.TRUE)
        }

        fun registerProcessor(key: HTMachineTypeKey) {
            register(key, TriState.FALSE)
        }
    }

    //    PropertyHelper    //

    class PropertyHelper(private val key: HTMachineTypeKey, private val properties: HTMutablePropertyHolder) {
        fun modify(type: HTMachineConvertible, builderAction: HTMutablePropertyHolder.() -> Unit) {
            modify(type.key, builderAction)
        }

        fun modify(key: HTMachineTypeKey, builderAction: HTMutablePropertyHolder.() -> Unit) {
            modify({ it == key }, builderAction)
        }

        fun modify(filter: Predicate<HTMachineTypeKey>, builderAction: HTMutablePropertyHolder.() -> Unit) {
            if (filter.test(this.key)) {
                properties.builderAction()
            }
        }
    }
}
