package hiiragi283.ragium.common

import hiiragi283.ragium.api.RagiumPlugin
import hiiragi283.ragium.api.extension.getMachineEntity
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.machine.entity.HTMachineEntity
import hiiragi283.ragium.api.machine.property.HTMachinePropertyKeys
import hiiragi283.ragium.api.machine.property.HTMachineTooltipAppender
import hiiragi283.ragium.common.init.RagiumMachineConditions
import hiiragi283.ragium.common.init.RagiumMachineTypes
import hiiragi283.ragium.common.machine.*
import net.minecraft.util.DyeColor
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

object RagiumDefaultPlugin : RagiumPlugin {
    override val priority: Int = -100

    override fun afterRagiumInit() {}

    override fun registerMachineType(register: RagiumPlugin.MachineRegister) {
        // generators
        register.registerGenerator(RagiumMachineTypes.HEAT_GENERATOR)
        RagiumMachineTypes.Generator.entries
            .map(RagiumMachineTypes.Generator::key)
            .forEach(register::registerGenerator)
        // processors
        register.registerProcessor(RagiumMachineTypes.BLAST_FURNACE)
        register.registerProcessor(RagiumMachineTypes.DISTILLATION_TOWER)
        register.registerProcessor(RagiumMachineTypes.SAW_MILL)
        RagiumMachineTypes.Processor.entries
            .map(RagiumMachineTypes.Processor::key)
            .forEach(register::registerProcessor)
    }

    override fun setupCommonMachineProperties(helper: RagiumPlugin.PropertyHelper) {
        // generators
        helper.modify(RagiumMachineTypes.HEAT_GENERATOR) {
            set(HTMachinePropertyKeys.GENERATOR_PREDICATE) { world: World, pos: BlockPos ->
                world
                    .getMachineEntity(pos)
                    ?.let { it as? HTHeatGeneratorMachineEntity }
                    ?.isBurning
                    ?: false
            }
            set(
                HTMachinePropertyKeys.MACHINE_FACTORY,
                HTMachineEntity.Factory.ofStatic(::HTHeatGeneratorMachineEntity),
            )
            set(HTMachinePropertyKeys.GENERATOR_COLOR, DyeColor.RED)
        }
        RagiumMachineTypes.Generator.entries.forEach {
            helper.modify(it.key, it::buildProperties)
        }
        // processors
        helper.modify(RagiumMachineTypes.BLAST_FURNACE) {
            set(
                HTMachinePropertyKeys.MACHINE_FACTORY,
                HTMachineEntity.Factory.ofStatic(::HTBlastFurnaceMachineEntity),
            )
            set(HTMachinePropertyKeys.TOOLTIP_BUILDER, HTMachineTooltipAppender.DEFAULT_PROCESSOR)
            set(HTMachinePropertyKeys.PROCESSOR_CONDITION, RagiumMachineConditions.ELECTRIC_CONDITION)
            set(HTMachinePropertyKeys.PROCESSOR_SUCCEEDED, RagiumMachineConditions.ELECTRIC_SUCCEEDED)
            set(HTMachinePropertyKeys.RECIPE_SIZE, HTMachineType.Size.LARGE)
        }
        helper.modify(RagiumMachineTypes.DISTILLATION_TOWER) {
            set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntity.Factory.ofStatic(::HTDistillationTowerMachineEntity))
            set(HTMachinePropertyKeys.TOOLTIP_BUILDER, HTMachineTooltipAppender.DEFAULT_PROCESSOR)
            set(HTMachinePropertyKeys.PROCESSOR_CONDITION, RagiumMachineConditions.ELECTRIC_CONDITION)
            set(HTMachinePropertyKeys.PROCESSOR_SUCCEEDED, RagiumMachineConditions.ELECTRIC_SUCCEEDED)
            set(HTMachinePropertyKeys.RECIPE_SIZE, HTMachineType.Size.LARGE)
        }
        helper.modify(RagiumMachineTypes.SAW_MILL) {
            set(HTMachinePropertyKeys.FRONT_TEX) { Identifier.of("block/stonecutter_saw") }
            set(HTMachinePropertyKeys.TOOLTIP_BUILDER, HTMachineTooltipAppender.DEFAULT_PROCESSOR)
            set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntity.Factory.ofStatic(::HTSawMillMachineEntity))
            set(HTMachinePropertyKeys.PROCESSOR_CONDITION, RagiumMachineConditions.ELECTRIC_CONDITION)
            set(HTMachinePropertyKeys.PROCESSOR_SUCCEEDED, RagiumMachineConditions.ELECTRIC_SUCCEEDED)
            set(HTMachinePropertyKeys.RECIPE_SIZE, HTMachineType.Size.LARGE)
        }
        RagiumMachineTypes.Processor.entries.forEach {
            helper.modify(it.key) {
                set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntity.Factory.ofProcessor(::HTSimpleProcessorMachineEntity))
                set(HTMachinePropertyKeys.TOOLTIP_BUILDER, HTMachineTooltipAppender.DEFAULT_PROCESSOR)
                set(HTMachinePropertyKeys.PROCESSOR_CONDITION, RagiumMachineConditions.ELECTRIC_CONDITION)
                set(HTMachinePropertyKeys.RECIPE_SIZE, HTMachineType.Size.SIMPLE)
                setIfNonNull(HTMachinePropertyKeys.PROCESSOR_SUCCEEDED, RagiumMachineConditions.ELECTRIC_SUCCEEDED)
            }
        }
    }
}
