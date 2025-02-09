package hiiragi283.ragium.data.server

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.toStack
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.CommonMaterials
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumMachineKeys
import net.minecraft.advancements.Advancement
import net.minecraft.advancements.AdvancementHolder
import net.minecraft.advancements.AdvancementType
import net.minecraft.advancements.DisplayInfo
import net.minecraft.advancements.critereon.InventoryChangeTrigger
import net.minecraft.advancements.critereon.ItemPredicate
import net.minecraft.core.HolderLookup
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.data.AdvancementProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredItem
import java.util.*
import java.util.function.Consumer

object RagiumAdvancementGenerator : AdvancementProvider.AdvancementGenerator {
    @JvmStatic
    private lateinit var root: AdvancementHolder

    @JvmStatic
    private lateinit var output: Consumer<AdvancementHolder>

    override fun generate(registries: HolderLookup.Provider, saver: Consumer<AdvancementHolder>, existingFileHelper: ExistingFileHelper) {
        output = saver
        val rawRaginite: ItemLike =
            RagiumItems.getMaterialItem(HTTagPrefix.RAW_MATERIAL, RagiumMaterials.RAGINITE)
        root = Advancement.Builder
            .advancement()
            .display(
                DisplayInfo(
                    rawRaginite.toStack(),
                    Component.literal(RagiumAPI.MOD_NAME),
                    Component.literal("Welcome to Ragium!"),
                    Optional.of(ResourceLocation.withDefaultNamespace("textures/block/bricks.png")),
                    AdvancementType.TASK,
                    true,
                    true,
                    false,
                ),
            ).hasItem("has_raginite", rawRaginite)
            .save("root")

        val ragiAlloy: AdvancementHolder = createMaterial(
            root,
            HTTagPrefix.INGOT,
            RagiumMaterials.RAGI_ALLOY,
            Component.empty(),
        )
        val grinder: AdvancementHolder = createSimple(
            ragiAlloy,
            RagiumBlocks.MANUAL_GRINDER,
            Component.empty(),
        )
        val raginiteDust: AdvancementHolder = createMaterial(
            grinder,
            HTTagPrefix.DUST,
            RagiumMaterials.RAGINITE,
            Component.empty(),
        )

        val pbf: AdvancementHolder = createSimple(
            ragiAlloy,
            RagiumBlocks.PRIMITIVE_BLAST_FURNACE,
            Component.empty(),
        )
        val steel: AdvancementHolder = createMaterial(
            pbf,
            HTTagPrefix.INGOT,
            CommonMaterials.STEEL,
            Component.empty(),
        )

        registerMachines(steel)
    }

    private fun registerMachines(steel: AdvancementHolder) {
        val casing: AdvancementHolder = createSimple(
            steel,
            RagiumItems.MACHINE_CASING,
            Component.empty(),
            type = AdvancementType.GOAL,
        )

        // Assembler
        val assembler: AdvancementHolder = createMachine(casing, RagiumMachineKeys.ASSEMBLER)
        val basicCircuit: AdvancementHolder = createSimple(assembler, RagiumItems.BASIC_CIRCUIT, Component.empty())
        val advancedCircuit: AdvancementHolder = createSimple(basicCircuit, RagiumItems.ADVANCED_CIRCUIT, Component.empty())
        val eliteCircuit: AdvancementHolder = createSimple(advancedCircuit, RagiumItems.ELITE_CIRCUIT, Component.empty())
        val ultimateCircuit: AdvancementHolder = createSimple(
            eliteCircuit,
            RagiumItems.ULTIMATE_CIRCUIT,
            Component.empty(),
            type = AdvancementType.GOAL,
        )

        val berriesCake: AdvancementHolder = createSimple(assembler, RagiumBlocks.SWEET_BERRIES_CAKE, Component.empty())
        val ambrosia: AdvancementHolder = createSimple(
            berriesCake,
            RagiumItems.AMBROSIA,
            Component.empty(),
            type = AdvancementType.CHALLENGE,
        )
        // Blast Furnace
        val blastFurnace: AdvancementHolder = createMachine(casing, RagiumMachineKeys.BLAST_FURNACE)
        val slagCollector: AdvancementHolder =
            createSimple(blastFurnace, RagiumBlocks.SLAG_COLLECTOR, Component.empty())
        val chemicalGlass: AdvancementHolder =
            createSimple(slagCollector, RagiumBlocks.CHEMICAL_GLASS, Component.empty())
        // Compressor
        val compressor: AdvancementHolder = createMachine(casing, RagiumMachineKeys.COMPRESSOR)
        val meatIngot: AdvancementHolder = createSimple(compressor, RagiumItems.MEAT_INGOT, Component.empty())
        val cannedMeat: AdvancementHolder = createSimple(
            meatIngot,
            RagiumItems.CANNED_COOKED_MEAT,
            Component.empty(),
            type = AdvancementType.GOAL,
        )
        // Grinder
        val grinder: AdvancementHolder = createMachine(casing, RagiumMachineKeys.GRINDER)
        val deepant: AdvancementHolder = createSimple(grinder, RagiumItems.DEEPANT_REAGENT, Component.empty())
        val deepSteel: AdvancementHolder = createMaterial(
            deepant,
            HTTagPrefix.INGOT,
            RagiumMaterials.DEEP_STEEL,
            Component.empty(),
        )
        registerChemical(deepSteel)
    }

