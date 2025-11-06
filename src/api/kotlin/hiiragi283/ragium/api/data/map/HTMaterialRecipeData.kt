package hiiragi283.ragium.api.data.map

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumPlatform
import hiiragi283.ragium.api.data.recipe.HTResultHelper
import hiiragi283.ragium.api.data.recipe.ingredient.HTFluidIngredientCreator
import hiiragi283.ragium.api.data.recipe.ingredient.HTItemIngredientCreator
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
import net.neoforged.neoforge.common.conditions.ICondition
import java.util.Optional
import java.util.function.Function

interface HTMaterialRecipeData {
    companion object {
        @JvmField
        val CODEC: Codec<HTMaterialRecipeData> = RagiumAPI.MATERIAL_RECIPE_TYPE_REGISTRY
            .byNameCodec()
            .dispatch(HTMaterialRecipeData::type, Function.identity())

        @JvmField
        val ID_MAP_CODEC: Codec<Map<ResourceLocation, HTMaterialRecipeData>> =
            Codec.unboundedMap(ResourceLocation.CODEC, CODEC)
    }

    fun type(): MapCodec<out HTMaterialRecipeData>

    fun generateRecipes(
        access: RegistryAccess,
        itemCreator: HTItemIngredientCreator,
        fluidCreator: HTFluidIngredientCreator,
    ): List<RecipeHolder<*>> {
        val idCache: MutableSet<ResourceLocation> = mutableSetOf()
        return buildList {
            val output: RecipeOutput = object : RecipeOutput {
                override fun accept(
                    id: ResourceLocation,
                    recipe: Recipe<*>,
                    advancement: AdvancementHolder?,
                    vararg conditions: ICondition?,
                ) {
                    val id1: ResourceLocation = id.withPrefix("/material/")
                    check(idCache.add(id1)) { "Duplicated material recipe: $id1" }
                    add(RecipeHolder(id1, recipe))
                    RagiumAPI.LOGGER.info("Added material recipe {}", id1)
                }

                override fun advancement(): Advancement.Builder = Advancement.Builder.recipeAdvancement()
            }
            generateRecipes(Helper(access, itemCreator, fluidCreator, HTResultHelper.INSTANCE, output))
        }
    }

    fun generateRecipes(helper: Helper)

    @JvmRecord
    data class Helper(
        val access: RegistryAccess,
        val itemCreator: HTItemIngredientCreator,
        val fluidCreator: HTFluidIngredientCreator,
        val resultHelper: HTResultHelper,
        val output: RecipeOutput,
    ) {
        fun getAllMaterials(): Set<HTMaterialKey> = RagiumPlatform.INSTANCE.getAllMaterials()

        fun isPresentTag(prefix: HTPrefixLike, material: HTMaterialLike): Boolean = isPresentTag(prefix.itemTagKey(material))

        fun isPresentTag(tagKey: TagKey<Item>): Boolean {
            val holderSet: Optional<HolderSet.Named<Item>> = access.lookupOrThrow(Registries.ITEM).get(tagKey)
            return holderSet.isPresent && holderSet.get().any()
        }
    }
}
