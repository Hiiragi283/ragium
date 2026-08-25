package hiiragi283.lib.recipe.display

import com.mojang.serialization.MapCodec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.HTPhysicalSideHelper
import hiiragi283.lib.fluid.FluidInstanceBuilder
import hiiragi283.lib.item.alchemy.HTBottleType
import hiiragi283.lib.item.alchemy.HTPotionFluidManager
import hiiragi283.lib.item.alchemy.HTPotionHelper
import hiiragi283.lib.serialization.codec.HTCodecs
import hiiragi283.lib.serialization.network.HTStreamCodecs
import java.util.stream.Stream
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.util.context.ContextMap
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.item.alchemy.Potions
import net.minecraft.world.item.crafting.display.DisplayContentsFactory
import net.minecraft.world.item.crafting.display.SlotDisplay
import net.minecraft.world.level.material.Fluid
import net.minecraft.world.level.material.Fluids
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.fluids.crafting.display.ForFluidStacks

@JvmRecord
data class HTPotionSlotDisplay(val potions: HolderSet<Potion>, val bottleType: HTBottleType) : SlotDisplay {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTPotionSlotDisplay> = HTCodecs.recordMap { instance ->
            instance
                .group(
                    HTCodecs.holderSet(Registries.POTION).fieldOf(HTConstants.POTIONS).forGetter(HTPotionSlotDisplay::potions),
                    HTBottleType.FIELD_CODEC.forGetter(HTPotionSlotDisplay::bottleType),
                ).apply(instance, ::HTPotionSlotDisplay)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTPotionSlotDisplay> = StreamCodec.composite(
            HTStreamCodecs.holderSet(Registries.POTION),
            HTPotionSlotDisplay::potions,
            HTBottleType.STREAM_CODEC,
            HTPotionSlotDisplay::bottleType,
            ::HTPotionSlotDisplay,
        )

        @JvmField
        val TYPE: SlotDisplay.Type<HTPotionSlotDisplay> = SlotDisplay.Type(CODEC, STREAM_CODEC)
    }

    override fun <T : Any> resolve(context: ContextMap, builder: DisplayContentsFactory<T>): Stream<T> = when (builder) {
        is DisplayContentsFactory.ForStacks<T> ->
            potions.stream()
                .map { HTPotionHelper.createPotion(it, bottleType) }
                .map(ItemStackTemplate::create)
                .map(builder::forStack)
        is ForFluidStacks<T> ->
            HTPotionFluidManager.handlers
                .entries
                .stream()
                .flatMap { (fluid: Fluid, handler: HTPotionFluidManager.Handler) ->
                    potions
                        .stream()
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
                }.map(builder::forStack)
        else -> Stream.empty()
    }

    override fun type(): SlotDisplay.Type<HTPotionSlotDisplay> = TYPE
}
