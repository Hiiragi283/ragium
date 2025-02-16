package hiiragi283.ragium.api.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.recipe.base.*
import hiiragi283.ragium.common.init.RagiumRecipeSerializers
import hiiragi283.ragium.common.init.RagiumRecipeTypes
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient
import java.util.*

class HTBreweryRecipe(
    group: String,
    val firstInput: HTItemIngredient,
    val secondInput: HTItemIngredient,
    val thirdInput: Optional<HTItemIngredient>,
    private val potion: Holder<Potion>,
) : HTMachineRecipeBase(group) {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTBreweryRecipe> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTRecipeCodecs.group(),
                    HTItemIngredient.CODEC
                        .fieldOf("first_item_input")
                        .forGetter(HTBreweryRecipe::firstInput),
                    HTItemIngredient.CODEC
                        .fieldOf("second_item_input")
                        .forGetter(HTBreweryRecipe::secondInput),
                    HTItemIngredient.CODEC
                        .optionalFieldOf("third_item_input")
                        .forGetter(HTBreweryRecipe::thirdInput),
                    Potion.CODEC
                        .fieldOf("effect")
                        .forGetter(HTBreweryRecipe::potion),
                ).apply(instance, ::HTBreweryRecipe)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTBreweryRecipe> = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            HTBreweryRecipe::getGroup,
            HTItemIngredient.STREAM_CODEC,
            HTBreweryRecipe::firstInput,
            HTItemIngredient.STREAM_CODEC,
            HTBreweryRecipe::secondInput,
            ByteBufCodecs.optional(HTItemIngredient.STREAM_CODEC),
            HTBreweryRecipe::thirdInput,
            ByteBufCodecs.holderRegistry(Registries.POTION),
            HTBreweryRecipe::potion,
            ::HTBreweryRecipe,
        )

        @JvmField
        val WATER_INGREDIENT: SizedFluidIngredient = SizedFluidIngredient.of(Tags.Fluids.WATER, FluidType.BUCKET_VOLUME)
    }

    override val itemOutputs: List<HTItemOutput> = listOf(
        HTItemOutput.of {
            val stack = ItemStack(Items.POTION, 3)
            stack.set(DataComponents.POTION_CONTENTS, PotionContents(potion))
            stack
        },
    )

    override fun matches(input: HTMachineRecipeInput, level: Level): Boolean {
        val bool1: Boolean = this.firstInput.test(input, 0)
        val bool2: Boolean = this.secondInput.test(input, 1)
        val bool3: Boolean = this.thirdInput.map { it.test(input, 2) }.orElse(true)
        val bool4: Boolean = WATER_INGREDIENT.test(input.getFluid(0))
        return bool1 && bool2 && bool3 && bool4
    }

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.BREWERY.get()

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.BREWERY.get()
}
