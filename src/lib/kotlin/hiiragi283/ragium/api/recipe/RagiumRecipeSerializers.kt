package hiiragi283.ragium.api.recipe

import com.mojang.serialization.MapCodec
import hiiragi283.lib.HTConstants
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConstants
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.Identifier
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeSerializer

/**
 * Ragiumで使用される[RecipeSerializer]をまとめたクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
data object RagiumRecipeSerializers {
    @JvmStatic
    val allSerializers: Map<Identifier, RecipeSerializer<*>>
        field: MutableMap<Identifier, RecipeSerializer<*>> = mutableMapOf()

    @JvmStatic
    private fun <RECIPE : Recipe<*>> register(
        name: String,
        codec: MapCodec<RECIPE>,
        streamCodec: StreamCodec<RegistryFriendlyByteBuf, RECIPE>
    ): RecipeSerializer<RECIPE> = register(name, RecipeSerializer(codec, streamCodec))

    @JvmStatic
    private fun <RECIPE : Recipe<*>> register(
        name: String,
        serializer: RecipeSerializer<RECIPE>
    ): RecipeSerializer<RECIPE> {
        allSerializers[RagiumAPI.id(name)] = serializer
        return serializer
    }

    // Mechanical
    @JvmField
    val ASSEMBLING: RecipeSerializer<RTAssemblingRecipe> =
        register(RagiumConstants.ASSEMBLING, RTAssemblingRecipe.SERIALIZER)

    @JvmField
    val COMPRESSING: RecipeSerializer<RTCompressingRecipe> =
        register(RagiumConstants.COMPRESSING, RTCompressingRecipe.SERIALIZER)

    @JvmField
    val CRUSHING: RecipeSerializer<RTCrushingRecipe> =
        register(RagiumConstants.CRUSHING, RTCrushingRecipe.SERIALIZER)

    @JvmField
    val CUTTING: RecipeSerializer<RTCuttingRecipe> =
        register(RagiumConstants.CUTTING, RTCuttingRecipe.SERIALIZER)

    @JvmField
    val DRAINING: RecipeSerializer<RTDrainingRecipe> =
        register(RagiumConstants.DRAINING, RTDrainingRecipe.SERIALIZER)

    @JvmField
    val FILLING: RecipeSerializer<RTFillingRecipe> =
        register(RagiumConstants.FILLING, RTFillingRecipe.SERIALIZER)

    // Heat
    @JvmField
    val FREEZING: RecipeSerializer<RTFreezingRecipe> =
        register(RagiumConstants.FREEZING, RTFreezingRecipe.SERIALIZER)

    @JvmField
    val MELTING: RecipeSerializer<RTMeltingRecipe> =
        register(RagiumConstants.MELTING, RTMeltingRecipe.SERIALIZER)

    @JvmField
    val SMELTING: RecipeSerializer<RTSmeltingRecipe> =
        register(HTConstants.SMELTING, RTSmeltingRecipe.SERIALIZER)

    @JvmField
    val PYROLYZING: RecipeSerializer<RTPyrolyzingRecipe> =
        register(RagiumConstants.PYROLYZING, RTPyrolyzingRecipe.SERIALIZER)

    @JvmField
    val REFINING: RecipeSerializer<RTRefiningRecipe> =
        register(RagiumConstants.REFINING, RTRefiningRecipe.SERIALIZER)

    // Chemical
    @JvmField
    val BATHING: RecipeSerializer<RTBathingRecipe> =
        register(RagiumConstants.BATHING, RTBathingRecipe.SERIALIZER)

    @JvmField
    val ELECTROLYZING: RecipeSerializer<RTElectrolyzingRecipe> =
        register(RagiumConstants.ELECTROLYZING, RTElectrolyzingRecipe.SERIALIZER)

    // Bio
    @JvmField
    val BREWING: RecipeSerializer<RTBrewingRecipe> =
        register(RagiumConstants.BREWING, RTBrewingRecipe.SERIALIZER)

    @JvmField
    val PLANTING: RecipeSerializer<RTPlantingRecipe> =
        register(RagiumConstants.PLANTING, RTPlantingRecipe.SERIALIZER)

    // Electronics

    // Arcane
}
