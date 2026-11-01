package hiiragi283.ragium.api.recipe

import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConstants
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap
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
        field: MutableMap<Identifier, RecipeSerializer<*>> = Object2ObjectLinkedOpenHashMap()

    @JvmStatic
    private fun <RECIPE : Recipe<*>> register(
        name: String,
        codec: MapCodec<RECIPE>,
        streamCodec: StreamCodec<RegistryFriendlyByteBuf, RECIPE>,
    ): RecipeSerializer<RECIPE> = register(name, RecipeSerializer(codec, streamCodec))

    @JvmStatic
    private fun <RECIPE : Recipe<*>> register(name: String, serializer: RecipeSerializer<RECIPE>): RecipeSerializer<RECIPE> {
        allSerializers[RagiumAPI.id(name)] = serializer
        return serializer
    }

    // Mechanical
    @JvmField
    val ASSEMBLING: RecipeSerializer<RTAssemblingRecipe> =
        register(RagiumConstants.ASSEMBLING, RTAssemblingRecipe.SERIALIZER)

    @JvmField
    val CENTRIFUGING: RecipeSerializer<RTCentrifugingRecipe> =
        register(RagiumConstants.CENTRIFUGING, RTCentrifugingRecipe.SERIALIZER)

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
    val FILLING: RecipeSerializer<RTFillingRecipe> =
        register(RagiumConstants.FILLING, RTFillingRecipe.SERIALIZER)

    @JvmField
    val PRESSING: RecipeSerializer<RTPressingRecipe> =
        register(RagiumConstants.PRESSING, RTPressingRecipe.SERIALIZER)

    // Heat
    @JvmField
    val ALLOYING: RecipeSerializer<RTAlloyingRecipe> =
        register(RagiumConstants.ALLOYING, RTAlloyingRecipe.SERIALIZER)

    @JvmField
    val DRYING: RecipeSerializer<RTDryingRecipe> =
        register(RagiumConstants.DRYING, RTDryingRecipe.SERIALIZER)

    @JvmField
    val FREEZING: RecipeSerializer<RTFreezingRecipe> =
        register(RagiumConstants.FREEZING, RTFreezingRecipe.SERIALIZER)

    @JvmField
    val MELTING: RecipeSerializer<RTMeltingRecipe> =
        register(RagiumConstants.MELTING, RTMeltingRecipe.SERIALIZER)

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
    val MIXING: RecipeSerializer<RTMixingRecipe> =
        register(RagiumConstants.MIXING, RTMixingRecipe.SERIALIZER)

    @JvmField
    val REACTING: RecipeSerializer<RTReactingRecipe> =
        register(RagiumConstants.REACTING, RTReactingRecipe.SERIALIZER)

    // Resource
    @JvmField
    val BREWING: RecipeSerializer<RTBrewingRecipe> =
        register(RagiumConstants.BREWING, RTBrewingRecipe.SERIALIZER)

    @JvmField
    val PLANTING: RecipeSerializer<RTPlantingRecipe> =
        register(RagiumConstants.PLANTING, RTPlantingRecipe.SERIALIZER)

    @JvmField
    val RESOURCE_PUMPING: RecipeSerializer<RTResourcePumpingRecipe> =
        register(RagiumConstants.RESOURCE_PUMPING, RTResourcePumpingRecipe.SERIALIZER)

    // Electronics
    @JvmField
    val ENGRAVING: RecipeSerializer<RTEngravingRecipe> =
        register(RagiumConstants.ENGRAVING, RTEngravingRecipe.SERIALIZER)

    // Arcane
    @JvmField
    val ENCHANTING: RecipeSerializer<RTEnchantingRecipe> =
        register(RagiumConstants.ENCHANTING, RTEnchantingRecipe.SERIALIZER)

    @JvmField
    val TRANSFORMING: RecipeSerializer<RTTransformingRecipe> =
        register(RagiumConstants.TRANSFORMING, RTTransformingRecipe.SERIALIZER)
}