    private fun registerChemical(deepSteel: AdvancementHolder) {
        val casing: AdvancementHolder = createSimple(
            deepSteel,
            RagiumItems.CHEMICAL_MACHINE_CASING,
            Component.empty(),
            type = AdvancementType.GOAL,
        )
        // Extractor
        val extractor: AdvancementHolder = createMachine(casing, RagiumMachineKeys.EXTRACTOR)
        // Infuser
        val infuser: AdvancementHolder = createMachine(casing, RagiumMachineKeys.INFUSER)
        val alumina: AdvancementHolder = createMaterial(
            infuser,
            HTTagPrefix.DUST,
            CommonMaterials.ALUMINA,
            Component.empty(),
        )
        val aluminum: AdvancementHolder = createMaterial(
            alumina,
            HTTagPrefix.INGOT,
            CommonMaterials.ALUMINUM,
            Component.empty(),
            type = AdvancementType.GOAL,
        )
        registerPrecision(aluminum)
        val cryolite: AdvancementHolder = createMaterial(
            aluminum,
            HTTagPrefix.GEM,
            CommonMaterials.CRYOLITE,
            Component.empty(),
            type = AdvancementType.CHALLENGE,
        )

        val fieryCoal: AdvancementHolder = createMaterial(
            infuser,
            HTTagPrefix.GEM,
            RagiumMaterials.FIERY_COAL,
            Component.empty(),
            type = AdvancementType.CHALLENGE,
        )

        // Mixer
        val mixer: AdvancementHolder = createMachine(casing, RagiumMachineKeys.MIXER)
        // Refinery
        val refinery: AdvancementHolder = createMachine(casing, RagiumMachineKeys.REFINERY)
        val crudeOil: AdvancementHolder = createSimple(refinery, RagiumItems.CRUDE_OIL_BUCKET, Component.empty())
        val resin: AdvancementHolder = createSimple(crudeOil, RagiumItems.POLYMER_RESIN, Component.empty())
        val plastic: AdvancementHolder = createSimple(resin, RagiumItems.PLASTIC_PLATE, Component.empty())

        val crimson: AdvancementHolder = createSimple(refinery, RagiumItems.CRIMSON_CRYSTAL, Component.empty(), type = AdvancementType.GOAL)
        val warped: AdvancementHolder = createSimple(refinery, RagiumItems.WARPED_CRYSTAL, Component.empty(), type = AdvancementType.GOAL)
    }

    private fun registerPrecision(aluminum: AdvancementHolder) {
        val casing: AdvancementHolder = createSimple(
            aluminum,
            RagiumItems.PRECISION_MACHINE_CASING,
            Component.empty(),
        )

        // Alchemical Brewery
        val brewery: AdvancementHolder = createMachine(casing, RagiumMachineKeys.ALCHEMICAL_BREWERY)
        // Arcane Enchanter
        val enchanter: AdvancementHolder = createMachine(casing, RagiumMachineKeys.ARCANE_ENCHANTER)
        // Laser Assembly
        val assembly: AdvancementHolder = createMachine(casing, RagiumMachineKeys.LASER_ASSEMBLY)
    }

    @JvmStatic
    private fun create(parent: AdvancementHolder): Advancement.Builder = Advancement.Builder.advancement().parent(parent)

    @JvmStatic
    private fun <T> createSimple(
        parent: AdvancementHolder,
        holder: T,
        desc: Component,
        title: Component = holder.toStack().hoverName,
        type: AdvancementType = AdvancementType.TASK,
        showToast: Boolean = true,
        showChat: Boolean = true,
        hidden: Boolean = false,
    ): AdvancementHolder where T : DeferredHolder<*, *>, T : ItemLike = create(parent)
        .display(
            DisplayInfo(
                ItemStack(holder),
                title,
                desc,
                Optional.empty(),
                type,
                showToast,
                showChat,
                hidden,
            ),
        ).hasItem("has_${holder.id.path}", holder)
        .save(holder.id.path)

    @JvmStatic
    private fun createMaterial(
        parent: AdvancementHolder,
        prefix: HTTagPrefix,
        material: HTMaterialKey,
        desc: Component,
        title: Component = prefix.createText(material),
        type: AdvancementType = AdvancementType.TASK,
        showToast: Boolean = true,
        showChat: Boolean = true,
        hidden: Boolean = false,
    ): AdvancementHolder {
        val item: DeferredItem<out Item> = RagiumItems.getMaterialItem(prefix, material)
        return create(parent)
            .display(
                DisplayInfo(
                    item.toStack(),
                    title,
                    desc,
                    Optional.empty(),
                    type,
                    showToast,
                    showChat,
                    hidden,
                ),
            ).hasItemTag("has_${item.id.path}", prefix.createTag(material))
            .save(item.id.path)
    }

    @JvmStatic
    private fun createMachine(
        parent: AdvancementHolder,
        machine: HTMachineKey,
        type: AdvancementType = AdvancementType.TASK,
        showToast: Boolean = true,
        showChat: Boolean = true,
        hidden: Boolean = false,
    ): AdvancementHolder = create(parent)
        .display(
            DisplayInfo(
                ItemStack(machine.getBlock()),
                machine.text,
                machine.descriptionText,
                Optional.empty(),
                type,
                showToast,
                showChat,
                hidden,
            ),
        ).hasItem("has_${machine.name}", machine.getBlock())
        .save(machine.name)

    @JvmStatic
    private fun Advancement.Builder.hasItem(key: String, item: ItemLike): Advancement.Builder =
        addCriterion(key, InventoryChangeTrigger.TriggerInstance.hasItems(item))

    @JvmStatic
    private fun Advancement.Builder.hasItemTag(key: String, tagKey: TagKey<Item>): Advancement.Builder = addCriterion(
        key,
        InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(tagKey)),
    )

    @JvmStatic
    private fun Advancement.Builder.save(path: String): AdvancementHolder = save(output, RagiumAPI.id(path).toString())
}
