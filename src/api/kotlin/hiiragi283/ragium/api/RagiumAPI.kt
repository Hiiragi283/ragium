package hiiragi283.ragium.api

import com.google.common.collect.Multimap
import com.google.common.collect.Table
import hiiragi283.ragium.api.addon.RagiumAddon
import hiiragi283.ragium.api.collection.HTMultiMap
import hiiragi283.ragium.api.collection.HTTable
import hiiragi283.ragium.api.extension.RegistryKey
import hiiragi283.ragium.api.extension.asKotlinRandom
import hiiragi283.ragium.api.extension.buildMultiMap
import hiiragi283.ragium.api.extension.lookupOrNull
import hiiragi283.ragium.api.extension.mutableTableOf
import hiiragi283.ragium.api.extension.toId
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.material.HTMaterialVariant
import hiiragi283.ragium.api.storage.energy.HTEnergyBattery
import hiiragi283.ragium.api.storage.item.HTItemHandler
import hiiragi283.ragium.api.storage.value.HTValueInput
import hiiragi283.ragium.api.storage.value.HTValueOutput
import io.wispforest.accessories.api.AccessoriesCapability
import net.minecraft.client.Minecraft
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.RegistryAccess
import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.level.Level
import net.neoforged.fml.loading.FMLEnvironment
import java.util.*
import kotlin.jvm.optionals.getOrNull
import kotlin.random.Random

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

        @JvmStatic
        fun wrapId(other: ResourceLocation): ResourceLocation = when (other.namespace) {
            MOD_ID -> other
            else -> id(other.path)
        }

        val INSTANCE: RagiumAPI by lazy(::getService)

        /**
         * @see [mekanism.api.MekanismAPI.getService]
         */
        @Suppress("UnstableApiUsage")
        @JvmStatic
        inline fun <reified SERVICE : Any> getService(): SERVICE =
            ServiceLoader.load(SERVICE::class.java, RagiumAPI::class.java.classLoader).first()
    }

    //    Addon    //

    fun getAddons(): List<RagiumAddon>

    fun getMaterialMap(): Map<HTMaterialType, HTMaterialVariant.ItemTag>

    fun getBaseVariant(material: HTMaterialType): HTMaterialVariant.ItemTag? = getMaterialMap()[material]

    //    Item    //

    fun createSoda(potion: Holder<Potion>, count: Int = 1): ItemStack = createSoda(PotionContents(potion), count)

    fun createSoda(potion: PotionContents, count: Int = 1): ItemStack

    //    Server    //

    fun getCurrentServer(): MinecraftServer?

    fun getLevel(key: ResourceKey<Level>): ServerLevel? = getCurrentServer()?.getLevel(key)

    fun getRegistryAccess(): RegistryAccess? = getCurrentServer()?.registryAccess() ?: when {
        FMLEnvironment.dist.isClient -> Minecraft.getInstance().level?.registryAccess()
        else -> null
    }

    fun <T : Any> getLookup(registryKey: RegistryKey<T>): HolderLookup.RegistryLookup<T>? = getRegistryAccess()?.lookupOrNull(registryKey)

    fun <T : Any> getHolder(key: ResourceKey<T>): Holder<T>? = getRegistryAccess()?.holder(key)?.getOrNull()

    fun getUniversalBundle(server: MinecraftServer, color: DyeColor): HTItemHandler

    /**
     * エネルギーネットワークのマネージャを返します。
     */
    fun getEnergyNetwork(level: Level?): HTEnergyBattery?

    /**
     * エネルギーネットワークのマネージャを返します。
     */
    fun getEnergyNetwork(key: ResourceKey<Level>): HTEnergyBattery?

    //    Storage    //

    fun createValueInput(lookup: HolderLookup.Provider, compoundTag: CompoundTag): HTValueInput

    fun createValueOutput(lookup: HolderLookup.Provider, compoundTag: CompoundTag): HTValueOutput

    //    Accessory    //

    fun getAccessoryCap(entity: LivingEntity): AccessoriesCapability? = AccessoriesCapability.get(entity)

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
     * @see [asKotlinRandom]
     */
    fun wrapRandom(random: RandomSource): Random
}
