package hiiragi283.lib.gui.widget

import hiiragi283.lib.gui.HTBounds

/**
 * [HTWidget]の抽象クラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
abstract class HTAbstractWidget(final override val bounds: HTBounds) : HTWidget {
    constructor(x: Int, y: Int, width: Int, height: Int) : this(HTBounds(x, y, width, height))
}
