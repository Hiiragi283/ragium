package hiiragi283.ragium.api

import hiiragi283.ragium.api.machine.HTMachineConvertible
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineTypeRegistry
import hiiragi283.ragium.common.InternalRagiumAPI
import hiiragi283.ragium.common.advancement.HTBuiltMachineCriterion
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition
import net.minecraft.advancement.AdvancementCriterion
import net.minecraft.fluid.Fluid
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import org.jetbrains.annotations.ApiStatus
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@ApiStatus.NonExtendable
interface RagiumAPI {
    companion object {
        const val MOD_ID = "ragium"
        const val MOD_NAME = "Ragium"

        @JvmStatic
        fun id(path: String): Identifier = Identifier.of(MOD_ID, path)

        @JvmStatic
        val logger: Logger = LoggerFactory.getLogger(MOD_NAME)

        @JvmStatic
        inline fun log(action: Logger.() -> Unit) {
            logger.action()
        }

        @JvmStatic
        fun getInstance(): RagiumAPI = InternalRagiumAPI
    }

    val config: Config
    val machineTypeRegistry: HTMachineTypeRegistry

    fun createBuiltMachineCriterion(
        type: HTMachineConvertible,
        minTier: HTMachineTier,
    ): AdvancementCriterion<HTBuiltMachineCriterion.Condition>

    fun createFilledCube(fluid: Fluid, count: Int = 1): ItemStack

    fun getHardModeCondition(isHard: Boolean): ResourceCondition

    fun registerIntegration(action: () -> Unit)

    //    Config    //

    @ApiStatus.NonExtendable
    interface Config {
        val isHardMode: Boolean
    }
}
