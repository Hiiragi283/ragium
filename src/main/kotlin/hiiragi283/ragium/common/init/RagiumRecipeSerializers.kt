package hiiragi283.ragium.common.init

import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.recipe.HTDistillationRecipe
import hiiragi283.ragium.common.recipe.HTDynamiteUpgradingRecipe
import hiiragi283.ragium.common.recipe.HTGrinderRecipe
import hiiragi283.ragium.common.recipe.HTMachineRecipe
import hiiragi283.ragium.common.recipe.HTRockGeneratorRecipe
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.recipe.Recipe
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object RagiumRecipeSerializers {
    @JvmField
    val DISTILLATION: RecipeSerializer<HTDistillationRecipe> = register(
        "distillation",
        HTDistillationRecipe.CODEC,
        HTDistillationRecipe.PACKET_CODEC,
    )

    @JvmField
    val DYNAMITE_UPGRADE: RecipeSerializer<HTDynamiteUpgradingRecipe> = register(
        "dynamite_upgrade",
        MapCodec.unit(HTDynamiteUpgradingRecipe),
        PacketCodec.unit(HTDynamiteUpgradingRecipe),
    )

    @JvmField
    val GRINDER: RecipeSerializer<HTGrinderRecipe> = register(
        "grinder",
        HTGrinderRecipe.CODEC,
        HTGrinderRecipe.PACKET_CODEC,
    )

    @JvmField
    val MACHINE: RecipeSerializer<HTMachineRecipe> = register(
        "machine",
        HTMachineRecipe.CODEC,
        HTMachineRecipe.PACKET_CODEC,
    )

    @JvmField
    val ROCK_GENERATOR: RecipeSerializer<HTRockGeneratorRecipe> = register(
        "rock_generator",
        HTRockGeneratorRecipe.CODEC,
        HTRockGeneratorRecipe.PACKET_CODEC,
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
