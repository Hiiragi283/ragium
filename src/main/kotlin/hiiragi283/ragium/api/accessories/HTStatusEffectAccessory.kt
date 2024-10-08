package hiiragi283.ragium.api.accessories

import io.wispforest.accessories.api.Accessory
import io.wispforest.accessories.api.slot.SlotReference
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.item.ItemStack
import net.minecraft.registry.entry.RegistryEntry

interface HTStatusEffectAccessory : Accessory {
    //    Provider    //

    data class Provider(private val effect: RegistryEntry<StatusEffect>, private val time: Int = -1, private val level: Int = 0) :
        HTStatusEffectAccessory {
        override fun onEquip(stack: ItemStack, reference: SlotReference) {
            reference.entity().addStatusEffect(StatusEffectInstance(effect, time, level))
        }

        override fun onUnequip(stack: ItemStack, reference: SlotReference) {
            reference.entity().removeStatusEffect(effect)
        }
    }

    //    Remover    //

    data class Remover(private val effects: List<RegistryEntry<StatusEffect>>) : HTStatusEffectAccessory {
        constructor(vararg effects: RegistryEntry<StatusEffect>) : this(effects.toList())

        override fun tick(stack: ItemStack, reference: SlotReference) {
            effects.forEach(reference.entity()::removeStatusEffect)
        }
    }
}
