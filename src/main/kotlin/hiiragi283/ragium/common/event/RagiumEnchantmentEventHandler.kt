package hiiragi283.ragium.common.event

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.util.HTIntrinsicEnchantment
import hiiragi283.ragium.setup.RagiumDataComponents
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.EnchantmentInstance
import net.minecraft.world.item.enchantment.ItemEnchantments
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.enchanting.GetEnchantmentLevelEvent

@EventBusSubscriber(modid = RagiumAPI.MOD_ID)
object RagiumEnchantmentEventHandler {
    @SubscribeEvent
    fun getEnchantmentLevel(event: GetEnchantmentLevelEvent) {
        val stack: ItemStack = event.stack
        val enchantments: ItemEnchantments.Mutable = event.enchantments
        val lookup: HolderLookup.RegistryLookup<Enchantment> = event.lookup

        val enchantment: HTIntrinsicEnchantment = stack.get(RagiumDataComponents.INTRINSIC_ENCHANTMENT) ?: return
        enchantment.getInstance(lookup).ifPresent { instance: EnchantmentInstance ->
            enchantments.set(instance.enchantment, instance.level)
        }
    }
}
