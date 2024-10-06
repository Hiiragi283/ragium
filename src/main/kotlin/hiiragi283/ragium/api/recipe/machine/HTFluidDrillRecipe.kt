package hiiragi283.ragium.api.recipe.machine

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.recipe.HTRecipeBase
import hiiragi283.ragium.api.recipe.HTRecipeResult
import hiiragi283.ragium.api.recipe.WeightedIngredient
import hiiragi283.ragium.common.init.RagiumRecipeTypes
import net.minecraft.item.ItemStack
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.recipe.RecipeType
import net.minecraft.recipe.input.RecipeInput
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.world.World
import net.minecraft.world.biome.Biome

class HTFluidDrillRecipe(val biomeKey: RegistryKey<Biome>, val result: HTRecipeResult) : HTRecipeBase<HTFluidDrillRecipe.Input> {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTFluidDrillRecipe> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    RegistryKey.createCodec(RegistryKeys.BIOME).fieldOf("biome").forGetter(HTFluidDrillRecipe::biomeKey),
                    HTRecipeResult.CODEC.fieldOf("result").forGetter(HTFluidDrillRecipe::result),
                ).apply(instance, ::HTFluidDrillRecipe)
        }

        @JvmField
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, HTFluidDrillRecipe> = PacketCodec.tuple(
            RegistryKey.createPacketCodec(RegistryKeys.BIOME),
            HTFluidDrillRecipe::biomeKey,
            HTRecipeResult.PACKET_CODEC,
            HTFluidDrillRecipe::result,
            ::HTFluidDrillRecipe,
        )
    }

    override val inputs: List<WeightedIngredient> = listOf()
    override val outputs: List<HTRecipeResult> = listOf(result)

    override fun matches(input: Input, world: World): Boolean = input.biomeKey == this.biomeKey

    override fun getSerializer(): RecipeSerializer<*> = Serializer

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.FLUID_DRILL

    //    Serializer    //

    data object Serializer : RecipeSerializer<HTFluidDrillRecipe> {
        override fun codec(): MapCodec<HTFluidDrillRecipe> = CODEC

        override fun packetCodec(): PacketCodec<RegistryByteBuf, HTFluidDrillRecipe> = PACKET_CODEC
    }

    //    Input    //

    class Input(val biomeKey: RegistryKey<Biome>) : RecipeInput {
        override fun getStackInSlot(slot: Int): ItemStack = ItemStack.EMPTY

        override fun getSize(): Int = 0
    }
}
