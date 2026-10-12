package hiiragi283.ragium.api.machine

import com.mojang.serialization.Codec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.serialization.codec.HTCodecs
import hiiragi283.lib.text.HTCommonTranslation
import hiiragi283.lib.text.Text
import hiiragi283.ragium.api.text.RagiumTranslation
import io.netty.buffer.ByteBuf
import net.minecraft.ChatFormatting
import net.minecraft.core.component.DataComponentGetter
import net.minecraft.network.chat.Component
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.Item
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.component.TooltipProvider
import net.neoforged.neoforge.common.util.NeoForgeExtraCodecs
import java.util.function.Consumer

/**
 * 機械のアップグレードのパラメータを保持するクラスです。
 * @param speed 機械の処理速度
 * @param efficiency 機械の電力効率
 * @param capacity 機械の電気容量
 * @param customKeys 固有処理のフラグの一覧
 * @author Hiiragi Tsubasa
 * @since 26.1.9
 */
@JvmRecord
data class HTMachineUpgrade(
    val speed: Int = 0,
    val efficiency: Int = 0,
    val capacity: Int = 0,
    val customKeys: Set<String> = setOf()
) : TooltipProvider {
    companion object {
        @JvmField
        val CODEC: Codec<HTMachineUpgrade> = HTCodecs.record { instance ->
            instance.group(
                Codec.INT.optionalFieldOf(HTConstants.SPEED, 0).forGetter(HTMachineUpgrade::speed),
                Codec.INT.optionalFieldOf(HTConstants.EFFICIENCY, 0).forGetter(HTMachineUpgrade::efficiency),
                Codec.INT.optionalFieldOf(HTConstants.CAPACITY, 0).forGetter(HTMachineUpgrade::capacity),
                NeoForgeExtraCodecs.setOf(Codec.STRING)
                    .optionalFieldOf("custom_keys", setOf())
                    .forGetter(HTMachineUpgrade::customKeys)
            ).apply(instance, ::HTMachineUpgrade)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<ByteBuf, HTMachineUpgrade> = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            HTMachineUpgrade::speed,
            ByteBufCodecs.VAR_INT,
            HTMachineUpgrade::efficiency,
            ByteBufCodecs.VAR_INT,
            HTMachineUpgrade::capacity,
            ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.collection(::LinkedHashSet)),
            HTMachineUpgrade::customKeys,
            ::HTMachineUpgrade
        )
    }

    override fun addToTooltip(
        context: Item.TooltipContext,
        consumer: Consumer<Component>,
        flag: TooltipFlag,
        components: DataComponentGetter
    ) {
        addToTooltip(consumer)
    }

    fun addToTooltip(consumer: Consumer<Component>) {
        setOf(
            this.speed to RagiumTranslation.TOOLTIPS_UPGRADE_SPEED,
            this.efficiency to RagiumTranslation.TOOLTIPS_UPGRADE_EFFICIENCY,
            this.capacity to RagiumTranslation.TOOLTIPS_UPGRADE_CAPACITY
        ).forEach { (value: Int, translation: RagiumTranslation) ->
            getText(value)?.let { consumer.accept(translation.translateColored(ChatFormatting.YELLOW, it)) }
        }
    }

    private fun getText(value: Int): Text? = when {
        value < 0 -> HTCommonTranslation.PERCENTAGE.translateColored(ChatFormatting.RED, "-$value")
        value > 0 -> HTCommonTranslation.PERCENTAGE.translateColored(ChatFormatting.GREEN, "+$value")
        else -> null
    }
}
