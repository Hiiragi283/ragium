package hiiragi283.ragium.api.data.map

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumPlatform
import hiiragi283.ragium.api.data.recipe.HTResultHelper
import hiiragi283.ragium.api.data.recipe.ingredient.HTFluidIngredientCreator
import hiiragi283.ragium.api.data.recipe.ingredient.HTItemIngredientCreator
import hiiragi283.ragium.api.material.HTMaterialDefinition
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialLike
import hiiragi283.ragium.api.material.prefix.HTPrefixLike
import net.minecraft.advancements.Advancement
import net.minecraft.advancements.AdvancementHolder
import net.minecraft.core.HolderSet
import net.minecraft.core.RegistryAccess
import net.minecraft.core.registries.Registries
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeManager
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.neoforge.common.conditions.ICondition
import java.util.Optional
import java.util.function.Function

interface HTRuntimeRecipeProvider {
    companion object {
        @JvmField
        val CODEC: Codec<HTRuntimeRecipeProvider> = RagiumAPI.RUNTIME_RECIPE_TYPE_REGISTRY
            .byNameCodec()
            .dispatch(HTRuntimeRecipeProvider::type, Function.identity())

        @JvmField
        val ID_MAP_CODEC: Codec<Map<ResourceLocation, HTRuntimeRecipeProvider>> =
            Codec.unboundedMap(ResourceLocation.CODEC, CODEC)
    }

    fun type(): MapCodec<out HTRuntimeRecipeProvider>

    fun generateRecipes(access: RegistryAccess, manager: RecipeManager): List<RecipeHolder<*>> {
        val itemCreator: HTItemIngredientCreator = RagiumPlatform.INSTANCE.createItemCreator(access)
        val fluidCreator: HTFluidIngredientCreator = RagiumPlatform.INSTANCE.createFluidCreator(access)

        val idCache: MutableSet<ResourceLocation> = mutableSetOf()
        return buildList {
            val output: RecipeOutput = object : RecipeOutput {
                override fun accept(
                    id: ResourceLocation,
                    recipe: Recipe<*>,
                    advancement: AdvancementHolder?,
                    vararg conditions: ICondition?,
                ) {
                    val id1: ResourceLocation = id.withPrefix("/runtime/")
                    check(idCache.add(id1)) { "Duplicated runtime recipe: $id1" }
                    add(RecipeHolder(id1, recipe))
                    RagiumAPI.LOGGER.debug("Added runtime recipe {}", id1)
                }

                override fun advancement(): Advancement.Builder = Advancement.Builder.recipeAdvancement()
            }
            generateRecipes(Helper(access, manager, itemCreator, fluidCreator, HTResultHelper, output))
        }
    }

    fun generateRecipes(helper: Helper)

    @JvmRecord
    data class Helper(
        val access: RegistryAccess,
        val manager: RecipeManager,
        val itemCreator: HTItemIngredientCreator,
        val fluidCreator: HTFluidIngredientCreator,
        val resultHelper: HTResultHelper,
        val output: RecipeOutput,
    ) {
        fun getAllMaterials(): Set<HTMaterialKey> = RagiumPlatform.INSTANCE.getAllMaterials()

        fun getDefinitions(): Map<HTMaterialKey, HTMaterialDefinition> = RagiumPlatform.INSTANCE.getMaterialDefinitions()

        fun isPresentTag(prefix: HTPrefixLike, material: HTMaterialLike): Boolean = isPresentTag(prefix.itemTagKey(material))

        fun isPresentTag(tagKey: TagKey<Item>): Boolean {
            val holderSet: Optional<HolderSet.Named<Item>> = access.lookupOrThrow(Registries.ITEM).get(tagKey)
            return holderSet.isPresent && holderSet.get().any()
        }

        fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>> getAllRecipes(recipeType: RecipeType<RECIPE>): Sequence<RecipeHolder<RECIPE>> =
            manager.getAllRecipesFor(recipeType).asSequence()
    }
}
