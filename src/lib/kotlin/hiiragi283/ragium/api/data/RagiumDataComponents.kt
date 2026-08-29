package hiiragi283.ragium.api.data

import hiiragi283.lib.data.DataComponentType
import hiiragi283.lib.item.alchemy.HTBottleType
import hiiragi283.lib.serialization.codec.HTCodecs
import net.minecraft.core.component.DataComponentType
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.world.item.ItemStackTemplate
import net.neoforged.neoforge.fluids.SimpleFluidContent

/**
 * Ragiumで使用される[DataComponentType]をまとめたクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
data object RagiumDataComponents {
    @JvmField
    val BOTTLE_TYPE: DataComponentType<HTBottleType> = DataComponentType(HTBottleType.CODEC, HTBottleType.STREAM_CODEC)

    @JvmField
    val ENERGY: DataComponentType<Int> = DataComponentType(HTCodecs.NON_NEGATIVE_INT, ByteBufCodecs.VAR_INT)

    @JvmField
    val FLUID: DataComponentType<SimpleFluidContent> = DataComponentType(SimpleFluidContent.CODEC, SimpleFluidContent.STREAM_CODEC)

    @JvmField
    val MEMORY_DISC_DATA: DataComponentType<ItemStackTemplate> = DataComponentType(ItemStackTemplate.CODEC, ItemStackTemplate.STREAM_CODEC)
}
