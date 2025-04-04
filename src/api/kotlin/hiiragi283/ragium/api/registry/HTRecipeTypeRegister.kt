package hiiragi283.ragium.api.registry

import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredRegister

class HTRecipeTypeRegister(modId: String) {
    private val serializerRegister: DeferredRegister<RecipeSerializer<*>> =
        DeferredRegister.create(Registries.RECIPE_SERIALIZER, modId)

    private val typeRegister: DeferredRegister<RecipeType<*>> =
        DeferredRegister.create(Registries.RECIPE_TYPE, modId)

    fun init(eventBus: IEventBus) {
        serializerRegister.register(eventBus)
        typeRegister.register(eventBus)
    }

    fun <R : Recipe<*>, T> register(name: String, type: T): T where T : RecipeType<R>, T : RecipeSerializer<R> {
        serializerRegister.register(name) { _: ResourceLocation -> type }
        typeRegister.register(name) { _: ResourceLocation -> type }
        return type
    }
}
