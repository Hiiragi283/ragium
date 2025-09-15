package hiiragi283.ragium.api.recipe

import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.codec.MapBiCodec
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeSerializer

class HTSimpleRecipeSerializer<RECIPE : Recipe<*>>(
    private val codec: MapCodec<RECIPE>,
    private val streamCodec: StreamCodec<RegistryFriendlyByteBuf, RECIPE>,
) : RecipeSerializer<RECIPE> {
    constructor(codec: MapBiCodec<RegistryFriendlyByteBuf, RECIPE>) : this(codec.codec, codec.streamCodec)

    override fun codec(): MapCodec<RECIPE> = codec

    override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, RECIPE> = streamCodec
}
