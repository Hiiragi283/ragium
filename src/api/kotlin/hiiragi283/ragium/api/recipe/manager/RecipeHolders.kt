package hiiragi283.ragium.api.recipe.manager

import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder

@Suppress("UNCHECKED_CAST")
fun <R1 : Recipe<*>, R2 : R1> RecipeHolder<out R1>.castRecipe(): RecipeHolder<R2>? = (this.value as? R2)?.let { RecipeHolder(this.id, it) }

fun <R1 : Recipe<*>> RecipeHolder<R1>.withPrefix(prefix: String): RecipeHolder<R1> = RecipeHolder(this.id.withPrefix(prefix), this.value)
