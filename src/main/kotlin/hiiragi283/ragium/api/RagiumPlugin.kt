package hiiragi283.ragium.api

import hiiragi283.ragium.api.machine.HTMachine
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTypeNew
import hiiragi283.ragium.api.property.HTMutablePropertyHolder
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

    fun afterRagiumInit(instance: RagiumAPI)

    //    MachineRegister    //

    class MachineRegister(private val register: (HTMachineKey, HTMachineTypeNew) -> Unit) {
        fun registerConsumer(key: HTMachineKey) {
            register(key, HTMachineTypeNew.CONSUMER)
        }

        fun registerGenerator(key: HTMachineKey) {
            register(key, HTMachineTypeNew.GENERATOR)
        }

        fun registerProcessor(key: HTMachineKey) {
            register(key, HTMachineTypeNew.PROCESSOR)
        }
    }

    //    PropertyHelper    //

    class PropertyHelper(private val key: HTMachineKey, private val properties: HTMutablePropertyHolder) {
        fun modify(type: HTMachine, builderAction: HTMutablePropertyHolder.() -> Unit) {
            modify(type.key, builderAction)
        }

        fun modify(key: HTMachineKey, builderAction: HTMutablePropertyHolder.() -> Unit) {
            modify({ it == key }, builderAction)
        }

        fun modify(filter: Predicate<HTMachineKey>, builderAction: HTMutablePropertyHolder.() -> Unit) {
            if (filter.test(this.key)) {
                properties.builderAction()
            }
        }
    }
}
