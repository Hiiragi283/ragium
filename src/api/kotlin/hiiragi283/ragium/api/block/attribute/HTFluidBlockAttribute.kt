package hiiragi283.ragium.api.block.attribute

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.text.HTHasTranslationKey
import net.minecraft.Util
import net.minecraft.util.StringRepresentable
import java.util.function.IntSupplier

@JvmRecord
data class HTFluidBlockAttribute(private val tankMap: Map<TankType, IntSupplier>) : HTBlockAttribute {
    private fun getTankCapacity(type: TankType): IntSupplier = tankMap[type] ?: error("Undefined tank capacity for ${type.serializedName}")

    fun getInputTank(): IntSupplier = getTankCapacity(TankType.INPUT)

    fun getOutputTank(): IntSupplier = getTankCapacity(TankType.OUTPUT)

    fun getFirstInputTank(): IntSupplier = getTankCapacity(TankType.FIRST_INPUT)

    fun getSecondInputTank(): IntSupplier = getTankCapacity(TankType.SECOND_INPUT)

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
