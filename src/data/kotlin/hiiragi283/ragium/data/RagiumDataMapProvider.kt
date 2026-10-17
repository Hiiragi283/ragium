package hiiragi283.ragium.data

import hiiragi283.lib.collection.forEach
import hiiragi283.lib.tag.BlockItemTag
import hiiragi283.lib.tag.CommonTagPrefixes
import hiiragi283.lib.tag.HTMaterialLike
import hiiragi283.lib.tag.HTTagPrefix
import hiiragi283.ragium.api.RagiumRegistries
import hiiragi283.ragium.api.data.chemical.HTChemical
import hiiragi283.ragium.api.data.chemical.RagiumChemicals
import hiiragi283.ragium.api.data.map.HTChemicalData
import hiiragi283.ragium.api.data.map.RagiumDataMaps
import hiiragi283.ragium.api.material.HTBlockPart
import hiiragi283.ragium.api.material.HTItemPart
import hiiragi283.ragium.api.material.HTStorageBlockPart
import hiiragi283.ragium.api.material.RagiumMaterial
import hiiragi283.ragium.api.tag.RagiumTags
import hiiragi283.ragium.common.block.RagiumBlocks
import hiiragi283.ragium.common.item.RagiumItems
import net.minecraft.core.Holder
import net.minecraft.core.HolderGetter
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.neoforged.neoforge.common.conditions.ICondition
import net.neoforged.neoforge.common.data.DataMapProvider
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps
import java.util.concurrent.CompletableFuture

