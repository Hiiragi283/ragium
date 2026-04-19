package hiiragi283.ragium.common.block.entity.generator

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.gui.HTSlotHelper
import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.api.material.part.CommonParts
import hiiragi283.core.api.registry.VanillaFluidContents
import hiiragi283.core.api.storage.holder.HTFluidTankHolder
import hiiragi283.core.api.storage.holder.HTItemSlotHolder
import hiiragi283.core.common.gui.widget.HTFluidWidget
import hiiragi283.core.common.gui.widget.HTItemSlotWidget
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.storage.fluid.HTBasicFluidTank
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.core.impl.recipe.handler.HTFluidInputHandler
import hiiragi283.core.impl.recipe.handler.HTFluidOutputHandler
import hiiragi283.core.impl.recipe.handler.HTItemInputHandler
import hiiragi283.core.impl.recipe.handler.HTItemOutputHandler
import hiiragi283.ragium.common.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.common.storge.fluid.HTVariableFluidTank
import hiiragi283.ragium.common.storge.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.storge.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storge.holder.HTSlotInfo
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumFluids
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.FluidStack

class HTBoilerBlockEntity(pos: BlockPos, state: BlockState) : HTMachineBlockEntity(RagiumBlockEntityTypes.BOILER, pos, state) {
    lateinit var waterTank: HTBasicFluidTank
        private set
    private lateinit var steamTank: HTBasicFluidTank

    override fun createFluidHandler(listener: HTContentListener): HTFluidTankHolder? {
        val builder: HTBasicFluidTankHolder.Builder = HTBasicFluidTankHolder.builder(this)
        waterTank = builder.addSlot(
            HTSlotInfo.INPUT,
            HTVariableFluidTank.input(
                listener,
                getTankCapacity(),
                filter = VanillaFluidContents.WATER::isOf,
            ),
        )
        steamTank = builder.addSlot(
            HTSlotInfo.OUTPUT,
            HTVariableFluidTank.output(listener, getTankCapacity()),
        )
        return builder.build()
    }

    private lateinit var fuelSlot: HTBasicItemSlot
    private lateinit var ashSlot: HTBasicItemSlot

    override fun createItemHandler(listener: HTContentListener): HTItemSlotHolder? {
        val builder: HTBasicItemSlotHolder.Builder = HTBasicItemSlotHolder.builder(this)
        fuelSlot = builder.addSlot(
            HTSlotInfo.INPUT,
            HTBasicItemSlot.input(listener, filter = { it.toStack().getBurnTime(null) > 0 }),
        )
        ashSlot = builder.addSlot(HTSlotInfo.OUTPUT, HTBasicItemSlot.output(listener))
        return builder.build()
    }

    override fun setupMenu(widgetHolder: HTWidgetHolder) {
        super.setupMenu(widgetHolder)
        // slot
        widgetHolder += HTItemSlotWidget.container(
            fuelSlot,
            HTSlotHelper.getSlotPosX(4),
            HTSlotHelper.getSlotPosY(0),
            HTBackgroundType.INPUT,
        )
        widgetHolder += HTItemSlotWidget.container(
            ashSlot,
            HTSlotHelper.getSlotPosX(4),
            HTSlotHelper.getSlotPosY(2),
            HTBackgroundType.EXTRA_OUTPUT,
        )
        // tanks
        widgetHolder += HTFluidWidget
            .createTank(
                waterTank,
                HTSlotHelper.getSlotPosX(2),
                HTSlotHelper.getSlotPosY(0),
            ).setBackground(HTBackgroundType.INPUT)
        widgetHolder += HTFluidWidget
            .createTank(
                steamTank,
                HTSlotHelper.getSlotPosX(6),
                HTSlotHelper.getSlotPosY(0),
            ).setBackground(HTBackgroundType.OUTPUT)
    }

    //    Processing    //

    private var remainingFuelTimes: Int = 0

    private val fuelHandler: HTItemInputHandler by lazy { HTItemInputHandler(fuelSlot) }
    private val ashHandler: HTItemOutputHandler by lazy { HTItemOutputHandler.single(ashSlot) }
    private val inputHandler: HTFluidInputHandler by lazy { HTFluidInputHandler(waterTank) }
    private val outputHandler: HTFluidOutputHandler by lazy { HTFluidOutputHandler.single(steamTank) }

    override fun onUpdateMachine(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean {
        if (remainingFuelTimes <= 0) {
            consumeFuel()
            return remainingFuelTimes > 0
        } else {
            remainingFuelTimes--
            if (remainingFuelTimes > 0) {
                val steamStack: FluidStack = RagiumFluids.STEAM.toStack(10)
                if (inputHandler.getAmount() >= 1 && outputHandler.canInsert(steamStack)) {
                    inputHandler.consume(1)
                    outputHandler.insert(steamStack)
                }
            }
            return true
        }
    }

    private fun consumeFuel() {
        val stack: ItemStack = fuelSlot.getStack()
        if (!fuelSlot.isEmpty()) {
            val burnTime: Int = stack.getBurnTime(null)
            if (burnTime > 0) {
                remainingFuelTimes += burnTime
                fuelHandler.consume(1)
                val ashStack: ItemStack = HiiragiCoreAccess.INSTANCE
                    .getMaterialItem(CommonParts.DUST, CommonMaterialKeys.ASH)
                    ?.toStack(maxOf(1, burnTime / 20 * 10))
                    ?: return
                ashHandler.insert(ashStack)
            }
        }
    }
}
