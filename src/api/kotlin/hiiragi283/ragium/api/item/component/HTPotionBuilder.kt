package hiiragi283.ragium.api.item.component

import hiiragi283.ragium.api.util.HTDslMarker
import hiiragi283.ragium.api.util.wrapOptional
import net.minecraft.core.Holder
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionContents
import java.awt.Color

@HTDslMarker
class HTPotionBuilder private constructor() {
    companion object {
        @JvmStatic
        fun create(builderAction: HTPotionBuilder.() -> Unit): PotionContents = HTPotionBuilder().apply(builderAction).build()

        @JvmStatic
        fun copyOf(parent: PotionContents, builderAction: HTPotionBuilder.() -> Unit): PotionContents = create {
            parent.potion.ifPresent { this.potion = it }
            parent.customColor.ifPresent { this.color = it }
            effects.addAll(parent.customEffects)
            builderAction()
        }
    }

    var potion: Holder<Potion>? = null
    var color: Int? = null
    private val effects: MutableList<MobEffectInstance> = mutableListOf()

    fun setColor(color: Color): HTPotionBuilder = apply {
        this.color = color.rgb
    }

    fun addEffect(effect: MobEffectInstance) {
        this.effects.add(effect)
    }

    fun addEffect(effect: Holder<MobEffect>, ticks: Int, amplifier: Int) {
        addEffect(MobEffectInstance(effect, ticks, amplifier))
    }

    fun addInfinityEffect(effect: Holder<MobEffect>, amplifier: Int) {
        addEffect(effect, -1, amplifier)
    }

    fun addEffectsFromPotion(other: Holder<Potion>) {
        this.effects.addAll(other.value().effects)
    }

    private fun build(): PotionContents = when {
        potion == null && color == null && effects.isEmpty() -> PotionContents.EMPTY
        else -> PotionContents(potion.wrapOptional(), color.wrapOptional(), effects)
    }
}
