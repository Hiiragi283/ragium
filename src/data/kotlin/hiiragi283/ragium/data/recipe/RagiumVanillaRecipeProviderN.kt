package hiiragi283.ragium.data.recipe

import hiiragi283.lib.item.component.HTToolCollection
import hiiragi283.lib.item.component.HTToolType
import hiiragi283.lib.recipe.RecipeKey
import hiiragi283.lib.registry.HTDecorationContent
import hiiragi283.lib.registry.HTSimpleDeferredBlockAndItem
import hiiragi283.lib.registry.HTSimpleDeferredItem
import hiiragi283.lib.resource.HTSimpleValueWithKey
import hiiragi283.lib.resource.HTValueWithKey
import hiiragi283.lib.resource.debugPath
import hiiragi283.lib.resource.vanillaId
import hiiragi283.lib.tag.CommonTagPrefixes
import hiiragi283.lib.tag.HTCommonTags
import hiiragi283.lib.tag.HTMaterialLike
import hiiragi283.lib.tag.HTTagPrefix
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.material.HTItemPart
import hiiragi283.ragium.api.material.HTStorageBlockPart
import hiiragi283.ragium.api.material.RagiumMaterial
import hiiragi283.ragium.api.tag.RagiumTags
import hiiragi283.ragium.common.block.RagiumBlocks
import hiiragi283.ragium.common.item.RagiumItems
import hiiragi283.ragium.common.item.component.RagiumToolMaterials
import hiiragi283.ragium.common.material.RagiumMaterialHelper
import net.minecraft.advancements.Advancement
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.RecipeProvider
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder
import net.minecraft.data.recipes.SingleItemRecipeBuilder
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.references.ItemIds
import net.minecraft.resources.Identifier
import net.minecraft.tags.BlockItemTagId
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.item.Items
import net.minecraft.world.item.ToolMaterial
import net.minecraft.world.item.crafting.CookingBookCategory
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.crafting.CompoundIngredient

