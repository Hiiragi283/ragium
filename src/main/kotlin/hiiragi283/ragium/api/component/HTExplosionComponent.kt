package hiiragi283.ragium.api.component

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.boolText
import hiiragi283.ragium.api.extension.floatText
import hiiragi283.ragium.common.init.RagiumTranslationKeys
import net.minecraft.ChatFormatting
import net.minecraft.core.BlockPos
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.entity.Entity
import net.minecraft.world.item.Item
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.component.TooltipProvider
import net.minecraft.world.level.Level
import java.util.function.Consumer

/**
 * ダイナマイトの爆発を管理するクラス
 * @param power 爆発の威力，0f~16fを取りうる
 * @param canDestroy trueの場合はブロックを破壊する
 */
data class HTExplosionComponent(val power: Float, val canDestroy: Boolean) : TooltipProvider {
    companion object {
        @JvmField
        val DEFAULT = HTExplosionComponent(2f, true)

        @JvmField
        val CODEC: Codec<HTExplosionComponent> = RecordCodecBuilder.create { instance ->
            instance
                .group(
                    Codec.floatRange(0f, 16f).optionalFieldOf("power", 2f).forGetter(HTExplosionComponent::power),
                    Codec.BOOL.optionalFieldOf("can_destroy", true).forGetter(HTExplosionComponent::canDestroy),
                ).apply(instance, ::HTExplosionComponent)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTExplosionComponent> = StreamCodec.composite(
            ByteBufCodecs.FLOAT,
            HTExplosionComponent::power,
            ByteBufCodecs.BOOL,
            HTExplosionComponent::canDestroy,
            ::HTExplosionComponent,
        )
    }

    private val sourceType: Level.ExplosionInteraction = when (canDestroy) {
        true -> Level.ExplosionInteraction.TNT
        false -> Level.ExplosionInteraction.NONE
    }

    /**
     * 指定した[level]の[pos]に爆発を起こします。
     */
    fun explode(level: Level, pos: BlockPos) {
        explode(level, null, pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble())
    }

    /**
     * 指定した[level]の[x], [y], [z]に[entity]が着火した爆発を起こします。
     */
    fun explode(
        level: Level,
        entity: Entity?,
        x: Double,
        y: Double,
        z: Double,
    ) {
        level.explode(entity, x, y, z, power, false, sourceType)
    }

    //    TooltipProvider    //

    override fun addToTooltip(context: Item.TooltipContext, tooltipAdder: Consumer<Component>, tooltipFlag: TooltipFlag) {
        tooltipAdder.accept(
            Component
                .translatable(
                    RagiumTranslationKeys.DYNAMITE_POWER,
                    floatText(power).withStyle(ChatFormatting.WHITE),
                ).withStyle(ChatFormatting.GRAY),
        )
        val destroyText: MutableComponent = boolText(canDestroy).withStyle(
            when (canDestroy) {
                true -> ChatFormatting.RED
                false -> ChatFormatting.AQUA
            },
        )
        tooltipAdder.accept(
            Component
                .translatable(
                    RagiumTranslationKeys.DYNAMITE_DESTROY,
                    destroyText,
                ).withStyle(ChatFormatting.GRAY),
        )
    }
}
