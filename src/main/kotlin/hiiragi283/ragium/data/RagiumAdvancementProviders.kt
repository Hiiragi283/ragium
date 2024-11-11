package hiiragi283.ragium.data

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.content.HTContent
import hiiragi283.ragium.api.extension.name
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.tags.RagiumItemTags
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.init.*
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider
import net.minecraft.advancement.Advancement
import net.minecraft.advancement.AdvancementEntry
import net.minecraft.advancement.AdvancementFrame
import net.minecraft.advancement.criterion.InventoryChangedCriterion
import net.minecraft.fluid.Fluid
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.predicate.ComponentPredicate
import net.minecraft.predicate.item.ItemPredicate
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.tag.TagKey
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import java.util.concurrent.CompletableFuture
import java.util.function.Consumer

object RagiumAdvancementProviders {
    @JvmStatic
    fun init(pack: FabricDataGenerator.Pack) {
        pack.addProvider(::Progress)
        pack.addProvider(::Chemistry)
        pack.addProvider(::Machine)
        pack.addProvider(::PetroChemistry)
    }

    private fun createAdvancement(
        consumer: Consumer<AdvancementEntry>,
        path: String,
        builderAction: Advancement.Builder.() -> Unit,
    ): AdvancementEntry = Advancement.Builder
        .create()
        .apply(builderAction)
        .build(consumer, RagiumAPI.id(path).toString())

    private fun createRoot(
        consumer: Consumer<AdvancementEntry>,
        path: String,
        icon: ItemConvertible,
        title: Text,
        desc: Text,
        background: Identifier,
        frame: AdvancementFrame = AdvancementFrame.TASK,
        showToast: Boolean = false,
        announce: Boolean = false,
        hidden: Boolean = false,
        builderAction: Advancement.Builder.() -> Unit,
    ): AdvancementEntry = createAdvancement(consumer, path) {
        display(
            icon,
            title,
            desc,
            background,
            frame,
            showToast,
            announce,
            hidden,
        )
        builderAction()
    }

    private fun createRoot(
        consumer: Consumer<AdvancementEntry>,
        path: String,
        icon: ItemStack,
        title: Text,
        desc: Text,
        background: Identifier,
        frame: AdvancementFrame = AdvancementFrame.TASK,
        showToast: Boolean = false,
        announce: Boolean = false,
        hidden: Boolean = false,
        builderAction: Advancement.Builder.() -> Unit,
    ): AdvancementEntry = createAdvancement(consumer, path) {
        display(
            icon,
            title,
            desc,
            background,
            frame,
            showToast,
            announce,
            hidden,
        )
        builderAction()
    }

    private fun createChild(
        consumer: Consumer<AdvancementEntry>,
        path: String,
        parent: AdvancementEntry,
        icon: ItemConvertible,
        title: Text = icon.asItem().name,
        desc: Text = Text.empty(),
        frame: AdvancementFrame = AdvancementFrame.TASK,
        showToast: Boolean = true,
        announce: Boolean = true,
        hidden: Boolean = false,
        builderAction: Advancement.Builder.() -> Unit,
    ): AdvancementEntry = createAdvancement(consumer, path) {
        parent(parent)
        display(
            icon,
            title,
            desc,
            null,
            frame,
            showToast,
            announce,
            hidden,
        )
        builderAction()
    }

    private fun createContentChild(
        consumer: Consumer<AdvancementEntry>,
        path: String,
        parent: AdvancementEntry,
        icon: HTContent.Material<*>,
        title: Text = icon.asItem().name,
        desc: Text = Text.empty(),
        frame: AdvancementFrame = AdvancementFrame.TASK,
        showToast: Boolean = true,
        announce: Boolean = true,
        hidden: Boolean = false,
    ): AdvancementEntry = createAdvancement(consumer, path) {
        parent(parent)
        display(
            icon,
            title,
            desc,
            null,
            frame,
            showToast,
            announce,
            hidden,
        )
        hasAnyItems(icon)
    }

