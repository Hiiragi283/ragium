package hiiragi283.ragium

import com.google.common.collect.Multimap
import com.google.common.collect.Table
import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.addon.RagiumAddon
import hiiragi283.ragium.api.gui.component.HTFluidWidget
import hiiragi283.ragium.api.item.HTFoodBuilder
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.tag.HTKeyOrTagEntry
import hiiragi283.ragium.api.util.HTMultiMap
import hiiragi283.ragium.api.util.HTTable
import hiiragi283.ragium.client.gui.component.HTFluidHandlerWidget
import hiiragi283.ragium.common.recipe.result.HTFluidResultImpl
import hiiragi283.ragium.common.recipe.result.HTItemResultImpl
import hiiragi283.ragium.setup.RagiumAttachmentTypes
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.util.HTAddonCollector
import hiiragi283.ragium.util.HTWrappedMultiMap
import hiiragi283.ragium.util.HTWrappedTable
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.component.DataComponents
import net.minecraft.resources.ResourceKey
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.energy.IEnergyStorage
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.server.ServerLifecycleHooks
import org.slf4j.Logger

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

    override fun createSoda(instances: List<MobEffectInstance>, count: Int): ItemStack {
        val stack: ItemStack = RagiumItems.ICE_CREAM_SODA.toStack(count)
        stack.set(DataComponents.FOOD, HTFoodBuilder.create { instances.forEach(this::addEffect) })
        return stack
    }

    //    Server    //

    override fun getEnergyNetwork(key: ResourceKey<Level>): IEnergyStorage? =
        ServerLifecycleHooks.getCurrentServer()?.getLevel(key)?.getData(RagiumAttachmentTypes.ENERGY_NETWORK)

    //    Platform    //

    override fun getConfigImpl(): RagiumAPI.Config = RagiumConfig

    override fun <K : Any, V : Any> createMultiMap(multimap: Multimap<K, V>): HTMultiMap.Mutable<K, V> = HTWrappedMultiMap.Mutable(multimap)

    override fun <R : Any, C : Any, V : Any> createTable(table: Table<R, C, V>): HTTable.Mutable<R, C, V> = HTWrappedTable.Mutable(table)

    override fun createFluidWidget(
        handler: IFluidHandler?,
        index: Int,
        x: Int,
        y: Int,
    ): HTFluidWidget = HTFluidHandlerWidget(
        handler,
        index,
        x,
        y,
    )

    override fun createItemResult(entry: HTKeyOrTagEntry<Item>, amount: Int, component: DataComponentPatch): HTItemResult =
        HTItemResultImpl(entry, amount, component)

    override fun createFluidResult(entry: HTKeyOrTagEntry<Fluid>, amount: Int, component: DataComponentPatch): HTFluidResult =
        HTFluidResultImpl(entry, amount, component)
}
