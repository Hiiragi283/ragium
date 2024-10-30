package hiiragi283.ragium.api

import hiiragi283.ragium.api.machine.HTMachineTypeKey
import hiiragi283.ragium.api.property.HTPropertyHolder

@JvmDefaultWithCompatibility
interface RagiumPlugin {
    companion object {
        const val KEY = "ragium.plugin"
    }

    val priority: Int

    fun shouldLoad(): Boolean = true

    fun registerMachineType(register: MachineRegister) {}

    fun modifyMachineProperties(helper: PropertyHelper) {}

    fun afterRagiumInit()

    //    MachineRegister    //

    class MachineRegister(private val register: (HTMachineTypeKey, HTPropertyHolder, Boolean) -> Unit) {
        fun registerGenerator(key: HTMachineTypeKey, builderAction: HTPropertyHolder.Mutable.() -> Unit) {
            register(
                key,
                HTPropertyHolder.create(builderAction = builderAction),
                true,
            )
        }

        fun registerProcessor(key: HTMachineTypeKey, builderAction: HTPropertyHolder.Mutable.() -> Unit) {
            register(
                key,
                HTPropertyHolder.create(builderAction = builderAction),
                false,
            )
        }
    }

    //    PropertyHelper    //

    class PropertyHelper(private val key: HTMachineTypeKey, private val properties: HTPropertyHolder.Mutable) {
        fun modify(key: HTMachineTypeKey, builderAction: HTPropertyHolder.Mutable.() -> Unit) {
            if (key == this.key) {
                properties.builderAction()
            }
        }
    }
}