    private fun createFluidChild(
        consumer: Consumer<AdvancementEntry>,
        path: String,
        parent: AdvancementEntry,
        fluid: Fluid,
        title: Text = fluid.name,
        desc: Text = Text.empty(),
        frame: AdvancementFrame = AdvancementFrame.TASK,
        showToast: Boolean = true,
        announce: Boolean = true,
        hidden: Boolean = false,
    ): AdvancementEntry = createAdvancement(consumer, path) {
        parent(parent)
        display(
            RagiumAPI.getInstance().createFilledCube(fluid),
            title,
            desc,
            null,
            frame,
            showToast,
            announce,
            hidden,
        )
        hasFluid(fluid)
    }

    private fun createMachineChild(
        consumer: Consumer<AdvancementEntry>,
        path: String,
        parent: AdvancementEntry,
        key: HTMachineKey,
        tier: HTMachineTier,
        title: Text = tier.createPrefixedText(key),
        desc: Text = Text.empty(),
        frame: AdvancementFrame = AdvancementFrame.TASK,
        showToast: Boolean = true,
        announce: Boolean = true,
        hidden: Boolean = false,
    ): AdvancementEntry = createAdvancement(consumer, path) {
        parent(parent)
        display(
            key.entry.getBlock(tier),
            title,
            desc,
            null,
            frame,
            showToast,
            announce,
            hidden,
        )
        hasAnyItems(key.tagKey)
    }

    @Suppress("removal")
    private fun createChild(
        consumer: Consumer<AdvancementEntry>,
        path: String,
        parent: Identifier,
        icon: ItemConvertible,
        title: Text = icon.asItem().name,
        desc: Text = Text.empty(),
        frame: AdvancementFrame = AdvancementFrame.TASK,
        showToast: Boolean = true,
        announce: Boolean = true,
        hidden: Boolean = false,
        builderAction: Advancement.Builder.() -> Unit,
    ): AdvancementEntry = createAdvancement(consumer, path) {
        parent(parent)
        display(
            icon,
            title,
            desc,
            null,
            frame,
            showToast,
            announce,
            hidden,
        )
        builderAction()
    }

    private fun Advancement.Builder.buildMultiblock(key: HTMachineKey, minTier: HTMachineTier): Advancement.Builder = criterion(
        "build_multiblock",
        RagiumAPI.getInstance().createBuiltMachineCriterion(key, minTier),
    )

    private fun Advancement.Builder.hasAllItems(vararg items: ItemConvertible): Advancement.Builder = criterion(
        "has_items",
        InventoryChangedCriterion.Conditions.items(*items),
    )

    private fun Advancement.Builder.hasAnyItems(tagKey: TagKey<Item>): Advancement.Builder = criterion(
        "has_items",
        InventoryChangedCriterion.Conditions.items(ItemPredicate.Builder.create().tag(tagKey)),
    )

    private fun Advancement.Builder.hasAnyItems(content: HTContent.Material<*>): Advancement.Builder = hasAnyItems(content.prefixedTagKey)

    private fun Advancement.Builder.hasFluid(fluid: Fluid): Advancement.Builder = criterion(
        "has_fluids",
        InventoryChangedCriterion.Conditions.items(
            ItemPredicate.Builder
                .create()
                .items(RagiumItems.FILLED_FLUID_CUBE)
                .component(
                    ComponentPredicate
                        .builder()
                        .add(RagiumComponentTypes.FLUID, fluid)
                        .build(),
                ),
        ),
    )

    private fun Advancement.Builder.hasMachine(key: HTMachineKey, tier: HTMachineTier): Advancement.Builder = criterion(
        "has_items",
        InventoryChangedCriterion.Conditions.items(key.createItemStack(tier).item),
    )

    //    Progress    //

