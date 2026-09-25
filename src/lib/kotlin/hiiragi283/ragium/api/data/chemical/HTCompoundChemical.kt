@file:OptIn(ExperimentalContracts::class)

package hiiragi283.ragium.api.data.chemical

import com.mojang.serialization.MapCodec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.collection.Nel
import hiiragi283.lib.collection.toNel
import hiiragi283.lib.serialization.codec.HTCodecs
import hiiragi283.lib.serialization.codec.nelOrElement
import hiiragi283.lib.serialization.network.HTStreamCodecs
import hiiragi283.lib.serialization.network.nelOf
import hiiragi283.lib.util.Either
import hiiragi283.ragium.api.RagiumRegistries
import hiiragi283.ragium.api.data.element.HTElement
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import net.minecraft.core.Holder
import net.minecraft.core.HolderGetter
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceKey
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * 化合物を表す[HTChemical]の実装クラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.8
 */
@JvmRecord
data class HTCompoundChemical(val composition: Nel<Pair<Either<Holder<HTChemical>, Holder<HTElement>>, Int>>) :
    HTChemical {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTCompoundChemical> = HTCodecs.mapPair(
            HTCodecs.mapEither(
                HTChemical.HOLDER_CODEC.fieldOf(HTConstants.CHEMICAL),
                HTElement.HOLDER_CODEC.fieldOf(HTConstants.ELEMENT)
            ),
            HTChemical.WEIGHT_CODEC
        ).codec().nelOrElement().fieldOf("composition").xmap(::HTCompoundChemical, HTCompoundChemical::composition)

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTCompoundChemical> = StreamCodec.composite(
            HTStreamCodecs.pair(
                HTStreamCodecs.either(HTChemical.HOLDER_STREAM_CODEC, HTElement.HOLDER_STREAM_CODEC),
                ByteBufCodecs.VAR_INT
            ).nelOf(),
            HTCompoundChemical::composition,
            ::HTCompoundChemical
        )

        @JvmField
        val TYPE: HTChemical.Type<HTCompoundChemical> = HTChemical.Type(CODEC, STREAM_CODEC)

        @JvmStatic
        inline fun build(context: BootstrapContext<*>, builderAction: Builder.() -> Unit): HTCompoundChemical {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            return Builder(context).apply(builderAction).build()
        }
    }

    override fun type(): HTChemical.Type<out HTChemical> = TYPE

    override fun getChemicalFormula(): String = composition.joinToString(
        separator = "",
        transform = { (either: Either<Holder<HTChemical>, Holder<HTElement>>, weight: Int) ->
            buildString {
                append(
                    either.fold(
                        { chemical: Holder<HTChemical> -> "(${chemical.value().getChemicalFormula()})" },
                        { element: Holder<HTElement> -> element.value().symbol }
                    )
                )
                append(HTChemical.format(weight))
            }
        }
    )

    class Builder(
        private val chemicalGetter: HolderGetter<HTChemical>,
        private val elementGetter: HolderGetter<HTElement>
    ) {
        constructor(context: BootstrapContext<*>) : this(
            context.lookup(RagiumRegistries.Keys.CHEMICAL),
            context.lookup(RagiumRegistries.Keys.ELEMENT)
        )

        private val composition: MutableList<Pair<Either<Holder<HTChemical>, Holder<HTElement>>, Int>> =
            ObjectArrayList()

        @JvmName("addChemical")
        fun add(chemical: ResourceKey<HTChemical>, weight: Int): Builder = apply {
            composition += Either.Left(chemicalGetter.getOrThrow(chemical)) to weight
        }

        @JvmName("addElement")
        fun add(element: ResourceKey<HTElement>, weight: Int): Builder = apply {
            composition += Either.Right(elementGetter.getOrThrow(element)) to weight
        }

        fun build(): HTCompoundChemical = HTCompoundChemical(composition.toNel())
    }
}
