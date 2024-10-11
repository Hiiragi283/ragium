package hiiragi283.ragium.api

import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.machine.HTMachineTypeKey
import hiiragi283.ragium.api.property.HTPropertyHolder

@JvmDefaultWithCompatibility
interface HTMachineTypeInitializer {
    companion object {
        const val KEY = "ragium.machine_type"
    }

    val priority: Int

    fun registerType(register: Register) {}

    fun modifyProperties(key: HTMachineTypeKey, properties: HTPropertyHolder.Mutable) {}

    //    Register    //

    class Register(private val register: (HTMachineTypeKey, HTPropertyHolder, HTMachineType.Category) -> Unit) {
        fun registerGenerator(key: HTMachineTypeKey, builderAction: HTPropertyHolder.Mutable.() -> Unit) {
            register(
                key,
                HTPropertyHolder.create(builderAction = builderAction),
                HTMachineType.Category.GENERATOR,
            )
        }

        fun registerProcessor(key: HTMachineTypeKey, builderAction: HTPropertyHolder.Mutable.() -> Unit) {
            register(
                key,
                HTPropertyHolder.create(builderAction = builderAction),
                HTMachineType.Category.PROCESSOR,
            )
        }
    }
}
