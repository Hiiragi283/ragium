package hiiragi283.ragium.common.block.entity.device

import hiiragi283.ragium.api.registry.HTFluidHolderLike
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.holder.HTSlotInfo
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.api.world.getRangedAABB
import hiiragi283.ragium.common.storage.fluid.tank.HTExpOrbTank
import hiiragi283.ragium.common.storage.fluid.tank.HTVariableFluidStackTank
import hiiragi283.ragium.common.storage.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.util.HTExperienceHelper
import hiiragi283.ragium.common.util.HTStackSlotHelper
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.Holder
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.tags.BiomeTags
import net.minecraft.tags.FluidTags
import net.minecraft.world.entity.EntitySelector
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.ExperienceOrb
import net.minecraft.world.level.Level
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.block.state.BlockState

class HTFluidCollectorBlockEntity(pos: BlockPos, state: BlockState) :
    HTDeviceBlockEntity.Tickable(RagiumBlocks.FLUID_COLLECTOR, pos, state) {
    lateinit var tank: HTVariableFluidStackTank
        private set

    override fun initializeFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: HTContentListener) {
        // output
        tank = builder.addSlot(
            HTSlotInfo.OUTPUT,
            HTVariableFluidStackTank.output(listener, RagiumConfig.COMMON.deviceCollectorTankCapacity),
        )
    }

    override fun onRemove(level: Level, pos: BlockPos) {
        super.onRemove(level, pos)
        if (RagiumFluidContents.EXPERIENCE.isOf(tank.getStack())) {
            ExperienceOrb(
                level,
                pos.x.toDouble(),
                pos.y.toDouble(),
                pos.z.toDouble(),
                HTExperienceHelper.expAmountFromFluid(tank.getAmount()),
            ).let(level::addFreshEntity)
        }
    }

    override fun getComparatorOutput(state: BlockState, level: Level, pos: BlockPos): Int = HTStackSlotHelper.calculateRedstoneLevel(tank)

    //    Ticking    //

    override fun actionServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean = when {
        hasUpgrade(RagiumItems.EXP_COLLECTOR_UPGRADE) -> collectExp(level, pos)
        else -> generateWater(level, pos)
    }

    private fun collectExp(level: ServerLevel, pos: BlockPos): Boolean {
        // 範囲内のExp Orbを取得する
        val expOrbs: List<ExperienceOrb> = level.getEntities(
            EntityType.EXPERIENCE_ORB,
            pos.center.getRangedAABB(RagiumConfig.COMMON.deviceCollectorEntityRange.asDouble),
            EntitySelector.NO_SPECTATORS,
        )
        if (expOrbs.isEmpty()) return false
        // それぞれのExp Orbに対して回収を行う
        expOrbs
            .asSequence()
            .filter(ExperienceOrb::isAlive)
            .map(::HTExpOrbTank)
            .forEach { tank: HTExpOrbTank -> HTStackSlotHelper.moveStack(tank, this.tank) }
        return true
    }

    private fun generateWater(level: ServerLevel, pos: BlockPos): Boolean {
        // 液体を生成できるかチェック
        val amount: Int = calculateWaterAmount(level, pos)
        val stack: ImmutableFluidStack = HTFluidHolderLike.WATER.toImmutableStack(amount) ?: return false
        // 液体を搬入できるかチェック
        if (tank.insert(stack, HTStorageAction.SIMULATE, HTStorageAccess.INTERNAL) != null) return false
        tank.insert(stack, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
        level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS)
        return true
    }

    private fun calculateWaterAmount(level: ServerLevel, pos: BlockPos): Int {
        var amount = 0
        // 海洋バイオームまたは河川系バイオームの場合 -> +1000 mB
        val biome: Holder<Biome> = level.getBiome(pos)
        if (biome.`is`(BiomeTags.IS_OCEAN) || biome.`is`(BiomeTags.IS_RIVER)) {
            amount += 1000
        }
        // 周囲に2ブロック以上の水源がある -> +500 mB
        val waterSources: Int = Direction.Plane.HORIZONTAL
            .count { direction: Direction -> level.getFluidState(pos.relative(direction)).`is`(FluidTags.WATER) }
        if (waterSources >= 2) {
            amount += 500
        }
        return amount
    }
}
