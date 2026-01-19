package hiiragi283.ragium.common.block.entity.enchant

import com.lowdragmc.lowdraglib2.gui.sync.bindings.impl.DataBindingBuilder
import com.lowdragmc.lowdraglib2.gui.ui.UIElement
import com.lowdragmc.lowdraglib2.gui.ui.elements.Switch
import com.lowdragmc.lowdraglib2.syncdata.annotation.DescSynced
import com.lowdragmc.lowdraglib2.syncdata.annotation.Persisted
import hiiragi283.core.api.gui.element.HTItemSlotElement
import hiiragi283.core.common.storage.fluid.HTBasicFluidTank
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.ragium.common.block.entity.HTProcessorBlockEntity
import hiiragi283.ragium.common.block.entity.component.HTRecipeComponent
import hiiragi283.ragium.common.gui.RagiumModularUIHelper
import hiiragi283.ragium.common.storge.fluid.HTVariableFluidTank
import hiiragi283.ragium.common.storge.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.storge.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storge.holder.HTSlotInfo
import hiiragi283.ragium.config.HTMachineConfig
import hiiragi283.ragium.config.RagiumFluidConfigType
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState

class HTEnchanterBlockEntity(pos: BlockPos, state: BlockState) : HTProcessorBlockEntity(TODO(), pos, state) {
    @DescSynced
    @Persisted(subPersisted = true)
    private val expTank: HTBasicFluidTank = HTVariableFluidTank.input(getTankCapacity(RagiumFluidConfigType.FIRST_INPUT))

    override fun createFluidTanks(builder: HTBasicFluidTankHolder.Builder) {
        builder.addSlot(HTSlotInfo.INPUT, expTank)
    }

    @DescSynced
    @Persisted(subPersisted = true)
    private val leftSlot: HTBasicItemSlot = HTBasicItemSlot.input()

    @DescSynced
    @Persisted(subPersisted = true)
    private val rightSlot: HTBasicItemSlot = HTBasicItemSlot.input()

    @DescSynced
    @Persisted(subPersisted = true)
    private val outputSlot: HTBasicItemSlot = HTBasicItemSlot.output()

    override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder) {
        builder.addSlot(HTSlotInfo.INPUT, leftSlot)
        builder.addSlot(HTSlotInfo.EXTRA_INPUT, rightSlot)

        builder.addSlot(HTSlotInfo.OUTPUT, outputSlot)
    }

    @DescSynced
    @Persisted
    private val isRandom: Boolean = false

    override fun setupMainTab(root: UIElement) {
        RagiumModularUIHelper.enchanting(
            root,
            createFluidSlot(0),
            HTItemSlotElement(leftSlot),
            HTItemSlotElement(rightSlot),
            HTItemSlotElement(outputSlot),
        )
        super.setupMainTab(root)
        root
            .addChild(Switch().bind(DataBindingBuilder.boolS2C(this::isRandom).build()))
    }

    //    Processing    //

    override fun createRecipeComponent(): HTRecipeComponent<*, *> {
        TODO("Not yet implemented")
    }

    override fun getConfig(): HTMachineConfig = HTMachineConfig.EMPTY
}