class RagiumDataMapProvider(packOutput: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>) :
    DataMapProvider(packOutput, lookupProvider) {
    override fun gather(provider: HolderLookup.Provider) {
        furnaceFuel()

        chemical(provider)
        mortarRepair()
    }

    private fun furnaceFuel() {
        val builder: Builder<FurnaceFuel, Item> = builder(NeoForgeDataMaps.FURNACE_FUELS)
            .add(RagiumItems.BAMBOO_CHARCOAL, FurnaceFuel(200 * 6), false)
            .add(RagiumItems.COAL_COKE, FurnaceFuel(200 * 16), false)
            .add(RagiumItems.TAR, FurnaceFuel(200 * 4), false)
            .add(RagiumItems.PITCH_COKE, FurnaceFuel(200 * 12), false)

        for (fuel: RagiumMaterial.Fuel in RagiumMaterial.Fuel.entries) {
            val time: Int = when (fuel) {
                RagiumMaterial.Fuel.COAL -> 200
                RagiumMaterial.Fuel.CHARCOAL -> 200
                RagiumMaterial.Fuel.COAL_COKE -> 400
                RagiumMaterial.Fuel.PITCH_COKE -> 300
            }
            RagiumBlocks.STORAGE_BLOCKS[fuel]?.item?.let { storage: Holder<Item> ->
                builder.add(storage, FurnaceFuel(time * 80), false)
            }
            RagiumItems.MATERIAL_ITEMS[HTItemPart.TINY, fuel]?.let { tiny: Holder<Item> ->
                builder.add(tiny, FurnaceFuel(time), false)
            }
        }
    }

    private fun chemical(provider: HolderLookup.Provider) {
        val getter: HolderGetter<HTChemical> = provider.lookupOrThrow(RagiumRegistries.Keys.CHEMICAL)
        val builder: Builder<HTChemicalData, Item> = builder(RagiumDataMaps.CHEMICAL)
            // Fuel
            .add(
                CommonTagPrefixes.STORAGE_BLOCK,
                RagiumMaterial.Fuel.COAL,
                HTChemicalData(getter.getOrThrow(RagiumChemicals.COAL), 9)
            ).add(
                Items.COAL,
                HTChemicalData(getter.getOrThrow(RagiumChemicals.COAL), 1)
            ).add(
                Items.CHARCOAL,
                HTChemicalData(getter.getOrThrow(RagiumChemicals.CHARCOAL), 1)
            ).add(
                RagiumItems.COAL_COKE,
                HTChemicalData(getter.getOrThrow(RagiumChemicals.CARBON), 1),
                false
            ).add(
                RagiumItems.PITCH_COKE,
                HTChemicalData(getter.getOrThrow(RagiumChemicals.CARBON), 1),
                false
            )
            // Gem
            .add(
                RagiumTags.BlockItems.QUARTZ_BLOCKS,
                HTChemicalData(getter.getOrThrow(RagiumChemicals.SILICON_DIOXIDE), 4)
            ).add(
                Blocks.AMETHYST_BLOCK.asItem(),
                HTChemicalData(getter.getOrThrow(RagiumChemicals.SILICON_DIOXIDE), 4)
            ).add(
                CommonTagPrefixes.STORAGE_BLOCK,
                RagiumMaterial.Gem.DIAMOND,
                HTChemicalData(getter.getOrThrow(RagiumChemicals.DIAMOND), 9)
            ).add(
                RagiumBlocks.FLUORITE_BLOCK.item,
                HTChemicalData(getter.getOrThrow(RagiumChemicals.FLUORITE), 4),
                false
            )
            // Metal
            .add(
                CommonTagPrefixes.STORAGE_BLOCK,
                RagiumMaterial.Metal.COPPER,
                HTChemicalData(getter.getOrThrow(RagiumChemicals.COPPER), 9)
            ).add(
                CommonTagPrefixes.STORAGE_BLOCK,
                RagiumMaterial.Metal.IRON,
                HTChemicalData(getter.getOrThrow(RagiumChemicals.IRON), 9)
            ).add(
                CommonTagPrefixes.STORAGE_BLOCK,
                RagiumMaterial.Metal.GOLD,
                HTChemicalData(getter.getOrThrow(RagiumChemicals.GOLD), 9)
            )
            // Other
            .add(Items.SNOWBALL, HTChemicalData(getter.getOrThrow(RagiumChemicals.WATER), 0.25f))
            .add(Items.SNOW_BLOCK, HTChemicalData(getter.getOrThrow(RagiumChemicals.WATER), 1))
            .add(Items.ICE, HTChemicalData(getter.getOrThrow(RagiumChemicals.WATER), 1))
            .add(Items.PACKED_ICE, HTChemicalData(getter.getOrThrow(RagiumChemicals.WATER), 9))
            .add(Items.BLUE_ICE, HTChemicalData(getter.getOrThrow(RagiumChemicals.WATER), 81))
            .add(
                RagiumItems.CRUDE_SILICON,
                HTChemicalData(getter.getOrThrow(RagiumChemicals.SILICON), 1),
                false
            ).add(
                RagiumItems.SODIUM_HYDROXIDE,
                HTChemicalData(getter.getOrThrow(RagiumChemicals.SODIUM_HYDROXIDE), 1),
                false
            )

        RagiumBlocks.MATERIAL_BLOCKS.forEach { (part: HTBlockPart, material: RagiumMaterial, _) ->
            val scale: Int = when (part) {
                is HTStorageBlockPart -> 9
                else -> return@forEach
            }
            val chemical: Holder<HTChemical> = material.chemicalKey?.let(getter::getOrThrow) ?: return@forEach
            builder.add(part.tagPrefix, material, HTChemicalData(chemical, scale))
        }
        for (part: HTItemPart in HTItemPart.entries) {
            val scale: Float = when (part) {
                HTItemPart.DUST -> 1f
                HTItemPart.GEAR -> 4f
                HTItemPart.GEM -> 1f
                HTItemPart.INGOT -> 1f
                HTItemPart.NUGGET -> 0.111f
                HTItemPart.RAW -> continue
                HTItemPart.TINY -> 0.125f
            }
            for (material: RagiumMaterial in RagiumMaterial.entries) {
                val chemical: Holder<HTChemical> = material.chemicalKey?.let(getter::getOrThrow) ?: continue
                builder.add(part.tagPrefix, material, HTChemicalData(chemical, scale))
            }
        }
    }

    private fun mortarRepair() {
        builder(RagiumDataMaps.MORTAR_REPAIR)
            .add(Blocks.CRACKED_DEEPSLATE_BRICKS, Blocks.DEEPSLATE_BRICKS)
            .add(Blocks.CRACKED_DEEPSLATE_TILES, Blocks.DEEPSLATE_TILES)
            .add(Blocks.CRACKED_NETHER_BRICKS, Blocks.NETHER_BRICKS)
            .add(Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS, Blocks.POLISHED_BLACKSTONE_BRICKS)
            .add(Blocks.CRACKED_STONE_BRICKS, Blocks.STONE_BRICKS)
            .add(Blocks.INFESTED_CRACKED_STONE_BRICKS, Blocks.INFESTED_STONE_BRICKS)
    }

    // Block
    @Suppress("DEPRECATION")
    private fun <T : Any> Builder<T, Block>.add(
        block: Block,
        value: T,
        replace: Boolean = false,
        vararg conditions: ICondition
    ): Builder<T, Block> = this.add(block.builtInRegistryHolder(), value, replace, *conditions)

    @JvmName("addBlock")
    private fun <T : Any> Builder<T, Block>.add(
        tag: BlockItemTag,
        value: T,
        replace: Boolean = false,
        vararg conditions: ICondition
    ): Builder<T, Block> = this.add(tag.block, value, replace, *conditions)

    @JvmName("addBlock")
    private fun <T : Any> Builder<T, Block>.add(
        prefix: HTTagPrefix,
        material: HTMaterialLike,
        value: T,
        replace: Boolean = false,
        vararg conditions: ICondition
    ): Builder<T, Block> = this.add(prefix.materialTag(material), value, replace, *conditions)

    // Item
    @Suppress("DEPRECATION")
    private fun <T : Any> Builder<T, Item>.add(
        item: Item,
        value: T,
        replace: Boolean = false,
        vararg conditions: ICondition
    ): Builder<T, Item> = this.add(item.builtInRegistryHolder(), value, replace, *conditions)

    @JvmName("addItem")
    private fun <T : Any> Builder<T, Item>.add(
        tag: BlockItemTag,
        value: T,
        replace: Boolean = false,
        vararg conditions: ICondition
    ): Builder<T, Item> = this.add(tag.item, value, replace, *conditions)

    @JvmName("addItem")
    private fun <T : Any> Builder<T, Item>.add(
        prefix: HTTagPrefix,
        material: HTMaterialLike,
        value: T,
        replace: Boolean = false,
        vararg conditions: ICondition
    ): Builder<T, Item> = this.add(prefix.materialTag(material), value, replace, *conditions)
}
