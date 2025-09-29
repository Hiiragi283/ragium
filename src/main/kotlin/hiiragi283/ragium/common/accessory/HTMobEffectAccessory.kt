package hiiragi283.ragium.common.accessory

import io.wispforest.accessories.api.Accessory
import io.wispforest.accessories.api.slot.SlotReference
import net.minecraft.core.Holder
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.item.ItemStack

class HTMobEffectAccessory(
    private val effect: Holder<MobEffect>,
    private val duration: Int,
    private val amplifier: Int = 0,
    private val ambient: Boolean = false,
    private val visible: Boolean = true,
) : Accessory {
    override fun onEquip(stack: ItemStack, reference: SlotReference) {
        reference.entity().addEffect(
            MobEffectInstance(
                effect,
                duration,
                amplifier,
                ambient,
                visible,
                visible,
            ),
        )
    }

    override fun onUnequip(stack: ItemStack, reference: SlotReference) {
        reference.entity().removeEffect(effect)
    }
}
