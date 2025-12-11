package hiiragi283.ragium.api.block.attribute

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.text.HTHasTranslationKey
import hiiragi283.ragium.api.upgrade.HTUpgradeHandler
import hiiragi283.ragium.api.upgrade.HTUpgradeHelper
import net.minecraft.Util
import net.minecraft.util.StringRepresentable
import java.util.function.IntSupplier

@JvmRecord
data class HTFluidBlockAttribute(private val tankMap: Map<TankType, IntSupplier>) : HTBlockAttribute {
    private fun getTankCapacity(type: TankType, handler: HTUpgradeHandler): IntSupplier {
        val baseCapacity: IntSupplier = tankMap[type] ?: error("Undefined tank capacity for ${type.serializedName}")
        return IntSupplier { HTUpgradeHelper.getTankCapacity(handler, baseCapacity.asInt) }
    }

    fun getInputTank(handler: HTUpgradeHandler): IntSupplier = getTankCapacity(TankType.INPUT, handler)

    fun getOutputTank(handler: HTUpgradeHandler): IntSupplier = getTankCapacity(TankType.OUTPUT, handler)

    fun getFirstInputTank(handler: HTUpgradeHandler): IntSupplier = getTankCapacity(TankType.FIRST_INPUT, handler)

    fun getSecondInputTank(handler: HTUpgradeHandler): IntSupplier = getTankCapacity(TankType.SECOND_INPUT, handler)

    enum class TankType :
        StringRepresentable,
        HTHasTranslationKey {
        INPUT,
        OUTPUT,
        FIRST_INPUT,
        SECOND_INPUT,
        ;

        override val translationKey: String =
            Util.makeDescriptionId("config", RagiumAPI.id("${serializedName}_tank_capacity"))

        override fun getSerializedName(): String = name.lowercase()
    }
}
