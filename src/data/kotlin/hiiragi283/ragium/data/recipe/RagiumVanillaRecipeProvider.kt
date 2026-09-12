package hiiragi283.ragium.data.recipe

import hiiragi283.lib.HTComparators
import hiiragi283.lib.color.HTDefaultColor
import hiiragi283.lib.item.component.HTToolCollection
import hiiragi283.lib.item.component.HTToolType
import hiiragi283.lib.recipe.RecipeKey
import hiiragi283.lib.registry.HTSimpleDeferredBlockAndItem
import hiiragi283.lib.registry.HTSimpleDeferredItem
import hiiragi283.lib.resource.debugPath
import hiiragi283.lib.resource.vanillaId
import hiiragi283.lib.tag.CommonTagPrefixes
import hiiragi283.lib.tag.HTCommonTags
import hiiragi283.lib.tag.HTMaterialLike
import hiiragi283.lib.tag.HTTagPrefix
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.material.HTItemPart
import hiiragi283.ragium.api.material.RagiumMaterial
import hiiragi283.ragium.api.tag.HTMachineType
import hiiragi283.ragium.common.block.RagiumBlocks
import hiiragi283.ragium.common.fluid.RagiumFluids
import hiiragi283.ragium.common.item.RagiumItems
import hiiragi283.ragium.common.item.component.RagiumToolMaterials
import hiiragi283.ragium.common.material.RagiumMaterialHelper
import net.minecraft.advancements.Advancement
import net.minecraft.advancements.AdvancementHolder
import net.minecraft.advancements.Criterion
import net.minecraft.advancements.criterion.InventoryChangeTrigger
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.RecipeProvider
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.data.recipes.ShapelessRecipeBuilder
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder
import net.minecraft.data.recipes.SingleItemRecipeBuilder
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Items
import net.minecraft.world.item.ToolMaterial
import net.minecraft.world.item.crafting.CookingBookCategory
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.conditions.ICondition
import net.neoforged.neoforge.common.crafting.CompoundIngredient
import java.util.concurrent.CompletableFuture

