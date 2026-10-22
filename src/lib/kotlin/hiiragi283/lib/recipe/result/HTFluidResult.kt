package hiiragi283.lib.recipe.result

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.fluid.HTFlowingFluidHelper
import hiiragi283.lib.item.alchemy.HTPotionHelper
import hiiragi283.lib.serialization.codec.HTCodecs
import hiiragi283.lib.serialization.codec.nelOf
import hiiragi283.lib.serialization.network.HTStreamCodecs
import hiiragi283.lib.serialization.network.nelOf
import hiiragi283.lib.util.DFUEither
import hiiragi283.lib.util.Ior
import hiiragi283.lib.util.IorNel
import hiiragi283.lib.util.fold
import hiiragi283.ragium.api.RagiumRegistries
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.Identifier
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.util.NeoForgeExtraCodecs
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidStackTemplate
import net.neoforged.neoforge.fluids.FluidType
import java.util.Optional

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

    override fun getId(): Identifier? = entry.getId()

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

        fun getId(): Identifier?
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
                    HTFlowingFluidHelper.SOURCE_FLUID_HOLDER_CODEC
                        .fieldOf(HTConstants.ID)
                        .forGetter(SimpleEntry::fluid),
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

        override fun getId(): Identifier = fluid.key().identifier()
    }

    @JvmRecord
    data class PotionEntry(
        val content: IorNel<MobEffectInstance, Holder<Potion>>,
        val customColor: Optional<Int>,
        val customName: Optional<String>
    ) : Entry {
        companion object {
            @JvmField
            val CODEC: MapCodec<PotionEntry> = HTCodecs.recordMap { instance ->
                instance.group(
                    HTCodecs.ior(
                        MobEffectInstance.CODEC.nelOf().fieldOf("custom_effects"),
                        Potion.CODEC.fieldOf("potion")
                    ).forGetter(PotionEntry::content),
                    Codec.INT.optionalFieldOf("custom_color").forGetter(PotionEntry::customColor),
                    Codec.STRING.optionalFieldOf("custom_name").forGetter(PotionEntry::customName)
                ).apply(instance, ::PotionEntry)
            }

            @JvmField
            val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, PotionEntry> = StreamCodec.composite(
                HTStreamCodecs.ior(
                    MobEffectInstance.STREAM_CODEC.nelOf(),
                    Potion.STREAM_CODEC
                ),
                PotionEntry::content,
                ByteBufCodecs.optional(ByteBufCodecs.VAR_INT),
                PotionEntry::customColor,
                ByteBufCodecs.optional(ByteBufCodecs.STRING_UTF8),
                PotionEntry::customName,
                ::PotionEntry
            )

            @JvmField
            val TYPE: HTFluidResultType<PotionEntry> = HTFluidResultType(CODEC, STREAM_CODEC)
        }

        constructor(potion: Holder<Potion>) : this(Ior.Right(potion), Optional.empty(), Optional.empty())

        override fun type(): HTFluidResultType<*> = TYPE

        override fun create(amount: Int): FluidStack =
            HTPotionHelper.createFluid(toVanilla(), amount).fold(FluidStackTemplate::create) { FluidStack.EMPTY }

        private fun toVanilla(): PotionContents = PotionContents(
            Optional.ofNullable(content.getRight()),
            customColor,
            content.getLeft() ?: listOf(),
            customName
        )

        override fun getId(): Identifier? = content.getRight()?.keyOrNull()?.identifier()
    }
}

//    Extensions    //

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.5
 */
fun HTFluidResult?.createOrEmpty(): FluidStack = this?.create() ?: FluidStack.EMPTY

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.5
 */
fun Optional<HTFluidResult>.createOrEmpty(): FluidStack = this.fold(FluidStack::EMPTY, HTFluidResult::create)
