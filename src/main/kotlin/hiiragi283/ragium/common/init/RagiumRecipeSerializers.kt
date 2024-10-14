package hiiragi283.ragium.common.init

import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.recipe.HTSmithingModuleRecipe
import hiiragi283.ragium.api.recipe.alchemy.HTInfusionRecipe
import hiiragi283.ragium.api.recipe.alchemy.HTTransformRecipe
import hiiragi283.ragium.api.recipe.machine.HTMachineRecipe
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
    val INFUSION: RecipeSerializer<HTInfusionRecipe> = register(
        "alchemical_infusion",
        HTInfusionRecipe.CODEC,
        HTInfusionRecipe.PACKET_CODEC,
    )

    @JvmField
    val TRANSFORM: RecipeSerializer<HTTransformRecipe> = register(
        "alchemical_transform",
        HTTransformRecipe.CODEC,
        HTTransformRecipe.PACKET_CODEC,
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