class RagiumVanillaRecipeProvider(registries: HolderLookup.Provider, output: RecipeOutput) :
    RecipeProvider(registries, output) {
    class Runner(packOutput: PackOutput, registries: CompletableFuture<HolderLookup.Provider>) :
        RecipeProvider.Runner(packOutput, registries) {
        override fun createRecipeProvider(registries: HolderLookup.Provider, output: RecipeOutput): RecipeProvider =
            RagiumVanillaRecipeProvider(
                registries,
                object : RecipeOutput {
                    override fun accept(
                        key: RecipeKey,
                        recipe: Recipe<*>,
                        advancement: AdvancementHolder?,
                        vararg conditions: ICondition
                    ) {
                        output.accept(
                            RecipeKey(RagiumAPI.id(key.identifier().path)),
                            recipe,
                            advancement?.let { AdvancementHolder(RagiumAPI.id(it.id().path), it.value()) },
                            *conditions
                        )
                    }

                    override fun advancement(): Advancement.Builder = output.advancement()

                    override fun includeRootAdvancement() {
                        output.includeRootAdvancement()
                    }
                }
            )

        override fun getName(): String = "Vanilla Recipes 2"
    }

    override fun buildRecipes() {
        machine()
        material()

        // Prismarine Bricks -> 9x Prismarine Shard
        shapeless(RecipeCategory.BUILDING_BLOCKS, Items.PRISMARINE_SHARD, 9)
            .requires(Items.PRISMARINE_BRICKS)
            .group(getItemName(Items.PRISMARINE_SHARD))
            .unlockedBy(getHasName(Items.PRISMARINE_BRICKS), has(Items.PRISMARINE_BRICKS))
            .saveSuffixed(output, "_from_bricks")
        // Gunpowder
        shapeless(RecipeCategory.MISC, Items.GUNPOWDER, 3)
            .requires(CommonTagPrefixes.DUST, RagiumMaterial.Fuel.COAL, RagiumMaterial.Fuel.CHARCOAL)
            .requires(CommonTagPrefixes.DUST, RagiumMaterial.Mineral.SULFUR)
            .requires(CommonTagPrefixes.DUST, RagiumMaterial.Mineral.NITER)
            .group(getItemName(Items.GUNPOWDER))
            .unlockedBy(
                getHasName(CommonTagPrefixes.DUST, RagiumMaterial.Mineral.SULFUR),
                has(CommonTagPrefixes.DUST, RagiumMaterial.Mineral.SULFUR)
            ).unlockedBy(
                getHasName(CommonTagPrefixes.DUST, RagiumMaterial.Mineral.NITER),
                has(CommonTagPrefixes.DUST, RagiumMaterial.Mineral.NITER)
            )
            .save(output)
        // Blaze Rod
        shaped(RecipeCategory.MISC, Items.BLAZE_ROD)
            .pattern("AAA")
            .pattern("BBB")
            .pattern("CCC")
            .define('A', CommonTagPrefixes.DUST, RagiumMaterial.Gem.AMETHYST)
            .define('B', Items.MAGMA_BLOCK)
            .define('C', CommonTagPrefixes.DUST, RagiumMaterial.Mineral.SULFUR)
            .group(getItemName(Items.BLAZE_ROD))
            .unlockedBy(
                getHasName(CommonTagPrefixes.DUST, RagiumMaterial.Gem.AMETHYST),
                has(CommonTagPrefixes.DUST, RagiumMaterial.Gem.AMETHYST)
            ).unlockedBy(
                getHasName(Items.MAGMA_BLOCK),
                has(Items.MAGMA_BLOCK)
            )
            .unlockedBy(
                getHasName(CommonTagPrefixes.DUST, RagiumMaterial.Mineral.SULFUR),
                has(CommonTagPrefixes.DUST, RagiumMaterial.Mineral.SULFUR)
            )
            .save(output)
        // Breeze Rod
        shaped(RecipeCategory.MISC, Items.BREEZE_ROD)
            .pattern("AAA")
            .pattern("BBB")
            .pattern("CCC")
            .define('A', CommonTagPrefixes.DUST, RagiumMaterial.Gem.AMETHYST)
            .define('B', Items.ICE)
            .define('C', CommonTagPrefixes.DUST, RagiumMaterial.Mineral.NITER)
            .group(getItemName(Items.BREEZE_ROD))
            .unlockedBy(
                getHasName(CommonTagPrefixes.DUST, RagiumMaterial.Gem.AMETHYST),
                has(CommonTagPrefixes.DUST, RagiumMaterial.Gem.AMETHYST)
            ).unlockedBy(
                getHasName(Items.ICE),
                has(Items.ICE)
            )
            .unlockedBy(
                getHasName(CommonTagPrefixes.DUST, RagiumMaterial.Mineral.NITER),
                has(CommonTagPrefixes.DUST, RagiumMaterial.Mineral.NITER)
            )
            .save(output)
        // Candle
        shaped(RecipeCategory.DECORATIONS, Items.CANDLE)
            .pattern("A")
            .pattern("B")
            .define('A', Tags.Items.STRINGS)
            .define('B', RagiumItems.BEESWAX)
            .unlockedBy(getHasName(Tags.Items.STRINGS), has(Tags.Items.STRINGS))
            .unlockedBy(getHasName(RagiumItems.BEESWAX), has(RagiumItems.BEESWAX))
            .save(output)

        // Bamboo Charcoal
        SimpleCookingRecipeBuilder.smelting(
            Ingredient.of(Items.BAMBOO),
            RecipeCategory.MISC,
            CookingBookCategory.MISC,
            RagiumItems.BAMBOO_CHARCOAL,
            0.5f,
            200
        ).unlockedBy(getHasName(Items.BAMBOO), has(Items.BAMBOO))
            .save(output)
        // Particle Board
        shaped(RecipeCategory.MISC, RagiumItems.PARTICLE_BOARD, 4)
            .pattern("AAA")
            .pattern("ABA")
            .pattern("AAA")
            .define('A', CommonTagPrefixes.DUST, RagiumMaterial.Other.WOOD)
            .define('B', HTCommonTags.Items.STICKY_BALLS)
            .unlockedBy(getHasName(HTCommonTags.Items.STICKY_BALLS), has(HTCommonTags.Items.STICKY_BALLS))
            .save(output)
        // Synthetic
        for (item: HTSimpleDeferredItem in listOf(
            RagiumItems.SYNTHETIC_FEATHER,
            RagiumItems.SYNTHETIC_FIBER,
            RagiumItems.SYNTHETIC_LEATHER
        )) {
            SingleItemRecipeBuilder.stonecutting(
                tag(HTCommonTags.Items.PLASTICS),
                RecipeCategory.MISC,
                item,
                1
            ).unlockedBy(getHasName(HTCommonTags.Items.PLASTICS), has(HTCommonTags.Items.PLASTICS))
                .save(output)
        }

        // XX Tools
        registerTools(RagiumItems.SOOTY_IRON_TOOLS, RagiumToolMaterials.SOOTY_IRON)

        // XX Dye Bucket
        for (color: HTDefaultColor in HTDefaultColor.entries) {
            shapeless(RecipeCategory.MISC, RagiumFluids.DYES[color].bucketHolder)
                .requires(Tags.Items.BUCKETS_WATER)
                .requires(color.dyesTag)
                .requires(color.dyesTag)
                .requires(color.dyesTag)
                .requires(color.dyesTag)
                .unlockedBy(getHasName(color.dyesTag), has(color.dyesTag))
                .save(output)
        }
    }

    private fun registerTools(tools: HTToolCollection<ItemLike>, material: ToolMaterial) {
        fun registerTool(toolType: HTToolType, patterns: Iterable<String>) {
            shaped(RecipeCategory.TOOLS, tools[toolType])
                .apply { patterns.forEach(::pattern) }
                .define('A', material.repairItems)
                .define('B', Tags.Items.RODS_WOODEN)
                .unlockedBy(getHasName(material.repairItems), has(material.repairItems))
                .save(output)
        }

        registerTool(HTToolType.SWORD, listOf("B", "A", "A"))
        registerTool(HTToolType.SHOVEL, listOf("B", "B", "A"))
        registerTool(HTToolType.PICKAXE, listOf(" B ", " B ", "AAA"))
        registerTool(HTToolType.AXE, listOf("B ", "BA", "AA"))
        registerTool(HTToolType.HOE, listOf("B ", "B ", "AA"))
    }

    //    Machine    //

    private fun machine() {
        // Mechanical
        shaped(RecipeCategory.MISC, RagiumItems.getParts(HTMachineType.MECHANICAL), 3)
            .pattern("AAA")
            .pattern("BBB")
            .pattern("AAA")
            .define('A', CommonTagPrefixes.INGOT, RagiumMaterial.Metal.SOOTY_IRON)
            .define('B', CommonTagPrefixes.DUST, RagiumMaterial.Mineral.REDSTONE)
            .unlockedBy(
                getHasName(CommonTagPrefixes.INGOT, RagiumMaterial.Metal.SOOTY_IRON),
                has(CommonTagPrefixes.INGOT, RagiumMaterial.Metal.SOOTY_IRON)
            )
            .save(output)

        mechanical(RagiumBlocks.ASSEMBLER) {
            define('D', Items.CRAFTER)
            unlockedBy(getHasName(Items.CRAFTER), has(Items.CRAFTER))
        }
        mechanical(RagiumBlocks.CRUSHER) {
            define('D', Items.GRINDSTONE)
            unlockedBy(getHasName(Items.GRINDSTONE), has(Items.GRINDSTONE))
        }
        mechanical(RagiumBlocks.COMPRESSOR) {
            define('D', ItemTags.ANVIL)
            unlockedBy(getHasName(ItemTags.ANVIL), has(ItemTags.ANVIL))
        }
        mechanical(RagiumBlocks.CUTTING_MACHINE) {
            define('D', Items.STONECUTTER)
            unlockedBy(getHasName(Items.STONECUTTER), has(Items.STONECUTTER))
        }
        // Heat
        heat(RagiumBlocks.FREEZER) {
            define('D', Tags.Items.BUCKETS_WATER)
            unlockedBy(getHasName(Tags.Items.BUCKETS_WATER), has(Tags.Items.BUCKETS_WATER))
        }
        heat(RagiumBlocks.MELTER) {
            define('D', Tags.Items.BUCKETS_LAVA)
            unlockedBy(getHasName(Tags.Items.BUCKETS_LAVA), has(Tags.Items.BUCKETS_LAVA))
        }

        heat(RagiumBlocks.SMELTER) {
            define('D', Items.FURNACE)
            unlockedBy(getHasName(Items.FURNACE), has(Items.FURNACE))
        }
        // Chemical
        chemical(RagiumBlocks.CHEMICAL_BATH) {
            define('D', Items.CAULDRON)
            unlockedBy(getHasName(Items.CAULDRON), has(Items.CAULDRON))
        }
        // Bio
        bio(RagiumBlocks.BREWERY) {
            define('D', Items.BREWING_STAND)
            unlockedBy(getHasName(Items.BREWING_STAND), has(Items.BREWING_STAND))
        }
        // Electronics
        // Arcane

        // Decoration
        shaped(RecipeCategory.DECORATIONS, RagiumBlocks.MACHINE_CASING, 4)
            .pattern("ABA")
            .pattern("B B")
            .pattern("ABA")
            .define('A', CommonTagPrefixes.NUGGET, RagiumMaterial.Metal.SOOTY_IRON)
            .define('B', CommonTagPrefixes.INGOT, RagiumMaterial.Metal.SOOTY_IRON)
            .unlockedBy(
                getHasName(CommonTagPrefixes.INGOT, RagiumMaterial.Metal.SOOTY_IRON),
                has(CommonTagPrefixes.INGOT, RagiumMaterial.Metal.SOOTY_IRON)
            )
            .save(output)

        for (block: HTSimpleDeferredBlockAndItem in RagiumBlocks.MACHINE_CASINGS.values) {
            SingleItemRecipeBuilder.stonecutting(
                Ingredient.of(RagiumBlocks.MACHINE_CASING),
                RecipeCategory.DECORATIONS,
                block,
                1
            ).unlockedBy(getHasName(RagiumBlocks.MACHINE_CASING), has(RagiumBlocks.MACHINE_CASING))
                .save(output)
        }
    }

    private inline fun machine(
        machineType: HTMachineType,
        material: HTMaterialLike,
        gear: HTMaterialLike,
        result: HTSimpleDeferredBlockAndItem,
        builderAction: ShapedRecipeBuilder.() -> Unit
    ) {
        shaped(RecipeCategory.MISC, result)
            .pattern("ABA")
            .pattern("BCB")
            .pattern("ADA")
            .define('A', CommonTagPrefixes.NUGGET, material)
            .define('B', RagiumItems.getParts(machineType))
            .define('C', CommonTagPrefixes.GEAR, gear)
            .apply(builderAction)
            .save(output)
    }

    private inline fun mechanical(result: HTSimpleDeferredBlockAndItem, builderAction: ShapedRecipeBuilder.() -> Unit) {
        machine(
            HTMachineType.MECHANICAL,
            RagiumMaterial.Metal.SOOTY_IRON,
            RagiumMaterial.Metal.COPPER,
            result,
            builderAction
        )
    }

    private inline fun heat(result: HTSimpleDeferredBlockAndItem, builderAction: ShapedRecipeBuilder.() -> Unit) {
        machine(
            HTMachineType.HEAT,
            RagiumMaterial.Metal.SOOTY_IRON,
            RagiumMaterial.Metal.IRON,
            result,
            builderAction
        )
    }

    private inline fun chemical(result: HTSimpleDeferredBlockAndItem, builderAction: ShapedRecipeBuilder.() -> Unit) {
        machine(
            HTMachineType.CHEMICAL,
            RagiumMaterial.Metal.BLACK_STEEL,
            RagiumMaterial.Metal.GOLD,
            result,
            builderAction
        )
    }

    private inline fun bio(result: HTSimpleDeferredBlockAndItem, builderAction: ShapedRecipeBuilder.() -> Unit) {
        machine(
            HTMachineType.BIO,
            RagiumMaterial.Metal.BLACK_STEEL,
            RagiumMaterial.Gem.EMERALD,
            result,
            builderAction
        )
    }

    private inline fun electronics(
        result: HTSimpleDeferredBlockAndItem,
        builderAction: ShapedRecipeBuilder.() -> Unit
    ) {
        machine(
            HTMachineType.ELECTRONICS,
            RagiumMaterial.Metal.VOID_METAL,
            RagiumMaterial.Gem.DIAMOND,
            result,
            builderAction
        )
    }

    private inline fun arcane(result: HTSimpleDeferredBlockAndItem, builderAction: ShapedRecipeBuilder.() -> Unit) {
        machine(
            HTMachineType.ARCANE,
            RagiumMaterial.Metal.VOID_METAL,
            RagiumMaterial.Metal.NETHERITE,
            result,
            builderAction
        )
    }

    //    Material    //

    private fun material() {
        // XX <-> Storage Block
        baseToBlock(RagiumMaterial.Gem.ECHO, CommonTagPrefixes.GEM, Items.ECHO_SHARD, size = StorageBlockSize.FOUR)
        baseToBlock(RagiumMaterial.Metal.SOOTY_IRON, HTItemPart.INGOT)
        baseToBlock(RagiumMaterial.Metal.BLACK_STEEL, HTItemPart.INGOT)
        baseToBlock(RagiumMaterial.Metal.VOID_METAL, HTItemPart.INGOT)
        // Ingot <-> Nugget
        ingotToNugget(RagiumMaterial.Metal.NETHERITE, ingot = Items.NETHERITE_INGOT)
        ingotToNugget(RagiumMaterial.Metal.SOOTY_IRON)
        ingotToNugget(RagiumMaterial.Metal.BLACK_STEEL)
        ingotToNugget(RagiumMaterial.Metal.VOID_METAL)
        // Gear
        RagiumItems.getOrThrow(HTItemPart.GEAR, RagiumMaterial.Other.WOOD).let { gear: HTSimpleDeferredItem ->
            shaped(RecipeCategory.MISC, gear)
                .pattern(" A ")
                .pattern("ABA")
                .pattern(" A ")
                .define('A', ItemTags.PLANKS)
                .define('B', ItemTags.WOODEN_BUTTONS)
                .group(gear.idOrThrow.path)
                .unlockedBy(getHasName(ItemTags.PLANKS), has(ItemTags.PLANKS))
                .save(output)
        }
        gear(CommonTagPrefixes.GEM, RagiumMaterial.Gem.DIAMOND)
        gear(CommonTagPrefixes.GEM, RagiumMaterial.Gem.EMERALD)
        gear(CommonTagPrefixes.INGOT, RagiumMaterial.Metal.COPPER)
        gear(CommonTagPrefixes.INGOT, RagiumMaterial.Metal.IRON)
        gear(CommonTagPrefixes.INGOT, RagiumMaterial.Metal.GOLD)
        RagiumItems.MATERIAL_ITEMS[HTItemPart.GEAR, RagiumMaterial.Metal.NETHERITE]?.let {
            SmithingTransformRecipeBuilder.smithing(
                Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
                tag(CommonTagPrefixes.GEAR, RagiumMaterial.Gem.DIAMOND),
                tag(CommonTagPrefixes.INGOT, RagiumMaterial.Metal.NETHERITE),
                RecipeCategory.MISC,
                it.get()
            ).unlocks(
                getHasName(CommonTagPrefixes.GEAR, RagiumMaterial.Gem.DIAMOND),
                has(CommonTagPrefixes.GEAR, RagiumMaterial.Gem.DIAMOND)
            ).save(output, RecipeKey(it.idOrThrow.withSuffix("_smithing")))
        }

        // Dust -> Ingot
        for (metal: RagiumMaterial.Metal in RagiumMaterial.Metal.entries) {
            val dust: HTSimpleDeferredItem = RagiumItems.MATERIAL_ITEMS[HTItemPart.DUST, metal] ?: continue
            val item: HTSimpleDeferredItem = when (metal) {
                RagiumMaterial.Metal.COPPER -> HTSimpleDeferredItem(vanillaId("copper_ingot"))
                RagiumMaterial.Metal.IRON -> HTSimpleDeferredItem(vanillaId("iron_ingot"))
                RagiumMaterial.Metal.GOLD -> HTSimpleDeferredItem(vanillaId("gold_ingot"))
                RagiumMaterial.Metal.NETHERITE -> HTSimpleDeferredItem(vanillaId("netherite_ingot"))
                else -> RagiumItems.MATERIAL_ITEMS[HTItemPart.INGOT, metal]
            } ?: continue
            SimpleCookingRecipeBuilder.smelting(
                Ingredient.of(dust),
                RecipeCategory.MISC,
                CookingBookCategory.MISC,
                item,
                0.35f,
                200
            ).group(item.idOrThrow.path)
                .unlockedBy(getHasName(dust), has(dust))
                .saveSuffixed(output, "_from_smeting_dust")
            SimpleCookingRecipeBuilder.blasting(
                Ingredient.of(dust),
                RecipeCategory.MISC,
                CookingBookCategory.MISC,
                item,
                0.35f,
                100
            ).group(item.idOrThrow.path)
                .unlockedBy(getHasName(dust), has(dust))
                .saveSuffixed(output, "_from_blasting_dust")
        }

        // Fuel
        for (fuel: RagiumMaterial.Fuel in RagiumMaterial.Fuel.entries) {
            val base: ItemLike = RagiumMaterialHelper.getFuelBase(fuel)
            // Storage
            baseToBlock(fuel, Ingredient.of(base), base, getHasName(base) to has(base))
            // Tiny
            val tiny: HTSimpleDeferredItem = RagiumItems.getOrThrow(HTItemPart.TINY, fuel)
            shapeless(RecipeCategory.MISC, tiny, 8)
                .requires(base)
                .group(tiny.idOrThrow.path)
                .unlockedBy(getHasName(base), has(base))
                .save(output)
            shaped(RecipeCategory.MISC, base)
                .pattern("AAA")
                .pattern("A A")
                .pattern("AAA")
                .define('A', CommonTagPrefixes.TINY, fuel)
                .group(getItemName(base))
                .unlockedBy(getHasName(CommonTagPrefixes.TINY, fuel), has(CommonTagPrefixes.TINY, fuel))
                .saveSuffixed(output, "_from_tiny")
        }
        // Sooty Iron
        val ironIngot: Ingredient = tag(CommonTagPrefixes.INGOT, RagiumMaterial.Metal.IRON)
        val sootyIronIngot: HTSimpleDeferredItem = RagiumItems.getOrThrow(
            HTItemPart.INGOT,
            RagiumMaterial.Metal.SOOTY_IRON
        )
        shaped(RecipeCategory.MISC, sootyIronIngot)
            .pattern("AAA")
            .pattern("ABA")
            .pattern("AAA")
            .define('A', CommonTagPrefixes.TINY, RagiumMaterial.Fuel.COAL, RagiumMaterial.Fuel.CHARCOAL)
            .define('B', ironIngot)
            .group(sootyIronIngot.idOrThrow.path)
            .unlockedBy(
                getHasName(CommonTagPrefixes.TINY, RagiumMaterial.Fuel.COAL),
                has(CommonTagPrefixes.TINY, RagiumMaterial.Fuel.COAL)
            )
            .unlockedBy(
                getHasName(CommonTagPrefixes.TINY, RagiumMaterial.Fuel.CHARCOAL),
                has(CommonTagPrefixes.TINY, RagiumMaterial.Fuel.CHARCOAL)
            )
            .save(output)
        shaped(RecipeCategory.MISC, sootyIronIngot)
            .pattern(" A ")
            .pattern("ABA")
            .pattern(" A ")
            .define('A', CommonTagPrefixes.TINY, RagiumMaterial.Fuel.COAL_COKE)
            .define('B', ironIngot)
            .group(sootyIronIngot.idOrThrow.path)
            .unlockedBy(
                getHasName(CommonTagPrefixes.TINY, RagiumMaterial.Fuel.COAL_COKE),
                has(CommonTagPrefixes.TINY, RagiumMaterial.Fuel.COAL_COKE)
            )
            .saveSuffixed(output, "_from_coke")
        shapeless(RecipeCategory.MISC, sootyIronIngot)
            .requires(ironIngot)
            .requires(CommonTagPrefixes.DUST, RagiumMaterial.Fuel.COAL, RagiumMaterial.Fuel.CHARCOAL)
            .group(sootyIronIngot.idOrThrow.path)
            .unlockedBy(
                getHasName(CommonTagPrefixes.DUST, RagiumMaterial.Fuel.COAL),
                has(CommonTagPrefixes.DUST, RagiumMaterial.Fuel.COAL)
            )
            .unlockedBy(
                getHasName(CommonTagPrefixes.DUST, RagiumMaterial.Fuel.CHARCOAL),
                has(CommonTagPrefixes.DUST, RagiumMaterial.Fuel.CHARCOAL)
            )
            .saveSuffixed(output, "_by_dust")
        shapeless(RecipeCategory.MISC, sootyIronIngot, 2)
            .requires(ironIngot)
            .requires(ironIngot)
            .requires(CommonTagPrefixes.DUST, RagiumMaterial.Fuel.COAL_COKE)
            .group(sootyIronIngot.idOrThrow.path)
            .unlockedBy(
                getHasName(CommonTagPrefixes.DUST, RagiumMaterial.Fuel.COAL_COKE),
                has(CommonTagPrefixes.DUST, RagiumMaterial.Fuel.COAL_COKE)
            )
            .saveSuffixed(output, "_by_coke_dust")
    }

    private fun baseToBlock(
        material: RagiumMaterial,
        basePrefix: HTTagPrefix,
        base: ItemLike,
        block: ItemLike? = RagiumBlocks.STORAGE_BLOCKS[material],
        size: StorageBlockSize = StorageBlockSize.NINE
    ) {
        baseToBlock(
            material,
            tag(basePrefix, material),
            base,
            getHasName(basePrefix, material) to has(basePrefix, material),
            block,
            size
        )
    }

    private fun baseToBlock(
        material: RagiumMaterial,
        basePart: HTItemPart,
        block: ItemLike? = RagiumBlocks.STORAGE_BLOCKS[material],
        size: StorageBlockSize = StorageBlockSize.NINE
    ) {
        val base: ItemLike = RagiumItems.MATERIAL_ITEMS[basePart, material] ?: return
        baseToBlock(material, basePart.tagPrefix, base, block, size)
    }

    private fun baseToBlock(
        material: RagiumMaterial,
        baseInput: Ingredient,
        base: ItemLike,
        criterion: Pair<String, Criterion<*>>,
        block: ItemLike? = RagiumBlocks.STORAGE_BLOCKS[material],
        size: StorageBlockSize = StorageBlockSize.NINE
    ) {
        if (block == null) return
        shapeless(RecipeCategory.MISC, base, size.count)
            .requires(CommonTagPrefixes.STORAGE_BLOCK, material)
            .group(getItemName(base))
            .unlockedBy(
                getHasName(CommonTagPrefixes.STORAGE_BLOCK, material),
                has(CommonTagPrefixes.STORAGE_BLOCK, material)
            ).saveSuffixed(output, "_from_block")
        shaped(RecipeCategory.BUILDING_BLOCKS, block)
            .apply { size.pattern.forEach(::pattern) }
            .define('A', baseInput)
            .define('B', base)
            .group(getItemName(block))
            .unlockedBy(criterion.first, criterion.second)
            .save(output)
    }

    private enum class StorageBlockSize(val count: Int, val pattern: List<String>) {
        FOUR(4, listOf("AA", "AB")),
        NINE(9, listOf("AAA", "ABA", "AAA"))
    }

    private fun ingotToNugget(
        material: RagiumMaterial,
        ingot: ItemLike? = RagiumItems.MATERIAL_ITEMS[HTItemPart.INGOT, material],
        nugget: ItemLike? = RagiumItems.MATERIAL_ITEMS[HTItemPart.NUGGET, material]
    ) {
        if (ingot == null || nugget == null) return
        shapeless(RecipeCategory.MISC, nugget, 9)
            .requires(CommonTagPrefixes.INGOT, material)
            .group(getItemName(nugget))
            .unlockedBy(getHasName(CommonTagPrefixes.INGOT, material), has(CommonTagPrefixes.INGOT, material))
            .saveSuffixed(output, "_from_ingot")
        shaped(RecipeCategory.MISC, ingot)
            .pattern("AAA")
            .pattern("ABA")
            .pattern("AAA")
            .define('A', CommonTagPrefixes.NUGGET, material)
            .define('B', nugget)
            .group(getItemName(ingot))
            .unlockedBy(getHasName(CommonTagPrefixes.NUGGET, material), has(CommonTagPrefixes.NUGGET, material))
            .saveSuffixed(output, "_from_nugget")
    }

    private fun gear(basePrefix: HTTagPrefix, material: RagiumMaterial) {
        val gear: ItemLike = RagiumItems.getOrThrow(HTItemPart.GEAR, material)
        shaped(RecipeCategory.MISC, gear)
            .pattern(" A ")
            .pattern("ABA")
            .pattern(" A ")
            .define('A', basePrefix, material)
            .define('B', CommonTagPrefixes.GEAR, RagiumMaterial.Other.WOOD)
            .group(getItemName(gear))
            .unlockedBy(getHasName(basePrefix, material), has(basePrefix, material))
            .save(output)
    }

    //    Extensions    //

    fun tag(prefix: HTTagPrefix, material: HTMaterialLike): Ingredient = tag(prefix.itemTagKey(material))

    fun getHasName(tagKey: TagKey<*>): String = "has_${tagKey.location().debugPath}"

    fun getHasName(prefix: HTTagPrefix, material: HTMaterialLike): String = getHasName(prefix.itemTagKey(material))

    fun has(prefix: HTTagPrefix, material: HTMaterialLike): Criterion<InventoryChangeTrigger.TriggerInstance> =
        has(prefix.itemTagKey(material))

    fun RecipeBuilder.saveSuffixed(output: RecipeOutput, suffix: String) {
        this.save(output, RecipeKey(this.defaultId().identifier().withSuffix(suffix)))
    }

    fun ShapedRecipeBuilder.define(symbol: Char, prefix: HTTagPrefix, material: HTMaterialLike): ShapedRecipeBuilder =
        this.define(symbol, prefix.itemTagKey(material))

    fun ShapedRecipeBuilder.define(
        symbol: Char,
        prefix: HTTagPrefix,
        vararg materials: HTMaterialLike
    ): ShapedRecipeBuilder = materials.map(prefix::itemTagKey)
        .let(HTComparators::sortTagKeys)
        .map(::tag)
        .let(::CompoundIngredient)
        .toVanilla()
        .let { this.define(symbol, it) }

    fun ShapelessRecipeBuilder.requires(prefix: HTTagPrefix, material: HTMaterialLike): ShapelessRecipeBuilder =
        this.requires(prefix.itemTagKey(material))

    fun ShapelessRecipeBuilder.requires(prefix: HTTagPrefix, vararg materials: HTMaterialLike): ShapelessRecipeBuilder =
        materials.map(prefix::itemTagKey)
            .let(HTComparators::sortTagKeys)
            .map(::tag)
            .let(::CompoundIngredient)
            .toVanilla()
            .let(this::requires)
}