class RagiumVanillaRecipeProviderN(
    recipeOutput: BootstrapContext<Recipe<*>>,
    advancementOutput: BootstrapContext<Advancement>
) : RecipeProvider(recipeOutput, advancementOutput) {
    public override fun buildRecipes() {
        decoration()
        storage()
        upgrade()
        material()

        // Bamboo Charcoal
        SimpleCookingRecipeBuilder.smelting(
            Ingredient.of(Items.BAMBOO),
            RecipeCategory.MISC,
            CookingBookCategory.MISC,
            RagiumItems.BAMBOO_CHARCOAL,
            0.1f,
            200
        ).unlockedBy(Items.BAMBOO)
            .save(output)
        // Particle Board
        shaped(RecipeCategory.MISC, RagiumItems.PARTICLE_BOARD, 4)
            .hollow8()
            .define('A', tag(CommonTagPrefixes.DUST, RagiumMaterial.Other.WOOD))
            .define('B', Tags.Items.SLIME_BALLS)
            .unlockedBy(Tags.Items.SLIME_BALLS)
            .save(output)
        // Splash Bottle
        shapeless(RecipeCategory.BREWING, RagiumItems.SPLASH_BOTTLE, 4)
            .requires(Tags.Items.GUNPOWDERS)
            .requires(Items.GLASS_BOTTLE)
            .requires(Items.GLASS_BOTTLE)
            .requires(Items.GLASS_BOTTLE)
            .requires(Items.GLASS_BOTTLE)
            .unlockedBy(Tags.Items.GUNPOWDERS)
            .save(output)
        // Lingering Bottle
        shapeless(RecipeCategory.BREWING, RagiumItems.LINGERING_BOTTLE, 4)
            .requires(Items.DRAGON_BREATH)
            .requires(Items.GLASS_BOTTLE)
            .requires(Items.GLASS_BOTTLE)
            .requires(Items.GLASS_BOTTLE)
            .requires(Items.GLASS_BOTTLE)
            .unlockedBy(Items.DRAGON_BREATH)
            .save(output)
        // Cement
        shapeless(RecipeCategory.MISC, RagiumItems.CEMENT, 2)
            .requires(tag(Tags.BlockItems.SANDS))
            .requires(tag(CommonTagPrefixes.DUST, RagiumMaterial.Chemicals.QUICK_LIME))
            .unlockedBy(CommonTagPrefixes.DUST, RagiumMaterial.Chemicals.QUICK_LIME)
            .save(output)
        // Mortar
        shapeless(RecipeCategory.MISC, RagiumItems.MORTAR, 4)
            .requires(Tags.Items.BUCKETS_WATER)
            .requires(RagiumItems.CEMENT)
            .requires(RagiumItems.CEMENT)
            .requires(RagiumItems.CEMENT)
            .requires(RagiumItems.CEMENT)
            .unlockedBy(RagiumItems.CEMENT)
            .save(output)
        shaped(RecipeCategory.BUILDING_BLOCKS, Items.BRICKS)
            .pattern("AB")
            .pattern("BA")
            .define('A', Tags.Items.BRICKS_NORMAL)
            .define('B', RagiumItems.MORTAR)
            .unlockedBy(RagiumItems.MORTAR)
            .save(output, vanillaId("bricks_with_mortar"))
        shaped(RecipeCategory.BUILDING_BLOCKS, Items.NETHER_BRICKS)
            .pattern("AB")
            .pattern("BA")
            .define('A', Tags.Items.BRICKS_NETHER)
            .define('B', RagiumItems.MORTAR)
            .unlockedBy(RagiumItems.MORTAR)
            .save(output, vanillaId("nether_bricks_with_mortar"))
        // Wither Doll
        shaped(RecipeCategory.COMBAT, RagiumItems.WITHER_DOLL)
            .pattern("AAA")
            .pattern("BBB")
            .pattern(" B ")
            .define('A', Items.WITHER_SKELETON_SKULL)
            .define('B', ItemTags.SOUL_FIRE_BASE_BLOCKS)
            .unlockedBy(Items.WITHER_SKELETON_SKULL)
            .save(output)

        // XX Press Die
        shaped(RecipeCategory.MISC, RagiumItems.BLANK_DIE)
            .pattern("AA")
            .pattern("AA")
            .define('A', tag(CommonTagPrefixes.INGOT, RagiumMaterial.Alloy.SOOTY_IRON))
            .unlockedBy(CommonTagPrefixes.INGOT, RagiumMaterial.Alloy.SOOTY_IRON)
            .save(output)
        for (pressDie: HTSimpleDeferredItem in RagiumItems.PRESS_DIES) {
            SingleItemRecipeBuilder.stonecutting(
                tag(RagiumTags.Items.PRESS_DIES),
                RecipeCategory.MISC,
                pressDie,
                1
            ).unlockedBy(RagiumTags.Items.PRESS_DIES)
                .save(output, pressDie.id.withSuffix("_with_stonecutting"))
        }
        // XX Mold
        shaped(RecipeCategory.MISC, RagiumItems.BLANK_MOLD)
            .pattern("AA")
            .pattern("AA")
            .define('A', tag(CommonTagPrefixes.INGOT, RagiumMaterial.Metal.GOLD))
            .unlockedBy(CommonTagPrefixes.INGOT, RagiumMaterial.Metal.GOLD)
            .save(output)
        for (mold: HTSimpleDeferredItem in RagiumItems.MOLDS) {
            SingleItemRecipeBuilder.stonecutting(
                tag(RagiumTags.Items.MOLDS),
                RecipeCategory.MISC,
                mold,
                1
            ).unlockedBy(RagiumTags.Items.MOLDS)
                .save(output, mold.id.withSuffix("_with_stonecutting"))
        }

        // XX Tools
        registerTools(RagiumItems.SOOTY_IRON_TOOLS, RagiumToolMaterials.SOOTY_IRON)
    }

    private fun registerTools(tools: HTToolCollection<HTSimpleDeferredItem>, material: ToolMaterial) {
        fun registerTool(toolType: HTToolType, patterns: Iterable<String>) {
            shaped(RecipeCategory.TOOLS, tools[toolType])
                .apply { patterns.forEach(this::pattern) }
                .define('A', material.repairItems)
                .define('B', Tags.Items.RODS_WOODEN)
                .unlockedBy(material.repairItems)
                .save(output)
        }

        registerTool(HTToolType.SWORD, listOf("B", "A", "A"))
        registerTool(HTToolType.SHOVEL, listOf("B", "B", "A"))
        registerTool(HTToolType.PICKAXE, listOf(" B ", " B ", "AAA"))
        registerTool(HTToolType.AXE, listOf("B ", "BA", "AA"))
        registerTool(HTToolType.HOE, listOf("B ", "B ", "AA"))
    }

    //    Vanilla    //

    private fun vanilla() {
        // Gunpowder
        shapeless(RecipeCategory.MISC, Items.GUNPOWDER, 3)
            .requires(tag(CommonTagPrefixes.DUST, RagiumMaterial.Mineral.SULFUR))
            .requires(tag(CommonTagPrefixes.DUST, RagiumMaterial.Mineral.NITER))
    }

    //    Decoration    //

    private fun decoration() {
        RagiumBlocks.ALL_DECORATIONS.forEach(::registerDecoration)

        // Quartz Glass Pane
        shaped(RecipeCategory.BUILDING_BLOCKS, RagiumBlocks.QUARTZ_GLASS_PANE, 16)
            .pattern("AAA")
            .pattern("AAA")
            .define('A', RagiumBlocks.QUARTZ_GLASS)
            .unlockedBy(RagiumBlocks.QUARTZ_GLASS)
            .save(output)
    }

    private fun registerDecoration(content: HTDecorationContent) {
        val (base, slab, stairs, wall) = content
        val stonecutterInput: Ingredient = Ingredient.of(listOfNotNull(base, content.parent()?.base).stream())
        // Slab
        shaped(RecipeCategory.BUILDING_BLOCKS, slab, 6)
            .pattern("AAA")
            .define('A', base)
            .unlockedBy(base)
            .save(output)
        stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, slab, base, 2)
        // Stairs
        if (stairs != null) {
            shaped(RecipeCategory.BUILDING_BLOCKS, stairs, 4)
                .pattern("A  ")
                .pattern("AA ")
                .pattern("AAA")
                .define('A', base)
                .unlockedBy(base)
                .save(output)
            stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, stairs, base)
        }
        // Wall
        if (wall != null) {
            shaped(RecipeCategory.BUILDING_BLOCKS, wall, 6)
                .pattern("AAA")
                .pattern("AAA")
                .define('A', base)
                .unlockedBy(base)
                .save(output)
            stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, wall, base)
        }
    }

    //    Storage    //

    private fun storage() {
        // Tank
        shaped(RecipeCategory.MISC, RagiumBlocks.TANK)
            .hollow8()
            .define('A', tag(CommonTagPrefixes.INGOT, RagiumMaterial.Alloy.SOOTY_IRON))
            .define('B', Tags.Items.BUCKETS_EMPTY)
            .group("tank")
            .unlockedBy(Tags.Items.BUCKETS_EMPTY)
            .save(output, RagiumBlocks.TANK.id().withSuffix("_by_sooty_iron"))
        shaped(RecipeCategory.MISC, RagiumBlocks.TANK)
            .hollow4()
            .define('A', tag(CommonTagPrefixes.INGOT, RagiumMaterial.Alloy.BLACK_STEEL))
            .define('B', Tags.Items.BUCKETS_EMPTY)
            .group("tank")
            .unlockedBy(Tags.Items.BUCKETS_EMPTY)
            .save(output, RagiumBlocks.TANK.id().withSuffix("_by_black_steel"))
        // Potion Tank
        shaped(RecipeCategory.MISC, RagiumBlocks.POTION_TANK)
            .hollow4()
            .define('A', Tags.Items.RODS_BLAZE)
            .define('B', Tags.Items.BUCKETS_EMPTY)
            .unlockedBy(Tags.Items.RODS_BLAZE)
            .save(output)
        // Void Tank
        shapeless(RecipeCategory.MISC, RagiumBlocks.VOID_TANK)
            .requires(RagiumBlocks.TANK)
            .requires(Tags.Items.ENDER_PEARLS)
            .unlockedBy(Tags.Items.ENDER_PEARLS)
            .save(output)
    }

    //    Upgrade    //

    private fun upgrade() {
        // Upgrade Station
        shaped(RecipeCategory.MISC, RagiumBlocks.UPGRADE_STATION)
            .pattern("ABA")
            .pattern("BCB")
            .pattern("ABA")
            .define('A', tag(CommonTagPrefixes.INGOT, RagiumMaterial.Alloy.SOOTY_IRON))
            .define('B', tag(CommonTagPrefixes.GEAR, RagiumMaterial.Metal.IRON))
            .define('C', tag(Tags.BlockItems.PLAYER_WORKSTATIONS_CRAFTING_TABLES))
            .unlockedBy(HTMachineType.PREFIX.rawCommonTag)
            .save(output)

        // Basic
        shaped(RecipeCategory.MISC, RagiumItems.BASIC_UPGRADE)
            .pattern("ABA")
            .pattern("CDC")
            .pattern("ABA")
            .define('A', tag(CommonTagPrefixes.INGOT, RagiumMaterial.Metal.IRON))
            .define('B', tag(Tags.BlockItems.GLASS_BLOCKS_CHEAP))
            .define('C', tag(CommonTagPrefixes.DUST, RagiumMaterial.Mineral.REDSTONE))
            .define('D', tag(CommonTagPrefixes.GEAR, RagiumMaterial.Metal.COPPER))
            .unlockedBy(RagiumBlocks.UPGRADE_STATION)
            .save(output)
        // Advanced
        shaped(RecipeCategory.MISC, RagiumItems.ADVANCED_UPGRADE)
            .pattern("ABA")
            .pattern("CDC")
            .pattern("ABA")
            .define('A', tag(CommonTagPrefixes.INGOT, RagiumMaterial.Metal.GOLD))
            .define('B', tag(CommonTagPrefixes.GEM, RagiumMaterial.Gem.QUARTZ))
            .define('C', tag(CommonTagPrefixes.GEAR, RagiumMaterial.Metal.IRON))
            .define('D', RagiumItems.BASIC_UPGRADE)
            .unlockedBy(RagiumItems.BASIC_UPGRADE)
            .save(output)
        // Elite
        shaped(RecipeCategory.MISC, RagiumItems.ELITE_UPGRADE)
            .pattern("ABA")
            .pattern("CDC")
            .pattern("ABA")
            .define('A', tag(CommonTagPrefixes.GEM, RagiumMaterial.Gem.DIAMOND))
            .define('B', tag(HTCommonTags.BlockItems.GLASS_BLOCKS_QUARTZ))
            .define('C', tag(CommonTagPrefixes.GEAR, RagiumMaterial.Metal.GOLD))
            .define('D', RagiumItems.ADVANCED_UPGRADE)
            .unlockedBy(RagiumItems.ADVANCED_UPGRADE)
            .save(output)
        // Ultimate
        netheriteSmithing(
            RagiumItems.ELITE_UPGRADE.asItem(),
            RecipeCategory.MISC,
            RagiumItems.ULTIMATE_UPGRADE.asItem()
        )
    }

    //    Material    //

    private fun material() {
        // XX <-> Storage Block
        gemBlock(RagiumMaterial.Gem.ECHO, RagiumBlocks.ECHO_BLOCK)
        gemBlock(RagiumMaterial.Gem.FLUORITE, RagiumBlocks.FLUORITE_BLOCK)
        // gemBlock(RagiumMaterial.Gem.CRYOLITE, RagiumBlocks.CRYOLITE_BLOCK)

        nineToBlock(RagiumMaterial.Metal.ALUMINUM, HTItemPart.INGOT)
        nineToBlock(RagiumMaterial.Metal.SILVER, HTItemPart.INGOT)
        nineToBlock(RagiumMaterial.Alloy.SOOTY_IRON, HTItemPart.INGOT)
        nineToBlock(RagiumMaterial.Alloy.BLACK_STEEL, HTItemPart.INGOT)
        nineToBlock(RagiumMaterial.Alloy.SHADOW_STEEL, HTItemPart.INGOT)
        // Ingot <-> Nugget
        ingotToNugget(RagiumMaterial.Metal.ALUMINUM)
        ingotToNugget(RagiumMaterial.Metal.SILVER)
        ingotToNugget(RagiumMaterial.Alloy.NETHERITE, ingot = HTSimpleDeferredItem(ItemIds.NETHERITE_INGOT))
        ingotToNugget(RagiumMaterial.Alloy.SOOTY_IRON)
        ingotToNugget(RagiumMaterial.Alloy.BLACK_STEEL)
        ingotToNugget(RagiumMaterial.Alloy.SHADOW_STEEL)
        // Gear
        RagiumItems.getOrThrow(HTItemPart.GEAR, RagiumMaterial.Other.WOOD).let { gear: HTSimpleDeferredItem ->
            shaped(RecipeCategory.MISC, gear)
                .hollow4()
                .define('A', ItemTags.PLANKS)
                .define('B', ItemTags.WOODEN_BUTTONS)
                .unlockedBy(ItemTags.PLANKS)
                .save(output)
        }
        gear(CommonTagPrefixes.GEM, RagiumMaterial.Gem.DIAMOND)
        gear(CommonTagPrefixes.GEM, RagiumMaterial.Gem.EMERALD)
        gear(CommonTagPrefixes.INGOT, RagiumMaterial.Metal.COPPER)
        gear(CommonTagPrefixes.INGOT, RagiumMaterial.Metal.IRON)
        gear(CommonTagPrefixes.INGOT, RagiumMaterial.Metal.GOLD)
        RagiumItems.MATERIAL_ITEMS[HTItemPart.GEAR, RagiumMaterial.Alloy.NETHERITE]?.let {
            netheriteSmithing(
                tag(CommonTagPrefixes.GEAR, RagiumMaterial.Gem.DIAMOND),
                RecipeCategory.MISC,
                it.asTemplate().getOrThrow()
            )
        }

        // Fuel
        for (fuel: RagiumMaterial.Fuel in RagiumMaterial.Fuel.entries) {
            val base: HTSimpleDeferredItem = RagiumMaterialHelper.getBase(fuel)
            // Storage
            nineToBlock(fuel, Ingredient.of(base), base)
            // Tiny
            val tiny: HTSimpleDeferredItem = RagiumItems.getOrThrow(HTItemPart.TINY, fuel)
            shapeless(RecipeCategory.MISC, tiny, 8)
                .requires(base)
                .group(tiny.id.path)
                .unlockedBy(base)
                .save(output)
            shaped(RecipeCategory.MISC, base)
                .hollow()
                .define('A', tag(CommonTagPrefixes.TINY, fuel))
                .group(base.id.path)
                .unlockedBy(CommonTagPrefixes.TINY, fuel)
                .save(output, base.id.withSuffix("_from_tiny"))
        }

        // Sooty Iron
        val ironIngot: TagKey<Item> = CommonTagPrefixes.INGOT.itemTagKey(RagiumMaterial.Metal.IRON)
        val sootyIronIngot: HTSimpleDeferredItem =
            RagiumItems.getOrThrow(HTItemPart.INGOT, RagiumMaterial.Alloy.SOOTY_IRON)
        shaped(RecipeCategory.MISC, sootyIronIngot)
            .hollow8()
            .define(
                'A',
                run {
                    setOf(RagiumMaterial.Fuel.COAL, RagiumMaterial.Fuel.CHARCOAL)
                        .map(CommonTagPrefixes.TINY::itemTagKey)
                        .map(::tag)
                        .let(::CompoundIngredient)
                        .toVanilla()
                }
            ).define('B', ironIngot)
            .group(sootyIronIngot.id.path)
            .unlockedBy(CommonTagPrefixes.TINY, RagiumMaterial.Fuel.COAL)
            .unlockedBy(CommonTagPrefixes.TINY, RagiumMaterial.Fuel.CHARCOAL)
            .save(output)
        shaped(RecipeCategory.MISC, sootyIronIngot)
            .hollow4()
            .define(
                'A',
                run {
                    setOf(RagiumMaterial.Fuel.COAL_COKE, RagiumMaterial.Fuel.PITCH_COKE)
                        .map(CommonTagPrefixes.TINY::itemTagKey)
                        .map(::tag)
                        .let(::CompoundIngredient)
                        .toVanilla()
                }
            ).define('B', ironIngot)
            .group(sootyIronIngot.id.path)
            .unlockedBy(CommonTagPrefixes.TINY, RagiumMaterial.Fuel.COAL_COKE)
            .unlockedBy(CommonTagPrefixes.TINY, RagiumMaterial.Fuel.PITCH_COKE)
            .save(output, sootyIronIngot.id.withSuffix("_with_cokes"))
    }

    private fun gemBlock(material: RagiumMaterial, block: HTSimpleDeferredBlockAndItem) {
        val gemTag: TagKey<Item> = CommonTagPrefixes.GEM.itemTagKey(material)
        shaped(RecipeCategory.BUILDING_BLOCKS, block)
            .pattern("AA")
            .pattern("AA")
            .define('A', gemTag)
            .group(block.id().path)
            .unlockedBy(gemTag)
            .save(output)
    }

    private fun nineToBlock(material: RagiumMaterial, basePart: HTItemPart) {
        val base: HTSimpleDeferredItem = RagiumItems.getOrThrow(basePart, material)
        val block: HTSimpleDeferredBlockAndItem = RagiumBlocks.getOrThrow(HTStorageBlockPart.DEFAULT, material)
        val blockTag: TagKey<Item> = CommonTagPrefixes.STORAGE_BLOCK.itemTagKey(material)

        shapeless(RecipeCategory.MISC, base, 9)
            .requires(blockTag)
            .group(base.id.path)
            .unlockedBy(blockTag)
            .save(output, base.id.withSuffix("_from_block"))
        shaped(RecipeCategory.BUILDING_BLOCKS, block)
            .hollow8()
            .define('A', tag(basePart.tagPrefix, material))
            .define('B', base)
            .group(block.id().path)
            .unlockedBy(base)
            .save(output)
    }

    private fun nineToBlock(material: RagiumMaterial, baseInput: Ingredient, base: HTSimpleDeferredItem) {
        val block: HTSimpleDeferredBlockAndItem = RagiumBlocks.STORAGE_BLOCKS[material] ?: return
        val blockTag: TagKey<Item> = CommonTagPrefixes.STORAGE_BLOCK.itemTagKey(material)

        shapeless(RecipeCategory.MISC, base, 9)
            .requires(blockTag)
            .group(base.id.path)
            .unlockedBy(blockTag)
            .save(output, base.id.withSuffix("_from_block"))
        shaped(RecipeCategory.BUILDING_BLOCKS, block)
            .hollow8()
            .define('A', baseInput)
            .define('B', base)
            .group(block.id().path)
            .unlockedBy(base)
            .save(output)
    }

    private fun ingotToNugget(
        material: RagiumMaterial,
        ingot: HTSimpleDeferredItem? = RagiumItems.MATERIAL_ITEMS[HTItemPart.INGOT, material],
        nugget: HTSimpleDeferredItem? = RagiumItems.MATERIAL_ITEMS[HTItemPart.NUGGET, material]
    ) {
        if (ingot == null || nugget == null) return
        val ingotTag: TagKey<Item> = CommonTagPrefixes.INGOT.itemTagKey(material)
        val nuggetTag: TagKey<Item> = CommonTagPrefixes.NUGGET.itemTagKey(material)
        shapeless(RecipeCategory.MISC, nugget, 9)
            .requires(ingotTag)
            .group(nugget.id.path)
            .unlockedBy(ingotTag)
            .save(output, nugget.id.withSuffix("_from_ingot"))
        shaped(RecipeCategory.MISC, ingot)
            .hollow8()
            .define('A', nuggetTag)
            .define('B', nugget)
            .group(ingot.id.path)
            .unlockedBy(nugget)
            .save(output, ingot.id.withSuffix("_from_nugget"))
    }

    private fun gear(basePrefix: HTTagPrefix, material: RagiumMaterial) {
        val input: TagKey<Item> = basePrefix.itemTagKey(material)
        shaped(RecipeCategory.MISC, RagiumItems.getOrThrow(HTItemPart.GEAR, material))
            .hollow4()
            .define('A', input)
            .define('B', tag(CommonTagPrefixes.GEAR, RagiumMaterial.Other.WOOD))
            .unlockedBy(input)
            .save(output)
    }

    //    Extension    //

    private fun RecipeBuilder.save(output: RecipeOutput, id: Identifier) {
        this.save(output, RecipeKey(id))
    }

    private fun <T : RecipeBuilder> T.unlockedBy(item: Item): T = apply {
        this.unlockedBy(getHasName(item), has(item))
    }

    private fun <T : RecipeBuilder> T.unlockedBy(value: HTValueWithKey<out ItemLike, *>): T = apply {
        this.unlockedBy(getHasName(value), has(value.getOrThrow()))
    }

    private fun <T : RecipeBuilder> T.unlockedBy(tagKey: TagKey<Item>): T = apply {
        this.unlockedBy(getHasName(tagKey), has(tagKey))
    }

    private fun <T : RecipeBuilder> T.unlockedBy(tag: BlockItemTagId): T = apply {
        this.unlockedBy(tag.item())
    }

    private fun <T : RecipeBuilder> T.unlockedBy(prefix: HTTagPrefix, material: HTMaterialLike): T = apply {
        this.unlockedBy(prefix.itemTagKey(material))
    }

    private fun ShapedRecipeBuilder.hollow(): ShapedRecipeBuilder = this
        .pattern("AAA")
        .pattern("A A")
        .pattern("AAA")

    private fun ShapedRecipeBuilder.hollow4(): ShapedRecipeBuilder = this
        .pattern(" A ")
        .pattern("ABA")
        .pattern(" A ")

    private fun ShapedRecipeBuilder.hollow8(): ShapedRecipeBuilder = this
        .pattern("AAA")
        .pattern("ABA")
        .pattern("AAA")

    private fun netheriteSmithing(base: Ingredient, category: RecipeCategory, result: ItemStackTemplate) {
        SmithingTransformRecipeBuilder(
            Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
            base,
            tag(ItemTags.NETHERITE_TOOL_MATERIALS),
            category,
            result
        ).unlocks("has_netherite_ingot", has(ItemTags.NETHERITE_TOOL_MATERIALS))
            .save(output, RecipeKey(result.item().key().identifier().withSuffix("_smithing")))
    }

    // Recipe
    private fun tag(tag: BlockItemTagId): Ingredient = tag(tag.item())

    private fun tag(prefix: HTTagPrefix, material: HTMaterialLike): Ingredient = tag(prefix.itemTagKey(material))

    // Advancement
    private fun getHasName(id: Identifier): String = "has_${id.debugPath}"

    private fun getHasName(value: HTSimpleValueWithKey<*>): String = getHasName(value.id())

    private fun getHasName(tagKey: TagKey<*>): String = getHasName(tagKey.location())
}
