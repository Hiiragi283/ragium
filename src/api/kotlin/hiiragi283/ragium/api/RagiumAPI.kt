package hiiragi283.ragium.api

import com.google.common.collect.Multimap
import com.google.common.collect.Table
import com.mojang.authlib.GameProfile
import hiiragi283.ragium.api.addon.RagiumAddon
import hiiragi283.ragium.api.extension.buildMultiMap
import hiiragi283.ragium.api.extension.mutableTableOf
import hiiragi283.ragium.api.gui.component.HTFluidWidget
import hiiragi283.ragium.api.gui.screen.HTContainerScreen
import hiiragi283.ragium.api.inventory.HTMenuDefinition
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.storage.energy.HTEnergyNetworkManager
import hiiragi283.ragium.api.util.HTMultiMap
import hiiragi283.ragium.api.util.HTTable
import net.minecraft.core.Holder
import net.minecraft.core.RegistryAccess
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.tags.TagKey
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionContents
import net.neoforged.neoforge.common.util.FakePlayer
import net.neoforged.neoforge.common.util.FakePlayerFactory
import net.neoforged.neoforge.fluids.FluidStack
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

        @JvmStatic
        fun id(prefix: String, suffix: String): ResourceLocation = id("$prefix/$suffix")

        private lateinit var instance: RagiumAPI

        @JvmStatic
        fun getInstance(): RagiumAPI {
            if (!::instance.isInitialized) {
                instance = ServiceLoader.load(RagiumAPI::class.java).first()
            }
            return instance
        }

        @JvmStatic
        fun getConfig(): Config = getInstance().getConfigImpl()
    }

    //    Addon    //

    fun getAddons(): List<RagiumAddon>

    //    Item    //

    fun createSoda(potion: Holder<Potion>, count: Int = 1): ItemStack = createSoda(potion.value().effects, count)

    fun createSoda(potion: PotionContents, count: Int = 1): ItemStack = createSoda(potion.allEffects.toList(), count)

    fun createSoda(instances: List<MobEffectInstance>, count: Int = 1): ItemStack

    //    Server    //

    /**
     * Ragiumが使用する[FakePlayer]を返します。
     */
    fun getFakePlayer(level: ServerLevel): FakePlayer = FakePlayerFactory.get(level, getRandomGameProfile())

    /**
     * [uuid]から[FakePlayer]を返します。
     */
    fun getFakePlayer(level: ServerLevel, uuid: UUID): FakePlayer = FakePlayerFactory.get(level, getRandomGameProfile(uuid))

    /**
     * Ragiumが内部で使用する[GameProfile]のインスタンスを返します。
     */
    fun getRandomGameProfile(): GameProfile = getRandomGameProfile(UUID.randomUUID())

    /**
     * [uuid]から[GameProfile]を返します。
     */
    fun getRandomGameProfile(uuid: UUID): GameProfile

    /**
     * [getCurrentServer]に基づいて，[uuid]から[ServerPlayer]を返します。
     * @return サーバーまたはプレイヤーが存在しない場合は`null`
     */
    fun getPlayer(uuid: UUID): ServerPlayer? = getPlayer(uuid, getCurrentServer())

    /**
     * [uuid]と[server]から[ServerPlayer]を返します。
     * @return プレイヤーが存在しない場合は`null`
     */
    fun getPlayer(uuid: UUID, server: MinecraftServer?): ServerPlayer? = server?.playerList?.getPlayer(uuid)

    /**
     * [uuid]と[level]から[ServerPlayer]を返します。
     * @return プレイヤーが存在しない場合は[getFakePlayer]
     */
    fun getPlayerOrFake(uuid: UUID, level: ServerLevel): ServerPlayer = getPlayer(uuid, level.server) ?: getFakePlayer(level, uuid)

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
     * エネルギーネットワークのマネージャを返します。
     */
    fun getEnergyNetworkManager(): HTEnergyNetworkManager

    //    Config    //

    fun getConfigImpl(): Config

    interface Config {
        // Machine
        fun getDefaultTankCapacity(): Int

        fun getBasicMachineEnergyUsage(): Int

        fun getAdvancedMachineEnergyUsage(): Int

        // Collector
        fun getEntityCollectorRange(): Int

        fun getExpCollectorMultiplier(): Int

        fun getMilkDrainMultiplier(): Int

        // Drum
        fun getSmallDrumCapacity(): Int

        fun getMediumDrumCapacity(): Int

        fun getLargeDrumCapacity(): Int

        fun getHugeDrumCapacity(): Int

        // Network
        fun getDefaultNetworkCapacity(): Int

        // Recipe
        fun getTagOutputPriority(): List<String>

        // World
        fun disableMilkCure(): Boolean
    }

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
     * @see [HTMenuDefinition.empty]
     */
    fun createEmptyMenuDefinition(size: Int): HTMenuDefinition

    /**
     * @see [HTItemResult.getFirstHolderFromId]
     */
    fun unifyItemFromId(holder: Holder<Item>, id: ResourceLocation): Holder<Item>

    /**
     * @see [HTItemResult.getFirstHolderFromTag]
     */
    fun unifyItemFromTag(holder: Holder<Item>, tagKey: TagKey<Item>): Holder<Item>

    /**
     * @see [HTContainerScreen.createFluidTankWidget]
     */
    fun createFluidTankWidget(
        stack: FluidStack?,
        capacity: Int?,
        x: Int,
        y: Int,
    ): HTFluidWidget
}
