package hiiragi283.ragium.api.recipe.manager

import hiiragi283.ragium.api.RagiumPlatform
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeType

/**
 * バニラの[RecipeType]を[HTRecipeType.Findable]に変換します。
 */
fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>> RecipeType<RECIPE>.toFindable(): HTRecipeType.Findable<INPUT, RECIPE> =
    RagiumPlatform.INSTANCE.wrapRecipeType(this)
