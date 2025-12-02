package hiiragi283.ragium.api.item.alchemy

import hiiragi283.ragium.api.registry.HTHolderLike
import hiiragi283.ragium.api.registry.idOrThrow
import hiiragi283.ragium.api.serialization.codec.BiCodec
import hiiragi283.ragium.api.serialization.codec.BiCodecs
import hiiragi283.ragium.api.serialization.codec.VanillaBiCodecs
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectInstance

/**
 * @see net.minecraft.world.effect.MobEffectInstance
 */
data class HTMobEffectInstance(
    val effect: Holder<MobEffect>,
    val duration: Int,
    val amplifier: Int = 0,
    val ambient: Boolean = false,
    val visible: Boolean = true,
    val showIcon: Boolean = visible,
) : HTHolderLike {
    companion object {
        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, HTMobEffectInstance> = BiCodec.composite(
            BiCodecs.lazy { VanillaBiCodecs.holder(Registries.MOB_EFFECT) }.fieldOf("id").forGetter(HTMobEffectInstance::effect),
            BiCodecs.intRange(-1, Int.MAX_VALUE).fieldOf("duration").forGetter(HTMobEffectInstance::duration),
            BiCodecs.NON_NEGATIVE_INT.optionalFieldOf("amplifier", 0).forGetter(HTMobEffectInstance::amplifier),
            BiCodec.BOOL.optionalFieldOf("ambient", false).forGetter(HTMobEffectInstance::ambient),
            BiCodec.BOOL.optionalFieldOf("visible", true).forGetter(HTMobEffectInstance::visible),
            BiCodec.BOOL.optionalFieldOf("show_icon", true).forGetter(HTMobEffectInstance::showIcon),
            ::HTMobEffectInstance,
        )
    }

    constructor(mutable: MobEffectInstance) : this(
        mutable.effect,
        mutable.duration,
        mutable.amplifier,
        mutable.isAmbient,
        mutable.isVisible,
        mutable.showIcon(),
    )

    fun toMutable(): MobEffectInstance = MobEffectInstance(effect, duration, amplifier, ambient, visible, showIcon)

    override fun getId(): ResourceLocation = effect.idOrThrow
}
