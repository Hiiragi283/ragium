package hiiragi283.ragium.common.init

import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.recipe.HTMachineRecipe
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object RagiumRecipes {
    //    Serializer    //

    @JvmField
    val SERIALIZER: DeferredRegister<RecipeSerializer<*>> =
        DeferredRegister.create(Registries.RECIPE_SERIALIZER, RagiumAPI.MOD_ID)

    @JvmStatic
    private fun <T : Recipe<*>> serializer(
        mapCodec: MapCodec<T>,
        streamCodec: StreamCodec<RegistryFriendlyByteBuf, T>,
    ): RecipeSerializer<T> = object : RecipeSerializer<T> {
        override fun codec(): MapCodec<T> = mapCodec

        override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, T> = streamCodec
    }

    @JvmField
    val MACHINE_SERIALIZER: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<HTMachineRecipe>> =
        SERIALIZER.register("machine") { _: ResourceLocation ->
            serializer(HTMachineRecipe.CODEC, HTMachineRecipe.STREAM_CODEC)
        }

    //    Type    //

    @JvmField
    val TYPE: DeferredRegister<RecipeType<*>> =
        DeferredRegister.create(Registries.RECIPE_TYPE, RagiumAPI.MOD_ID)

    @JvmStatic
    val MACHINE_TYPE: DeferredHolder<RecipeType<*>?, RecipeType<HTMachineRecipe>> =
        TYPE.register("machine", RecipeType<HTMachineRecipe>::simple)
}
