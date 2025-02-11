package hiiragi283.ragium.common.internal

import com.google.common.collect.Multimap
import com.google.common.collect.Table
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.capability.HTStorageIO
import hiiragi283.ragium.api.extension.getServerSavedData
import hiiragi283.ragium.api.fluid.HTMachineFluidTank
import hiiragi283.ragium.api.machine.HTMachineRegistry
import hiiragi283.ragium.api.material.HTMaterialRegistry
import hiiragi283.ragium.api.recipe.base.HTItemResult
import hiiragi283.ragium.api.util.HTMultiMap
import hiiragi283.ragium.api.util.HTTable
import hiiragi283.ragium.common.energy.HTEnergyNetwork
import hiiragi283.ragium.common.energy.HTLimitedEnergyStorage
import hiiragi283.ragium.common.fluid.HTLimitedFluidHandler
import hiiragi283.ragium.common.fluid.HTMachineFluidTankImpl
import hiiragi283.ragium.common.item.HTLimitedItemHandler
import hiiragi283.ragium.common.recipe.HTSimpleItemResult
import hiiragi283.ragium.common.util.HTWrappedMultiMap
import hiiragi283.ragium.common.util.HTWrappedTable
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.Item
import net.neoforged.neoforge.energy.IEnergyStorage
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.items.IItemHandlerModifiable

class InternalRagiumAPI : RagiumAPI {
    override fun getMachineRegistry(): HTMachineRegistry = HTMachineRegistryImpl

    override fun getMaterialRegistry(): HTMaterialRegistry = HTMaterialRegistryImpl

    override fun <K : Any, V : Any> createMultiMap(multimap: Multimap<K, V>): HTMultiMap.Mutable<K, V> = HTWrappedMultiMap.Mutable(multimap)

    override fun <R : Any, C : Any, V : Any> createTable(table: Table<R, C, V>): HTTable.Mutable<R, C, V> = HTWrappedTable.Mutable(table)

    override fun createTank(capacity: Int, callback: () -> Unit): HTMachineFluidTank = HTMachineFluidTankImpl(capacity, callback)

    override fun getEnergyNetwork(level: ServerLevel): IEnergyStorage = level.getServerSavedData(HTEnergyNetwork.DATA_FACTORY)

    override fun wrapItemHandler(storageIO: HTStorageIO, handler: IItemHandlerModifiable): IItemHandlerModifiable =
        HTLimitedItemHandler(storageIO, handler)

    override fun wrapFluidHandler(storageIO: HTStorageIO, handler: IFluidHandler): IFluidHandler = HTLimitedFluidHandler(storageIO, handler)

    override fun wrapEnergyStorage(storageIO: HTStorageIO, storage: IEnergyStorage): IEnergyStorage =
        HTLimitedEnergyStorage(storageIO, storage)

    override fun getCurrentServer(): MinecraftServer? = RagiumGameEvents.currentServer

    override fun createItemResult(item: Item, count: Int, components: DataComponentPatch): HTItemResult =
        HTSimpleItemResult(item, count, components)
}
