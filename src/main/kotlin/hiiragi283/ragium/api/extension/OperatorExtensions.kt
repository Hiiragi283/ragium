package hiiragi283.ragium.api.extension

import net.minecraft.core.HolderSet
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder

//    typealias    //

typealias MPair<F, S> = com.mojang.datafixers.util.Pair<F, S>

//    contains    //

operator fun <T : Any> HolderSet<T>.contains(value: T): Boolean = any { it.isOf(value) }

//    get    //

//    component    //

operator fun <T : Recipe<*>> RecipeHolder<T>.component1(): ResourceKey<Recipe<*>> = this.id

operator fun <T : Recipe<*>> RecipeHolder<T>.component2(): T = this.value

operator fun <F : Any, S : Any> MPair<F, S>.component1(): F = first

operator fun <F : Any, S : Any> MPair<F, S>.component2(): S = second
