package hiiragi283.ragium.common.block.entity.processor

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.block.entity.HTUpgradableBlockEntity
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.setup.RagiumItems
import org.apache.commons.lang3.math.Fraction

object HTProcessorHelper {
    fun hasLubricantUpgrade(machine: HTUpgradableBlockEntity): Boolean = machine.hasUpgrade(RagiumItems.EFFICIENT_CRUSH_UPGRADE)

    fun getLubricantModifier(machine: HTUpgradableBlockEntity, inputTank: HTFluidTank): Fraction {
        val bool1: Boolean = hasLubricantUpgrade(machine)
        val bool2: Boolean = inputTank
            .extract(
                RagiumConst.LUBRICANT_CONSUME,
                HTStorageAction.SIMULATE,
                HTStorageAccess.INTERNAL,
            )?.amount() == RagiumConst.LUBRICANT_CONSUME
        return when (bool1 && bool2) {
            true -> Fraction.THREE_QUARTERS
            false -> Fraction.ONE
        }
    }

    fun consumeLubricant(machine: HTUpgradableBlockEntity, inputTank: HTFluidTank) {
        if (hasLubricantUpgrade(machine)) {
            inputTank.extract(RagiumConst.LUBRICANT_CONSUME, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
        }
    }
}
