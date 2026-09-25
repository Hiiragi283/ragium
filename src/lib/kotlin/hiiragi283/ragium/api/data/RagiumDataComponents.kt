package hiiragi283.ragium.api.data

import hiiragi283.lib.data.DataComponentType
import hiiragi283.lib.serialization.codec.HTCodecs
import hiiragi283.lib.serialization.network.HTStreamCodecs
import hiiragi283.ragium.api.data.oreSlurry.HTOreSlurryData
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentType
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.world.item.ItemStackTemplate
import net.neoforged.neoforge.fluids.SimpleFluidContent
import java.util.UUID

/**
 * Ragiumで使用される[DataComponentType]をまとめたクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
data object RagiumDataComponents {
    // Item Stack
    @JvmField
    val ENERGY: DataComponentType<Int> = DataComponentType(HTCodecs.NON_NEGATIVE_INT, ByteBufCodecs.VAR_INT)

    @JvmField
    val FLUID: DataComponentType<SimpleFluidContent> =
        DataComponentType(SimpleFluidContent.CODEC, SimpleFluidContent.STREAM_CODEC)

    @JvmField
    val MEMORY_DISC_DATA: DataComponentType<ItemStackTemplate> = DataComponentType(
        ItemStackTemplate.CODEC,
        ItemStackTemplate.STREAM_CODEC
    )

    /**
     * @since 26.1.5
     */
    @JvmField
    val ORE_SLURRY_DATA: DataComponentType<Holder<HTOreSlurryData>> =
        DataComponentType(HTOreSlurryData.CODEC, HTOreSlurryData.STREAM_CODEC)

    /**
     * @since 26.1.7
     */
    @JvmField
    val OWNER_ID: DataComponentType<UUID> = DataComponentType(HTCodecs.UUID, HTStreamCodecs.UUID)
}
