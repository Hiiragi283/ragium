package hiiragi283.ragium.api

import com.google.common.collect.Multimap
import com.google.common.collect.Table
import hiiragi283.ragium.api.capability.HTStorageIO
import hiiragi283.ragium.api.fluid.HTMachineFluidTank
import hiiragi283.ragium.api.item.HTMachineItemHandler
import hiiragi283.ragium.api.machine.HTMachineRegistry
import hiiragi283.ragium.api.material.HTMaterialRegistry
import hiiragi283.ragium.api.recipe.base.HTItemResult
import hiiragi283.ragium.api.util.HTMultiMap
import hiiragi283.ragium.api.util.HTTable
import net.minecraft.core.BlockPos
import net.minecraft.core.RegistryAccess
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.energy.IEnergyStorage
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.items.IItemHandler
import net.neoforged.neoforge.items.IItemHandlerModifiable
import java.util.*

interface RagiumAPI {
    companion object {
        const val MOD_ID = "ragium"
        const val MOD_NAME = "Ragium"

        const val DEFAULT_TANK_CAPACITY = 8000

        /**
         * 名前空間が`ragium`となる[ResourceLocation]を返します。
         */
        @JvmStatic
        fun id(path: String): ResourceLocation = ResourceLocation.fromNamespaceAndPath(MOD_ID, path)

        /**
         * 指定した[id]の名前空間を`ragium`に変えます。
         */
        @JvmStatic
        fun wrapId(id: ResourceLocation): ResourceLocation = id(id.path)

        private lateinit var instance: RagiumAPI

        @JvmStatic
        fun getInstance(): RagiumAPI {
            if (!::instance.isInitialized) {
                instance = ServiceLoader.load(RagiumAPI::class.java).first()
            }
            return instance
        }
    }

    /**
     * 機械レジストリのインスタンスを返します。。
     */
    fun getMachineRegistry(): HTMachineRegistry

    /**
     * 素材レジストリのインスタンスを返します。
     */
    fun getMaterialRegistry(): HTMaterialRegistry

    //    Platform    //

    fun <K : Any, V : Any> createMultiMap(multimap: Multimap<K, V>): HTMultiMap.Mutable<K, V>

    fun <R : Any, C : Any, V : Any> createTable(table: Table<R, C, V>): HTTable.Mutable<R, C, V>

    fun createItemHandler(callback: () -> Unit): HTMachineItemHandler = createItemHandler(1, callback)

    fun createItemHandler(size: Int, callback: () -> Unit): HTMachineItemHandler

    fun createTank(callback: () -> Unit): HTMachineFluidTank = createTank(DEFAULT_TANK_CAPACITY, callback)

    fun createTank(capacity: Int, callback: () -> Unit): HTMachineFluidTank

    fun getEnergyNetwork(level: ServerLevel): IEnergyStorage

    fun wrapItemHandler(storageIO: HTStorageIO, handler: IItemHandlerModifiable): IItemHandlerModifiable

    fun wrapFluidHandler(storageIO: HTStorageIO, handler: IFluidHandler): IFluidHandler

    fun wrapEnergyStorage(storageIO: HTStorageIO, storage: IEnergyStorage): IEnergyStorage

    fun getCurrentLookup(): RegistryAccess? = getCurrentServer()?.registryAccess()

    fun getCurrentServer(): MinecraftServer?

    fun createItemResult(stack: ItemStack): HTItemResult = createItemResult(stack.item, stack.count, stack.componentsPatch)

    fun createItemResult(item: Item, count: Int = 1, components: DataComponentPatch = DataComponentPatch.EMPTY): HTItemResult

    fun createSingleItemMenu(
        syncId: Int,
        playerInv: Inventory,
        pos: BlockPos,
        itemHandler: IItemHandler,
    ): AbstractContainerMenu
}
