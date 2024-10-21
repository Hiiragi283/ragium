package hiiragi283.ragium.api.recipe.machine

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.bucketStack
import hiiragi283.ragium.api.extension.itemStack
import hiiragi283.ragium.api.extension.toList
import hiiragi283.ragium.api.machine.HTMachineDefinition
import hiiragi283.ragium.api.recipe.HTIngredientNew
import hiiragi283.ragium.api.recipe.HTRecipeResultNew
import hiiragi283.ragium.common.init.RagiumRecipeSerializers
import hiiragi283.ragium.common.init.RagiumRecipeTypes
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant
import net.fabricmc.fabric.api.transfer.v1.storage.base.ResourceAmount
import net.minecraft.fluid.Fluid
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.recipe.Recipe
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.recipe.RecipeType
import net.minecraft.recipe.input.RecipeInput
import net.minecraft.registry.RegistryWrapper
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class HTMachineRecipeNew(
    val definition: HTMachineDefinition,
    val catalyst: HTIngredientNew<Item, ItemVariant>,
    val itemInputs: List<HTIngredientNew<Item, ItemVariant>>,
    val fluidInputs: List<HTIngredientNew<Fluid, FluidVariant>>,
    val itemOutputs: List<HTRecipeResultNew<Item, ItemVariant>>,
    val fluidOutputs: List<HTRecipeResultNew<Fluid, FluidVariant>>,
) : Recipe<HTMachineRecipeNew.Input> {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTMachineRecipeNew> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTMachineDefinition.CODEC
                        .fieldOf("definition")
                        .forGetter(HTMachineRecipeNew::definition),
                    HTIngredientNew.ITEM_CODEC
                        .optionalFieldOf("catalyst", HTIngredientNew.EMPTY_ITEM)
                        .forGetter(HTMachineRecipeNew::catalyst),
                    HTIngredientNew.ITEM_CODEC
                        .listOf()
                        .optionalFieldOf("item_inputs", listOf())
                        .forGetter(HTMachineRecipeNew::itemInputs),
                    HTIngredientNew.FLUID_CODEC
                        .listOf()
                        .optionalFieldOf("fluid_inputs", listOf())
                        .forGetter(HTMachineRecipeNew::fluidInputs),
                    HTRecipeResultNew.ITEM_CODEC
                        .listOf()
                        .optionalFieldOf("item_outputs", listOf())
                        .forGetter(HTMachineRecipeNew::itemOutputs),
                    HTRecipeResultNew.FLUID_CODEC
                        .listOf()
                        .optionalFieldOf("fluid_outputs", listOf())
                        .forGetter(HTMachineRecipeNew::fluidOutputs),
                ).apply(instance, ::HTMachineRecipeNew)
        }

        @JvmField
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, HTMachineRecipeNew> = PacketCodec.tuple(
            HTMachineDefinition.PACKET_CODEC,
            HTMachineRecipeNew::definition,
            HTIngredientNew.ITEM_PACKET_CODEC,
            HTMachineRecipeNew::catalyst,
            HTIngredientNew.ITEM_PACKET_CODEC.toList(),
            HTMachineRecipeNew::itemInputs,
            HTIngredientNew.FLUID_PACKET_CODEC.toList(),
            HTMachineRecipeNew::fluidInputs,
            HTRecipeResultNew.ITEM_PACKET_CODEC.toList(),
            HTMachineRecipeNew::itemOutputs,
            HTRecipeResultNew.FLUID_PACKET_CODEC.toList(),
            HTMachineRecipeNew::fluidOutputs,
            ::HTMachineRecipeNew,
        )
    }

    init {
        check(itemInputs.isNotEmpty() || fluidInputs.isNotEmpty()) {
            "Either item or fluid inputs must not be empty!"
        }
        check(itemOutputs.isNotEmpty() || fluidOutputs.isNotEmpty()) {
            "Either item or fluid outputs must not be empty!"
        }
    }

    override fun matches(input: Input, world: World): Boolean {
        TODO("Not yet implemented")
    }

    override fun craft(input: Input, lookup: RegistryWrapper.WrapperLookup): ItemStack = getResult(lookup)

    override fun fits(width: Int, height: Int): Boolean = true

    override fun getResult(registriesLookup: RegistryWrapper.WrapperLookup): ItemStack =
        itemOutputs.getOrNull(0)?.resourceAmount?.itemStack ?: ItemStack.EMPTY

    override fun isIgnoredInRecipeBook(): Boolean = true

    override fun showNotification(): Boolean = false

    override fun createIcon(): ItemStack = definition.iconStack

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.MACHINE_NEW

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.MACHINE_NEW

    //    Input    //

    class Input(
        val pos: BlockPos,
        val firstItem: ResourceAmount<ItemVariant>,
        val secondItem: ResourceAmount<ItemVariant>,
        val firstFluid: ResourceAmount<FluidVariant>,
        val secondFluid: ResourceAmount<FluidVariant>,
    ) : RecipeInput {
        override fun getStackInSlot(slot: Int): ItemStack = when (slot) {
            0 -> firstItem.itemStack
            1 -> secondItem.itemStack
            2 -> firstFluid.bucketStack
            3 -> secondFluid.bucketStack
            else -> ItemStack.EMPTY
        }

        override fun getSize(): Int = 4
    }
}
