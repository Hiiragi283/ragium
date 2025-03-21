package hiiragi283.ragium.common.internal

import com.google.common.collect.Multimap
import com.google.common.collect.Table
import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.addon.HTAddonCollector
import hiiragi283.ragium.api.addon.RagiumAddon
import hiiragi283.ragium.api.material.HTMaterialRegistry
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.energy.HTEnergyNetworkManager
import hiiragi283.ragium.api.storage.fluid.HTFluidSlotHandler
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.fluid.HTFluidVariant
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.storage.item.HTItemSlotHandler
import hiiragi283.ragium.api.storage.item.HTItemVariant
import hiiragi283.ragium.api.util.HTMultiMap
import hiiragi283.ragium.api.util.HTTable
import hiiragi283.ragium.common.storage.energy.HTLimitedEnergyStorage
import hiiragi283.ragium.common.storage.fluid.HTFluidTankImpl
import hiiragi283.ragium.common.storage.item.HTItemSlotImpl
import hiiragi283.ragium.common.util.HTWrappedMultiMap
import hiiragi283.ragium.common.util.HTWrappedTable
import net.minecraft.server.MinecraftServer
import net.minecraft.world.item.ItemStack
import net.neoforged.fml.LogicalSide
import net.neoforged.fml.util.thread.EffectiveSide
import net.neoforged.neoforge.energy.IEnergyStorage
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.items.IItemHandlerModifiable
import net.neoforged.neoforge.server.ServerLifecycleHooks
import org.slf4j.Logger
import thedarkcolour.kotlinforforge.neoforge.kotlin.supply

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
                .sortedBy(RagiumAddon::priority)
                .onEach { addon: RagiumAddon ->
                    LOGGER.info("Loaded addon from ${addon::class.qualifiedName}!")
                }
        }
        return addonCache
    }

    //    Material    //

    override fun getMaterialRegistry(): HTMaterialRegistry = HTMaterialRegistryImpl

    //    Server    //

    override fun getCurrentServer(): MinecraftServer? = ServerLifecycleHooks.getCurrentServer()

    override fun getCurrentSide(): LogicalSide = EffectiveSide.get()

    override fun getEnergyNetworkManager(): HTEnergyNetworkManager = HTEnergyNetworkManagerImpl

    //    Platform    //

    override fun <K : Any, V : Any> createMultiMap(multimap: Multimap<K, V>): HTMultiMap.Mutable<K, V> = HTWrappedMultiMap.Mutable(multimap)

    override fun <R : Any, C : Any, V : Any> createTable(table: Table<R, C, V>): HTTable.Mutable<R, C, V> = HTWrappedTable.Mutable(table)

    override fun wrapItemSlot(storageIO: HTStorageIO, slotIn: HTItemSlot): IItemHandlerModifiable = object : HTItemSlotHandler {
        override fun getItemIoFromSlot(slot: Int): HTStorageIO = storageIO

        override fun getItemSlot(slot: Int): HTItemSlot? = slotIn

        override fun getSlots(): Int = 1
    }

    override fun wrapFluidTank(storageIO: HTStorageIO, tankIn: HTFluidTank): IFluidHandler = object : HTFluidSlotHandler {
        override fun getFluidIoFromSlot(tank: Int): HTStorageIO = storageIO

        override fun getFluidTank(tank: Int): HTFluidTank? = tankIn

        override fun getTanks(): Int = 1
    }

    override fun wrapEnergyStorage(storageIO: HTStorageIO, storage: IEnergyStorage): IEnergyStorage =
        HTLimitedEnergyStorage(storageIO, storage)

    override fun buildItemSlot(
        nbtKey: String,
        capacity: Int,
        validator: (HTItemVariant) -> Boolean,
        callback: Runnable,
    ): HTItemSlot = HTItemSlotImpl(
        nbtKey,
        capacity,
        validator,
        callback,
    )

    override fun buildFluidTank(
        nbtKey: String,
        capacity: Int,
        validator: (HTFluidVariant) -> Boolean,
        callback: Runnable,
    ): HTFluidTank = HTFluidTankImpl(
        nbtKey,
        capacity,
        validator,
        callback,
    )

    override fun getEffectRange(stack: ItemStack): Int = stack.getOrDefault(supply(TODO()), 5)
}
