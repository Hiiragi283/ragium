package hiiragi283.ragium

import com.almostreliable.unified.api.AlmostUnified
import com.google.common.collect.Multimap
import com.google.common.collect.Table
import com.mojang.authlib.GameProfile
import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.addon.RagiumAddon
import hiiragi283.ragium.api.extension.asItemHolder
import hiiragi283.ragium.api.extension.createItemStack
import hiiragi283.ragium.api.gui.component.HTFluidWidget
import hiiragi283.ragium.api.inventory.HTMenuDefinition
import hiiragi283.ragium.api.item.HTFoodBuilder
import hiiragi283.ragium.api.storage.energy.HTEnergyNetworkManager
import hiiragi283.ragium.api.util.HTMultiMap
import hiiragi283.ragium.api.util.HTTable
import hiiragi283.ragium.api.util.RagiumConst
import hiiragi283.ragium.client.gui.component.HTFluidTankWidget
import hiiragi283.ragium.common.storage.energy.HTEnergyNetworkManagerImpl
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.util.HTAddonCollector
import hiiragi283.ragium.util.HTWrappedMultiMap
import hiiragi283.ragium.util.HTWrappedTable
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponents
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.MinecraftServer
import net.minecraft.tags.TagKey
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.inventory.SimpleContainerData
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.neoforged.fml.ModList
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.items.ItemStackHandler
import net.neoforged.neoforge.server.ServerLifecycleHooks
import org.slf4j.Logger
import java.util.UUID

class InternalRagiumAPI : RagiumAPI {
    companion object {
        @JvmStatic
        private val LOGGER: Logger = LogUtils.getLogger()
    }

    //    Addon    //

    private lateinit var addonCache: List<RagiumAddon>

    override fun getAddons(): List<RagiumAddon> {
        if (!::addonCache.isInitialized) {
            LOGGER.info("Collecting addons for Ragium...")
            addonCache = HTAddonCollector
                .collectInstances<RagiumAddon>()
                .onEach { addon: RagiumAddon ->
                    LOGGER.info("Loaded addon from ${addon::class.qualifiedName}!")
                }
        }
        return addonCache
    }

    //    Item    //

    override fun createSoda(instances: List<MobEffectInstance>, count: Int): ItemStack =
        createItemStack(RagiumItems.ICE_CREAM_SODA, count) {
            set(DataComponents.FOOD, HTFoodBuilder.create { instances.forEach(this::addEffect) })
        }

    //    Server    //

    override fun getRandomGameProfile(uuid: UUID): GameProfile = GameProfile(uuid, "[${RagiumAPI.MOD_NAME}]")

    override fun getCurrentServer(): MinecraftServer? = ServerLifecycleHooks.getCurrentServer()

    override fun getEnergyNetworkManager(): HTEnergyNetworkManager = HTEnergyNetworkManagerImpl

    //    Platform    //

    override fun getConfigImpl(): RagiumAPI.Config = RagiumConfig

    override fun <K : Any, V : Any> createMultiMap(multimap: Multimap<K, V>): HTMultiMap.Mutable<K, V> = HTWrappedMultiMap.Mutable(multimap)

    override fun <R : Any, C : Any, V : Any> createTable(table: Table<R, C, V>): HTTable.Mutable<R, C, V> = HTWrappedTable.Mutable(table)

    override fun createEmptyMenuDefinition(size: Int): HTMenuDefinition = HTMenuDefinition(
        ItemStackHandler(size),
        ItemStackHandler(4),
        SimpleContainerData(2),
    )

    override fun unifyItemFromId(holder: Holder<Item>, id: ResourceLocation): Holder<Item> {
        if (ModList.get().isLoaded(RagiumConst.ALMOST)) {
            return AlmostUnified.INSTANCE.getVariantItemTarget(holder.value())?.asItemHolder() ?: holder
        }
        return holder
    }

    override fun unifyItemFromTag(holder: Holder<Item>, tagKey: TagKey<Item>): Holder<Item> {
        if (ModList.get().isLoaded(RagiumConst.ALMOST)) {
            return AlmostUnified.INSTANCE.getTagTargetItem(tagKey)?.asItemHolder() ?: holder
        }
        return holder
    }

    override fun createFluidTankWidget(
        stack: FluidStack?,
        capacity: Int?,
        x: Int,
        y: Int,
    ): HTFluidWidget = HTFluidTankWidget(
        stack ?: FluidStack.EMPTY,
        capacity ?: 0,
        x,
        y,
    )
}
