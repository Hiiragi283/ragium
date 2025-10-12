package hiiragi283.ragium.api.recipe.manager

import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import java.util.function.UnaryOperator

inline fun <R1 : Recipe<*>, reified R2 : R1> RecipeHolder<R1>.castRecipe(): RecipeHolder<R2> = RecipeHolder(this.id, this.value as R2)

fun <R1 : Recipe<*>> RecipeHolder<R1>.mapPath(action: UnaryOperator<String>): RecipeHolder<R1> =
    RecipeHolder(this.id.withPath(action), this.value)

inline fun <R1 : Recipe<*>, R2 : Recipe<*>> RecipeHolder<R1>.mapRecipe(action: (R1) -> R2): RecipeHolder<R2> =
    RecipeHolder(this.id, action(this.value))
