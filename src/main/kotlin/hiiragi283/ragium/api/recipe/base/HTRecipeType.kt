package hiiragi283.ragium.api.recipe.base

import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.machine.HTMachineType
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType

/**
 * [RecipeSerializer]と[RecipeType]を束ねたクラス
 * @see [HTMachineRecipeBase.getRecipeType]
 */
class HTRecipeType<T : HTMachineRecipeBase>(val machine: HTMachineType, val serializer: RecipeSerializer<T>) : RecipeType<T> {
    constructor(
        machine: HTMachineType,
        codec: MapCodec<T>,
        streamCodec: StreamCodec<RegistryFriendlyByteBuf, T>,
    ) : this(
        machine,
        object : RecipeSerializer<T> {
            override fun codec(): MapCodec<T> = codec

            override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, T> = streamCodec
        },
    )

    override fun toString(): String = machine.serializedName
}
