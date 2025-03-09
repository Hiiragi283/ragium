package hiiragi283.ragium.common.internal

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTHammerDrop
import hiiragi283.ragium.api.data.RagiumDataMaps
import hiiragi283.ragium.api.extension.enchLookup
import hiiragi283.ragium.api.extension.modifyEnchantment
import hiiragi283.ragium.api.extension.toCenterVec3
import hiiragi283.ragium.api.recipe.base.HTItemOutput
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.network.HTPotionBundlePacket
import net.minecraft.core.HolderLookup
import net.minecraft.core.RegistryAccess
import net.minecraft.resources.ResourceKey
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.item.enchantment.ItemEnchantments
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.Vec3
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.RegisterCommandsEvent
import net.neoforged.neoforge.event.entity.player.PlayerEvent
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent
import net.neoforged.neoforge.event.level.BlockDropsEvent
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

    @SubscribeEvent
    fun onHammerBreak(event: BlockDropsEvent) {
        if (event.isCanceled) return
        val tool: ItemStack = event.tool
        if (!tool.`is`(RagiumItemTags.TOOLS_FORGE_HAMMER)) return

        val state: BlockState = event.state
        val hammerDrop: HTHammerDrop = state.blockHolder.getData(RagiumDataMaps.HAMMER_DROP) ?: return

        val level: ServerLevel = event.level
        val pos: Vec3 = event.pos.toCenterVec3()
        val drops: MutableList<ItemEntity> = event.drops
        val (output: HTItemOutput, chance: Float, replace: Boolean) = hammerDrop
        if (replace) drops.clear()
        if (chance > level.random.nextFloat()) {
            drops.add(ItemEntity(level, pos.x, pos.y, pos.z, output.get()))
        }
    }

    fun onItemCrafted(event: PlayerEvent.ItemCraftedEvent) {
        val result: ItemStack = event.crafting
        if (result.isEmpty) return
        val access: RegistryAccess = event.entity.level().registryAccess()
        val enchLookup: HolderLookup.RegistryLookup<Enchantment> = access.enchLookup()
        val enchMap: Map<ResourceKey<Enchantment>, Int> = when {
            TODO() -> mapOf(Enchantments.FIRE_PROTECTION to 2)
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
