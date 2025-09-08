package hiiragi283.ragium.api

import com.google.common.collect.Multimap
import com.google.common.collect.Table
import hiiragi283.ragium.api.addon.RagiumAddon
import hiiragi283.ragium.api.collection.HTMultiMap
import hiiragi283.ragium.api.collection.HTTable
import hiiragi283.ragium.api.data.recipe.HTResultHelper
import hiiragi283.ragium.api.extension.buildMultiMap
import hiiragi283.ragium.api.extension.mutableTableOf
import hiiragi283.ragium.api.extension.toId
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.material.HTMaterialVariant
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.MinecraftServer
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.level.Level
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
        fun id(path: String): ResourceLocation = MOD_ID.toId(path)

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
    }

    //    Addon    //

    fun getAddons(): List<RagiumAddon>

    fun getMaterialMap(): Map<HTMaterialType, HTMaterialVariant.ItemTag>

    fun getBaseVariant(material: HTMaterialType): HTMaterialVariant.ItemTag? = getMaterialMap()[material]

    //    Item    //

    fun createSoda(potion: Holder<Potion>, count: Int = 1): ItemStack = createSoda(potion.value().effects, count)

    fun createSoda(potion: PotionContents, count: Int = 1): ItemStack = createSoda(potion.allEffects.toList(), count)

    fun createSoda(instances: List<MobEffectInstance>, count: Int = 1): ItemStack

    //    Server    //

    fun getCurrentServer(): MinecraftServer?

    fun <T : Any> resolveLookup(registryKey: ResourceKey<out Registry<T>>): HolderLookup.RegistryLookup<T>?

    /**
     * エネルギーネットワークのマネージャを返します。
     */
    fun getEnergyNetwork(key: ResourceKey<Level>): IEnergyStorage?

    //    Recipe    //

    fun getResultHelper(): HTResultHelper

    //    Platform    //

    /**
     * @see [buildMultiMap]
     */
    fun <K : Any, V : Any> createMultiMap(multimap: Multimap<K, V>): HTMultiMap.Mutable<K, V>

    /**
     * @see [mutableTableOf]
     */
    fun <R : Any, C : Any, V : Any> createTable(table: Table<R, C, V>): HTTable.Mutable<R, C, V>
}
