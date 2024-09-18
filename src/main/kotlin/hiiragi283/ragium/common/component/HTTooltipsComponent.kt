package hiiragi283.ragium.common.component

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.item.tooltip.TooltipAppender
import net.minecraft.item.tooltip.TooltipType
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.text.Text
import net.minecraft.text.TextCodecs
import net.minecraft.util.Formatting
import java.util.function.Consumer

@Suppress("DataClassPrivateConstructor")
data class HTTooltipsComponent private constructor(val text: Text) : TooltipAppender {
    companion object {
        @JvmField
        val CODEC: Codec<HTTooltipsComponent> = RecordCodecBuilder.create { instance ->
            instance
                .group(
                    TextCodecs.CODEC.fieldOf("text").forGetter(HTTooltipsComponent::text),
                ).apply(instance, ::HTTooltipsComponent)
        }

        @JvmField
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, HTTooltipsComponent> = PacketCodec.tuple(
            TextCodecs.PACKET_CODEC,
            HTTooltipsComponent::text,
            ::HTTooltipsComponent,
        )

        @JvmStatic
        fun fromBlock(block: Block, vararg formattings: Formatting): HTTooltipsComponent =
            fromTranslation("${block.translationKey}.tooltip", *formattings)

        @JvmStatic
        fun fromItem(item: Item, vararg formattings: Formatting): HTTooltipsComponent =
            fromTranslation("${item.translationKey}.tooltip", *formattings)

        @JvmStatic
        fun fromTranslation(key: String, vararg formattings: Formatting): HTTooltipsComponent =
            HTTooltipsComponent(Text.translatable(key).formatted(*formattings))
    }

    //    TooltipAppender    //

    override fun appendTooltip(context: Item.TooltipContext, tooltip: Consumer<Text>, type: TooltipType) {
        tooltip.accept(text)
    }
}
