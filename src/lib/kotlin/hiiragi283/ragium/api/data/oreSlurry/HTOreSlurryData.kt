package hiiragi283.ragium.api.data.oreSlurry

import com.mojang.serialization.Codec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.lib.recipe.result.createOrEmpty
import hiiragi283.lib.serialization.codec.HTCodecs
import hiiragi283.lib.serialization.network.HTStreamCodecs
import hiiragi283.lib.text.Text
import hiiragi283.ragium.api.RagiumRegistries
import net.minecraft.core.Holder
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.util.ExtraCodecs
import net.minecraft.world.item.ItemStack

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.5
 */
@JvmRecord
data class HTOreSlurryData(val title: Text, val color: Int, val result: HTItemResult) {
    companion object {
        @JvmField
        val DIRECT_CODEC: Codec<HTOreSlurryData> = HTCodecs.record { instance ->
            instance.group(
                HTCodecs.TEXT.fieldOf("title").forGetter(HTOreSlurryData::title),
                ExtraCodecs.STRING_RGB_COLOR.fieldOf("color").forGetter(HTOreSlurryData::color),
                HTItemResult.CODEC.fieldOf(HTConstants.RESULT).forGetter(HTOreSlurryData::result)
            ).apply(instance, ::HTOreSlurryData)
        }

        @JvmField
        val CODEC: Codec<Holder<HTOreSlurryData>> = HTCodecs.holder(RagiumRegistries.Keys.ORE_SLURRY_DATA, DIRECT_CODEC)

        @JvmField
        val DIRECT_STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTOreSlurryData> = StreamCodec.composite(
            HTStreamCodecs.TEXT,
            HTOreSlurryData::title,
            ByteBufCodecs.VAR_INT,
            HTOreSlurryData::color,
            HTItemResult.STREAM_CODEC,
            HTOreSlurryData::result,
            ::HTOreSlurryData
        )

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, Holder<HTOreSlurryData>> =
            HTStreamCodecs.holder(RagiumRegistries.Keys.ORE_SLURRY_DATA, DIRECT_STREAM_CODEC)
    }

    fun createResult(): ItemStack = result.createOrEmpty()
}