    private class Progress(output: FabricDataOutput, registryLookup: CompletableFuture<RegistryWrapper.WrapperLookup>) :
        FabricAdvancementProvider(output, registryLookup) {
        override fun getName(): String = "${super.getName()}/Progress"

        override fun generateAdvancement(registryLookup: RegistryWrapper.WrapperLookup, consumer: Consumer<AdvancementEntry>) {
            // tier 1
            val root: AdvancementEntry = createRoot(
                consumer,
                "progress/root",
                RagiumContents.Ores.CRUDE_RAGINITE,
                Text.literal(RagiumAPI.MOD_NAME),
                Text.empty(),
                Identifier.of("textures/block/bricks.png"),
            ) { hasAnyItems(RagiumContents.RawMaterials.CRUDE_RAGINITE) }
            val ragiAlloy: AdvancementEntry = createContentChild(
                consumer,
                "progress/ragi_alloy",
                root,
                RagiumContents.Ingots.RAGI_ALLOY,
                frame = AdvancementFrame.GOAL,
            )
            val manualGrinder: AdvancementEntry = createChild(
                consumer,
                "progress/ragi_grinder",
                ragiAlloy,
                RagiumBlocks.MANUAL_GRINDER,
            ) { hasAllItems(RagiumBlocks.MANUAL_GRINDER) }
            val manualForge: AdvancementEntry = createChild(
                consumer,
                "progress/manual_forge",
                manualGrinder,
                RagiumBlocks.MANUAL_FORGE,
            ) { hasAllItems(RagiumBlocks.MANUAL_FORGE) }
            val ragiAlloyPlate: AdvancementEntry = createChild(
                consumer,
                "progress/ragi_alloy_plate",
                manualForge,
                RagiumItems.FORGE_HAMMER,
            ) { hasAnyItems(RagiumContents.Plates.RAGI_ALLOY) }
            val primitiveHull: AdvancementEntry = createChild(
                consumer,
                "progress/primitive_hull",
                ragiAlloyPlate,
                RagiumContents.Hulls.PRIMITIVE,
            ) { hasAllItems(RagiumContents.Hulls.PRIMITIVE) }
            val alloyFurnace: AdvancementEntry = createMachineChild(
                consumer,
                "progress/primitive_alloy_furnace",
                primitiveHull,
                RagiumMachineKeys.ALLOY_FURNACE,
                HTMachineTier.PRIMITIVE,
            )
            val blastFurnace: AdvancementEntry = createChild(
                consumer,
                "progress/primitive_blast_furnace",
                primitiveHull,
                RagiumMachineKeys.BLAST_FURNACE.entry.getBlock(HTMachineTier.PRIMITIVE),
            ) { buildMultiblock(RagiumMachineKeys.BLAST_FURNACE, HTMachineTier.PRIMITIVE) }
            // tier 2
            val ragiSteel: AdvancementEntry = createContentChild(
                consumer,
                "progress/ragi_steel",
                blastFurnace,
                RagiumContents.Ingots.RAGI_STEEL,
                frame = AdvancementFrame.GOAL,
            )
            val basicHull: AdvancementEntry = createChild(
                consumer,
                "progress/basic_hull",
                ragiSteel,
                RagiumContents.Hulls.BASIC,
            ) { hasAllItems(RagiumContents.Hulls.BASIC) }
            val blastFurnace1: AdvancementEntry = createChild(
                consumer,
                "progress/basic_blast_furnace",
                basicHull,
                RagiumMachineKeys.BLAST_FURNACE.entry.getBlock(HTMachineTier.BASIC),
            ) { buildMultiblock(RagiumMachineKeys.BLAST_FURNACE, HTMachineTier.BASIC) }
            // tier 3
            val ragiCrystal: AdvancementEntry = createContentChild(
                consumer,
                "progress/ragi_crystal",
                blastFurnace1,
                RagiumContents.Gems.RAGI_CRYSTAL,
            )
            val refinedRagiSteel: AdvancementEntry = createContentChild(
                consumer,
                "progress/refined_ragi_steel",
                ragiCrystal,
                RagiumContents.Ingots.REFINED_RAGI_STEEL,
                frame = AdvancementFrame.GOAL,
            )
            val advancedHull: AdvancementEntry = createChild(
                consumer,
                "progress/advanced_hull",
                refinedRagiSteel,
                RagiumContents.Hulls.ADVANCED,
            ) { hasAllItems(RagiumContents.Hulls.ADVANCED) }
            val laserTransformer: AdvancementEntry = createMachineChild(
                consumer,
                "progress/laser_transformer",
                advancedHull,
                RagiumMachineKeys.LASER_TRANSFORMER,
                HTMachineTier.ADVANCED,
            )
            // tier 4
            val ragium: AdvancementEntry = createContentChild(
                consumer,
                "progress/ragium",
                laserTransformer,
                RagiumContents.Gems.RAGIUM,
                frame = AdvancementFrame.CHALLENGE,
            )
            val stellaSuit: AdvancementEntry = createChild(
                consumer,
                "progress/stella_suit",
                ragium,
                RagiumItems.STELLA_GOGGLE,
                title = Text.translatable(RagiumTranslationKeys.ADVANCEMENT_STELLA_SUIT),
                frame = AdvancementFrame.CHALLENGE,
            ) {
                hasAllItems(
                    RagiumItems.STELLA_GOGGLE,
                    RagiumItems.STELLA_JACKET,
                    RagiumItems.STELLA_LEGGINGS,
                    RagiumItems.STELLA_BOOTS,
                )
            }
            val bujin: AdvancementEntry = createChild(
                consumer,
                "progress/bujin",
                ragium,
                RagiumItems.BUJIN,
                title = Text.translatable(RagiumTranslationKeys.ADVANCEMENT_BUJIN),
                frame = AdvancementFrame.CHALLENGE,
            ) { hasAllItems(RagiumItems.BUJIN) }
        }
    }

