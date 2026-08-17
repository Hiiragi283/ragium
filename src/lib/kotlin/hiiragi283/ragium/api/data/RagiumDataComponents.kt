package hiiragi283.ragium.api.data

import hiiragi283.lib.data.DataComponentType
import hiiragi283.lib.item.alchemy.HTBottleType
import net.minecraft.core.component.DataComponentType
import net.neoforged.neoforge.fluids.FluidStackTemplate

data object RagiumDataComponents {
    @JvmField
    val BOTTLE_TYPE: DataComponentType<HTBottleType> = DataComponentType(HTBottleType.CODEC, HTBottleType.STREAM_CODEC)

    @JvmField
    val FLUID: DataComponentType<FluidStackTemplate> = DataComponentType(FluidStackTemplate.CODEC, FluidStackTemplate.STREAM_CODEC)
}
