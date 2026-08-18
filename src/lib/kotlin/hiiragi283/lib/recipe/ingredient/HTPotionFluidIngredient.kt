package hiiragi283.lib.recipe.ingredient

import com.mojang.serialization.MapCodec
import hiiragi283.lib.HTPhysicalSideHelper
import hiiragi283.lib.fluid.FluidInstanceBuilder
import hiiragi283.lib.item.alchemy.BottledPotionContents
import hiiragi283.lib.item.alchemy.HTBottleType
import hiiragi283.lib.item.alchemy.HTPotionFluidManager
import hiiragi283.lib.item.alchemy.HTPotionHelper
import hiiragi283.lib.serialization.codec.HTCodecs
import hiiragi283.lib.serialization.network.HTStreamCodecs
import java.util.Objects
import java.util.stream.Stream
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.item.alchemy.Potions
import net.minecraft.world.item.crafting.display.SlotDisplay
import net.minecraft.world.level.material.Fluid
import net.minecraft.world.level.material.Fluids
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.fluids.crafting.FluidIngredient
import net.neoforged.neoforge.fluids.crafting.FluidIngredientType
import net.neoforged.neoforge.fluids.crafting.display.FluidStackSlotDisplay

/**
 * [HTPotionFluidManager]に基づいて液体ポーションを扱う[FluidIngredient]の実装クラスです。
 * @param potions 対象となるポーションの一覧
 * @param bottleType 対象となるポーション瓶の種類
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
class HTPotionFluidIngredient(val potions: HolderSet<Potion>, val bottleType: HTBottleType) : FluidIngredient() {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTPotionFluidIngredient> = HTCodecs.recordMap { instance ->
            instance
                .group(
                    HTCodecs.holderSet(Registries.POTION).fieldOf("potions").forGetter(HTPotionFluidIngredient::potions),
                    HTBottleType.CODEC.fieldOf("bottle_type").forGetter(HTPotionFluidIngredient::bottleType),
                ).apply(instance, ::HTPotionFluidIngredient)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTPotionFluidIngredient> = StreamCodec.composite(
            HTStreamCodecs.holderSet(Registries.POTION),
            HTPotionFluidIngredient::potions,
            HTBottleType.STREAM_CODEC,
            HTPotionFluidIngredient::bottleType,
            ::HTPotionFluidIngredient,
        )

        @JvmField
        val TYPE: FluidIngredientType<HTPotionFluidIngredient> = FluidIngredientType(CODEC, STREAM_CODEC)
    }

    constructor(potion: Holder<Potion>) : this(HolderSet.direct(potion), HTBottleType.DEFAULT)

    override fun test(fluidStack: FluidStack): Boolean {
        val contents: BottledPotionContents = HTPotionHelper.getContents(fluidStack) ?: return false
        if (contents.bottleType != bottleType) return false
        return contents.potion?.let(potions::contains) ?: false
    }

    @Suppress("DEPRECATION")
    override fun generateFluids(): Stream<Holder<Fluid>> = HTPotionFluidManager.handlers.keys.stream().map { it.builtInRegistryHolder() }

    override fun display(): SlotDisplay = HTPotionFluidManager.handlers
        .flatMap { (fluid: Fluid, handler: HTPotionFluidManager.Handler) ->
            potions
                .filter { it.value().isEnabled(HTPhysicalSideHelper.getFeatureFlags()) }
                .map { potion: Holder<Potion> ->
                    when (potion) {
                        Potions.WATER -> FluidStack(Fluids.WATER, FluidType.BUCKET_VOLUME)
                        else -> FluidInstanceBuilder.buildStack {
                            +fluid
                            components {
                                set(DataComponents.POTION_CONTENTS, PotionContents(potion))
                                handler[this] = bottleType
                            }
                        }
                    }
                }
        }.map(::FluidStackSlotDisplay)
        .let(SlotDisplay::Composite)

    override fun isSimple(): Boolean = false

    override fun getType(): FluidIngredientType<*> = TYPE

    override fun hashCode(): Int = Objects.hash(potions, bottleType)

    override fun equals(obj: Any?): Boolean = (obj as? HTPotionFluidIngredient)?.let {
        it.potions == this.potions && it.bottleType == this.bottleType
    } ?: false

    override fun toString(): String = "HTPotionFluidIngredient(potions=$potions, bottleType=$bottleType)"
}
