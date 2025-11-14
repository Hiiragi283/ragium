package hiiragi283.ragium.api.item.alchemy

import hiiragi283.ragium.api.serialization.codec.BiCodec
import hiiragi283.ragium.api.serialization.codec.BiCodecs
import hiiragi283.ragium.api.serialization.codec.VanillaBiCodecs
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectInstance

/**
 * 毎回エンコードのたびに`cure`の項目が入れ替わるのがうざいので導入
 * @see MobEffectInstance
 */
data class HTMobEffectInstance(
    val effect: Holder<MobEffect>,
    val duration: Int,
    val amplifier: Int = 0,
    val ambient: Boolean = false,
    val visible: Boolean = true,
) {
    companion object {
        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, HTMobEffectInstance> = BiCodec.composite(
            VanillaBiCodecs.holder(Registries.MOB_EFFECT).fieldOf("effect"),
            HTMobEffectInstance::effect,
            BiCodec.INT.optionalFieldOf("duration", -1),
            HTMobEffectInstance::duration,
            BiCodecs.NON_NEGATIVE_INT.optionalFieldOf("amplifier", 0),
            HTMobEffectInstance::amplifier,
            BiCodec.BOOL.optionalFieldOf("ambient", false),
            HTMobEffectInstance::ambient,
            BiCodec.BOOL.optionalFieldOf("visible", true),
            HTMobEffectInstance::visible,
            ::HTMobEffectInstance,
        )
    }

    constructor(instance: MobEffectInstance) : this(
        instance.effect,
        instance.duration,
        instance.amplifier,
        instance.isAmbient,
        instance.isVisible,
    )

    fun toVanilla(): MobEffectInstance = MobEffectInstance(effect, duration, amplifier, ambient, visible)
}
