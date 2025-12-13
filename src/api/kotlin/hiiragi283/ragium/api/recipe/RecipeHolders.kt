package hiiragi283.ragium.api.recipe

import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder

fun <R : Recipe<*>> RecipeHolder<R>.toPair(): Pair<ResourceLocation, R> = this.id to this.value

fun <R : Recipe<*>> Pair<ResourceLocation, R>.toHolder(): RecipeHolder<R> = RecipeHolder(this.first, this.second)
