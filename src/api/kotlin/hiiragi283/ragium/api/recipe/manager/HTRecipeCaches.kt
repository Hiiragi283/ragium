package hiiragi283.ragium.api.recipe.manager

import hiiragi283.ragium.api.RagiumPlatform
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeType

/**
 * [HTRecipeFinder]から[HTRecipeCache]のインスタンスを作成します。
 */
fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>> HTRecipeFinder<INPUT, RECIPE>.createCache(): HTRecipeCache<INPUT, RECIPE> =
    RagiumPlatform.INSTANCE.createCache(this)

/**
 * バニラの[RecipeType]から[HTRecipeCache]のインスタンスを作成します。
 */
fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>> RecipeType<RECIPE>.createCache(): HTRecipeCache<INPUT, RECIPE> =
    this.toFindable().createCache()
