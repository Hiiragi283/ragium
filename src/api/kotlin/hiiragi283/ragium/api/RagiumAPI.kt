package hiiragi283.ragium.api

import com.google.common.collect.Multimap
import com.google.common.collect.Table
import hiiragi283.ragium.api.addon.RagiumAddon
import hiiragi283.ragium.api.data.recipe.HTResultHelper
import hiiragi283.ragium.api.extension.buildMultiMap
import hiiragi283.ragium.api.extension.mutableTableOf
import hiiragi283.ragium.api.gui.component.HTFluidWidget
import hiiragi283.ragium.api.gui.screen.HTContainerScreen
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.tag.HTKeyOrTagEntry
import hiiragi283.ragium.api.util.HTMultiMap
import hiiragi283.ragium.api.util.HTTable
import hiiragi283.ragium.api.util.tool.HTToolVariant
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.level.Level
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.energy.IEnergyStorage
import net.neoforged.neoforge.fluids.capability.IFluidHandler
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

    fun getToolVariants(): List<HTToolVariant>

    fun getToolVariant(name: String): HTToolVariant = getToolVariants()
        .firstOrNull { variant: HTToolVariant -> variant.serializedName == name }
        ?: error("Unknown tool variant: $name")

    //    Item    //

    fun createSoda(potion: Holder<Potion>, count: Int = 1): ItemStack = createSoda(potion.value().effects, count)

    fun createSoda(potion: PotionContents, count: Int = 1): ItemStack = createSoda(potion.allEffects.toList(), count)

    fun createSoda(instances: List<MobEffectInstance>, count: Int = 1): ItemStack

    //    Server    //

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
     * 現在のサーバーのインスタンスを返します。
     * @return 存在しない場合は`null`
     */
    fun getCurrentServer(): MinecraftServer?

    /**
     * エネルギーネットワークのマネージャを返します。
     */
    fun getEnergyNetwork(key: ResourceKey<Level>): IEnergyStorage?

    //    Config    //

    fun getConfigImpl(): Config

    interface Config {
        // Generator
        fun getGeneratorEnergyRate(key: String): Int

        // Machine
        fun getMachineTankCapacity(key: String): Int

        fun getProcessorEnergyUsage(key: String): Int

        fun getDefaultNetworkCapacity(): Int

        // Device
        fun getDeviceTankCapacity(): Int

        fun getEntityCollectorRange(): Int

        fun getExpCollectorMultiplier(): Int

        fun getMilkDrainMultiplier(): Int

        // Drum
        fun getDrumCapacity(key: String): Int

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
     * @see [HTContainerScreen.createFluidWidget]
     */
    fun createFluidWidget(
        handler: IFluidHandler?,
        index: Int,
        x: Int,
        y: Int,
    ): HTFluidWidget

    /**
     * @see [HTResultHelper.item]
     */
    fun createItemResult(entry: HTKeyOrTagEntry<Item>, amount: Int, component: DataComponentPatch): HTItemResult

    /**
     * @see [HTResultHelper.fluid]
     */
    fun createFluidResult(entry: HTKeyOrTagEntry<Fluid>, amount: Int, component: DataComponentPatch): HTFluidResult
}
