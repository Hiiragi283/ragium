package hiiragi283.lib.recipe.result

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.item.alchemy.BottledPotionContents
import hiiragi283.lib.registry.getKeyOrThrow
import hiiragi283.lib.serialization.codec.HTCodecs
import hiiragi283.lib.util.DFUEither
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumRegistries
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.Identifier
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.util.NeoForgeExtraCodecs
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidStackTemplate
import net.neoforged.neoforge.fluids.FluidType

/**
 * 液体の完成品を表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
@JvmRecord
data class HTFluidResult(val entry: Entry, val amount: Int) : HTRecipeResult<FluidStack> {
    companion object {
        @JvmField
        val MAP_CODEC: MapCodec<HTFluidResult> = HTCodecs.recordMap { instance ->
            instance.group(
                Entry.MAP_CODEC.forGetter(HTFluidResult::entry),
                HTCodecs.POSITIVE_INT.fieldOf(HTConstants.AMOUNT).forGetter(HTFluidResult::amount)
            ).apply(instance, ::HTFluidResult)
        }

        @JvmField
        val CODEC: Codec<HTFluidResult> =
            Codec.withAlternative(MAP_CODEC.codec(), Entry.MAP_CODEC.codec()) { it.toResult() }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTFluidResult> = StreamCodec.composite(
            Entry.STREAM_CODEC,
            HTFluidResult::entry,
            ByteBufCodecs.VAR_INT,
            HTFluidResult::amount,
            ::HTFluidResult
        )
    }

    constructor(template: FluidStackTemplate) : this(SimpleEntry(template), template.amount())

    constructor(stack: FluidStack) : this(SimpleEntry(stack), stack.amount())

    /**
     * このインスタンスのコピーを作成します。
     * @param newAmount 新しい量
     */
    fun copyWithAmount(newAmount: Int): HTFluidResult = HTFluidResult(entry, newAmount)

    override fun create(): FluidStack = entry.create(amount)

    override fun getId(): Identifier = entry.getId()

    //    Entry    //

    interface Entry {
        companion object {
            @JvmField
            val MAP_CODEC: MapCodec<Entry> = NeoForgeExtraCodecs.dispatchMapOrElse(
                RagiumRegistries.FLUID_RESULT_TYPE.byNameCodec(),
                Entry::type,
                HTFluidResultType<*>::codec,
                SimpleEntry.CODEC
            ).xmap(
                { DFUEither.unwrap(it) },
                { entry: Entry ->
                    when (entry) {
                        is SimpleEntry -> DFUEither.right(entry)
                        else -> DFUEither.left(entry)
                    }
                }
            )

            @JvmField
            val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, Entry> =
                ByteBufCodecs.registry(RagiumRegistries.Keys.FLUID_RESULT_TYPE)
                    .dispatch(Entry::type, HTFluidResultType<*>::streamCodec)
        }

        fun type(): HTFluidResultType<*>

        fun create(amount: Int = FluidType.BUCKET_VOLUME): FluidStack

        fun toResult(amount: Int = FluidType.BUCKET_VOLUME): HTFluidResult = HTFluidResult(this, amount)

        fun getId(): Identifier
    }

    @JvmRecord
    data class SimpleEntry @JvmOverloads constructor(
        val fluid: Holder<Fluid>,
        val components: DataComponentPatch = DataComponentPatch.EMPTY
    ) : Entry {
        companion object {
            @JvmField
            val CODEC: MapCodec<SimpleEntry> = HTCodecs.recordMap { instance ->
                instance.group(
                    FluidStack.FLUID_HOLDER_CODEC.fieldOf(HTConstants.ID).forGetter(SimpleEntry::fluid),
                    DataComponentPatch.CODEC
                        .optionalFieldOf(HTConstants.COMPONENTS, DataComponentPatch.EMPTY)
                        .forGetter(SimpleEntry::components)
                ).apply(instance, ::SimpleEntry)
            }

            @JvmField
            val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, SimpleEntry> = StreamCodec.composite(
                FluidStack.FLUID_HOLDER_STREAM_CODEC,
                SimpleEntry::fluid,
                DataComponentPatch.STREAM_CODEC,
                SimpleEntry::components,
                ::SimpleEntry
            )

            @JvmField
            val TYPE: HTFluidResultType<SimpleEntry> = HTFluidResultType(CODEC, STREAM_CODEC)
        }

        constructor(template: FluidStackTemplate) : this(template.fluid(), template.components())

        constructor(stack: FluidStack) : this(stack.typeHolder(), stack.componentsPatch)

        override fun type(): HTFluidResultType<*> = TYPE

        override fun create(amount: Int): FluidStack = FluidStack(fluid, amount, components)

        override fun getId(): Identifier = fluid.getKeyOrThrow().identifier()
    }

    @JvmRecord
    data class PotionEntry(val contents: BottledPotionContents) : Entry {
        companion object {
            @JvmField
            val CODEC: MapCodec<PotionEntry> =
                BottledPotionContents.MAP_CODEC.xmap(::PotionEntry, PotionEntry::contents)

            @JvmField
            val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, PotionEntry> =
                BottledPotionContents.STREAM_CODEC.map(::PotionEntry, PotionEntry::contents)

            @JvmField
            val TYPE: HTFluidResultType<PotionEntry> = HTFluidResultType(CODEC, STREAM_CODEC)
        }

        override fun type(): HTFluidResultType<*> = TYPE

        override fun create(amount: Int): FluidStack = contents.toFluidStack(amount)

        override fun getId(): Identifier =
            contents.potion?.getKeyOrThrow()?.identifier() ?: RagiumAPI.id(HTConstants.POTION)
    }
}
