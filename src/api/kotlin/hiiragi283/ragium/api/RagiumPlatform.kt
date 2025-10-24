package hiiragi283.ragium.api

import com.google.gson.JsonObject
import hiiragi283.ragium.api.addon.RagiumAddon
import hiiragi283.ragium.api.extension.asKotlinRandom
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.material.HTMaterialVariant
import hiiragi283.ragium.api.recipe.manager.HTRecipeCache
import hiiragi283.ragium.api.recipe.manager.HTRecipeFinder
import hiiragi283.ragium.api.recipe.manager.HTRecipeType
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
import net.minecraft.server.level.ServerPlayer
import net.minecraft.util.RandomSource
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import net.neoforged.fml.loading.FMLEnvironment
import java.util.UUID
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

    fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>> createCache(finder: HTRecipeFinder<INPUT, RECIPE>): HTRecipeCache<INPUT, RECIPE>

    fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>> wrapRecipeType(recipeType: RecipeType<RECIPE>): HTRecipeType.Findable<INPUT, RECIPE>

    //    Server    //

    fun getCurrentServer(): MinecraftServer?

    fun getLevel(key: ResourceKey<Level>): ServerLevel? = getCurrentServer()?.getLevel(key)

    fun getRegistryAccess(): RegistryAccess? = getCurrentServer()?.registryAccess() ?: when {
        FMLEnvironment.dist.isClient -> runCatching {
            Minecraft.getInstance().level?.registryAccess()
        }.getOrNull()
        else -> null
    }

    fun <T : Any> getLookup(lookup: HolderLookup.Provider?, registryKey: RegistryKey<T>): HolderLookup.RegistryLookup<T>? =
        (lookup ?: getRegistryAccess())?.lookup(registryKey)?.getOrNull()

    fun <T : Any> getHolder(lookup: HolderLookup.Provider?, key: ResourceKey<T>): Holder<T>? =
        (lookup ?: getRegistryAccess())?.holder(key)?.getOrNull()

    fun getPlayer(uuid: UUID?): ServerPlayer? {
        if (uuid == null) return null
        return getCurrentServer()?.playerList?.getPlayer(uuid)
    }

    fun getUniversalBundle(server: MinecraftServer, color: DyeColor): HTItemHandler

    /**
     * 指定した[level]からエネルギーのネットワークを返します。
     * @return 取得できなかった場合は`null`
     */
    fun getEnergyNetwork(level: Level?): HTEnergyBattery?

    //    Storage    //

    fun createValueInput(lookup: HolderLookup.Provider, jsonObject: JsonObject): HTValueInput

    fun createValueOutput(lookup: HolderLookup.Provider, jsonObject: JsonObject): HTValueOutput

    fun createValueInput(lookup: HolderLookup.Provider, compoundTag: CompoundTag): HTValueInput

    fun createValueOutput(lookup: HolderLookup.Provider, compoundTag: CompoundTag): HTValueOutput

    //    Accessory    //

    fun getAccessoryCap(entity: LivingEntity): AccessoriesCapability? = AccessoriesCapability.get(entity)
}
