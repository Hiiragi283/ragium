package hiiragi283.ragium.common.init

import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.recipe.HTMachineRecipe
import hiiragi283.ragium.api.recipe.HTSmithingModuleRecipe
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.recipe.Recipe
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object RagiumRecipeSerializers {
    @JvmField
    val MACHINE: RecipeSerializer<HTMachineRecipe> = register(
        "machine",
        HTMachineRecipe.CODEC,
        HTMachineRecipe.PACKET_CODEC,
    )

    @JvmField
    val MODULE_INSTALL: RecipeSerializer<HTSmithingModuleRecipe> = register(
        "module_install",
        HTSmithingModuleRecipe.CODEC,
        HTSmithingModuleRecipe.PACKET_CODEC,
    )

    @JvmStatic
    private fun <T : Recipe<*>> register(
        name: String,
        mapCodec: MapCodec<T>,
        packetCodec: PacketCodec<RegistryByteBuf, T>,
    ): RecipeSerializer<T> = Registry.register(
        Registries.RECIPE_SERIALIZER,
        RagiumAPI.id(name),
        object : RecipeSerializer<T> {
            override fun codec(): MapCodec<T> = mapCodec

            override fun packetCodec(): PacketCodec<RegistryByteBuf, T> = packetCodec
        },
    )
}
