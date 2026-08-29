package hiiragi283.lib.recipe.ingredient

import com.mojang.serialization.MapCodec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.item.alchemy.BottledPotionContents
import hiiragi283.lib.item.alchemy.HTBottleType
import hiiragi283.lib.item.alchemy.HTPotionFluidManager
import hiiragi283.lib.item.alchemy.HTPotionHelper
import hiiragi283.lib.recipe.display.HTPotionSlotDisplay
import hiiragi283.lib.serialization.codec.HTCodecs
import hiiragi283.lib.serialization.network.HTStreamCodecs
import java.util.stream.Stream
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.crafting.display.SlotDisplay
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.crafting.FluidIngredient
import net.neoforged.neoforge.fluids.crafting.FluidIngredientType

/**
 * [HTPotionFluidManager]に基づいて液体ポーションを扱う[FluidIngredient]の実装クラスです。
 * @param potions 対象となるポーションの一覧
 * @param bottleType 対象となるポーション瓶の種類
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
data class HTPotionFluidIngredient(val potions: HolderSet<Potion>, val bottleType: HTBottleType) : FluidIngredient() {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTPotionFluidIngredient> = HTCodecs.recordMap { instance ->
            instance
                .group(
                    HTCodecs.holderSet(Registries.POTION).fieldOf(HTConstants.POTIONS).forGetter(HTPotionFluidIngredient::potions),
                    HTBottleType.FIELD_CODEC.forGetter(HTPotionFluidIngredient::bottleType),
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
        return contents.bottleType == bottleType && contents.potion?.let(potions::contains) ?: false
    }

    @Suppress("DEPRECATION")
    override fun generateFluids(): Stream<Holder<Fluid>> = HTPotionFluidManager.handlers.keys.stream().map { it.builtInRegistryHolder() }

    override fun display(): SlotDisplay = HTPotionSlotDisplay(potions, bottleType)

    override fun isSimple(): Boolean = false

    override fun getType(): FluidIngredientType<*> = TYPE
}
