package hiiragi283.ragium.api.text

import com.mojang.serialization.Codec
import hiiragi283.lib.text.HTHasTranslationKey
import hiiragi283.lib.text.HTTranslation
import io.netty.buffer.ByteBuf
import net.minecraft.ChatFormatting
import net.minecraft.core.component.DataComponentGetter
import net.minecraft.network.chat.Component
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.Item
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.component.TooltipProvider
import java.util.function.Consumer

/**
 * 短い説明文を乗せるためのクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.8
 */
@JvmRecord
data class HTItemTooltip(override val translationKey: String) :
    HTTranslation,
    TooltipProvider {
    companion object {
        @JvmField
        val CODEC: Codec<HTItemTooltip> = Codec.STRING.xmap(::HTItemTooltip, HTItemTooltip::translationKey)

        @JvmField
        val STREAM_CODEC: StreamCodec<ByteBuf, HTItemTooltip> =
            ByteBufCodecs.STRING_UTF8.map(::HTItemTooltip, HTItemTooltip::translationKey)

        @JvmStatic
        fun wrap(delegate: HTHasTranslationKey): HTItemTooltip = HTItemTooltip(delegate.translationKey)
    }

    override fun addToTooltip(
        context: Item.TooltipContext,
        consumer: Consumer<Component>,
        flag: TooltipFlag,
        components: DataComponentGetter
    ) {
        consumer.accept(this.translateColored(ChatFormatting.AQUA))
    }
}