    //    Machine    //

    private class Machine(output: FabricDataOutput, registryLookup: CompletableFuture<RegistryWrapper.WrapperLookup>) :
        FabricAdvancementProvider(output, registryLookup) {
        override fun getName(): String = "${super.getName()}/Machine"

        override fun generateAdvancement(registryLookup: RegistryWrapper.WrapperLookup, consumer: Consumer<AdvancementEntry>) {
            val root: AdvancementEntry = createRoot(
                consumer,
                "machine/root",
                RagiumContents.Hulls.PRIMITIVE,
                Text.literal("Machine"),
                Text.empty(),
                Identifier.of("textures/block/smooth_stone.png"),
            ) { hasAllItems(RagiumContents.Hulls.PRIMITIVE) }
            val multiSmelter: AdvancementEntry = createMachineChild(
                consumer,
                "machine/multi_smelter",
                root,
                RagiumMachineKeys.MULTI_SMELTER,
                HTMachineTier.PRIMITIVE,
            )
            // assembler
            val assembler: AdvancementEntry = createMachineChild(
                consumer,
                "machine/assembler",
                root,
                RagiumMachineKeys.ASSEMBLER,
                HTMachineTier.PRIMITIVE,
            )
            val primitiveCircuit: AdvancementEntry = createChild(
                consumer,
                "machine/primitive_circuit",
                assembler,
                RagiumContents.Circuits.PRIMITIVE,
            ) { hasAllItems(RagiumContents.Circuits.PRIMITIVE) }
            val basicCircuit: AdvancementEntry = createChild(
                consumer,
                "machine/basic_circuit",
                primitiveCircuit,
                RagiumContents.Circuits.BASIC,
            ) { hasAllItems(RagiumContents.Circuits.BASIC) }
            val advancedCircuit: AdvancementEntry = createChild(
                consumer,
                "machine/advanced_circuit",
                basicCircuit,
                RagiumContents.Circuits.ADVANCED,
            ) { hasAllItems(RagiumContents.Circuits.ADVANCED) }
            val processor: AdvancementEntry = createChild(
                consumer,
                "machine/processor",
                advancedCircuit,
                RagiumItems.RAGI_CRYSTAL_PROCESSOR,
                frame = AdvancementFrame.GOAL,
            ) { hasAllItems(RagiumItems.RAGI_CRYSTAL_PROCESSOR) }
            val largeProcessor: AdvancementEntry = createChild(
                consumer,
                "machine/large_processor",
                processor,
                RagiumBlocks.LARGE_PROCESSOR,
                frame = AdvancementFrame.CHALLENGE,
            ) { hasAllItems(RagiumBlocks.LARGE_PROCESSOR) }
        }
    }

