package hiiragi283.ragium.api

import com.google.common.collect.Multimap
import com.google.common.collect.Table
import com.mojang.authlib.GameProfile
import hiiragi283.ragium.api.addon.RagiumAddon
import hiiragi283.ragium.api.extension.buildMultiMap
import hiiragi283.ragium.api.extension.mutableTableOf
import hiiragi283.ragium.api.inventory.HTMenuDefinition
import hiiragi283.ragium.api.recipe.HTBlockInteractingRecipe
import hiiragi283.ragium.api.recipe.HTCauldronDroppingRecipe
import hiiragi283.ragium.api.recipe.HTTransmuteRecipe
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.energy.HTEnergyNetworkManager
import hiiragi283.ragium.api.util.HTMultiMap
import hiiragi283.ragium.api.util.HTTable
import net.minecraft.core.Holder
import net.minecraft.core.RegistryAccess
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.fml.LogicalSide
import net.neoforged.neoforge.common.util.FakePlayer
import net.neoforged.neoforge.common.util.FakePlayerFactory
import net.neoforged.neoforge.energy.IEnergyStorage
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

    //    Item    //

    fun createSoda(potion: Holder<Potion>, count: Int = 1): ItemStack = createSoda(potion.value().effects, count)

    fun createSoda(potion: PotionContents, count: Int = 1): ItemStack = createSoda(potion.allEffects.toList(), count)

    fun createSoda(instances: List<MobEffectInstance>, count: Int = 1): ItemStack

    //    Server    //

    /**
     * Ragiumが使用する[FakePlayer]を返します。
     */
    fun getFakePlayer(level: ServerLevel): FakePlayer = FakePlayerFactory.get(level, getRagiumGameProfile())

    /**
     * Ragiumが内部で使用する[GameProfile]のインスタンスを返します。
     */
    fun getRagiumGameProfile(): GameProfile

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
     * @see [HTStorageIO.wrapEnergyStorage]
     */
    fun wrapEnergyStorage(storageIO: HTStorageIO, storage: IEnergyStorage): IEnergyStorage

    /**
     * @see [HTBlockInteractingRecipe.getType]
     */
    fun getBlockInteractingRecipeType(): RecipeType<HTBlockInteractingRecipe>

    /**
     * @see [HTCauldronDroppingRecipe.getType]
     */
    fun getCauldronDroppingRecipeType(): RecipeType<HTCauldronDroppingRecipe>

    /**
     * @see [HTTransmuteRecipe.getSerializer]
     */
    fun getTransmuteRecipeSerializer(): RecipeSerializer<HTTransmuteRecipe>

    /**
     * @see [HTMenuDefinition.empty]
     */
    fun createEmptyMenuDefinition(size: Int): HTMenuDefinition
}
