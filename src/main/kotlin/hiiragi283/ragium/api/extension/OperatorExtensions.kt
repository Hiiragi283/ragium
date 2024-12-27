package hiiragi283.ragium.api.extension

import net.minecraft.recipe.Recipe
import net.minecraft.recipe.RecipeEntry
import net.minecraft.registry.entry.RegistryEntryList
import net.minecraft.util.Identifier

//    typealias    //

typealias MPair<F, S> = com.mojang.datafixers.util.Pair<F, S>

//    contains    //

operator fun <T : Any> RegistryEntryList<T>.contains(value: T): Boolean = any { it.isOf(value) }

//    get    //

//    component    //

operator fun <T : Recipe<*>> RecipeEntry<T>.component1(): Identifier = this.id

operator fun <T : Recipe<*>> RecipeEntry<T>.component2(): T = this.value

operator fun <F : Any, S : Any> MPair<F, S>.component1(): F = first

operator fun <F : Any, S : Any> MPair<F, S>.component2(): S = second
