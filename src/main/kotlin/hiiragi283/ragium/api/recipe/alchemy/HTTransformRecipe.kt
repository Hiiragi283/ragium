package hiiragi283.ragium.api.recipe.alchemy

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.recipe.HTIngredient
import hiiragi283.ragium.api.recipe.HTRecipeResult
import hiiragi283.ragium.common.init.RagiumRecipeSerializers
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.world.World

class HTTransformRecipe(val target: HTIngredient, val upgrades: List<HTIngredient>, override val result: HTRecipeResult) :
    HTAlchemyRecipe {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTTransformRecipe> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTIngredient.CODEC.fieldOf("target").forGetter(HTTransformRecipe::target),
                    HTIngredient.CODEC
                        .listOf()
                        .fieldOf("upgrades")
                        .forGetter(HTTransformRecipe::upgrades),
                    HTRecipeResult.CODEC
                        .fieldOf("result")
                        .forGetter(HTTransformRecipe::result),
                ).apply(instance, ::HTTransformRecipe)
        }

        @JvmField
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, HTTransformRecipe> = PacketCodec.tuple(
            HTIngredient.PACKET_CODEC,
            HTTransformRecipe::target,
            HTIngredient.LIST_PACKET_CODEC,
            HTTransformRecipe::upgrades,
            HTRecipeResult.PACKET_CODEC,
            HTTransformRecipe::result,
            ::HTTransformRecipe,
        )
    }

    override val inputs: List<HTIngredient> = buildList {
        add(target)
        addAll(upgrades)
    }

    override fun matches(input: HTAlchemyRecipe.Input, world: World): Boolean = input.matches(
        target,
        upgrades.getOrNull(0),
        upgrades.getOrNull(1),
        upgrades.getOrNull(2),
    )

    override fun craft(input: HTAlchemyRecipe.Input, lookup: RegistryWrapper.WrapperLookup): ItemStack {
        val parent: ItemStack = input.getStackInSlot(0).copy()
        val entry: RegistryEntry<Item> = result.firstEntry ?: return parent
        val stack1 = ItemStack(entry, result.count, parent.componentChanges)
        stack1.applyUnvalidatedChanges(result.components)
        return stack1
    }

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.TRANSFORM
}