    //    Chemistry    //

    private class Chemistry(output: FabricDataOutput, registryLookup: CompletableFuture<RegistryWrapper.WrapperLookup>) :
        FabricAdvancementProvider(output, registryLookup) {
        override fun getName(): String = "${super.getName()}/Chemistry"

        override fun generateAdvancement(registryLookup: RegistryWrapper.WrapperLookup, consumer: Consumer<AdvancementEntry>) {
            val root: AdvancementEntry = createRoot(
                consumer,
                "chemistry/root",
                RagiumMachineKeys.CHEMICAL_REACTOR.entry.getBlock(HTMachineTier.PRIMITIVE),
                Text.literal("Chemistry"),
                Text.empty(),
                Identifier.of("textures/block/quartz_block_top.png"),
            ) { hasMachine(RagiumMachineKeys.CHEMICAL_REACTOR, HTMachineTier.PRIMITIVE) }
            // hydrogen
            val hydrogen: AdvancementEntry = createFluidChild(
                consumer,
                "chemistry/hydrogen",
                root,
                RagiumFluids.HYDROGEN.value,
            )
            val alcohol: AdvancementEntry = createFluidChild(
                consumer,
                "chemistry/alcohol",
                hydrogen,
                RagiumFluids.ALCOHOL.value,
            )
            val bioFuel: AdvancementEntry = createFluidChild(
                consumer,
                "chemistry/bio_fuel",
                alcohol,
                RagiumFluids.BIO_FUEL.value,
                frame = AdvancementFrame.GOAL,
            )
            // oxygen
            val oxygen: AdvancementEntry = createFluidChild(
                consumer,
                "chemistry/oxygen",
                root,
                RagiumFluids.OXYGEN.value,
            )
            // nitrogen
            val niter: AdvancementEntry = createContentChild(
                consumer,
                "chemistry/niter",
                root,
                RagiumContents.Dusts.NITER,
            )
            val nitricAcid: AdvancementEntry = createFluidChild(
                consumer,
                "chemistry/nitric_acid",
                niter,
                RagiumFluids.NITRIC_ACID.value,
            )
            val mixtureAcid: AdvancementEntry = createFluidChild(
                consumer,
                "chemistry/mixture_acid",
                nitricAcid,
                RagiumFluids.MIXTURE_ACID.value,
            )
            val dynamite: AdvancementEntry = createChild(
                consumer,
                "chemistry/dynamite",
                mixtureAcid,
                RagiumItems.DYNAMITE,
                frame = AdvancementFrame.GOAL,
            ) { hasAllItems(RagiumItems.DYNAMITE) }
            // alkali
            val alkali: AdvancementEntry = createChild(
                consumer,
                "chemistry/alkali",
                root,
                RagiumContents.Dusts.ALKALI,
            ) { hasAnyItems(RagiumItemTags.ALKALI) }
            val soap: AdvancementEntry = createChild(
                consumer,
                "chemistry/soap",
                alkali,
                RagiumItems.SOAP_INGOT,
            ) { hasAllItems(RagiumItems.SOAP_INGOT) }
            // aluminum
            val bauxite: AdvancementEntry = createContentChild(
                consumer,
                "chemistry/bauxite",
                root,
                RagiumContents.Dusts.BAUXITE,
            )
            val aluminum: AdvancementEntry = createContentChild(
                consumer,
                "chemistry/aluminum",
                bauxite,
                RagiumContents.Ingots.ALUMINUM,
                frame = AdvancementFrame.GOAL,
            )
            val cryolite: AdvancementEntry = createContentChild(
                consumer,
                "chemistry/cryolite",
                aluminum,
                RagiumContents.Gems.CRYOLITE,
            )
            // sulfur
            val sulfur: AdvancementEntry = createContentChild(
                consumer,
                "chemistry/sulfur",
                root,
                RagiumContents.Dusts.SULFUR,
            )
            val sulfuricAcid: AdvancementEntry = createFluidChild(
                consumer,
                "chemistry/sulfuric_acid",
                sulfur,
                RagiumFluids.SULFURIC_ACID.value,
            )
            // chlorine
            val chlorine: AdvancementEntry = createFluidChild(
                consumer,
                "chemistry/chlorine",
                root,
                RagiumFluids.CHLORINE.value,
            )
            val hydrochloricAcid: AdvancementEntry = createFluidChild(
                consumer,
                "chemistry/hydrochloric_acid",
                chlorine,
                RagiumFluids.HYDROCHLORIC_ACID.value,
            )
            val aquaRegia: AdvancementEntry = createFluidChild(
                consumer,
                "chemistry/aqua_regia",
                hydrochloricAcid,
                RagiumFluids.AQUA_REGIA.value,
            )
            val deepSteel: AdvancementEntry = createContentChild(
                consumer,
                "chemistry/deep_steel",
                aquaRegia,
                RagiumContents.Ingots.DEEP_STEEL,
                frame = AdvancementFrame.GOAL,
            )
        }
    }

