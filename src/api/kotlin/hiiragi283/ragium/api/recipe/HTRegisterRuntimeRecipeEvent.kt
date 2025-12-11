package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumPlatform
import hiiragi283.ragium.api.data.recipe.HTResultHelper
import hiiragi283.ragium.api.data.recipe.ingredient.HTFluidIngredientCreator
import hiiragi283.ragium.api.data.recipe.ingredient.HTItemIngredientCreator
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
import net.minecraft.world.item.crafting.RecipeManager
import net.neoforged.bus.api.Event
import net.neoforged.neoforge.common.conditions.ICondition
import java.util.Optional
import java.util.function.Consumer
import kotlin.collections.any

/**
 * @see plus.dragons.createdragonsplus.common.recipe.UpdateRecipesEvent
 */
class HTRegisterRuntimeRecipeEvent(
    val registryAccess: RegistryAccess,
    val recipeManager: RecipeManager,
    private val consumer: Consumer<RecipeHolder<*>>,
) : Event() {
    val output: RecipeOutput = object : RecipeOutput {
        override fun accept(
            id: ResourceLocation,
            recipe: Recipe<*>,
            advancement: AdvancementHolder?,
            vararg conditions: ICondition?,
        ) {
            val id1: ResourceLocation = id.withPrefix("runtime/")
            consumer.accept(RecipeHolder(id1, recipe))
            RagiumAPI.LOGGER.debug("Added runtime recipe {}", id1)
        }

        override fun advancement(): Advancement.Builder = Advancement.Builder.recipeAdvancement()
    }

    val itemCreator: HTItemIngredientCreator = RagiumPlatform.INSTANCE.itemCreator()
    val fluidCreator: HTFluidIngredientCreator = RagiumPlatform.INSTANCE.fluidCreator()
    val resultHelper: HTResultHelper = HTResultHelper

    fun isPresentTag(prefix: HTPrefixLike, material: HTMaterialLike): Boolean = isPresentTag(prefix.itemTagKey(material))

    fun isPresentTag(tagKey: TagKey<Item>): Boolean {
        val holderSet: Optional<HolderSet.Named<Item>> = registryAccess.lookupOrThrow(Registries.ITEM).get(tagKey)
        return holderSet.isPresent && holderSet.get().any()
    }

    fun save(id: ResourceLocation, recipe: Recipe<*>) {
        output.accept(id, recipe, null)
    }
}
