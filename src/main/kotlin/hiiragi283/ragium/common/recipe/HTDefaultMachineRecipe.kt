package hiiragi283.ragium.common.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.machine.HTMachineDefinition
import hiiragi283.ragium.api.recipe.*
import hiiragi283.ragium.common.init.RagiumRecipeSerializers
import hiiragi283.ragium.common.init.RagiumRecipeTypes
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.recipe.RecipeType
import net.minecraft.world.World

class HTDefaultMachineRecipe(definition: HTMachineDefinition, data: HTMachineRecipeData) : HTMachineRecipe(definition, data) {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTDefaultMachineRecipe> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTMachineDefinition.CODEC.forGetter(HTDefaultMachineRecipe::definition),
                    HTMachineRecipeData.CODEC.forGetter(HTDefaultMachineRecipe::data),
                ).apply(instance, ::HTDefaultMachineRecipe)
        }

        @JvmField
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, HTDefaultMachineRecipe> = createPacketCodec(::HTDefaultMachineRecipe)
    }

    //    HTMachineRecipeBase    //

    override fun matches(input: HTMachineInput, world: World): Boolean {
        if (!data.isValidOutput(true)) return false
        if (input.key != this.key) return false
        if (input.tier < this.tier) return false
        if (!HTShapelessInputResolver.canMatch(data.itemIngredients, input.itemInputs)) return false
        data.fluidIngredients.forEachIndexed { index: Int, fluid: HTFluidIngredient ->
            if (!fluid.test(input.getFluidInSlot(index))) {
                return false
            }
        }
        return data.catalyst?.test(input.catalyst) ?: input.catalyst.isEmpty
    }

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.MACHINE

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.MACHINE
}
