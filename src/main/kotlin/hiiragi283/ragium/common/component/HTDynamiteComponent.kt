package hiiragi283.ragium.common.component

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.boolText
import hiiragi283.ragium.api.extension.floatText
import hiiragi283.ragium.common.init.RagiumTranslationKeys
import net.minecraft.entity.Entity
import net.minecraft.item.Item
import net.minecraft.item.tooltip.TooltipAppender
import net.minecraft.item.tooltip.TooltipType
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.world.World
import java.util.function.Consumer

data class HTDynamiteComponent(val power: Float, val canDestroy: Boolean) : TooltipAppender {
    companion object {
        @JvmField
        val DEFAULT = HTDynamiteComponent(2f, true)

        @JvmField
        val CODEC: Codec<HTDynamiteComponent> = RecordCodecBuilder.create { instance ->
            instance
                .group(
                    Codec.floatRange(0f, 16f).optionalFieldOf("power", 2f).forGetter(HTDynamiteComponent::power),
                    Codec.BOOL.optionalFieldOf("can_destroy", true).forGetter(HTDynamiteComponent::canDestroy),
                ).apply(instance, ::HTDynamiteComponent)
        }

        @JvmField
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, HTDynamiteComponent> = PacketCodec.tuple(
            PacketCodecs.FLOAT,
            HTDynamiteComponent::power,
            PacketCodecs.BOOL,
            HTDynamiteComponent::canDestroy,
            ::HTDynamiteComponent,
        )
    }

    init {
        check(power in (0f..16f)) { "Invalid explosion power; $power (allowed range is 0f ~ 16f)" }
    }

    private val sourceType: World.ExplosionSourceType = when (canDestroy) {
        true -> World.ExplosionSourceType.TNT
        false -> World.ExplosionSourceType.NONE
    }

    fun createExplosion(
        world: World,
        entity: Entity,
        x: Double,
        y: Double,
        z: Double,
    ) {
        world.createExplosion(entity, x, y, z, power, false, sourceType)
    }

    //    TooltipAppender    //

    override fun appendTooltip(context: Item.TooltipContext, tooltip: Consumer<Text>, type: TooltipType) {
        tooltip.accept(
            Text
                .translatable(
                    RagiumTranslationKeys.DYNAMITE_POWER,
                    floatText(power).formatted(Formatting.WHITE),
                ).formatted(Formatting.GRAY),
        )
        val destroyText: MutableText = boolText(canDestroy).formatted(
            when (canDestroy) {
                true -> Formatting.RED
                false -> Formatting.AQUA
            }
        )
        tooltip.accept(
            Text
                .translatable(
                    RagiumTranslationKeys.DYNAMITE_DESTROY,
                    destroyText,
                ).formatted(Formatting.GRAY),
        )
    }
}
