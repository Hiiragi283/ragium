package hiiragi283.ragium.api

import com.google.common.collect.Multimap
import com.google.common.collect.Table
import hiiragi283.ragium.api.extension.buildMultiMap
import hiiragi283.ragium.api.extension.mutableTableOf
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialRegistry
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.fluid.HTFluidVariant
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.storage.item.HTItemVariant
import hiiragi283.ragium.api.util.HTMultiMap
import hiiragi283.ragium.api.util.HTTable
import net.minecraft.core.BlockPos
import net.minecraft.core.RegistryAccess
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.AbstractContainerMenu
import net.neoforged.fml.LogicalSide
import net.neoforged.neoforge.energy.IEnergyStorage
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.items.IItemHandlerModifiable
import net.neoforged.neoforge.registries.DeferredBlock
import java.util.*

interface RagiumAPI {
    companion object {
        const val MOD_ID = "ragium"
        const val MOD_NAME = "Ragium"

        const val INGOT_AMOUNT = 90

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

    //    Material    //

    /**
     * 素材レジストリのインスタンスを返します。
     */
    fun getMaterialRegistry(): HTMaterialRegistry

    //    Server    //

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
    fun getRegistryAccess(): RegistryAccess? = getCurrentServer()?.registryAccess()

    /**
     * 現在のサーバーのインスタンスを返します。
     * @return 存在しない場合は`null`
     */
    fun getCurrentServer(): MinecraftServer?

    /**
     * 現在の物理サイドを返します。
     */
    fun getCurrentSide(): LogicalSide

    fun getEnergyNetwork(): (ServerLevel) -> IEnergyStorage = ::getEnergyNetwork

    /**
     * 指定した[level]からエネルギーネットワークのインスタンスを返します。
     */
    fun getEnergyNetwork(level: ServerLevel): IEnergyStorage

    //    Durability    //

    fun getForgeHammerDurability(): Int

    //    Machine    //

    fun getTankCapacityWithEnch(enchLevel: Int): Int = getDefaultTankCapacity() * (enchLevel + 1)

    fun getDefaultTankCapacity(): Int

    fun getMachineSoundVolume(): Float

    //    Misc    //

    fun getDynamitePower(): Float

    fun getGrinderOutputCount(key: HTMaterialKey): Int

    //    Platform    //

    /**
     * @see [buildMultiMap]
     */
    fun <K : Any, V : Any> createMultiMap(multimap: Multimap<K, V>): HTMultiMap.Mutable<K, V>

    /**
     * @see [mutableTableOf]
     */
    fun <R : Any, C : Any, V : Any> createTable(table: Table<R, C, V>): HTTable.Mutable<R, C, V>

    /**
     * @see [HTStorageIO.wrapItemSlot]
     */
    fun wrapItemSlot(storageIO: HTStorageIO, slot: HTItemSlot): IItemHandlerModifiable

    /**
     * @see [HTStorageIO.wrapFluidTank]
     */
    fun wrapFluidTank(storageIO: HTStorageIO, tank: HTFluidTank): IFluidHandler

    /**
     * @see [HTStorageIO.wrapEnergyStorage]
     */
    fun wrapEnergyStorage(storageIO: HTStorageIO, storage: IEnergyStorage): IEnergyStorage

    fun createSingleItemMenu(
        containerId: Int,
        inventory: Inventory,
        pos: BlockPos,
        inputSlot: HTItemSlot,
        catalystSlot: HTItemSlot,
        outputSlot: HTItemSlot,
    ): AbstractContainerMenu

    fun createMultiItemMenu(
        containerId: Int,
        inventory: Inventory,
        pos: BlockPos,
        firstInputSlot: HTItemSlot,
        secondInputSlot: HTItemSlot,
        thirdInputSlot: HTItemSlot,
        outputSlot: HTItemSlot,
    ): AbstractContainerMenu

    /**
     * @see [HTMachineType.getBlock]
     */
    fun getMachineBlock(type: HTMachineType): DeferredBlock<*>

    fun emptyItemSlot(): HTItemSlot = HTItemSlot.Builder().build("")

    /**
     * @see [HTItemSlot.Builder.build]
     */
    fun buildItemSlot(
        nbtKey: String,
        capacity: Int,
        validator: (HTItemVariant) -> Boolean,
        callback: Runnable,
    ): HTItemSlot

    /**
     * @see [HTFluidTank.Builder.build]
     */
    fun buildFluidTank(
        nbtKey: String,
        capacity: Int,
        validator: (HTFluidVariant) -> Boolean,
        callback: Runnable,
    ): HTFluidTank
}
