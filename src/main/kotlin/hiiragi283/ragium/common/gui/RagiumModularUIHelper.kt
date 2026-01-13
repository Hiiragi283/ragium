package hiiragi283.ragium.common.gui

import com.lowdragmc.lowdraglib2.gui.ui.UIElement
import com.lowdragmc.lowdraglib2.gui.ui.elements.ProgressBar
import hiiragi283.core.api.gui.HTModularUIHelper
import hiiragi283.core.api.gui.element.addRowChild
import hiiragi283.core.api.gui.element.alineCenter
import hiiragi283.core.api.text.HTCommonTranslation

/**
 * @see HTModularUIHelper
 */
object RagiumModularUIHelper {
    @JvmStatic
    fun fakeProgress(time: Int): ProgressBar = ProgressBar().label {
        it.setText(HTCommonTranslation.SECONDS.translate(time / 20.0f, time))
    }

    //    Machine    //

    @JvmStatic
    fun alloySmelter(
        root: UIElement,
        topInput: UIElement,
        leftInput: UIElement,
        rightInput: UIElement,
        output: UIElement,
    ) {
        root.addRowChild {
            alineCenter()
            addChild(topInput)
            addChild(HTModularUIHelper.plusIcon().layout { it.marginHorizontalPercent(5f) })
            addChild(leftInput)
            addChild(rightInput)
            addChild(HTModularUIHelper.rightArrowIcon().layout { it.marginHorizontalPercent(5f) })
            addChild(output)
        }
    }

    @JvmStatic
    fun chanced(
        root: UIElement,
        lubricant: UIElement,
        input: UIElement,
        output: UIElement,
        extraOutputs: Iterable<UIElement>,
    ) {
        root.addRowChild {
            alineCenter()
            addChild(lubricant)
            addChild(input)
            addChild(HTModularUIHelper.rightArrowIcon().layout { it.marginHorizontalPercent(5f) })
            addChild(output)
            if (extraOutputs.any()) {
                addChild(HTModularUIHelper.plusIcon())
                extraOutputs.forEach(this::addChild)
            }
        }
    }

    @JvmStatic
    fun complex(
        root: UIElement,
        fluidInput: UIElement,
        itemInput: UIElement,
        itemOutput: UIElement,
        fluidOutput: UIElement,
    ) {
        root.addRowChild {
            alineCenter()
            addChild(fluidInput)
            addChild(HTModularUIHelper.plusIcon())
            addChild(itemInput)
            addChild(HTModularUIHelper.rightArrowIcon().layout { it.marginHorizontalPercent(5f) })
            addChild(itemOutput)
            addChild(HTModularUIHelper.plusIcon())
            addChild(fluidOutput)
        }
    }

    @JvmStatic
    fun melter(root: UIElement, input: UIElement, output: UIElement) {
        root.addRowChild {
            alineCenter()
            addChild(input)
            addChild(HTModularUIHelper.rightArrowIcon().layout { it.marginHorizontalPercent(10f) })
            addChild(output)
        }
    }

    @JvmStatic
    fun pyrolyzer(
        root: UIElement,
        input: UIElement,
        itemOutput: UIElement,
        fluidOutput: UIElement,
    ) {
        root.addRowChild {
            alineCenter()
            addChild(input)
            addChild(HTModularUIHelper.rightArrowIcon().layout { it.marginHorizontalPercent(5f) })
            addChild(itemOutput)
            addChild(HTModularUIHelper.plusIcon().layout { it.marginHorizontalPercent(5f) })
            addChild(fluidOutput)
        }
    }

    @JvmStatic
    fun singleCatalyst(
        root: UIElement,
        input: UIElement,
        catalyst: UIElement,
        output: UIElement,
    ) {
        root.addRowChild {
            alineCenter()
            addChild(input)
            addChild(HTModularUIHelper.plusIcon().layout { it.marginHorizontalPercent(5f) })
            addChild(catalyst)
            addChild(HTModularUIHelper.rightArrowIcon().layout { it.marginHorizontalPercent(5f) })
            addChild(output)
        }
    }
}
