package hiiragi283.ragium.api.block.attribute

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.entity.HTBlockEntityWithUpgrade
import hiiragi283.ragium.api.item.component.HTMachineUpgrade
import hiiragi283.ragium.api.math.times
import hiiragi283.ragium.api.text.HTHasTranslationKey
import net.minecraft.Util
import net.minecraft.util.StringRepresentable
import java.util.function.IntSupplier

@JvmRecord
data class HTFluidBlockAttribute(private val tankMap: Map<TankType, IntSupplier>) : HTBlockAttribute {
    private fun getTankCapacity(type: TankType, upgrade: HTBlockEntityWithUpgrade): IntSupplier {
        val baseCapacity: IntSupplier = tankMap[type] ?: error("Undefined tank capacity for ${type.serializedName}")
        return IntSupplier { upgrade.modifyValue(HTMachineUpgrade.Key.FLUID_CAPACITY) { baseCapacity.asInt * it } }
    }

    fun getInputTank(upgrade: HTBlockEntityWithUpgrade): IntSupplier = getTankCapacity(TankType.INPUT, upgrade)

    fun getOutputTank(upgrade: HTBlockEntityWithUpgrade): IntSupplier = getTankCapacity(TankType.OUTPUT, upgrade)

    fun getFirstInputTank(upgrade: HTBlockEntityWithUpgrade): IntSupplier = getTankCapacity(TankType.FIRST_INPUT, upgrade)

    fun getSecondInputTank(upgrade: HTBlockEntityWithUpgrade): IntSupplier = getTankCapacity(TankType.SECOND_INPUT, upgrade)

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
