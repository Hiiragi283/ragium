package hiiragi283.ragium.api

import com.google.common.collect.Multimap
import com.google.common.collect.Table
import hiiragi283.ragium.api.capability.HTStorageIO
import hiiragi283.ragium.api.extension.buildMultiMap
import hiiragi283.ragium.api.extension.constFunction2
import hiiragi283.ragium.api.extension.mutableTableOf
import hiiragi283.ragium.api.fluid.HTMachineFluidTank
import hiiragi283.ragium.api.item.HTMachineItemHandler
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.material.HTMaterialRegistry
import hiiragi283.ragium.api.util.HTMultiMap
import hiiragi283.ragium.api.util.HTTable
import net.minecraft.core.BlockPos
import net.minecraft.core.RegistryAccess
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.energy.IEnergyStorage
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.fluids.capability.templates.FluidTank
import net.neoforged.neoforge.items.IItemHandler
import net.neoforged.neoforge.items.IItemHandlerModifiable
import net.neoforged.neoforge.registries.DeferredBlock
import java.util.*

interface RagiumAPI {
    companion object {
        const val MOD_ID = "ragium"
        const val MOD_NAME = "Ragium"

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
     * 素材レジストリのインスタンスを返します。
     */
    fun getMaterialRegistry(): HTMaterialRegistry

    /**
     * [getCurrentServer]に基づいて，[uuid]から[ServerPlayer]を返します。
     * @return サーバーまたはプレイヤーが存在しない場合は`null`
     */
    fun getPlayer(uuid: UUID?): ServerPlayer? {
        val uuid1: UUID = uuid ?: return null
        return getCurrentServer()?.playerList?.getPlayer(uuid1)
    }

    /**
     * [getCurrentServer]に基づいた[RegistryAccess]のインスタンスを返します。
     * @return 存在しない場合は`null`
     */
    fun getCurrentLookup(): RegistryAccess? = getCurrentServer()?.registryAccess()

    /**
     * 現在のサーバーのインスタンスを返します。
     * @return 存在しない場合は`null`
     */
    fun getCurrentServer(): MinecraftServer?

    fun getEnergyNetwork(): (ServerLevel) -> IEnergyStorage = ::getEnergyNetwork

    /**
     * 指定した[level]からエネルギーネットワークのインスタンスを返します。
     */
    fun getEnergyNetwork(level: ServerLevel): IEnergyStorage

    //    Durability    //

    fun getForgeHammerDurability(): Int

    fun getSoapDurability(): Int

    //    Machine    //

    fun getTankCapacityWithEnch(enchLevel: Int): Int = getDefaultTankCapacity() * (enchLevel + 1)

    fun getDefaultTankCapacity(): Int

    fun getMachineSoundVolume(): Float

    //    Misc    //

    fun getDynamitePower(): Float

    //    Platform    //

    /**
     * @see [buildMultiMap]
     */
    fun <K : Any, V : Any> createMultiMap(multimap: Multimap<K, V>): HTMultiMap.Mutable<K, V>

    /**
     * @see [mutableTableOf]
     */
    fun <R : Any, C : Any, V : Any> createTable(table: Table<R, C, V>): HTTable.Mutable<R, C, V>

    fun createItemHandler(callback: () -> Unit): HTMachineItemHandler = createItemHandler(1, callback)

    fun createItemHandler(size: Int, callback: () -> Unit): HTMachineItemHandler

    /**
     * @param capacity タンクの基本容量
     * @param callback [FluidTank.onContentsChanged]で呼び出されるブロック
     * @param filter 液体を搬入可能か判定するブロック
     */
    fun createTank(callback: () -> Unit, tagKey: TagKey<Fluid>, capacity: Int = getDefaultTankCapacity()): HTMachineFluidTank =
        createTank(callback, { it.`is`(tagKey) }, capacity)

    /**
     * @param capacity タンクの基本容量
     * @param callback [FluidTank.onContentsChanged]で呼び出されるブロック
     * @param filter 液体を搬入可能か判定するブロック
     */
    fun createTank(
        callback: () -> Unit,
        filter: (FluidStack) -> Boolean = constFunction2(true),
        capacity: Int = getDefaultTankCapacity(),
    ): HTMachineFluidTank

    /**
     * @see [HTStorageIO.wrapItemHandler]
     */
    fun wrapItemHandler(storageIO: HTStorageIO, handler: IItemHandlerModifiable): IItemHandlerModifiable

    /**
     * @see [HTStorageIO.wrapFluidHandler]
     */
    fun wrapFluidHandler(storageIO: HTStorageIO, handler: IFluidHandler): IFluidHandler

    /**
     * @see [HTStorageIO.wrapEnergyStorage]
     */
    fun wrapEnergyStorage(storageIO: HTStorageIO, storage: IEnergyStorage): IEnergyStorage

    fun createSingleItemMenu(
        containerId: Int,
        playerInv: Inventory,
        pos: BlockPos,
        itemInput: IItemHandler,
        itemCatalyst: IItemHandler,
        itemOutput: IItemHandler,
    ): AbstractContainerMenu

    fun createMultiItemMenu(
        containerId: Int,
        playerInv: Inventory,
        pos: BlockPos,
        itemInput: IItemHandler,
        itemOutput: IItemHandler,
    ): AbstractContainerMenu

    /**
     * @see [HTMachineType.getBlock]
     */
    fun getMachineBlock(type: HTMachineType): DeferredBlock<*>
}
