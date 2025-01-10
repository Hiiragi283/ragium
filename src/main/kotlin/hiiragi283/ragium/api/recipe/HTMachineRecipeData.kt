package hiiragi283.ragium.api.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.forOptionalGetter
import hiiragi283.ragium.api.extension.toList
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import java.util.*
import kotlin.jvm.optionals.getOrNull

/**
 * 機械レシピのインプット/アウトプットをまとめたクラス
 * @param itemIngredients アイテムの材料の一覧
 * @param fluidIngredients 液体の材料の一覧
 * @param catalyst 触媒となる[HTItemIngredient]
 * @param itemResults アイテムの完成品の一覧
 * @param fluidResults 液体の完成品の一覧
 */
data class HTMachineRecipeData(
    val itemIngredients: List<HTItemIngredient>,
    val fluidIngredients: List<HTFluidIngredient>,
    val catalyst: HTItemIngredient?,
    val itemResults: List<HTItemResult>,
    val fluidResults: List<HTFluidResult>,
) {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTMachineRecipeData> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTItemIngredient.CODEC
                        .listOf()
                        .optionalFieldOf("item_inputs", listOf())
                        .forGetter(HTMachineRecipeData::itemIngredients),
                    HTFluidIngredient.CODEC
                        .listOf()
                        .optionalFieldOf("fluid_inputs", listOf())
                        .forGetter(HTMachineRecipeData::fluidIngredients),
                    HTItemIngredient.CODEC
                        .optionalFieldOf("catalyst")
                        .forOptionalGetter(HTMachineRecipeData::catalyst),
                    HTItemResult.CODEC
                        .listOf()
                        .optionalFieldOf("item_outputs", listOf())
                        .forGetter(HTMachineRecipeData::itemResults),
                    HTFluidResult.CODEC
                        .listOf()
                        .optionalFieldOf("fluid_outputs", listOf())
                        .forGetter(HTMachineRecipeData::fluidResults),
                ).apply(instance, ::HTMachineRecipeData)
        }

        @JvmField
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, HTMachineRecipeData> = PacketCodec.tuple(
            HTItemIngredient.PACKET_CODEC.toList(),
            HTMachineRecipeData::itemIngredients,
            HTFluidIngredient.PACKET_CODEC.toList(),
            HTMachineRecipeData::fluidIngredients,
            PacketCodecs.optional(HTItemIngredient.PACKET_CODEC),
            { Optional.ofNullable(it.catalyst) },
            HTItemResult.PACKET_CODEC.toList(),
            HTMachineRecipeData::itemResults,
            HTFluidResult.PACKET_CODEC.toList(),
            HTMachineRecipeData::fluidResults,
            ::HTMachineRecipeData,
        )
    }

    constructor(
        itemInputs: List<HTItemIngredient>,
        fluidInputs: List<HTFluidIngredient>,
        catalyst: Optional<HTItemIngredient>,
        itemOutputs: List<HTItemResult>,
        fluidOutputs: List<HTFluidResult>,
    ) : this(
        itemInputs,
        fluidInputs,
        catalyst.getOrNull(),
        itemOutputs,
        fluidOutputs,
    )

    /**
     * このデータのアウトプットが有効かどうか判定します。
     * @param checkTag trueの場合，タグをベースとしたアウトプットが有効な値と紐づいているか判定
     * @return 有効な場合はtrue
     */
    fun isValidOutput(checkTag: Boolean): Boolean {
        val bool1: Boolean = itemResults.isNotEmpty()
        val bool2: Boolean = fluidResults.isNotEmpty() && fluidResults.none(HTFluidResult::isEmpty)
        val bool12: Boolean = bool1 || bool2
        return when {
            checkTag -> bool12 && itemResults.none(HTItemResult::isEmpty)
            else -> bool12
        }
    }
}
