package hiiragi283.ragium.api

import com.google.gson.JsonObject
import hiiragi283.ragium.api.data.recipe.ingredient.HTFluidIngredientCreator
import hiiragi283.ragium.api.data.recipe.ingredient.HTItemIngredientCreator
import hiiragi283.ragium.api.material.HTMaterialDefinition
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.prefix.HTMaterialPrefix
import hiiragi283.ragium.api.recipe.extra.HTPlantingRecipe
import hiiragi283.ragium.api.recipe.multi.HTRockGeneratingRecipe
import hiiragi283.ragium.api.registry.RegistryKey
import hiiragi283.ragium.api.serialization.value.HTValueInput
import hiiragi283.ragium.api.serialization.value.HTValueOutput
import hiiragi283.ragium.api.storage.energy.HTEnergyBattery
import hiiragi283.ragium.api.storage.item.HTItemHandler
import net.minecraft.client.Minecraft
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.RegistryAccess
import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.ResourceKey
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.level.Level
import net.neoforged.fml.loading.FMLEnvironment
import kotlin.jvm.optionals.getOrNull

interface RagiumPlatform {
    companion object {
        @JvmField
        val INSTANCE: RagiumPlatform = RagiumAPI.getService()
    }

    //    Material    //

    fun getMaterialDefinitions(): Map<HTMaterialKey, HTMaterialDefinition>

    fun getAllMaterials(): Set<HTMaterialKey> = getMaterialDefinitions().keys

    fun getMaterialDefinition(key: HTMaterialKey): HTMaterialDefinition = getMaterialDefinitions()[key] ?: HTMaterialDefinition.Empty

    fun getPrefixMap(): Map<String, HTMaterialPrefix>

    fun getPrefix(name: String): HTMaterialPrefix? = getPrefixMap()[name]

    //    Recipe    //

    fun itemCreator(): HTItemIngredientCreator

    fun fluidCreator(): HTFluidIngredientCreator

    fun getPlantingRecipeSerializer(): RecipeSerializer<HTPlantingRecipe>

    fun getRockGeneratingRecipeSerializer(): RecipeSerializer<HTRockGeneratingRecipe>

    //    Server    //

    fun getCurrentServer(): MinecraftServer?

    fun getLevel(key: ResourceKey<Level>): ServerLevel? = getCurrentServer()?.getLevel(key)

    fun getRegistryAccess(): RegistryAccess? = getCurrentServer()?.registryAccess() ?: when {
        FMLEnvironment.dist.isClient -> runCatching {
            Minecraft.getInstance().level?.registryAccess()
        }.getOrNull()
        else -> null
    }

    fun <T : Any> getLookup(provider: HolderLookup.Provider?, registryKey: RegistryKey<T>): HolderLookup.RegistryLookup<T>? =
        (provider ?: getRegistryAccess())?.lookup(registryKey)?.getOrNull()

    fun <T : Any> getHolder(provider: HolderLookup.Provider?, key: ResourceKey<T>): Holder<T>? =
        (provider ?: getRegistryAccess())?.holder(key)?.getOrNull()

    fun getUniversalBundle(server: MinecraftServer, color: DyeColor): HTItemHandler

    /**
     * 指定した[level]からエネルギーのネットワークを返します。
     * @return 取得できなかった場合は`null`
     */
    fun getEnergyNetwork(level: Level?): HTEnergyBattery?

    //    Storage    //

    fun createValueInput(provider: HolderLookup.Provider, jsonObject: JsonObject): HTValueInput

    fun createValueOutput(provider: HolderLookup.Provider, jsonObject: JsonObject): HTValueOutput

    fun createValueInput(provider: HolderLookup.Provider, compoundTag: CompoundTag): HTValueInput

    fun createValueOutput(provider: HolderLookup.Provider, compoundTag: CompoundTag): HTValueOutput
}
