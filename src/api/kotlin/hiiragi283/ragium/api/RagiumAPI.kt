package hiiragi283.ragium.api

import com.google.common.collect.Multimap
import com.google.common.collect.Table
import hiiragi283.ragium.api.addon.RagiumAddon
import hiiragi283.ragium.api.extension.buildMultiMap
import hiiragi283.ragium.api.extension.intText
import hiiragi283.ragium.api.extension.mutableTableOf
import hiiragi283.ragium.api.material.HTMaterialRegistry
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.energy.HTEnergyNetworkManager
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.fluid.HTFluidVariant
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.storage.item.HTItemVariant
import hiiragi283.ragium.api.util.HTMultiMap
import hiiragi283.ragium.api.util.HTTable
import net.minecraft.ChatFormatting
import net.minecraft.core.RegistryAccess
import net.minecraft.core.component.DataComponentType
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntity
import net.neoforged.fml.LogicalSide
import net.neoforged.neoforge.energy.IEnergyStorage
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.items.IItemHandlerModifiable
import java.util.*
import net.minecraft.util.Unit as MCUnit

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

    //    Addon    //

    fun getAddons(): List<RagiumAddon>

    //    Component    //

    fun getActiveComponent(): DataComponentType<MCUnit>

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

    /**
     * エネルギーネットワークのマネージャを返します。
     */
    fun getEnergyNetworkManager(): HTEnergyNetworkManager

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

    fun emptyItemSlot(): HTItemSlot = HTItemSlot.Builder().build("")

    /**
     * @see [HTItemSlot.Builder.build]
     */
    fun buildItemSlot(
        nbtKey: String,
        capacity: Int,
        validator: (HTItemVariant) -> Boolean,
        callback: () -> Unit,
    ): HTItemSlot

    /**
     * @see [HTFluidTank.Builder.build]
     */
    fun buildFluidTank(
        nbtKey: String,
        capacity: Int,
        validator: (HTFluidVariant) -> Boolean,
        callback: () -> Unit,
    ): HTFluidTank

    fun createRangeText(stack: ItemStack): Component = Component
        .translatable(
            TODO(),
            intText(getEffectRange(stack)).withStyle(ChatFormatting.WHITE),
        ).withStyle(ChatFormatting.GRAY)

    fun getEffectRange(stack: ItemStack): Int

    fun sendUpdatePayload(blockEntity: BlockEntity, serverLevel: ServerLevel)
}
