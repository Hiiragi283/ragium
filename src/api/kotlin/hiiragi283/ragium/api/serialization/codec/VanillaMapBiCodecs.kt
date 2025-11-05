package hiiragi283.ragium.api.serialization.codec

import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.fluids.crafting.FluidIngredient

object VanillaMapBiCodecs {
    @JvmField
    val INGREDIENT: MapBiCodec<RegistryFriendlyByteBuf, Ingredient> = MapBiCodec.of(
        Ingredient.MAP_CODEC_NONEMPTY,
        Ingredient.CONTENTS_STREAM_CODEC,
    )

    @JvmField
    val FLUID_INGREDIENT: MapBiCodec<RegistryFriendlyByteBuf, FluidIngredient> = MapBiCodec.of(
        FluidIngredient.MAP_CODEC_NONEMPTY,
        FluidIngredient.STREAM_CODEC,
    )
}