    //    Petro Chemistry    //

    private class PetroChemistry(output: FabricDataOutput, registryLookup: CompletableFuture<RegistryWrapper.WrapperLookup>) :
        FabricAdvancementProvider(output, registryLookup) {
        override fun getName(): String = "${super.getName()}/PetroChemistry"

        override fun generateAdvancement(registryLookup: RegistryWrapper.WrapperLookup, consumer: Consumer<AdvancementEntry>) {
            val root: AdvancementEntry = createRoot(
                consumer,
                "petro_chemistry/root",
                RagiumAPI.getInstance().createFilledCube(RagiumFluids.CRUDE_OIL.value),
                Text.literal("Petro Chemistry"),
                Text.empty(),
                Identifier.of("textures/block/blast_furnace_top.png"),
            ) { hasFluid(RagiumFluids.CRUDE_OIL.value) }
            val distillation: AdvancementEntry = createMachineChild(
                consumer,
                "petro_chemistry/distillation_tower",
                root,
                RagiumMachineKeys.DISTILLATION_TOWER,
                HTMachineTier.BASIC,
            )
            // plastic
            val polymerResin: AdvancementEntry = createChild(
                consumer,
                "petro_chemistry/polymer_resin",
                distillation,
                RagiumItems.POLYMER_RESIN,
            ) { hasAllItems(RagiumItems.POLYMER_RESIN) }
            val engineeringPlastic: AdvancementEntry = createContentChild(
                consumer,
                "petro_chemistry/engineering_plastic",
                polymerResin,
                RagiumContents.Plates.ENGINEERING_PLASTIC,
                frame = AdvancementFrame.GOAL,
            )
            // refined gas
            val refinedGas: AdvancementEntry = createFluidChild(
                consumer,
                "petro_chemistry/refined_gas",
                distillation,
                RagiumFluids.REFINED_GAS.value,
            )
            val nobleGas: AdvancementEntry = createFluidChild(
                consumer,
                "petro_chemistry/noble_gas",
                refinedGas,
                RagiumFluids.NOBLE_GAS.value,
            )
            // fuel
            val fuel: AdvancementEntry = createFluidChild(
                consumer,
                "petro_chemistry/fuel",
                distillation,
                RagiumFluids.FUEL.value,
            )
            val nitroFuel: AdvancementEntry = createFluidChild(
                consumer,
                "petro_chemistry/nitro_fuel",
                fuel,
                RagiumFluids.NITRO_FUEL.value,
                frame = AdvancementFrame.GOAL,
            )
            // residual
            val residualOil: AdvancementEntry = createFluidChild(
                consumer,
                "petro_chemistry/residual_oil",
                distillation,
                RagiumFluids.RESIDUAL_OIL.value,
            )
            val aromaticCompound: AdvancementEntry = createFluidChild(
                consumer,
                "petro_chemistry/aromatic_compound",
                residualOil,
                RagiumFluids.AROMATIC_COMPOUNDS.value,
            )
        }
    }
}
