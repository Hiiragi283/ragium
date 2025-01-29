package hiiragi283.ragium.api.component

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.extension.stringCodec
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.util.StringRepresentable
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity

/**
 * 放射能レベルを管理するクラス
 */
enum class HTRadioactivity(color: ChatFormatting) : StringRepresentable {
    LOW(ChatFormatting.YELLOW),
    MEDIUM(ChatFormatting.RED),
    HIGH(ChatFormatting.DARK_PURPLE),
    ;

    companion object {
        @JvmField
        val CODEC: Codec<HTRadioactivity> = stringCodec(HTRadioactivity.entries)
    }

    val text: MutableComponent = Component.literal(name).withStyle(color)

    /**
     * 指定した[entity]にデバフを付与する
     */
    fun applyEffect(entity: Entity?) {
        val livingEntity: LivingEntity = entity as? LivingEntity ?: return
        // low
        // livingEntity.addEffect(MobEffects.WEAKNESS)
        // medium
        if (this >= MEDIUM) {
            // livingEntity.addEffect(MobEffects.NAUSEA, 20 * 10, 0)
        }
        // high
        if (this == HIGH) {
            // livingEntity.addEffect(MobEffects.WITHER)
        }
    }

    override fun getSerializedName(): String = name.lowercase()
}
