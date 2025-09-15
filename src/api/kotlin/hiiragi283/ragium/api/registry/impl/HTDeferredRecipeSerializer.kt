package hiiragi283.ragium.api.registry.impl

import hiiragi283.ragium.api.registry.HTDeferredHolder
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeSerializer

class HTDeferredRecipeSerializer<RECIPE : Recipe<*>>(key: ResourceKey<RecipeSerializer<*>>) :
    HTDeferredHolder<RecipeSerializer<*>, RecipeSerializer<RECIPE>>(key) {
    constructor(id: ResourceLocation) : this(ResourceKey.create(Registries.RECIPE_SERIALIZER, id))
}
