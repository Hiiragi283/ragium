package hiiragi283.ragium.api.component

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.extension.addInfinityStatusEffect
import hiiragi283.ragium.api.extension.addStatusEffect
import hiiragi283.ragium.api.extension.identifiedCodec
import hiiragi283.ragium.api.extension.identifiedPacketCodec
import net.minecraft.component.ComponentType
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.StringIdentifiable

/**
 * 放射能レベルを管理するクラス
 */
enum class HTRadioactiveComponent(color: Formatting) : StringIdentifiable {
    LOW(Formatting.YELLOW),
    MEDIUM(Formatting.RED),
    HIGH(Formatting.DARK_PURPLE),
    ;

    companion object {
        @JvmField
        val CODEC: Codec<HTRadioactiveComponent> = identifiedCodec(HTRadioactiveComponent.entries)

        @JvmField
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, HTRadioactiveComponent> =
            identifiedPacketCodec(HTRadioactiveComponent.entries)

        @JvmField
        val COMPONENT_TYPE: ComponentType<HTRadioactiveComponent> = ComponentType
            .builder<HTRadioactiveComponent>()
            .codec(HTRadioactiveComponent.CODEC)
            .packetCodec(HTRadioactiveComponent.PACKET_CODEC)
            .build()
    }

    val text: MutableText = Text.literal(name).formatted(color)

    /**
     * 指定した[entity]にデバフを付与する
     */
    fun applyEffect(entity: Entity?) {
        val livingEntity: LivingEntity = entity as? LivingEntity ?: return
        // low
        livingEntity.addInfinityStatusEffect(StatusEffects.WEAKNESS)
        // medium
        if (this >= MEDIUM) {
            livingEntity.addStatusEffect(StatusEffects.NAUSEA, 20 * 10, 0)
        }
        // high
        if (this == HIGH) {
            livingEntity.addInfinityStatusEffect(StatusEffects.WITHER)
        }
    }

    override fun asString(): String = name.lowercase()
}
