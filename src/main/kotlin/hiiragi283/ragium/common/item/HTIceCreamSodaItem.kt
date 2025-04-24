package hiiragi283.ragium.common.item

import hiiragi283.ragium.api.extension.createItemStack
import hiiragi283.ragium.api.item.HTConsumableItem
import hiiragi283.ragium.api.item.HTFoodBuilder
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponents
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionContents

class HTIceCreamSodaItem(properties: Properties) : HTConsumableItem(properties) {
    companion object {
        @JvmStatic
        fun createSoda(potion: Holder<Potion>, count: Int = 1): ItemStack = createSoda(potion.value().effects, count)

        @JvmStatic
        fun createSoda(potion: PotionContents, count: Int = 1): ItemStack = createSoda(potion.allEffects.toList(), count)

        @JvmStatic
        fun createSoda(instances: List<MobEffectInstance>, count: Int = 1): ItemStack = createItemStack(RagiumItems.ICE_CREAM_SODA, count) {
            set(DataComponents.FOOD, HTFoodBuilder.create { instances.forEach(this::addEffect) })
        }
    }

    override fun getEatingSound(): SoundEvent = SoundEvents.GENERIC_DRINK
}
