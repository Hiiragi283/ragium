package hiiragi283.ragium.common.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.component.ComponentChanges
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.world.World

class HTTransformRecipe(
    val target: WeightedIngredient,
    val upgrades: List<WeightedIngredient>,
    val output: RegistryEntry<Item>,
    val overrides: ComponentChanges,
) : HTAlchemyRecipe {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTTransformRecipe> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    WeightedIngredient.CODEC.fieldOf("target").forGetter(HTTransformRecipe::target),
                    WeightedIngredient.CODEC
                        .listOf()
                        .fieldOf("upgrades")
                        .forGetter(HTTransformRecipe::upgrades),
                    ItemStack.ITEM_CODEC.fieldOf("output").forGetter(HTTransformRecipe::output),
                    ComponentChanges.CODEC.fieldOf("overrides").forGetter(HTTransformRecipe::overrides),
                ).apply(instance, ::HTTransformRecipe)
        }

        @JvmField
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, HTTransformRecipe> = PacketCodec.tuple(
            WeightedIngredient.PACKET_CODEC,
            HTTransformRecipe::target,
            WeightedIngredient.LIST_PACKET_CODEC,
            HTTransformRecipe::upgrades,
            PacketCodecs.registryEntry(RegistryKeys.ITEM),
            HTTransformRecipe::output,
            ComponentChanges.PACKET_CODEC,
            HTTransformRecipe::overrides,
            ::HTTransformRecipe,
        )
    }

    override val inputs: List<WeightedIngredient> = buildList {
        add(target)
        addAll(upgrades)
    }
    override val result: ItemStack = output.value().defaultStack

    override fun matches(input: HTAlchemyRecipe.Input, world: World): Boolean = input.matches(
        target,
        upgrades.getOrNull(0),
        upgrades.getOrNull(1),
        upgrades.getOrNull(2),
    )

    override fun craft(input: HTAlchemyRecipe.Input, lookup: RegistryWrapper.WrapperLookup): ItemStack {
        val stack1: ItemStack = input.getStackInSlot(0).copyComponentsToNewStack(output.value(), 1)
        stack1.applyUnvalidatedChanges(overrides)
        return stack1
    }

    override fun getSerializer(): RecipeSerializer<*> = Serializer

    data object Serializer : RecipeSerializer<HTTransformRecipe> {
        override fun codec(): MapCodec<HTTransformRecipe> = CODEC

        override fun packetCodec(): PacketCodec<RegistryByteBuf, HTTransformRecipe> = PACKET_CODEC
    }
}
