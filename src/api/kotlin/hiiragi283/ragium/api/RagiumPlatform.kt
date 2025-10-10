package hiiragi283.ragium.api

import com.google.gson.JsonObject
import hiiragi283.ragium.api.addon.RagiumAddon
import hiiragi283.ragium.api.extension.asKotlinRandom
import hiiragi283.ragium.api.extension.recipeAccess
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.material.HTMaterialVariant
import hiiragi283.ragium.api.recipe.manager.HTRecipeAccess
import hiiragi283.ragium.api.registry.RegistryKey
import hiiragi283.ragium.api.serialization.value.HTValueInput
import hiiragi283.ragium.api.serialization.value.HTValueOutput
import hiiragi283.ragium.api.storage.energy.HTEnergyBattery
import hiiragi283.ragium.api.storage.item.HTItemHandler
import io.wispforest.accessories.api.AccessoriesCapability
import net.minecraft.client.Minecraft
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.RegistryAccess
import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.ResourceKey
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.item.crafting.RecipeManager
import net.minecraft.world.level.Level
import net.neoforged.fml.loading.FMLEnvironment
import kotlin.jvm.optionals.getOrNull
import kotlin.random.Random

interface RagiumPlatform {
    companion object {
        @JvmField
        val INSTANCE: RagiumPlatform = RagiumAPI.getService()
    }

    //    Addon    //

    fun getAddons(): List<RagiumAddon>

    fun getMaterialMap(): Map<HTMaterialType, HTMaterialVariant.ItemTag>

    fun getBaseVariant(material: HTMaterialType): HTMaterialVariant.ItemTag? = getMaterialMap()[material]

    /**
     * @see [asKotlinRandom]
     */
    fun wrapRandom(random: RandomSource): Random

    //    Item    //

    fun createSoda(potion: Holder<Potion>, count: Int = 1): ItemStack = createSoda(PotionContents(potion), count)

    fun createSoda(potion: PotionContents, count: Int = 1): ItemStack

    //    Recipe    //

    /**
     * @see [recipeAccess]
     */
    fun wrapRecipeManager(recipeManager: RecipeManager): HTRecipeAccess

    //    Server    //

    fun getCurrentServer(): MinecraftServer?

    fun getLevel(key: ResourceKey<Level>): ServerLevel? = getCurrentServer()?.getLevel(key)

    fun getRegistryAccess(): RegistryAccess? = getCurrentServer()?.registryAccess() ?: when {
        FMLEnvironment.dist.isClient -> Minecraft.getInstance().level?.registryAccess()
        else -> null
    }

    fun <T : Any> getLookup(lookup: HolderLookup.Provider?, registryKey: RegistryKey<T>): HolderLookup.RegistryLookup<T>? =
        (lookup ?: getRegistryAccess())?.lookup(registryKey)?.getOrNull()

    fun <T : Any> getHolder(lookup: HolderLookup.Provider?, key: ResourceKey<T>): Holder<T>? =
        (lookup ?: getRegistryAccess())?.holder(key)?.getOrNull()

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

    fun createValueInput(lookup: HolderLookup.Provider, jsonObject: JsonObject): HTValueInput

    fun createValueOutput(lookup: HolderLookup.Provider, jsonObject: JsonObject): HTValueOutput

    fun createValueInput(lookup: HolderLookup.Provider, compoundTag: CompoundTag): HTValueInput

    fun createValueOutput(lookup: HolderLookup.Provider, compoundTag: CompoundTag): HTValueOutput

    //    Accessory    //

    fun getAccessoryCap(entity: LivingEntity): AccessoriesCapability? = AccessoriesCapability.get(entity)
}
