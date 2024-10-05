package hiiragi283.ragium.api

import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.common.InternalRagiumAPI
import hiiragi283.ragium.common.advancement.HTBuiltMachineCriterion
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition
import net.minecraft.advancement.AdvancementCriterion
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

        val logger: Logger = LoggerFactory.getLogger(MOD_NAME)

        @JvmStatic
        inline fun log(action: Logger.() -> Unit) {
            logger.action()
        }

        @JvmStatic
        fun getInstance(): RagiumAPI = InternalRagiumAPI
    }

    val config: Config

    fun createBuiltMachineCriterion(
        machineType: HTMachineType,
        minTier: HTMachineTier,
    ): AdvancementCriterion<HTBuiltMachineCriterion.Condition>

    fun getHardModeCondition(isHard: Boolean): ResourceCondition

    //    Config    //

    @ApiStatus.NonExtendable
    interface Config {
        val isHardMode: Boolean
    }
}
