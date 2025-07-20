package hiiragi283.ragium.common.event

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.setup.RagiumEnchantments
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.Enchantments
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
        // Add Noise Canceling V to Deep Steel Sword
        if (stack.`is`(RagiumItems.DEEP_STEEL_TOOLS.swordItem)) {
            lookup.get(RagiumEnchantments.NOISE_CANCELING).ifPresent { holder: Holder.Reference<Enchantment> ->
                enchantments.upgrade(holder, 5)
            }
            return
        }
        // Set Fortune V to Deep Steel Pickaxe
        if (stack.`is`(RagiumItems.DEEP_STEEL_TOOLS.pickaxeItem)) {
            lookup.get(Enchantments.FORTUNE).ifPresent { holder: Holder.Reference<Enchantment> ->
                enchantments.set(holder, 5)
            }
            return
        }
        // Set Sonic Protection to Deep Steel Armors
        if (stack.`is`(RagiumItems.DEEP_STEEL_ARMORS.getItemHolderSet())) {
            lookup.get(RagiumEnchantments.SONIC_PROTECTION).ifPresent { holder: Holder.Reference<Enchantment> ->
                enchantments.set(holder, 1)
            }
            return
        }
    }
}
