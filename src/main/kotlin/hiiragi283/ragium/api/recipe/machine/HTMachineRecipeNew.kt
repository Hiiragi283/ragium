package hiiragi283.ragium.api.recipe.machine

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.itemStack
import hiiragi283.ragium.api.extension.toList
import hiiragi283.ragium.api.machine.HTMachineConvertible
import hiiragi283.ragium.api.machine.HTMachineDefinition
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.recipe.*
import hiiragi283.ragium.common.init.RagiumRecipeSerializers
import hiiragi283.ragium.common.init.RagiumRecipeTypes
import net.minecraft.item.ItemStack
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.recipe.Recipe
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.recipe.RecipeType
import net.minecraft.recipe.input.RecipeInput
import net.minecraft.registry.RegistryWrapper
import net.minecraft.world.World

sealed class HTMachineRecipeNew<T : RecipeInput>(val definition: HTMachineDefinition) : Recipe<T> {
    companion object {
        @JvmStatic
        fun <T : HTMachineRecipeNew<*>> createCodec(
            function: Function6<HTMachineDefinition, List<HTItemIngredient>, List<HTFluidIngredient>, HTItemIngredient, List<HTItemResult>, List<HTFluidResult>, T>,
        ): MapCodec<T> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTMachineDefinition.CODEC
                        .fieldOf("definition")
                        .forGetter { it.definition },
                    HTItemIngredient.ITEM_CODEC
                        .listOf()
                        .optionalFieldOf("item_inputs", listOf())
                        .forGetter { it.itemInputs },
                    HTFluidIngredient.FLUID_CODEC
                        .listOf()
                        .optionalFieldOf("fluid_inputs", listOf())
                        .forGetter { it.fluidInputs },
                    HTItemIngredient.ITEM_CODEC
                        .optionalFieldOf("catalyst", HTItemIngredient.EMPTY_ITEM)
                        .forGetter { it.catalyst },
                    HTItemResult.ITEM_CODEC
                        .listOf()
                        .optionalFieldOf("item_outputs", listOf())
                        .forGetter { it.itemOutputs },
                    HTFluidResult.FLUID_CODEC
                        .listOf()
                        .optionalFieldOf("fluid_outputs", listOf())
                        .forGetter { it.fluidOutputs },
                ).apply(instance, function)
        }

        @JvmStatic
        fun <T : HTMachineRecipeNew<*>> createPacketCodec(
            function: Function6<HTMachineDefinition, List<HTItemIngredient>, List<HTFluidIngredient>, HTItemIngredient, List<HTItemResult>, List<HTFluidResult>, T>,
        ): PacketCodec<RegistryByteBuf, T> = PacketCodec.tuple(
            HTMachineDefinition.PACKET_CODEC,
            { it.definition },
            HTItemIngredient.ITEM_PACKET_CODEC.toList(),
            { it.itemInputs },
            HTFluidIngredient.FLUID_PACKET_CODEC.toList(),
            { it.fluidInputs },
            HTItemIngredient.ITEM_PACKET_CODEC,
            { it.catalyst },
            HTItemResult.ITEM_PACKET_CODEC.toList(),
            { it.itemOutputs },
            HTFluidResult.FLUID_PACKET_CODEC.toList(),
            { it.fluidOutputs },
            function,
        )
    }

    constructor(type: HTMachineConvertible, tier: HTMachineTier) : this(HTMachineDefinition(type, tier))

    val machineType: HTMachineType
        get() = definition.type
    val tier: HTMachineTier
        get() = definition.tier

    abstract val itemInputs: List<HTItemIngredient>
    abstract val fluidInputs: List<HTFluidIngredient>
    abstract val catalyst: HTItemIngredient
    abstract val itemOutputs: List<HTItemResult>
    abstract val fluidOutputs: List<HTFluidResult>

    val firstOutput: ItemStack
        get() = itemOutputs.getOrNull(0)?.resourceAmount?.itemStack ?: ItemStack.EMPTY

    //    Recipe    //

    override fun craft(input: T, lookup: RegistryWrapper.WrapperLookup): ItemStack = getResult(lookup)

    override fun fits(width: Int, height: Int): Boolean = true

    override fun getResult(registriesLookup: RegistryWrapper.WrapperLookup): ItemStack = firstOutput

    final override fun isIgnoredInRecipeBook(): Boolean = true

    final override fun showNotification(): Boolean = false

    final override fun createIcon(): ItemStack = definition.iconStack

    final override fun isEmpty(): Boolean = true

    //    Simple    //

    class Simple(
        definition: HTMachineDefinition,
        override val itemInputs: List<HTItemIngredient>,
        override val fluidInputs: List<HTFluidIngredient>,
        override val catalyst: HTItemIngredient,
        override val itemOutputs: List<HTItemResult>,
        override val fluidOutputs: List<HTFluidResult>,
    ) : HTMachineRecipeNew<HTRecipeInputs.Double>(definition) {
        companion object {
            @JvmField
            val CODEC: MapCodec<Simple> = createCodec(::Simple)

            @JvmField
            val PACKET_CODEC: PacketCodec<RegistryByteBuf, Simple> = createPacketCodec(::Simple)
        }

        override fun matches(input: HTRecipeInputs.Double, world: World): Boolean = false

        override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.SIMPLE_MACHINE

        override fun getType(): RecipeType<*> = RagiumRecipeTypes.SIMPLE_MACHINE
    }

    //    Large    //

    class Large(
        definition: HTMachineDefinition,
        override val itemInputs: List<HTItemIngredient>,
        override val fluidInputs: List<HTFluidIngredient>,
        override val catalyst: HTItemIngredient,
        override val itemOutputs: List<HTItemResult>,
        override val fluidOutputs: List<HTFluidResult>,
    ) : HTMachineRecipeNew<HTRecipeInputs.Double>(definition) {
        companion object {
            @JvmField
            val CODEC: MapCodec<Large> = createCodec(::Large)

            @JvmField
            val PACKET_CODEC: PacketCodec<RegistryByteBuf, Large> = createPacketCodec(::Large)
        }

        init {
        }

        override fun matches(input: HTRecipeInputs.Double, world: World): Boolean = false

        override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.LARGE_MACHINE

        override fun getType(): RecipeType<*> = RagiumRecipeTypes.LARGE_MACHINE
    }
}
