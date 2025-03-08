package hiiragi283.ragium.common.internal

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.enchLookup
import hiiragi283.ragium.api.extension.modifyEnchantment
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.network.HTPotionBundlePacket
import net.minecraft.core.HolderLookup
import net.minecraft.core.RegistryAccess
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.item.enchantment.ItemEnchantments
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.RegisterCommandsEvent
import net.neoforged.neoforge.event.entity.player.PlayerEvent
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent
import net.neoforged.neoforge.network.PacketDistributor
import org.slf4j.Logger

@EventBusSubscriber(modid = RagiumAPI.MOD_ID)
internal object RagiumGameEvents {
    @JvmStatic
    private val LOGGER: Logger = LogUtils.getLogger()

    fun onRegisterCommands(event: RegisterCommandsEvent) {
        LOGGER.info("Registered Commands!")
    }

    @SubscribeEvent
    fun onLeftClickBlock(event: PlayerInteractEvent.LeftClickEmpty) {
        val stack: ItemStack = event.itemStack
        if (!stack.isEmpty && stack.`is`(RagiumItems.POTION_BUNDLE)) {
            PacketDistributor.sendToServer(HTPotionBundlePacket)
        }
    }

    fun onItemCrafted(event: PlayerEvent.ItemCraftedEvent) {
        val result: ItemStack = event.crafting
        if (result.isEmpty) return
        val access: RegistryAccess = event.entity.level().registryAccess()
        val enchLookup: HolderLookup.RegistryLookup<Enchantment> = access.enchLookup()
        val enchMap: Map<ResourceKey<Enchantment>, Int> = when {
            result.item in RagiumItems.EMBER_ALLOY_ARMORS -> mapOf(Enchantments.FIRE_PROTECTION to 2)
            else -> return
        }
        result.modifyEnchantment { mutable: ItemEnchantments.Mutable ->
            for ((key: ResourceKey<Enchantment>, value: Int) in enchMap) {
                enchLookup.get(key).ifPresent { mutable.set(it, value) }
            }
            mutable.toImmutable()
        }
    }
}
