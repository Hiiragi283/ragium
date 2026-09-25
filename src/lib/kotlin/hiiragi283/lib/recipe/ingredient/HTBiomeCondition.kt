package hiiragi283.lib.recipe.ingredient

import com.mojang.serialization.Codec
import hiiragi283.lib.data.buildDataPatch
import hiiragi283.lib.recipe.display.SlotDisplay
import hiiragi283.lib.serialization.network.HTStreamCodecs
import hiiragi283.lib.text.HTCommonTranslation
import hiiragi283.lib.text.toText
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.tags.TagKey
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.display.SlotDisplay
import net.minecraft.world.level.biome.Biome
import java.util.function.Predicate

/**
 * [HolderSet]に基づいて[バイオーム][Biome]を判定するクラスです。
 *
 * 参照 : [Minecraft - Ingredient][net.minecraft.world.item.crafting.Ingredient]
 * @author Hiiragi Tsubasa
 * @since 26.1.7
 */
@JvmRecord
data class HTBiomeCondition(val biomes: HolderSet<Biome>) : Predicate<Holder<Biome>> {
    companion object {
        @JvmField
        val CODEC: Codec<HTBiomeCondition> = Biome.LIST_CODEC.xmap(::HTBiomeCondition, HTBiomeCondition::biomes)

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTBiomeCondition> =
            HTStreamCodecs.holderSet(Registries.BIOME).map(::HTBiomeCondition, HTBiomeCondition::biomes)
    }

    init {
        if (this@HTBiomeCondition.biomes.isImmediatelyResolvable) {
            this@HTBiomeCondition.biomes.unwrap().ifRight { holders: List<Holder<Biome>> ->
                if (holders.isEmpty()) {
                    throw UnsupportedOperationException("Biomes can't be empty")
                }
            }
        }
    }

    val isEmpty: Boolean get() = this@HTBiomeCondition.biomes.size() == 0

    override fun test(biome: Holder<Biome>): Boolean = biome in this@HTBiomeCondition.biomes

    /**
     * 一致するバイオームのプレビュー
     */
    val display: SlotDisplay get() = this@HTBiomeCondition.biomes.unwrap().map(
        { tagKey: TagKey<Biome> -> createDisplay("#${tagKey.location()}") },
        { holders: Iterable<Holder<Biome>> ->
            holders
                .mapNotNull { it.key?.identifier()?.toString() }
                .map(::createDisplay)
                .let(::SlotDisplay)
        }
    )

    private fun createDisplay(biomeKey: String): SlotDisplay = biomeKey
        .toText()
        .let(HTCommonTranslation.BIOME::translate)
        .let { ItemStackTemplate(Items.COMPASS, buildDataPatch { set(DataComponents.ITEM_NAME, it) }) }
        .let(SlotDisplay::ItemStackSlotDisplay)
}
